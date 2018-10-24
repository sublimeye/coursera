package week1;/* *****************************************************************************
 *  Name:    Roman Morozov
 *  NetID:   sublimeye.ua@gmail.com
 *  Precept: sublimeye
 *
 *  Partner Name:    Huh? I don't have any yet
 *  Partner NetID:   unknown
 *  Partner Precept: P00
 *
 *  Description:  Percolation data type for Coursera's Princeton course
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int top; // top site
    private final int bottom; // bottom site
    private final int size; // total size of percolation
    private int opened; // total size of percolation
    // used to track sites opened at bottom row (prevent backwash)
    private final int[] bot; // track bottom row / prevent backwash
    private final boolean[] grid; // hashset with opened sites indexes
    private final WeightedQuickUnionUF qu; // QuickUnion

    /**
     * Create n-by-n matrix, with all sites blocked
     *
     * @param n {int} Number of sites
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be a positive int");
        }

        opened = 0;
        size = n;
        top = n * n;
        grid = new boolean[top];
        bottom = top + 1;
        qu = new WeightedQuickUnionUF(top + 2); // include top & bottom
        bot = new int[n];
    }

    /**
     * Useless main method
     *
     * @param args {String[]} CLI arguments
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (n < 0) {
            throw new IllegalArgumentException(
                    "First argument must be a positive integer");
        }

        Percolation p = new Percolation(n);
        p.open(1, 1);
        p.isFull(1, 1);
        p.isOpen(1, 1);
        if (p.numberOfOpenSites() == 1) {
            System.out.println("Sites are open");
        }
        p.percolates();
    }

    /**
     * validates that row and col are within valid range
     *
     * @param row {int} row to validate
     * @param col {int} col to validate
     */
    private void validate(int row, int col) {
        // row = row + 1;
        // col = col + 1;

        if ((row < 1 || row > size) || (col < 1 || col > size)) {
            throw new IllegalArgumentException("row:col " + row + " " + col);
        }
    }

    /**
     * convert row, column to single dimension array index (to use with union)
     *
     * @param row {int} row to convert to index
     * @param col {int} col to convert to index
     * @return {int} zero-based index that can be used to address a site
     */
    private int toIndex(int row, int col) {
        // row = row + 1;
        // col = col + 1;
        return (col - 1) + ((row - 1) * size);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int index = toIndex(row, col);
        if (grid[index]) {
            return;
        }
        // TODO: optimize isFull here
        boolean full = isFull(row, col);
        // row = row + 1;
        // col = col + 1;

        opened++;
        grid[index] = true;

        // vertical union
        // first row - connect with top
        // TODO: optimize first, last, between rows
        if (row == 1) {
            qu.union(index, top);
            if (size == 1) {
                qu.union(top, bottom);
                return;
            }
        }
        // not first - connect with previous row
        else if (grid[index - size]) {
            qu.union(index, index - size);
        }

        // last row - connect with bottom
        if (row == size) {
            if (full) {
                qu.union(index, bottom);
            }
            else {
                addBottom(index);
            }
        }
        // not last - check next row
        else if (grid[index + size]) {
            qu.union(index, index + size);
        }

        // connect left
        if ((col != 1) && grid[index - 1]) {
            qu.union(index, index - 1);
        }

        // connect right
        if ((col != size) && grid[index + 1]) {
            qu.union(index, index + 1);
        }

        connectBottom(index, full);
    }

    /**
     * to prevent backwash only sites that are full will be connected to virtual
     * bottom component. because of it - every time for each site that becomes
     * full and is not yet connected to the top we check if it is connected to
     * the bottom row (from bottom row open sites
     */
    private void connectBottom(int index, boolean wasFull) {
        // TODO: think of a way to optimize this
        boolean isFull = qu.connected(index, top);
        if (!wasFull && isFull && !qu.connected(index, bottom)) {
            for (int i = 0; i < bot.length; i++) {
                if (bot[i] == 0) {
                    break;
                }
                else {
                    if (qu.connected(index, bot[i])) {
                        qu.union(bot[i], bottom);
                        bot[i] = 0;
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param index {int} index
     */
    private void addBottom(int index) {
        for (int i = 0; i < bot.length; i++) {
            if (bot[i] == 0) {
                bot[i] = index;
                break;
            }
        }
    }

    /**
     * Check if site is open
     *
     * @param row {int} Row to check
     * @param col {int} Col to check
     * @return {boolean} open or closed
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[toIndex(row, col)];
    }

    /**
     * Check if site is full at row/col
     *
     * @param row {int} Row to check
     * @param col {int} Col to check
     * @return {boolean} full or not
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = toIndex(row, col);
        return grid[index] && qu.connected(index, top);
    }

    /**
     * Count number of open sites
     *
     * @return {int} number of grid sites
     */
    public int numberOfOpenSites() {
        return opened;
    }

    /**
     * Does the system percolate?
     *
     * @return {boolean}
     */
    public boolean percolates() {
        return qu.connected(top, bottom);
    }
}
