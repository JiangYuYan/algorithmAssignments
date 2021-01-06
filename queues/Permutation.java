import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int n = 0;
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            ++n;
            if (n <= k) {
                queue.enqueue(StdIn.readString());
            } else {
                String temp = StdIn.readString();
                if (StdRandom.uniform() < 1.0 * k / n) {
                    queue.dequeue();
                    queue.enqueue(temp);
                }
            }
        }
        if (k > n)
            throw new IllegalArgumentException("k can't be larger than " + queue.size() + ".");
        else {
            for (int i = 0; i != k; ++i) {
                StdOut.println(queue.dequeue());
            }
        }
   }
}
