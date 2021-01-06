import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PermutationTest {
    private double[] thisProbabilities;

    public PermutationTest(int n, int k, int t) {
        thisProbabilities = new double[n];
        int[] thisArray = new int[n];
        for (int i = 0; i != n; ++i) {
            thisArray[i] = i;
        }
        // try t times
        for (int i = 0; i != t; ++i) {
            int m = 0;
            RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
            while (m != n) {
                ++m;
                if (m <= k) {
                    queue.enqueue(thisArray[m-1]);
                } else {
                    int temp = thisArray[m-1];
                    if (StdRandom.uniform() < 1.0 * k / m) {
                        queue.dequeue();
                        queue.enqueue(temp);
                    }
                }
            }
            if (k > m)
                throw new IllegalArgumentException("k can't be larger than " + queue.size() + ".");
            else {
                for (int j = 0; j != k; ++j) {
                    thisProbabilities[queue.dequeue()] += 1;
                }
            }
        }
        for (int i = 0; i != n; ++i) {
            thisProbabilities[i] /= t;
            StdOut.println(thisProbabilities[i]);
        }
        StdOut.println(1.0 * k / n);
    }

    public double[] getProbability() {
        return thisProbabilities;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        PermutationTest perm = new PermutationTest(n, k, t);
        StdOut.println("probabilty = " + 1.0 * k / n);
        StdOut.println("mean       = " + StdStats.mean(perm.getProbability()));
        StdOut.println("stddev     = " + StdStats.stddev(perm.getProbability()));
   }
}