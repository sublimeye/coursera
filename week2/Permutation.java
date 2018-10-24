/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Oct 9 2018
 *  Description: Permutations client
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }

        int i = 0;
        for (Iterator<String> it = rq.iterator(); it.hasNext() && i < n; i++) {
            String x = it.next();
            System.out.println(x);
        }
    }
}
