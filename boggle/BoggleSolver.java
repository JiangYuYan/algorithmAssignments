import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver
{
    private TrieSET thisTrie;
    private HashSet<Integer>[] adj;
    private char[] chars;
    private int thisRow;
    private int thisCol;
    private int thisN;
    private boolean thisMarked[];
    private HashSet<String> thisSet;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        thisTrie = new TrieSET();
        for (String s : dictionary) {
            thisTrie.add(s);
        }
        // print();
    }

    private void print() {
        for (String s : thisTrie) {
            StdOut.println(s);
        }
    }
   
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        thisRow = board.rows();
        thisCol = board.cols();
        thisN = thisRow * thisCol;
        adj = (HashSet<Integer>[]) new HashSet[thisN];
        chars = new char[thisN];
        thisMarked = new boolean[thisN];
        thisSet = new HashSet<String>();
        // copy board to chars[], and get adj[]
        int count = 0;
        for (int i = 0; i < thisRow; ++i) {
            for (int j = 0; j < thisCol; ++j) {
                chars[count] = board.getLetter(i, j);
                adj[count] = getAdj(i, j, thisRow, thisCol);
                ++count;
            }
        }

        // for (int i = 0; i < thisN; ++i) {
        //     StdOut.print(chars[i] + ": ");
        //     for (int j : adj[i]) {
        //         StdOut.print(chars[j] + " ");
        //     }
        //     StdOut.println();
        // }
        

        for (int i = 0; i < thisN; ++i) {
            TrieSET.Node root = thisTrie.getRoot();
            StringBuilder s = new StringBuilder();
            dfs(i, s, root);
        }
        return thisSet;
    }

    private void dfs(int v, StringBuilder str, TrieSET.Node lastNode) {

        TrieSET.Node vNode = thisTrie.hasChar(chars[v], lastNode);
        if (vNode == null) return;
        str.append(chars[v]);
        if (chars[v] == 'Q') {
            str.append('U');
        }
        if (str.length() > 2 && vNode.isString) {
            thisSet.add(str.toString());
        }
        thisMarked[v] = true;
        for (int w : adj[v]) {
            if (!thisMarked[w]) {
                dfs(w, str, vNode);
            }
        }
        thisMarked[v] = false;
        str.deleteCharAt(str.length() - 1);
        if (chars[v] == 'Q') {
            str.deleteCharAt(str.length() - 1);
        }
    }

    private HashSet<Integer> getAdj(int i, int j, int thisRow, int thisCol) {
        HashSet<Integer> set = new HashSet<Integer>();
        if (i != 0) {
            set.add(getIndex(i - 1, j, thisCol));
            if (j != 0) {
                set.add(getIndex(i - 1, j - 1, thisCol));
            }
            if (j != thisCol - 1) {
                set.add(getIndex(i - 1, j + 1, thisCol));
            }
        }
        if (i != thisRow - 1) {
            set.add(getIndex(i + 1, j, thisCol));
            if (j != 0) {
                set.add(getIndex(i + 1, j - 1, thisCol));
            }
            if (j != thisCol - 1) {
                set.add(getIndex(i + 1, j + 1, thisCol));
            }
        }
        if (j != 0)
            set.add(getIndex(i, j - 1, thisCol));
        if (j != thisCol - 1)
            set.add(getIndex(i, j + 1, thisCol));
        return set;
    }

    private int getIndex(int i, int j, int thisCol) {
        return i * thisCol + j;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!thisTrie.contains(word))
            return 0;
        int score = word.length();
        if (score < 3)
            score = 0;
        else if (score < 5)
            score = 1;
        else if (score < 6)
            score = 2;
        else if (score < 7)
            score = 3;
        else if (score < 8)
            score = 5;
        else
            score = 11;
        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        // score = solver.scoreOf("QEUES");
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        StringBuilder s = new StringBuilder();
    }
}
