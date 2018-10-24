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

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE = 1.96; // constant
    private final double mean; // mean number
    private final double stddev; // stddev number
    private final double confidenceLo; // lo number
    private final double confidenceHi; // hi number

    /**
     * perform trials independent experiments on an n-by-n grid
     *
     * @param n      {int} number of items on the side
     * @param trials {int} number of trials
     */
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException("must be positive integers");
        }

        int size = n * n;
        // threshold array
        double[] threshold = new double[trials];

        for (int trial = 0; trial < trials; trial++) {
            Percolation p = new Percolation(n);
            int[] order = PercolationStats.generateRandomOrder(size);
            int iteration = 0;

            while (!p.percolates()) {
                p.open(1 + (order[iteration] / n), 1 + (order[iteration] % n));
                iteration++;
            }

            threshold[trial] =
                    (double) p.numberOfOpenSites() / (double) size;
        }

        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        confidenceLo = mean - ((PercolationStats.CONFIDENCE
                * stddev) / Math.sqrt((double) trials));

        confidenceHi = mean + ((PercolationStats.CONFIDENCE
                * stddev) / Math.sqrt((double) trials));
    }

    /**
     * Creates array of size N with values from 0 to N and shuffles it for
     * random order;
     *
     * @param size {int} total number of items in the grid
     * @return {int} array of randomly generated items
     */
    private static int[] generateRandomOrder(int size) {
        int[] a = new int[size];
        for (int i = 0; i < size; ++i) {
            a[i] = i;
        }
        StdRandom.shuffle(a);
        return a;
    }

    /**
     * Sample mean of percolation threshold
     *
     * @return {double} mean
     */
    public double mean() {
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     *
     * @return {double} stddev
     */
    public double stddev() {
        return stddev;
    }

    /**
     * low  endpoint of 95% CONFIDENCE interval
     *
     * @return {double} CONFIDENCE lo
     */
    public double confidenceLo() {
        return confidenceLo;
    }

    /**
     * high endpoint of 95% CONFIDENCE interval
     *
     * @return {double} CONFIDENCE hi
     */
    public double confidenceHi() {
        return confidenceHi;
    }

    /**
     * test client
     *
     * @param args {String[]} Accepts N - size and T - amount of repetitions
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (n < 0) {
            throw new IllegalArgumentException(
                    "First argument must be a positive integer");
        }

        int trials = Integer.parseInt(args[1]);
        if (trials < 0) {
            throw new IllegalArgumentException(
                    "Second argument must be positive int");
        }

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean   " + stats.mean());
        System.out.println("stddev " + stats.stddev());
        System.out.println("lo     " + stats.confidenceLo());
        System.out.println("hi     " + stats.confidenceHi());
    }
}
