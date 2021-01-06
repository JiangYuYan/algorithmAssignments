import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int thisN;
    private static final double CONFIDENCE_95 = 1.96;
    private final int thisNumberOfTrials;
    private final int thisNumberOfSites;
    private double[] thisThreshold;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("N must be positive integer.");
        if (trials <= 0)
            throw new IllegalArgumentException("T must be positive integer.");
        thisN = n;
        thisNumberOfTrials = trials;
        thisNumberOfSites = n*n;
        thisThreshold = new double[trials];
        for (int t = 0; t != thisNumberOfTrials; ++t) {
            Percolation perc = new Percolation(n);
            int[] blocked = new int[thisNumberOfSites];
            for (int i = 0; i != thisNumberOfSites; ++i) {
                blocked[i] = i;
            }
            int numberOfOpenSites = 0;
            while (!perc.percolates()) {
                int end = thisNumberOfSites - numberOfOpenSites;
                int indexBlocked = StdRandom.uniform(end);
                int temp = blocked[indexBlocked];
                int col = temp % thisN + 1;
                int row = temp / thisN + 1;
                perc.open(row, col);
                ++numberOfOpenSites;
                blocked[indexBlocked] = blocked[end - 1];
                blocked[end - 1] = temp;
            }
            thisThreshold[t] = 1.0 * numberOfOpenSites / thisNumberOfSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thisThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thisThreshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(thisThreshold) - CONFIDENCE_95 * StdStats.stddev(thisThreshold) / Math.sqrt(thisNumberOfTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(thisThreshold) + CONFIDENCE_95 * StdStats.stddev(thisThreshold) / Math.sqrt(thisNumberOfTrials);
    }

   // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");
    }
}
