/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Oct 21 2018
 *  Description: Fast Collinear Points
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> collinear =
            new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] incomingPoints) {
        validate(incomingPoints);
        Point[] points = incomingPoints.clone();
        Arrays.sort(points);

        if (hasDuplicates(points)) {
            throw new IllegalArgumentException("duplicate points");
        }
        final int cutoff = 2; // num of contiguous sequences

        if (incomingPoints.length < cutoff + 2) {
            return;
        }

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            Arrays.sort(points, p.slopeOrder());

            int count = 0;
            double skipSlope = Double.NEGATIVE_INFINITY;
            double s1 = p.slopeTo(points[1]);

            for (int j = 2; j < points.length; j++) {
                double s2 = p.slopeTo(points[j]);

                if (Double.compare(skipSlope, s2) == 0) {
                    continue;
                }
                else if (Double.compare(s1, s2) == 0) {
                    // equal slopes
                    if (count == 0) {
                        if (p.compareTo(points[j - 1]) < 0) {
                            count++;
                        }
                        else {
                            skipSlope = s2;
                        }
                    }
                    else {
                        // keep going, we're in the middle of a sequence
                        count++;
                    }
                }
                else {
                    // not equal slopes
                    if (count >= cutoff) {
                        collinear.add(new LineSegment(p, points[j - 1]));
                    }

                    s1 = s2;
                    count = 0;
                }

                if (j == points.length - 1 && count >= cutoff) {
                    collinear.add(new LineSegment(p, points[j]));
                }
            }

            Arrays.sort(points);
        }
    }

    public int numberOfSegments() {
        return collinear.size();
    }

    public LineSegment[] segments() {
        LineSegment[] a = new LineSegment[collinear.size()];
        return collinear.toArray(a);
    }

    public static void main(String[] args) {
        Point[] p1 = new Point[] {
                new Point(1, 1),
                };

        FastCollinearPoints c1 = new FastCollinearPoints(p1);
        System.out.println(Arrays.toString(c1.segments()));

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

}
