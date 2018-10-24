/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Oct 21 2018
 *  Description: Brute force solution for Collinear Points problem
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    // collinear segments
    private final ArrayList<LineSegment> collinear =
            new ArrayList<LineSegment>();

    /**
     * Find all segments containing 4 points
     *
     * @param points points to analyze
     */
    public BruteCollinearPoints(Point[] incomingPoints) {
        validate(incomingPoints);
        Point[] points = incomingPoints.clone();
        Arrays.sort(points);

        if (hasDuplicates(points)) {
            throw new IllegalArgumentException("duplicate points");
        }

        final int k = 4;
        final int n = incomingPoints.length;

        if (n < k) return;

        double s1, s2, s3;

        int i1, i2, i3, i4;
        for (i1 = 0; i1 < n - k + 1; i1++) {
            for (i2 = i1 + 1; i2 < n - k + 2; i2++) {
                s1 = points[i1].slopeTo(points[i2]);

                for (i3 = i2 + 1; i3 < n - k + 3; i3++) {
                    s2 = points[i2].slopeTo(points[i3]);
                    if (Double.compare(s1, s2) != 0) continue;

                    for (i4 = i3 + 1; i4 < n - k + 4; i4++) {
                        s3 = points[i3].slopeTo(points[i4]);
                        if (Double.compare(s2, s3) == 0) {
                            collinear.add(new LineSegment(points[i1],
                                                          points[i4]));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("missing points array");
        }

        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException("point can't be null");
        }
    }

    /**
     * Iterates over points array assuming it is sorted and checks if there are
     * any duplicates
     *
     * @param points Array of points to validate/check for duplicates
     */
    private boolean hasDuplicates(Point[] points) {
        if (points.length > 1) {
            for (int i = 1; i < points.length; i++) {
                if (points[i].compareTo(points[i - 1]) == 0) return true;
            }
        }

        return false;
    }

    /**
     * The number of line segments
     *
     * @return The number of line segments
     */
    public int numberOfSegments() {
        return collinear.size();
    }

    public LineSegment[] segments() {
        LineSegment[] a = new LineSegment[collinear.size()];
        return collinear.toArray(a);
    }

    public static void main(String[] args) {
        Point[] p0 = new Point[] {
                new Point(19000, 10000),
                new Point(14000, 8000)
        };

        Point[] p1 = new Point[] {
                new Point(19000, 10000),
                new Point(18000, 10000),
                new Point(32000, 10000),
                new Point(21000, 10000),
                new Point(1234, 5678),
                new Point(14000, 8000)
        };

        BruteCollinearPoints c0 = new BruteCollinearPoints(p0);
        BruteCollinearPoints c1 = new BruteCollinearPoints(p1);
        assert c0.numberOfSegments() == 0;
        assert c1.numberOfSegments() == 1;
    }
}
