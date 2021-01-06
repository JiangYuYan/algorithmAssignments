import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private ArrayList<LineSegment> thisSegment;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] givenPoints) {
        thisSegment = new ArrayList<LineSegment>();
        if (givenPoints == null) throw new IllegalArgumentException("Arguement null.");
        int n = givenPoints.length;
        Point[] points = new Point[n];
        for (int i = 0; i != n; ++i) {
            if (givenPoints[i] == null) throw new IllegalArgumentException("Point null.");
            points[i] = givenPoints[i];
            for (int j = i + 1; j != n; ++j) {
                if (givenPoints[i].compareTo(givenPoints[j]) == 0)
                    throw new IllegalArgumentException("Duplicate points.");
            }
        }
        for (int i = 0; i != n; ++i) {
            Point currPoint = givenPoints[i];
            // sorts the points according to the slopes
            Arrays.sort(points, currPoint.slopeOrder());
            if (n < 2) break;
            int firstPoint = 1;
            double firstSlope = currPoint.slopeTo(points[firstPoint]);
            if (firstSlope == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException("Duplicate points.");
            int count = 1;
            if (n < 4) continue;
            for (int j = 2; j != n; ++j) {
                double temp = currPoint.slopeTo(points[j]);
                if (temp == firstSlope) ++count;
                else {
                    if (count >= 3) {
                        // record the LineSegment
                        int max = 0;
                        int min = 0;
                        for (int k = firstPoint; k != j; ++k) {
                            if (points[k].compareTo(points[min]) < 0) min = k;
                            if (points[k].compareTo(points[max]) > 0) max = k;
                        }
                        if (min == 0)
                            thisSegment.add(new LineSegment(points[min], points[max]));
                    }
                    count = 1;
                    firstPoint = j;
                    firstSlope = currPoint.slopeTo(points[firstPoint]);
                }
            }
            if (count >= 3) {
                // record the LineSegment
                int max = 0;
                int min = 0;
                for (int k = firstPoint; k != n; ++k) {
                    if (points[k].compareTo(points[min]) < 0) min = k;
                    if (points[k].compareTo(points[max]) > 0) max = k;
                }
                if (min == 0)
                    thisSegment.add(new LineSegment(points[min], points[max]));
            }
        }
    }

    // the number of line segments
    public           int numberOfSegments() {
        return thisSegment.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return thisSegment.toArray(new LineSegment[thisSegment.size()]);
    }

    public static void main(String[] args) {
        /* YOUR CODE HERE */
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        StdDraw.setPenRadius(0.001);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}