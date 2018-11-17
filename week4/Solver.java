/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {
    private final Node finish;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("no null");
        }

        int n = initial.dimension();
        int lim = (n * n) * (n + n - 2);
        int count = 0;
        Node goal = null;

        MinPQ<Node> pq = new MinPQ<Node>();
        Node start = new Node(initial, 0, null);
        pq.insert(start);

        MinPQ<Node> pqTwin = new MinPQ<Node>();
        Node startTwin = new Node(initial.twin(), 0, null);
        pqTwin.insert(startTwin);

        while (!pq.isEmpty() || !pqTwin.isEmpty() || count < lim) {
            count++;
            Node min = pq.delMin();
            if (min.board.isGoal()) {
                goal = min;
                break;
            }

            Node minTwin = pqTwin.delMin();
            if (minTwin.board.isGoal()) {
                break;
            }


            for (Board b : min.board.neighbors()) {
                if (min.parent != null && b.equals(min.parent.board)) continue;
                pq.insert(new Node(b, min.moves + 1, min));
                if (b.isGoal()) break;
            }

            for (Board b : minTwin.board.neighbors()) {
                if (minTwin.parent != null && b.equals(minTwin.parent.board)) continue;
                pqTwin.insert(new Node(b, minTwin.moves + 1, minTwin));
                if (b.isGoal()) break;
            }
        }

        this.finish = goal;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    public boolean isSolvable() {
        return this.finish != null;
    }

    public int moves() {
        if (this.finish == null) return -1;
        return this.finish.moves;
    }

    public Iterable<Board> solution() {
        if (this.finish == null) return null;
        return new SolutionWalk(this.finish);
    }

    private class SolutionWalk implements Iterable<Board> {
        private final LinkedStack<Board> boards = new LinkedStack<Board>();

        public SolutionWalk(Node node) {
            if (node == null) throw new IllegalArgumentException();

            Node next = node;
            while (next != null) {
                boards.push(next.board);
                next = next.parent;
            }
        }

        @Override
        public Iterator<Board> iterator() {
            return boards.iterator();
        }
    }

    private class Node implements Comparable<Node> {
        private final int moves;
        private final Node parent;
        private final Board board;

        public Node(Board board, int moves, Node parent) {
            if (board == null) {
                throw new IllegalArgumentException("no null");
            }

            this.moves = moves;
            this.board = board;
            this.parent = parent;
        }

        public int compareTo(Node that) {
            return (this.board.manhattan() + this.moves)
                    - (that.board.manhattan() + that.moves);
        }
    }
}
