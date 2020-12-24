import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
public class SeamCarver {
    private Picture thisPicture;
    private int thisWidth;
    private int thisHeight;
    private double[] thisEnergy;
    private int[] thisR;
    private int[] thisG;
    private int[] thisB;
    private boolean thisColMajor = false;
    private boolean thisModified = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Null arguement.");
        thisPicture = new Picture(picture);
        thisWidth = thisPicture.width();
        thisHeight = thisPicture.height();
        thisR = new int[thisWidth*thisHeight];
        thisG = new int[thisWidth*thisHeight];
        thisB = new int[thisWidth*thisHeight];
        thisEnergy = new double[thisWidth*thisHeight];
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                int rgb = thisPicture.getRGB(i, j);
                int index = getIndex(i, j);
                thisR[index] = (rgb >> 16) & 0xFF;
                thisG[index] = (rgb >> 8) & 0xFF;
                thisB[index] = (rgb >> 0) & 0xFF;
            }
        }
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                thisEnergy[getIndex(i, j)] = getEnergy(i, j);
            }
        }
    }

    private double getEnergy(int x, int y) {
        if (x < 0 || x >= thisWidth || y < 0 || y >= thisHeight)
            throw new IllegalArgumentException("Out of range.");
        if (x == 0 || x == thisWidth - 1 || y == 0 || y == thisHeight - 1)
            return 1000;
        double sum = 0;
        sum += Math.pow(thisR[getIndex(x+1, y)] - thisR[getIndex(x-1, y)], 2);
        sum += Math.pow(thisG[getIndex(x+1, y)] - thisG[getIndex(x-1, y)], 2);
        sum += Math.pow(thisB[getIndex(x+1, y)] - thisB[getIndex(x-1, y)], 2);
        sum += Math.pow(thisR[getIndex(x, y+1)] - thisR[getIndex(x, y-1)], 2);
        sum += Math.pow(thisG[getIndex(x, y+1)] - thisG[getIndex(x, y-1)], 2);
        sum += Math.pow(thisB[getIndex(x, y+1)] - thisB[getIndex(x, y-1)], 2);
        sum = Math.sqrt(sum);
        return sum;
    }

    // current picture
    public Picture picture() {
        if (!thisModified)
            return thisPicture;
        thisPicture = new Picture(width(), height());
        for (int i = 0; i < width(); ++i) {
            for (int j = 0; j < height(); ++j) {
                int index = getIndex(i, j);
                int rgb = (thisR[index] << 16) + (thisG[index] << 8) + (thisB[index] << 0);
                thisPicture.setRGB(i, j, rgb);
            }
        }
        thisModified = false;
        return thisPicture;
    }

    // width of current picture
    public int width() {
        return thisWidth;
    }

    // height of current picture
    public int height() {
        return thisHeight;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return thisEnergy[getIndex(x, y)];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[thisWidth];
        int[] pixelTo = new int[thisWidth*thisHeight];
        double[] disTo = new double[thisWidth*thisHeight];
        for (int j = 0; j < thisHeight; ++j) {
            disTo[getIndex(0, j)] = thisEnergy[getIndex(0, j)];
            pixelTo[getIndex(0, j)] = -1;
        }
        for (int i = 1; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                disTo[getIndex(i, j)] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < thisWidth - 1; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                relax(pixelTo, disTo, i, j);
            }
        }
        // for (int j = 0; j < thisHeight; ++j) {
        //     for (int i = 0; i < thisWidth; ++i) {
        //         StdOut.print(pixelTo[i][j] + " ");
        //     }
        //     StdOut.println();
        // }
        // for (int j = 0; j < thisHeight; ++j) {
        //     for (int i = 0; i < thisWidth; ++i) {
        //         StdOut.print(disTo[i][j] + " ");
        //     }
        //     StdOut.println();
        // }
        double disMin = Double.POSITIVE_INFINITY;
        int pixelMin = 0;
        for (int j = 0; j < thisHeight; ++j) {
            if (disTo[getIndex(thisWidth-1, j)] < disMin) {
                disMin = disTo[getIndex(thisWidth-1, j)];
                pixelMin = j;
            }
        }
        seam[thisWidth-1] = pixelMin;
        for (int i = thisWidth - 1; i > 0; --i) {
            seam[i-1] = getJ(pixelTo[getIndex(i, seam[i])]);
        }
        return seam;
    }

    private void relax(int[] pixelTo, double[] disTo, int x, int y) {
        int index = getIndex(x, y);
        for (int i : adj(x, y)) {
            if (disTo[i] > disTo[index] + thisEnergy[i]) {
                pixelTo[i] = index;
                disTo[i] = disTo[index] + thisEnergy[i];
            }
        }
    }

    private int getIndex(int x, int y) {
        if (thisColMajor)
            return y * thisWidth + x;
        else
            return x * thisHeight + y;
    }

    private int getJ(int index) {
        if (thisColMajor)
            return index / thisWidth;
        else
            return index % thisHeight;
    }

    private int[] adj(int x, int y) {
        int[] adjacent;
        if (y == 0) {
            adjacent = new int[2];
            adjacent[0] = getIndex(x + 1, y);
            adjacent[1] = getIndex(x + 1, y + 1);
        } else if (y == thisHeight - 1) {
            adjacent = new int[2];
            adjacent[0] = getIndex(x + 1, y - 1);
            adjacent[1] = getIndex(x + 1, y);
        } else {
            adjacent = new int[3];
            adjacent[0] = getIndex(x + 1, y - 1);
            adjacent[1] = getIndex(x + 1, y);
            adjacent[2] = getIndex(x + 1, y + 1);
        }
        return adjacent;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        transpose();
        int[] seam = findHorizontalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        int temp = thisWidth;
        thisWidth = thisHeight;
        thisHeight = temp;
        thisColMajor = !thisColMajor;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("Null arguement.");
        if (thisHeight <= 1)
            throw new IllegalArgumentException("Height <= 1.");
        if (seam.length != thisWidth)
            throw new IllegalArgumentException("Wrong length.");
        for (int i = 0; i < thisWidth; ++i) {
            if (seam[i] < 0 || seam[i] >= thisHeight)
                throw new IllegalArgumentException("Out of range.");
        }
        shiftdata(seam);
        thisHeight -= 1;
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                if (j >= seam[i] - 1 && j <= seam[i]) {
                    thisEnergy[getIndex(i, j)] = getEnergy(i, j);
                }
            }
        }
        thisModified = true;
    }

    private void shiftdata(int[] cols) {
        int[] seamedIndices = new int[thisWidth];
        for (int i = 0; i < thisWidth; ++i) {
            seamedIndices[i] = getIndex(i, cols[i]);
        }
        int newIndex = 0;
        int count = 0;
        for (int oldIndex = 0; oldIndex < thisWidth * thisHeight; ++oldIndex) {
            if (count < thisWidth && oldIndex == seamedIndices[count]) {
                ++count;
            } else {
                thisR[newIndex] = thisR[oldIndex];
                thisG[newIndex] = thisG[oldIndex];
                thisB[newIndex] = thisB[oldIndex];
                thisEnergy[newIndex] = thisEnergy[oldIndex];
                ++newIndex;
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}