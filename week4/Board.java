/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Nov 7 2018
 *  Description: Board class for 8-pieces Puzzle
 **************************************************************************** */

public class Board {
    private int n;
    private Board twin;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("cant be null");
        }
        this.n = blocks[0].length;
        this.twin = new Board(blocks);
    }

    public static void main(String[] args) {

    }

    // board dimension n
    public int dimensions() {
        return this.n;
    }

    // number of blocks out of place
    public int hamming() {
        return 0;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return 0;
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
        return "";
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
    }
}
