import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int thisN;
    private WeightedQuickUnionUF thisTopology;
    private WeightedQuickUnionUF thisTopologyTop;
    private boolean[][] thisState; // false:blocked, true:open
    private int thisNumberOfOpenSites;
    
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("N must be positive integer.");
        thisN = n;
        thisTopology = new WeightedQuickUnionUF(n * n + 2);
        thisTopologyTop = new WeightedQuickUnionUF(n * n + 1);
        thisState = new boolean[n][n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        thisState[row-1][col-1] = true;
        ++thisNumberOfOpenSites;
        int index = convert(row, col);
        // connects to open neighbors
        if (row != 1) {
            if (isOpen(row-1, col)) {
                thisTopology.union(index, convert(row-1, col));
                thisTopologyTop.union(index, convert(row-1, col));
            }
        } else {
            thisTopology.union(index, 0);
            thisTopologyTop.union(index, 0);
        }
        if (row != thisN) {
            if (isOpen(row+1, col)) {
                thisTopology.union(index, convert(row+1, col));
                thisTopologyTop.union(index, convert(row+1, col));
            }
        } else {
            thisTopology.union(index, thisN*thisN + 1);
        }
        if (col != 1) {
            if (isOpen(row, col-1)) {
                thisTopology.union(index, convert(row, col-1));
                thisTopologyTop.union(index, convert(row, col-1));
            }
        }
        if (col != thisN) {
            if (isOpen(row, col+1)) {
                thisTopology.union(index, convert(row, col+1));
                thisTopologyTop.union(index, convert(row, col+1));
            }
        } 
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return thisState[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        check(row, col);
        int index = convert(row, col);
        return isOpen(row, col) && (thisTopologyTop.find(index) == thisTopologyTop.find(0));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return thisNumberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return thisTopology.find(thisN*thisN + 1) == thisTopology.find(0);
    }

    // is out of bound?
    private void check(int row, int col) {
        if (row < 1 || row > thisN || col < 1 || col > thisN)
            throw new IllegalArgumentException("Out of bound.");
    }

    // converts from 2d-index to 1d-index
    private int convert(int row, int col) {
        return (row - 1) * thisN + col;
    }

    // test client (optional)
    public static void main(String[] args) {

    }


}