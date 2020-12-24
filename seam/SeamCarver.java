import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture thisPicture;
    private final int thisWidth;
    private final int thisHeight;
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
        if (x < 0 || x >= thisWidth || y < 0 || y >= thisHeight)
            throw new IllegalArgumentException("Out of range.");
        if (x == 0 || x == thisWidth - 1 || y == 0 || y == thisHeight - 1)
            return 1000;
        double sum = Math.pow(thisRed[x+1][y] - thisRed[x-1][y], 2) + Math.pow(thisGreen[x+1][y] - thisGreen[x-1][y], 2) + Math.pow(thisBlue[x+1][y] - thisBlue[x-1][y], 2);
        sum += Math.pow(thisRed[x][y+1] - thisRed[x][y-1], 2) + Math.pow(thisGreen[x][y+1] - thisGreen[x][y-1], 2) + Math.pow(thisBlue[x][y+1] - thisBlue[x][y-1], 2);
        sum = Math.sqrt(sum);
        return sum;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[thisWidth];
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[thisHeight];
        return seam;
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