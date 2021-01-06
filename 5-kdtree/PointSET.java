import java.util.TreeSet;
import java.util.Comparator;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private final TreeSet<Point2D> thisPoints;
    // construct an empty set of points 
    public         PointSET() {
        thisPoints = new TreeSet<Point2D>();
    }

    // is the set empty? 
    public           boolean isEmpty() {
        return thisPoints.isEmpty();
    }

    // number of points in the set 
    public               int size() {
        return thisPoints.size();
    }

    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Insert input is null.");
        thisPoints.add(p);
    }

    // does the set contain point p? 
    public           boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Contains input is null.");
        return thisPoints.contains(p);
    }

    // draw all points to standard draw 
    public              void draw() {
        for (Point2D p : thisPoints) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Range input is null.");
        Queue<Point2D> queue = new Queue<Point2D>();
        if (!isEmpty()) {
            for (Point2D p : thisPoints) {
                if (rect.contains(p))
                    queue.enqueue(p);
            }
        }
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public           Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Nearest input is null.");
        if (isEmpty()) {
            return null;
        }
        Point2D min = thisPoints.first();
        double minDis2 = min.distanceSquaredTo(p);
        for (Point2D point : thisPoints) {
            double thisDis2 = point.distanceSquaredTo(p);
            if (thisDis2 < minDis2) {
                min = point;
                minDis2 = thisDis2;
            }
        }
        return min;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        StdOut.println(brute.size());
        StdOut.println(brute.contains(new Point2D(0.2, 0.3)));
    }
}
