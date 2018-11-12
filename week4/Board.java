/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Nov 7 2018
 *  Description: Board class for 8-pieces Puzzle
 **************************************************************************** */

public class Board {
    private Board twin;
    private int n;
    private int moves;
    private int[][] blocks;
    private int manhattanCost;
    private int hammingCost;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("cant be null");
        }

        this.blocks = cloneBoard(blocks);
        this.moves = 0;
        this.n = blocks.length;
        hammingCost = calculateHamming(blocks);
        manhattanCost = calculateManhattan(blocks);
        // this.twin = new Board(blocks);
    }

    private int[][] cloneBoard(int[][] original) {
        int[][] cloned = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            cloned[i] = original[i].clone();
        }
        return cloned;
    }

    private int calculateHamming(int[][] items) {
        int length = items.length;
        int wrong = 0;
        // iterate over all N elements out of place if:
        for (int row = 0; row < items.length; row++) {
            for (int col = 0; col < items.length; col++) {
                int number = items[row][col];
                if (number == 0) {
                    continue;
                }
                if (number != (row * length) + (col + 1)) {
                    wrong++;
                }
            }
        }

        return wrong;
    }

    private int calculateManhattan(int[][] items) {
        int length = items.length;
        int distance = 0;
        // iterate over all N elements out of place if:
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                int number = items[row][col];
                if (number == 0) {
                    continue;
                }
                if (number != (row * length) + (col + 1)) {
                    int reqRow = number / length; // 8/3 = 2 // row - reqRow
                    int reqCol = (number - 1) - (reqRow * n);
                    distance += Math.abs(reqRow - row) + Math.abs(reqCol - col);
                }
            }
        }

        return distance;
    }

    public static void main(String[] args) {
        int[][] test = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b = new Board(test);
        System.out.println("hamming expected 5, got: " + b.hamming());
        assert (b.hamming() == 5);
        System.out.println("manahttan expected 10, got: " + b.manhattan());
        assert (b.manhattan() == 10);
        System.out.println("board.toString");
        System.out.println(b.toString());
        System.out.println("dimensions expected 3, got: " + b.dimensions());
        assert (b.dimensions() == 3);
    }

    // number of blocks out of place
    public int hamming() {
        return moves + hammingCost;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return moves + manhattanCost;
    }

    // board dimension n
    public int dimensions() {
        return n;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        return this.twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
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

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }
}
