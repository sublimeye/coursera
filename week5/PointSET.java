import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    public PointSET() {
    }

    public static void main(String[] args) {
    }

    public boolean isEmpty() {
        // TODO: implement
        return true;
    }

    public int size() {
        // TODO: implement
        return 0;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        // TODO: implement
        // TODO: perf: logarithm of the number of points in the set
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        // TODO: implement
        // TODO: perf: logarithm of the number of points in the set
        return false;
    }

    public void draw() {
        // TODO: implement
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        // TODO: implement
        // TODO: perf: time proportional to the number of points in the set.
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        // TODO: implement
        // TODO: perf: time proportional to the number of points in the set.
        return new Point2D(0, 0);
    }

}
