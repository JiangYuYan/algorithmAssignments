import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;

public class Board {
    private final char[][] thisTiles;
    private final int thisN;
    private final int thisHamming;
    private final int thisManhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        thisN = tiles.length;
        thisTiles = new char[thisN][thisN];
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                thisTiles[i][j] = (char) tiles[i][j]; 
            }
        }
        int hamming = 0;
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                if (thisTiles[i][j] != (char) convert(i, j) && thisTiles[i][j] != '\0')
                    ++hamming;
            }
        }
        thisHamming = hamming;
        int manhattan = 0;
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                int temp = (int) thisTiles[i][j];
                if (temp != 0) {
                    int thisi = (temp - 1) / thisN;
                    int thisj = (temp - 1) % thisN;
                    manhattan += Math.abs(thisi - i) + Math.abs(thisj - j);
                }
            }
        }
        thisManhattan = manhattan;
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(thisN + "\n");
        for (int i = 0; i < thisN; i++) {
            for (int j = 0; j < thisN; j++) {
                s.append(String.format("%6d", (int) thisTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return thisN;
    }

    // number of tiles out of place
    public int hamming() {
        return thisHamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return thisManhattan; 
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                if (thisTiles[i][j] != (char) convert(i, j) && thisTiles[i][j] != '\0') return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        try {
            if (thisN != ((Board) y).thisN)
                return false;
            return Arrays.deepEquals(thisTiles, ((Board) y).thisTiles);
        } catch (NullPointerException | ClassCastException e) {
            return false;
        } 
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int[][] temp = copyint();
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                if (thisTiles[i][j] == '\0') {
                    if (i != 0)
                        stack.push(new Board(twinBuilder(temp, convert(i, j), convert(i - 1, j))));
                    if (i != thisN - 1)
                        stack.push(new Board(twinBuilder(temp, convert(i, j), convert(i + 1, j))));
                    if (j != 0)
                        stack.push(new Board(twinBuilder(temp, convert(i, j), convert(i, j - 1))));
                    if (j != thisN - 1)
                        stack.push(new Board(twinBuilder(temp, convert(i, j), convert(i, j + 1))));
                }
            }
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] temp = copyint();
        if (thisTiles[0][0] != '\0') {
            if (thisTiles[0][1] != '\0')
                return new Board(twinBuilder(temp, 1, 2));
            else
                return new Board(twinBuilder(temp, 1, thisN + 1));
        } else {
            return new Board(twinBuilder(temp, 2, convert(1, 1)));
        }
    }

    private int convert(int i, int j) {
        return (thisN * i + j + 1);
    }

    private int[][] twinBuilder(int[][] array, int p, int q) {
        int[][] arrayTemp = new int[thisN][thisN];
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                arrayTemp[i][j] = array[i][j];
            }
        }
        int thisi = (p - 1) / thisN;
        int thisj = (p - 1) % thisN;
        int thati = (q - 1) / thisN;
        int thatj = (q - 1) % thisN;
        int temp = arrayTemp[thisi][thisj];
        arrayTemp[thisi][thisj] = arrayTemp[thati][thatj];
        arrayTemp[thati][thatj] = temp;
        return arrayTemp;
    }

    private int[][] copyint() {
        int[][] temp = new int[thisN][thisN];
        for (int i = 0; i != thisN; ++i) {
            for (int j = 0; j != thisN; ++j) {
                temp[i][j] = (int) thisTiles[i][j];
            }
        }
        return temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial);
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        Board twin = initial.twin();
        StdOut.println(twin);
        StdOut.println(twin.hamming());
        StdOut.println(twin.manhattan());
        for (Board b : twin.neighbors()) {
            StdOut.println(b);
            StdOut.println(b.hamming());
            StdOut.println(b.manhattan());
        }
    }
}
