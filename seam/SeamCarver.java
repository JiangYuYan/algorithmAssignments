import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture thisPicture;
    private int thisWidth;
    private int thisHeight;
    private double[][] thisEnergy;
    private int[][] thisRed;
    private int[][] thisGreen;
    private int[][] thisBlue;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        thisPicture = new Picture(picture);
        thisWidth = thisPicture.width();
        thisHeight = thisPicture.height();
        thisRed = new int[thisWidth][thisHeight];
        thisGreen = new int[thisWidth][thisHeight];
        thisBlue = new int[thisWidth][thisHeight];
        thisEnergy = new double[thisWidth][thisHeight];
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                int rgb = thisPicture.getRGB(i, j);
                thisRed[i][j] = (rgb >> 16) & 0xFF;
                thisGreen[i][j] = (rgb >> 8) & 0xFF;
                thisBlue[i][j] = (rgb >> 0) & 0xFF;
            }
        }
        for (int i = 0; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                thisEnergy[i][j] = energy(i, j);
            }
        }
    }

    private double getEnergy(int x, int y) {
        if (x < 0 || x >= thisWidth || y < 0 || y >= thisHeight)
            throw new IllegalArgumentException("Out of range.");
        if (x == 0 || x == thisWidth - 1 || y == 0 || y == thisHeight - 1)
            return 1000;
        double sum = Math.pow(thisRed[x+1][y] - thisRed[x-1][y], 2) + Math.pow(thisGreen[x+1][y] - thisGreen[x-1][y], 2) + Math.pow(thisBlue[x+1][y] - thisBlue[x-1][y], 2);
        sum += Math.pow(thisRed[x][y+1] - thisRed[x][y-1], 2) + Math.pow(thisGreen[x][y+1] - thisGreen[x][y-1], 2) + Math.pow(thisBlue[x][y+1] - thisBlue[x][y-1], 2);
        sum = Math.sqrt(sum);
        return sum;
    }

    // current picture
    public Picture picture() {
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
        
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[thisWidth];
        int[][] pixelTo = new int[thisWidth][thisHeight];
        double[][] disTo = new double[thisWidth][thisHeight];
        for (int j = 0; j < thisHeight; ++j) {
            disTo[0][j] = thisEnergy[0][j];
            pixelTo[0][j] = -1;
        }
        for (int i = 1; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                disTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 1; i < thisWidth; ++i) {
            for (int j = 0; j < thisHeight; ++j) {
                relax(pixelTo, disTo, i, j);
            }
        }
        double disMin = Double.POSITIVE_INFINITY;
        int pixelMin = 0;
        for (int j = 0; j < thisHeight; ++j) {
            if (disTo[thisWidth-1][j] < disMin) {
                disMin = disTo[thisWidth-1][j];
                pixelMin = j;
            }
        }
        seam[thisWidth-1] = pixelMin;
        for (int i = thisWidth - 1; i > 0; --i) {
            seam[i-1] = pixelTo[i, seam[i]];
        }
        return seam;
    }

    private void relax(int[][] pixelTo, double[][] disTo, int x, int y) {
        if (x == thisWidth - 1)
            return;
        int xNext = x + 1;
        int index = convert(x, y);
        for (int i : adj(x, y)) {
            if (disTo[xNext][i] > disTo[x][y] + thisEnergy(xNext, y)) {
                pixelTo[xNext][i] = index;
                distTo[xNext][i] = disTo[x][y] + thisEnergy(xNext, y);
            }
        }
    }

    private int convert(int x, int y, boolean flag) {
        if (!flag)
            return x * thisWidth + y;
        else
            return y * thisWidth + x;
    }
    
    private int[] adj(int x, int y) {
        int[] adjacent;
        if (y == 0) {
            adjacent = new int[2];
            adjacent[0] = 0;
            adjacent[1] = 1;
        } else if (y == thisHeight - 1) {
            adjacent = new int[2];
            adjacent[0] = y - 1;
            adjacent[1] = y;
        } else {
            adjacent = new int[3];
            adjacent[0] = y - 1;
            adjacent[1] = y;
            adjacent[2] = y + 1;
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
        double[][] 
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}