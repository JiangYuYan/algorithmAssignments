import java.util.PriorityQueue;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int thisLength;
    private int[] thisIndex;
    private String thisString;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("Null input.");
        thisString = s;
        thisLength = s.length();
        thisIndex = new int[thisLength];
        if (thisLength != 0) {
            PriorityQueue<CircularSuffix> pq = new PriorityQueue<CircularSuffix>(thisLength);
            for (int i = 0; i < thisLength; ++i) {
                pq.add(new CircularSuffix(i));
            }
            for (int i = 0; i < thisLength; ++i) {
                thisIndex[i] = pq.poll().index;
            }
        }
    }

    // length of s
    public int length() {
        return thisLength;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= thisLength)
            throw new IllegalArgumentException("Out of range.");
        return thisIndex[i];
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        public int index;
        public CircularSuffix(int i) {
            index = i;
        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < thisLength; ++i) {
                char v = thisString.charAt((this.index + i) % thisLength);
                char w = thisString.charAt((that.index + i) % thisLength);
                if (v > w)
                    return +1;
                if (v < w)
                    return -1;
            }
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ARD!RCAAAABB";
        CircularSuffixArray cir = new CircularSuffixArray(s);
        StdOut.println(cir.length());
        for (int i = 0; i < s.length(); ++i) {
            StdOut.println(cir.index(i));
        }
    }

}