import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // use linked list ADT
    private Node thisFirst;
    private Node thisLast;
    private int thisN;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        thisFirst = null;
        thisLast = null;
        thisN = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return thisN == 0;
    }

    // return the number of items on the deque
    public int size() {
        return thisN;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("addFirst argument can't be null");
        Node oldFirst = thisFirst;
        thisFirst = new Node();
        thisFirst.item = item;
        thisFirst.next = oldFirst;
        if (isEmpty()) thisLast = thisFirst;
        else           oldFirst.prev = thisFirst;
        ++thisN;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("addLast argument can't be null.");
        Node oldLast = thisLast;
        thisLast = new Node();
        thisLast.item = item;
        thisLast.prev = oldLast;
        if (isEmpty()) thisFirst = thisLast;
        else           oldLast.next = thisLast;
        ++thisN;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow front.");
        Item item = thisFirst.item;
        thisFirst = thisFirst.next;
        --thisN;        
        if (isEmpty()) thisLast = null;
        else           thisFirst.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow back.");
        Item item = thisLast.item;
        thisLast = thisLast.prev;
        --thisN;
        if (isEmpty()) thisFirst = null;
        else           thisLast.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = thisFirst;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more item in iterator.");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("No remove in Iterator.");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        Deque<Integer> queue = new Deque<Integer>();
        StdOut.println(queue.isEmpty());
        for (int i = 0; i < n; i++) {
            queue.addFirst(i);
        }
        for (int a : queue) {
            StdOut.print(a + " ");
        }
        StdOut.println();
        for (int i = 0; i < n; i++) {
            queue.addLast(i);
        }
        for (int a : queue) {
            StdOut.print(a + " ");
        }
        StdOut.println(queue.size());
        StdOut.println(queue.removeFirst());
        StdOut.println(queue.removeLast());
        for (int a : queue) {
            StdOut.print(a + " ");
        }
        StdOut.println(queue.size());
    }

}
