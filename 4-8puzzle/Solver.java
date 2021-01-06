import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;

public class Solver {
    private int thisMove;
    private Stack<Board> thisStack = new Stack<Board>();
    private boolean thisSolvable;
    private Node thisNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("No input.");
        Board twin = initial.twin();
        MinPQ<Node> thisPQ = new MinPQ<Node>(new Node.ManhattanOrder());
        MinPQ<Node> twinPQ = new MinPQ<Node>(new Node.ManhattanOrder());
        thisPQ.insert(new Node(initial, null, 0));
        twinPQ.insert(new Node(twin, null, 0));
        while (thisPQ.size() != 0 && twinPQ.size() != 0) {
            // deal with thisNode
            Node thisTemp = thisPQ.delMin();
            thisMove = thisTemp.moves();
            if (thisTemp.board().isGoal()) {
                thisSolvable = true;
                thisNode = thisTemp;
                break;
            }
            for (Board b: thisTemp.board().neighbors()) {
                if (check(b, thisTemp))
                    thisPQ.insert(new Node(b, thisTemp, thisMove + 1));
            }

            // deal with twinNode
            Node twinTemp = twinPQ.delMin();
            int twinMove = twinTemp.moves();
            if (twinTemp.board().isGoal()) {
                thisMove = -1;
                thisSolvable = false;
                break;
            }
            for (Board b: twinTemp.board().neighbors()) {
                if (check(b, twinTemp))
                    twinPQ.insert(new Node(b, twinTemp, twinMove + 1));
            }
        }
        if (isSolvable()) {
            thisStack.push(thisNode.board());
            Node tempNode = thisNode.previous();
            while (tempNode != null) {
                thisStack.push(tempNode.board());
                tempNode = tempNode.previous();
            }
        } else {
            thisStack = null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return thisSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return thisMove;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return thisStack;
    }

    private boolean check(Board board, Node node) {
        if (node.previous() == null)
            return true;
        if (board.equals(node.previous().board()))
            return false;
        return true;
    }
    
    private static class Node {
        private final Board thisBoard;
        private final int hammingPriority;
        private final int manhattanPriority;
        private final int thisMoves;
        private final Node thisPrevious;

        public Node(Board board, Node previous, int moves) {
            thisBoard = board;
            hammingPriority = moves + board.hamming();
            manhattanPriority = moves + board.manhattan();
            thisMoves = moves;
            thisPrevious = previous;
        }

        public int moves() {
            return thisMoves;
        }

        public int hammingPriority() {
            return hammingPriority;
        }

        public int manhattanPriority() {
            return manhattanPriority;
        }

        public Board board() {
            return thisBoard;
        }

        public Node previous() {
            return thisPrevious;
        }

        public static class HammingOrder implements Comparator<Node> {
            public int compare(Node v, Node w) {
                if (v.hammingPriority < w.hammingPriority)
                    return -1;
                if (v.hammingPriority > w.hammingPriority)
                    return +1;
                return 0;
            }
        }

        public static class ManhattanOrder implements Comparator<Node> {
            public int compare(Node v, Node w) {
                if (v.manhattanPriority < w.manhattanPriority)
                    return -1;
                if (v.manhattanPriority > w.manhattanPriority)
                    return +1;
                return 0;
            }
        }
    }
    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
