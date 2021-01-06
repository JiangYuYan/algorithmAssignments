import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private final Digraph thisG;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Input of SAP is null.");
        thisG = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        check(v);
        check(w);
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(thisG, v);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(thisG, w);
        int minLen = thisG.E() + 1;
        for (int i = 0; i < thisG.V(); ++i) {
            if (bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i)) {
                int tempLen = bfdpv.distTo(i) + bfdpw.distTo(i);
                if (tempLen < minLen)
                    minLen = tempLen;
            }
        }
        if (minLen > thisG.E())
            return -1;
        return minLen;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        check(v);
        check(w);
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(thisG, v);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(thisG, w);
        int minLen = thisG.E() + 1;
        int ancestor = 0;
        for (int i = 0; i < thisG.V(); ++i) {
            if (bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i)) {
                int tempLen = bfdpv.distTo(i) + bfdpw.distTo(i);
                if (tempLen < minLen) {
                    minLen = tempLen;
                    ancestor = i;
                }
            }
        }
        if (minLen > thisG.E())
            return -1;
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return -1;
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(thisG, v);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(thisG, w);
        int minLen = thisG.E() + 1;
        for (int i = 0; i < thisG.V(); ++i) {
            if (bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i)) {
                int tempLen = bfdpv.distTo(i) + bfdpw.distTo(i);
                if (tempLen < minLen)
                    minLen = tempLen;
            }
        }
        if (minLen > thisG.E())
            return -1;
        return minLen;
    }

    
        
        // if (!v.iterator().hasNext())
            // throw new IllegalArgumentException("The container is empty.");
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return -1;
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(thisG, v);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(thisG, w);
        int minLen = thisG.E() + 1;
        int ancestor = 0;
        for (int i = 0; i < thisG.V(); ++i) {
            if (bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i)) {
                int tempLen = bfdpv.distTo(i) + bfdpw.distTo(i);
                if (tempLen < minLen) {
                    minLen = tempLen;
                    ancestor = i;
                }
            }
        }
        if (minLen > thisG.E())
            return -1;
        return ancestor;
    }

    private void checkIterable(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("The container doesn't exist.");
        for (Integer i : v) check(i);
    }

    private void check(Integer x) {
        if (x == null)
            throw new IllegalArgumentException("Null pointer.");
        if (x < 0 || x >= thisG.V())
            throw new IllegalArgumentException("Out of Range.");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}