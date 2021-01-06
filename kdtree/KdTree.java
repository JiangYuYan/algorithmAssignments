import java.util.Comparator;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final boolean VER = false;
    private Node thisRoot;
    private int thisSize;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private final boolean hv;     // h:1, v:0
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, boolean hv, RectHV rect) {
            this.p = p;
            this.hv = hv;
            this.rect = rect;
        }
    }

    // construct an empty set of points 
    public         KdTree() {
        thisSize = 0;
    }

    // is the set empty? 
    public           boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set 
    public               int size() {
        return thisSize;
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Insert input is null.");
        thisRoot = insert(thisRoot, p, null);
    }

    // x is the current Node; p is the point to be insert;
    // parent is the previous Node which can provide rect and hv;
    // hv : 0 for vertical split and 1 for horizontal split for current Node;
    private Node insert(Node x, Point2D p, Node parent) {
        boolean hv;
        if (parent == null)
            hv = VER;
        else
            hv = !parent.hv;
        if (x == null) {
            if (parent == null || !p.equals(parent.p)) {
                ++thisSize;
                return new Node(p, hv, subrect(p, parent));
            }
        }
        int cmp;
        if (hv)
            cmp = compare(Point2D.Y_ORDER, p, x.p); // horizontal split
        else
            cmp = compare(Point2D.X_ORDER, p, x.p);
        if (cmp == 0 && p.equals(x.p))
            return x;
        if (cmp < 0) {
            x.lb = insert(x.lb, p, x);
        } else {
            x.rt = insert(x.rt, p, x);
        }
        return x;
    }

    // does the set contain point p? 
    public           boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Contains input is null.");
        return get(thisRoot, p);
    }

    private boolean get(Node x, Point2D p) {
        if (x == null) return false;
        int cmp;
        if (x.hv)
            cmp = compare(Point2D.Y_ORDER, p, x.p); // horizontal split
        else
            cmp = compare(Point2D.X_ORDER, p, x.p);
        if (cmp == 0 && p.equals(x.p))
            return true;
        if (cmp < 0) {
            return get(x.lb, p);
        } else {
            return get(x.rt, p);
        }
    }

    // draw all points to standard draw 
    public              void draw() {
        for (Node node : iterator()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            StdDraw.setPenRadius(0.005);
            if (node.hv == VER) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Range input is null.");
        Queue<Point2D> queue = new Queue<Point2D>();
        if (!isEmpty())
            range(thisRoot, queue, rect);
        return queue;
    }

    private void range(Node x, Queue<Point2D> queue, RectHV rect) {
        if (x == null) return;
        if (x.lb != null && rect.intersects(x.lb.rect))
            range(x.lb, queue, rect);
        if (rect.contains(x.p))
            queue.enqueue(x.p);
        if (x.rt != null && rect.intersects(x.rt.rect))
            range(x.rt, queue, rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public           Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Nearest input is null.");
        if (isEmpty())
            return null;
        Point2D min = thisRoot.p;
        double minDis2 = min.distanceSquaredTo(p);
        min = nearest(thisRoot, p, min, minDis2);
        return min;
    }

    private Point2D nearest(Node x, Point2D p, Point2D min, double minDis2) {
        if (x == null) return min;
        double temp = x.p.distanceSquaredTo(p);
        if (temp < minDis2) {
            min = x.p;
            minDis2 = temp;
        }
        int cmp;
        if (x.hv)
            cmp = compare(Point2D.Y_ORDER, p, x.p); // horizontal split
        else
            cmp = compare(Point2D.X_ORDER, p, x.p);
        if (cmp < 0) {
            if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < minDis2) {
                min = nearest(x.lb, p, min, minDis2);
                minDis2 = min.distanceSquaredTo(p);
            }
            if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < minDis2) {
                min = nearest(x.rt, p, min, minDis2);
                minDis2 = min.distanceSquaredTo(p);
            }
        } else {
            if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < minDis2) {
                min = nearest(x.rt, p, min, minDis2);
                minDis2 = min.distanceSquaredTo(p);
            }
            if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < minDis2) {
                min = nearest(x.lb, p, min, minDis2);
                minDis2 = min.distanceSquaredTo(p);
            }
        }
        
        
        return min;
    }

    private static int compare(Comparator<Point2D> c, Point2D v, Point2D w) {
        return c.compare(v, w);
    }

    private static RectHV subrect(Point2D p, Node parent) {
        if (parent == null) // creat thisRoot
            return new RectHV(0, 0, 1, 1);
        if (parent.hv) { // parent is horizontal split, compare y
            if (compare(Point2D.Y_ORDER, p, parent.p) < 0)
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
            else
                return new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
        } else {
            if (compare(Point2D.X_ORDER, p, parent.p) < 0)
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
            else
                return new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        }
    }

    private Iterable<Node> iterator() {
        if (isEmpty()) return new Queue<Node>();
        return iterator(new RectHV(0, 0, 1, 1));
    }

    private Iterable<Node> iterator(RectHV rect) {
        if (isEmpty()) return new Queue<Node>();
        Queue<Node> queue = new Queue<Node>();
        iterator(thisRoot, queue, rect);
        return queue;
    }

    private void iterator(Node x, Queue<Node> queue, RectHV rect) {
        if (x == null) return;
        if (x.lb != null && rect.intersects(x.lb.rect))
            iterator(x.lb, queue, rect);
        if (rect.contains(x.p))
            queue.enqueue(x);
        if (x.rt != null && rect.intersects(x.rt.rect))
            iterator(x.rt, queue, rect);
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree brute = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        StdOut.println(brute.size());
        StdOut.println(brute.contains(new Point2D(0.25, 0.25)));
        for (Point2D p : brute.range(new RectHV(0, 0, 1, 1))) {
            StdOut.println(p.toString());
        }
    }
}
