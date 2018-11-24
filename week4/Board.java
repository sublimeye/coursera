/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Nov 7 2018
 *  Description: Board class for 8-pieces Puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] blocks;
    private final int manhattanDistance;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("cant be null");
        }

        this.blocks = cloneBoard(blocks);
        this.manhattanDistance = this.calcManhattan();
    }

    private int[][] cloneBoard(int[][] original) {
        int[][] cloned = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            cloned[i] = original[i].clone();
        }
        return cloned;
    }

    public static void main(String[] args) {
        Board b = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        Board bcopy = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        Board broken = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } });
        Board brokenTwin = new Board(new int[][] { { 2, 1, 3 }, { 4, 5, 6 }, { 8, 7, 0 } });

        Board zeroInit = new Board(new int[][] { { 0, 3, 2 }, { 4, 5, 6 }, { 8, 7, 1 } });
        Board zeroInitTwin = new Board(new int[][] { { 0, 3, 2 }, { 4, 5, 6 }, { 8, 1, 7 } });
        Board goal = new Board(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } });
        Board twinb = b.twin();

        System.out.println("twin should swap first or last two");
        assert (broken.twin().equals(brokenTwin));
        System.out.println("twin should swap first or last two");
        assert (zeroInit.twin().equals(zeroInitTwin));

        System.out.println("board.toString");
        System.out.println(b.toString());
        System.out.println("hamming expected 5, got: " + b.hamming());
        assert (b.hamming() == 5);

        System.out.println("manahttan expected 10, got: " + b.manhattan());
        assert (b.manhattan() == 10);
        System.out.println("dimension expected 3, got: " + b.dimension());
        assert (b.dimension() == 3);
        assert (b.equals(bcopy));
        System.out.println("b equals bcopy: ");
        assert (!b.equals(broken));
        System.out.println("b is not equals broken: ");
        assert (goal.isGoal());
        System.out.println("goal is goal");
        assert (!b.equals(twinb));
        System.out.println("twin is different");

        System.out.println("iterable");
        System.out.println(b.toString());
        for (Board bb : b.neighbors()) {
            System.out.println(bb);
        }
    }

    // a board that is obtained by exchanging any pair of board-items
    public Board twin() {
        int n = this.dimension();
        boolean initialEmpty = blocks[0][0] == 0 || blocks[0][1] == 0;
        int[][] twinBlocks = cloneBoard(blocks);

        if (!initialEmpty) {
            int swap = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[0][1];
            twinBlocks[0][1] = swap;
        }
        else {
            int row = n - 1;
            int col = n - 1;
            int swap = twinBlocks[row][col];
            twinBlocks[row][col] = twinBlocks[row][col - 1];
            twinBlocks[row][col - 1] = swap;
        }
        return new Board(twinBlocks);
    }

    // number of blocks out of place
    public int hamming() {
        int length = this.dimension();
        int hamming = 0;

        for (int i = 1; i < length * length; i++) {
            int row = (i - 1) / length;
            int col = (i - 1) % length;
            if (i != blocks[row][col]) hamming++;
        }

        return hamming;
    }

    private int calcManhattan() {
        int length = this.dimension();
        int manhattan = 0;
        int[] empty = this.findEmpty();

        // iterate over all N elements out of place if:
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                int number = blocks[row][col];
                if (number == 0) {
                    empty[0] = row;
                    empty[1] = col;
                    continue;
                }
                int expectedRow = (number - 1) / length;
                int expectedCol = (number - 1) % length;
                if (row != expectedRow || col != expectedCol) {
                    manhattan += Math.abs(expectedRow - row) + Math.abs(expectedCol - col);
                }
            }
        }
        return manhattan;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDistance;
    }

    private int[] findEmpty() {
        int[] empty = new int[] { 0, 0 };
        int length = this.dimension();
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                int number = blocks[row][col];
                if (number == 0) {
                    empty[0] = row;
                    empty[1] = col;
                    return empty;
                }
            }
        }
        return empty;
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.blocks.length != this.blocks.length) return false;

        int n = blocks.length;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (that.blocks[row][col] != this.blocks[row][col]) return false;
            }
        }
        return true;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        int n = this.dimension();
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

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<>();
        Board b = this;
        int[] empty = this.findEmpty();
        int[][] bcopy = b.cloneBoard(b.blocks);
        int n = b.dimension();
        int row = empty[0];
        int col = empty[1];

        if (row != 0) {
            enqueueSwapped(boards, bcopy, row, col, -1, 0);
        }
        if (row != n - 1) {
            enqueueSwapped(boards, bcopy, row, col, 1, 0);
        }

        if (col != 0) {
            enqueueSwapped(boards, bcopy, row, col, 0, -1);
        }
        if (col != n - 1) {
            enqueueSwapped(boards, bcopy, row, col, 0, 1);
        }

        return boards;
    }

    private void enqueueSwapped(Queue<Board> queue, int[][] bcopy, int row, int col, int roff,
                                int coff) {
        int tmp = bcopy[row][col];

        int swappable = bcopy[row + roff][col + coff];
        // enqueueSwapped
        bcopy[row + roff][col + coff] = tmp;
        bcopy[row][col] = swappable;
        queue.enqueue(new Board(bcopy));
        // revert
        bcopy[row + roff][col + coff] = swappable;
        bcopy[row][col] = tmp;
    }
}
