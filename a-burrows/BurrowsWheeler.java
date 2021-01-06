import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
public class BurrowsWheeler {
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circular = new CircularSuffixArray(s);
        int len = circular.length();
        for (int i = 0; i < len; ++i) {
            if (circular.index(i) == 0)
                BinaryStdOut.write(i);
        }
        for (int i = 0; i < len; ++i) {
            BinaryStdOut.write(s.charAt((circular.index(i) - 1 + len) % len));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int next = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int len = s.length();
        CircularSuffixArray2 circular = new CircularSuffixArray2(s);
        for (int i = 0; i < len; ++i) {
            next = circular.index(next);
            BinaryStdOut.write(s.charAt(next));
        }
        BinaryStdOut.close();
    }
 
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}