import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int thisNumberOfSegments;
    private LineSegment[] thisSegment;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Arguement null.");
        thisNumberOfSegments = 0;
        int n = points.length;
        int[] pointsOfSeg1 = new int[n];
        int[] pointsOfSeg2 = new int[n];
        for (int i = 0; i != n; ++i) {
            if (points[i] == null) throw new IllegalArgumentException("Point null.");
        }
        for (int i = 0; i != n; ++i) {
            for (int j = i + 1; j != n; ++j) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Duplicate points.");
                for (int k = j + 1; k != n; ++k) {
                    for (int l = k + 1; l != n; ++l) {
                        if (points[i].slopeOrder().compare(points[j],points[k]) == 0 && points[l].slopeOrder().compare(points[j],points[k]) == 0) {
                            int min = i;
                            int max = i;
                            if (points[j].compareTo(points[min]) < 0) min = j;
                            if (points[k].compareTo(points[min]) < 0) min = k;
                            if (points[l].compareTo(points[min]) < 0) min = l;
                            if (points[j].compareTo(points[max]) > 0) max = j;
                            if (points[k].compareTo(points[max]) > 0) max = k;
                            if (points[l].compareTo(points[max]) > 0) max = l;
                            pointsOfSeg1[thisNumberOfSegments] = min;
                            pointsOfSeg2[thisNumberOfSegments] = max;
                            ++thisNumberOfSegments;
                        }
                    }
                }
            }
        }
        thisSegment = new LineSegment[thisNumberOfSegments];
        for (int i = 0; i != thisNumberOfSegments; ++i) {
            thisSegment[i] = new LineSegment(points[pointsOfSeg1[i]], points[pointsOfSeg2[i]]);
        }
    }

    // the number of line segments
    public           int numberOfSegments() {
        return thisNumberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] temp = new LineSegment[thisNumberOfSegments];
        for (int i = 0; i != thisNumberOfSegments; ++i) {
            temp[i] = thisSegment[i];
        }
        return temp;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}