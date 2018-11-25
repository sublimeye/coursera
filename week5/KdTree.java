import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private SET<Point2D> set;

    public KdTree() {
        set = new SET<Point2D>();
    }

    public static void main(String[] args) {
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }

        StdDraw.show();
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     *
     * @param rect RectHV rectangle boundries
     * @return Iterable<Point2D> points that belongs to provided rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> points = new Stack<>();

        for (Point2D p : set) {
            if (rect.contains(p)) points.push(p);
        }

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    // time proportional to the number of points in the set
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        Point2D nearestPoint = null;
        double distance, closest = Double.POSITIVE_INFINITY;

        for (Point2D sp : set) {
            distance = sp.distanceSquaredTo(p);

            if (distance < closest) {
                closest = distance;
                nearestPoint = sp;
            }
        }

        return nearestPoint;
    }

}
