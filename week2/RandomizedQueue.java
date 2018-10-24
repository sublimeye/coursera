/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: Oct 7 2018
 *  Description: Randomized Queue implementation for Princeton coursera course
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // number of items on the randomized queue
    private int n;
    // array of items
    private Item[] a;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("can't be null");
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
        assert check();
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("can't dequeue an empty queue");
        int randomIndex = StdRandom.uniform(n);
        int last = n - 1;

        Item item = a[randomIndex];

        a[randomIndex] = a[last];
        a[last] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        // assert check();
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("can't dequeue an empty queue");
        return a[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomQueueIterator implements Iterator<Item> {
        private int i;
        private final int[] shuffled;

        public RandomQueueIterator() {
            shuffled = StdRandom.permutation(n);
            i = n - 1;
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[shuffled[i--]];
        }
    }

    // internal testing tool
    private boolean check() {

        // check a few properties of instance variable 'first'
        if (n < 0) {
            return false;
        }
        if (n == 0) {
            if (a.length != 0) return false;
        }
        else if (n == 1) {
            if (a.length != 2) return false;
            if (!sample().equals(sample())) return false;
        }

        return true;
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        assert rq.isEmpty();
        assert rq.size() == 0;
        rq.enqueue("A");
        rq.enqueue("B");
        rq.enqueue("C");
        rq.enqueue("D");
        rq.enqueue("E");
        assert !rq.isEmpty();
        assert rq.size() == 5;
        for (String x : rq) {
            System.out.print(x + " ");
        }
        System.out.print("\n");
        for (String y : rq) {
            System.out.print(y + " ");
        }
        System.out.print("\n");

        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());

    }
}
