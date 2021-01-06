import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
public class MoveToFront {
    private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = new char[R];
        for (int i = 0; i < R; ++i) {
            chars[i] = (char)i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = find(chars, c);
            BinaryStdOut.write((char)index);
            change(chars, index);
        }
        BinaryStdOut.close();
    }

    private static int find(char[] chars, char c) {
        for (int i = 0; i < R; ++i) {
            if (c == chars[i])
                return i;
        }
        return R;
    }

    private static void change(char[] chars, int index) {
        if (index == 0)
            return;
        char temp = chars[index];
        for (int i = index; i > 0; --i) {
            chars[i] = chars[i - 1];
        }
        chars[0] = temp;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[R];
        for (int i = 0; i < R; ++i) {
            chars[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = (int) BinaryStdIn.readChar();
            BinaryStdOut.write(chars[index]);
            change(chars, index);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}