/* *****************************************************************************
 *  Name: Roman Morozov
 *  Date: October 7, 2018
 *  Description: A double-ended queue implementation for week2 assignment
 *  Coursera / Princeton Algorithms course
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;         // first item in the queue
    private Node last;          // last item in the queue
    private int size;           // number of items in the queue

    // helper linked list class
    private class Node {
        private final Item item;
        private Node next;
        private Node prev;

        private Node(Item newNode) {
            item = newNode;
            next = null;
            prev = null;
        }
    }

    /**
     * Initializes an empty Deque.
     */
    public Deque() {
        first = null;
        last = null;
        size = 0;
        assert check();
    }

    /**
     * Is this deque empty?
     *
     * @return true if this deque is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in the deque.
     *
     * @return the number of items in the deque
     */
    public int size() {
        return this.size;
    }

    /**
     * Adds the item to the beginning of this deque.
     *
     * @param item the item to add
     */
    public void addFirst(Item item) {
        validate(item);
        Node oldfirst = first;
        first = new Node(item);
        first.next = oldfirst;
        if (oldfirst != null) {
            oldfirst.prev = first;
        }

        if (size == 0) {
            last = first;
        }
        else if (size == 1) {
            last.prev = first;
        }

        size++;

        assert check();
    }

    /**
     * Adds the item to the end of this deque.
     *
     * @param item the item to add
     */
    public void addLast(Item item) {
        validate(item);
        Node oldlast = last;
        last = new Node(item);
        last.prev = oldlast;
        if (oldlast != null) {
            oldlast.next = last;
        }

        if (size == 0) {
            first = last;
        }
        else if (size == 1) {
            first.next = last;
        }

        size++;

        assert check();
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        if (first != null) {
            first.prev = null;             // remove ref to just removed first
        }
        else {
            last = null;
        }

        size--;
        assert check();
        return item;                   // return the saved item
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = last.item;       // save item to return
        last = last.prev;            // delete first node
        if (last != null) {
            last.next = null;            // remove ref to just removed last
        }
        else {
            first = null;
        }

        size--;
        assert check();
        return item;                   // return the saved item
    }

    private void validate(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null");
        }
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }

    // check internal invariants
    private boolean check() {

        // check a few properties of instance variable 'first'
        if (size < 0) {
            return false;
        }
        if (size == 0) {
            if (first != null) return false;
            if (last != null) return false;
        }
        else if (size == 1) {
            if (first == null) return false;
            if (first.next != null) return false;
            if (first.prev != null) return false;

            if (last == null) return false;
            if (last.next != null) return false;
            if (last.prev != null) return false;
        }
        else if (size == 2) {
            if (first == null) return false;
            if (first.next != last) return false;
            if (first.prev != null) return false;

            if (last == null) return false;
            if (last.next != null) return false;
            if (last.prev != first) return false;
        }
        else {
            if (first == null) return false;
            if (last == null) return false;
            if (first.next == null) return false;
            if (last.prev == null) return false;

            if (first.next == last) return false;
            if (last.prev == first) return false;
        }

        // check internal consistency of instance variable n
        int numberOfNodes = 0;
        for (Node x = first; x != null && numberOfNodes <= size; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != size) return false;

        return true;
    }

    public static void main(String[] args) {
        String item;

        Deque<String> d = new Deque<>();
        d.addFirst("Roman");
        d.addFirst("Bogdan");
        item = d.removeLast();
        assert d.first.item.equals("Bogdan");
        assert d.first.next == null;
        assert d.first.prev == null;
        assert d.last.item.equals("Bogdan");
        assert d.last.next == null;
        assert d.last.prev == null;
        assert d.first == d.last;

        assert (item.equals("Roman")) : "should be Roman";
        item = d.removeLast();
        assert d.first == null;
        assert d.last == null;
        assert (item.equals("Bogdan")) : "should be Bogdan";
        assert d.isEmpty() : "isEmpty";
        assert d.size() == 0 : "size 0 initially";
        d.addLast("X");
        d.addLast("Y");
        d.addLast("Z"); // first -- X Y Z -- last
        assert d.first.prev == null;
        assert d.last.next == null;

        item = d.removeFirst();
        assert (item.equals("X"));
        item = d.removeFirst();
        assert (item.equals("Y"));
        assert d.size() == 1 : "size 1";

        assert d.first.item.equals("Z");
        assert d.first.next == null;
        assert d.first.prev == null;
        assert d.last.next == null;
        assert d.last.prev == null;
        assert d.last == d.first;

        assert !d.isEmpty() : "not Empty";
        item = d.removeFirst();
        assert (item.equals("Z"));
        assert d.isEmpty() : "isEmpty";
        assert d.size() == 0 : "size 0 initially";

    }
}
