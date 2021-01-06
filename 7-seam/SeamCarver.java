import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int thisWidth;
    private int thisHeight;
    private double[] thisEnergy;
    private int[] thisRGB;
    private boolean thisColMajor = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Null arguement.");
        thisWidth = picture.width();
        thisHeight = picture.height();
        thisRGB = new int[thisWidth*thisHeight];
        thisEnergy = new double[thisWidth*thisHeight];
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                thisRGB[getIndex(i, j)] = picture.getRGB(i, j);
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
        return Math.sqrt(getDy(x, y) + getDx(x, y));
    }

    private double getDx(int x, int y) {
        int rgb1 = thisRGB[getIndex(x + 1, y)];
        int rgb2 = thisRGB[getIndex(x - 1, y)];
        return getPow(rgb1, rgb2);
    }

    private double getDy(int x, int y) {
        int rgb1 = thisRGB[getIndex(x, y + 1)];
        int rgb2 = thisRGB[getIndex(x, y - 1)];
        return getPow(rgb1, rgb2);
    }

    private double getPow(int rgb1, int rgb2) {
        double sum = 0;
        sum += Math.pow((getR(rgb1) - getR(rgb2)), 2);
        sum += Math.pow((getG(rgb1) - getG(rgb2)), 2);
        sum += Math.pow((getB(rgb1) - getB(rgb2)), 2);
        return sum;
    }

    private int getR(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getG(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int getB(int rgb) {
        return (rgb >> 0) & 0xFF;
    }
    // current picture
    public Picture picture() {
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < width(); ++i) {
            for (int j = 0; j < height(); ++j) {
                int rgb = thisRGB[getIndex(i, j)];
                pic.setRGB(i, j, rgb);
            }
        }
        return pic;
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
        if (x < 0 || x >= thisWidth || y < 0 || y >= thisHeight)
            throw new IllegalArgumentException("Out of range.");
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
        if (thisHeight == 1) {
            adjacent = new int[1];
            adjacent[0] = getIndex(x + 1, y);
        } else if (thisHeight == 2) {
            adjacent = new int[2];
            if (y == 0) {
                adjacent[0] = getIndex(x + 1, y);
                adjacent[1] = getIndex(x + 1, y + 1);
            } else {
                adjacent[0] = getIndex(x + 1, y - 1);
                adjacent[1] = getIndex(x + 1, y);
            }
        } else {
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
        for (int i = 1; i < thisWidth; ++i) {
            if ((seam[i] - seam[i-1]) > 1 || seam[i] - seam[i-1] < -1)
                throw new IllegalArgumentException("Don't satisfy continuity.");
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
        int newIndex, oldIndex;
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                if (j < cols[i]) {
                    --thisHeight;
                    newIndex = getIndex(i, j);
                    ++thisHeight;
                    oldIndex = getIndex(i, j);
                    thisRGB[newIndex] = thisRGB[oldIndex];
                    thisEnergy[newIndex] = thisEnergy[oldIndex];
                } else if (j > cols[i]) {
                    --thisHeight;
                    newIndex = getIndex(i, j-1);
                    ++thisHeight;
                    oldIndex = getIndex(i, j);
                    thisRGB[newIndex] = thisRGB[oldIndex];
                    thisEnergy[newIndex] = thisEnergy[oldIndex];
                }
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