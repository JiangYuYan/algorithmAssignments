import java.util.PriorityQueue;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray2 {
    private int thisLength;
    private String thisString;
    private CircularSuffix[] thisSuffix; 
    // circular suffix array of s
    public CircularSuffixArray2(String s) {
        if (s == null)
            throw new IllegalArgumentException("Null input.");
        thisString = s;
        thisLength = s.length();
        thisSuffix = new CircularSuffix[thisLength];
        for (int i = 0; i < thisLength; ++i) {
            thisSuffix[i] = new CircularSuffix(i);
        }
        Arrays.sort(thisSuffix);
    }

    // length of s
    public int length() {
        return thisLength;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= thisLength)
            throw new IllegalArgumentException("Out of range.");
        return thisSuffix[i].index;
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        public int index;
        public CircularSuffix(int i) {
            index = i;
        }

        public int compareTo(CircularSuffix that) {
            int vid = this.index;
            int wid = that.index;
            char v = thisString.charAt(vid);
            char w = thisString.charAt(wid);
            if (v > w)
                return +1;
            if (v < w)
                return -1;
            if (vid > wid)
                return +1;
            if (vid < wid)
                return -1;
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ARD!RCAAAABB";
        CircularSuffixArray2 cir = new CircularSuffixArray2(s);
        StdOut.println(cir.length());
        for (int i = 0; i < s.length(); ++i) {
            StdOut.println(cir.index(i));
        }
    }

}