import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean RED = true;
    private Node root = null;
    private int size = 0;

    public KdTree() {
    }

    public static void main(String[] args) {
        KdTree kt = new KdTree();
        assert (kt.size() == 0);
        assert (kt.root == null);

        kt.insert(new Point2D(7, 2));
        kt.insert(new Point2D(5, 4));
        kt.insert(new Point2D(2, 3));
        kt.insert(new Point2D(4, 7));
        kt.insert(new Point2D(9, 6));
        System.out.println("Expected size to be 5, received: " + kt.size());
        assert (kt.size() == 5);

        kt.insert(new Point2D(2, 3));
        kt.insert(new Point2D(4, 7));
        kt.insert(new Point2D(9, 6));
        System.out.println("Size should not change when same points are added");
        assert (kt.size() == 5);

        System.out.println("Should contain point 9, 6");
        assert (kt.contains(new Point2D(9, 6)));

        System.out.println("Should not contain point 19, 6");
        assert (!kt.contains(new Point2D(19, 6)));

        System.out.println("Test range iterable");
        Iterable<Point2D> it = kt.range(new RectHV(1, 2, 10, 6));
        System.out.println(it.toString());

        Point2D nearestOne = kt.nearest(new Point2D(1, 2));
        System.out.println("Nearest point: " + nearestOne.toString());
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        root = put(root, p, RED);
    }

    private Node put(Node h, Point2D p, boolean color) {
        if (h == null) {
            size++;
            return new Node(p, color);
        }

        int cmp = comparePoints(p, h.point, isRed(h));
        if (cmp < 0) h.left = put(h.left, p, !color);
        else if (cmp > 0) h.right = put(h.right, p, !color);

        return h;
    }

    /**
     * Check if point exists in the set
     *
     * @param p point to search for
     * @return true if point exists in the set
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p) != null;
    }

    /**
     * get in BST should return a value, in our case we already know the point
     * (which is a key X, Y) in our case; since we don't really need any value
     * we just return a node if there's a node with point provided
     */
    private Node get(Node h, Point2D p) {
        while (h != null) {
            int cmp = comparePoints(p, h.point, isRed(h));
            if (cmp < 0) h = h.left;
            else if (cmp > 0) h = h.right;
            else return h;
        }
        return null;
    }

    private int comparePoints(Point2D p, Point2D nodeP, boolean r) {
        if (p == null || nodeP == null) throw new IllegalArgumentException();

        if (r) {
            if (p.x() < nodeP.x()) {
                return -1;
            }
            else if (nodeP.compareTo(p) != 0) {
                return 1;
            }
        }
        else {
            if (p.y() < nodeP.y()) {
                return -1;
            }
            else if (nodeP.compareTo(p) != 0) {
                return 1;
            }
        }
        return 0;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);

        drawPoints(root);

        StdDraw.show();
    }

    private void drawPoints(Node h) {
        if (h == null) return;

        StdDraw.point(h.point.x(), h.point.y());

        drawPoints(h.left);
        drawPoints(h.right);
    }

    /**
     * Range search. To find all points contained in a given query rectangle,
     * start at the root and recursively search for points in both subtrees
     * using the following pruning rule: if the query rectangle does not
     * intersect the rectangle corresponding to a node, there is no need to
     * explore that node (or its subtrees). A subtree is searched only if it
     * might contain a point contained in the query rectangle.
     *
     * @param rect RectHV rectangle boundries
     * @return Iterable<Point2D> points that belongs to provided rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> s = new Stack<>();

        searchRange(root, s, rect);
        return s;
    }

    private void searchRange(Node h, Stack<Point2D> s, RectHV rect) {
        if (h == null) return;

        if (isRed(h)) {
            if (h.point.x() > rect.xmax()) searchRange(h.left, s, rect);
            else if (h.point.x() < rect.xmin()) searchRange(h.right, s, rect);
            else {
                if (rect.contains(h.point)) s.push(h.point);
                searchRange(h.left, s, rect);
                searchRange(h.right, s, rect);
            }
        }
        else {
            if (h.point.y() > rect.ymax()) searchRange(h.left, s, rect);
            else if (h.point.y() < rect.ymin()) searchRange(h.right, s, rect);
            else {
                if (rect.contains(h.point)) s.push(h.point);
                searchRange(h.left, s, rect);
                searchRange(h.right, s, rect);
            }
        }
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    // time proportional to the number of points in the set

    /**
     * Nearest-neighbor search. To find a closest point to a given query point,
     * start at the root and recursively search in both subtrees using the
     * following pruning rule: if the closest point discovered so far is closer
     * than the distance between the query point and the rectangle corresponding
     * to a node, there is no need to explore that node (or its subtrees).  That
     * is, search a node only only if it might contain a point  that is closer
     * than the best one found so far. The effectiveness of  the pruning rule
     * depends on quickly finding a nearby point.  To do this, organize the
     * recursive method so that when there are  two possible subtrees to go
     * down, you always choose the subtree that  is on the same side of the
     * splitting line as the query point as the  first subtree to explore—the
     * closest point found while exploring the  first subtree may enable pruning
     * of the second subtree.
     *
     * @param p Point2D central point to search for
     * @return Point2D nearest point to provided point
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        return searchNearest(root, p, root.point);
    }

    private Point2D searchNearest(Node h, Point2D p, Point2D minP) {
        if (h == null) return minP;

        double xdist = Math.abs(h.point.x() - p.x());
        double ydist = Math.abs(h.point.y() - p.y());

        double candidate = h.point.distanceTo(p);
        double champion = minP.distanceTo(p);

        if (candidate < champion) minP = h.point;

        int cmp = comparePoints(p, h.point, isRed(h));

        // query(p) ... | LINE (closer left)
        if (cmp < 0) {
            minP = searchNearest(h.left, p, minP);
            champion = minP.distanceTo(p);
            boolean champCloser = isRed(h) ? champion < xdist :
                                  champion < ydist;
            if (!champCloser) {
                minP = searchNearest(h.right, p, minP);
            }
        }
        // LINE | ... query(p) (closer right)
        else {
            minP = searchNearest(h.right, p, minP);
            champion = minP.distanceTo(p);
            boolean champCloser = isRed(h) ? champion < xdist :
                                  champion < ydist;
            if (!champCloser) {
                minP = searchNearest(h.left, p, minP);
            }
        }

        return minP;
    }

    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private class Node {
        private final Point2D point;
        // private RectHV rect;
        private Node left, right;
        private final boolean color;

        public Node(Point2D p, boolean c) {
            point = p;
            left = null;
            right = null;
            color = c;
            // rect = r;
        }
    }

}
