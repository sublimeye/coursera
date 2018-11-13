/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Nov 7 2018
 *  Description: Board class for 8-pieces Puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private Board twin;
    private int n;
    private int[][] blocks;
    private int manhattanCost;
    private int hammingCost;
    private int[] empty;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("cant be null");
        }

        this.empty = new int[2];
        this.blocks = cloneBoard(blocks);
        this.n = blocks.length;
        calculatePriority(blocks);
    }

    private int[][] cloneBoard(int[][] original) {
        int[][] cloned = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            cloned[i] = original[i].clone();
        }
        return cloned;
    }

    private void calculatePriority(int[][] items) {
        int length = items.length;
        int hamming = 0;
        int manhattan = 0;
        // iterate over all N elements out of place if:
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                int number = items[row][col];
                if (number == 0) {
                    this.empty[0] = row;
                    this.empty[1] = col;
                    continue;
                }
                int expectedRow = (number - 1) / items.length;
                int expectedCol = (number - 1) % items.length;
                if (row != expectedRow || col != expectedCol) {
                    hamming++;
                    manhattan += Math.abs(expectedRow - row) + Math.abs(expectedCol - col);
                }
            }
        }
        this.hammingCost = hamming;
        this.manhattanCost = manhattan;
    }

    public static void main(String[] args) {
        Board b = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        Board bcopy = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        Board broken = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } });
        Board goal = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } });
        Board twinb = b.twin();

        System.out.println("board.toString");
        System.out.println(b.toString());
        System.out.println("hamming expected 5, got: " + b.hamming());
        assert (b.hamming() == 5);
        int[] bempty = new int[] { 1, 1 };
        assert (Arrays.equals(b.empty, bempty));
        System.out.println("empty location: [1,1]" + Arrays.toString(b.empty));

        System.out.println("manahttan expected 10, got: " + b.manhattan());
        assert (b.manhattan() == 10);
        System.out.println("dimensions expected 3, got: " + b.dimensions());
        assert (b.dimensions() == 3);
        assert (b.equals(bcopy));
        System.out.println("b equals bcopy: ");
        assert (!b.equals(broken));
        System.out.println("b is not equals broken: ");
        assert (goal.isGoal());
        System.out.println("goal is goal");
        int[] gempty = new int[] { 2, 2 };
        assert (Arrays.equals(goal.empty, gempty));
        System.out.println("empty location: [2,2]" + Arrays.toString(goal.empty));
        assert (!b.equals(twinb));
        System.out.println("twin is different");

        System.out.println("iterable");
        System.out.println(b.toString());
        for (Board bb : b.neighbors()) {
            System.out.println(bb);
        }
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (this.twin == null) {
            int[][] twinBlocks = cloneBoard(blocks);
            int swap = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[0][1];
            twinBlocks[0][1] = swap;
            this.twin = new Board(twinBlocks);
        }

        return this.twin;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingCost;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanCost;
    }

    // board dimension n
    public int dimensions() {
        return n;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.dimensions() != this.dimensions()) return false;
        if (that.manhattan() != this.manhattan()) return false;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (that.blocks[row][col] != this.blocks[row][col]) return false;
            }
        }
        return true;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattanCost == 0;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors(this);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder b = new StringBuilder(n * (n + 3) + n);
        b.append(n + "\n");
        for (int row = 0; row < blocks.length; row++) {
            b.append(" ");
            for (int col = 0; col < blocks.length; col++) {
                b.append(blocks[row][col]);
                if (col != n - 1) {
                    b.append("  ");
                }
            }
            b.append(" \n");
        }

        return b.toString();
    }

    private class Neighbors implements Iterable<Board> {
        private Queue<Board> boards = new Queue<>();

        public Neighbors(Board b) {
            // get row|col from b.empty
            int[][] bcopy = b.cloneBoard(b.blocks);
            int row = b.empty[0];
            int col = b.empty[1];

            if (row != 0) {
                enqueueSwapped(bcopy, row, col, -1, 0);
            }
            if (row != n - 1) {
                enqueueSwapped(bcopy, row, col, 1, 0);
            }

            if (col != 0) {
                enqueueSwapped(bcopy, row, col, 0, -1);
            }
            if (col != n - 1) {
                enqueueSwapped(bcopy, row, col, 0, 1);
            }
        }

        private void enqueueSwapped(int[][] bcopy, int row, int col, int roff, int coff) {
            int tmp = bcopy[row][col];

            int swappable = bcopy[row + roff][col + coff];
            // enqueueSwapped
            bcopy[row + roff][col + coff] = tmp;
            bcopy[row][col] = swappable;
            this.boards.enqueue(new Board(bcopy));
            // revert
            bcopy[row + roff][col + coff] = swappable;
            bcopy[row][col] = tmp;
        }

        @Override
        public Iterator<Board> iterator() {
            return boards.iterator();
        }
    }
}
