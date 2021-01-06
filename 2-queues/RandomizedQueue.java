import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // use array ADT
    private static final int INIT_CAPACITY = 1;
    private Item[] thisArray;
    private int thisN;

    // construct an empty randomized queue
    public RandomizedQueue() {
        thisArray = (Item[]) new Object[INIT_CAPACITY];
        thisN = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return thisN == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return thisN;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("enqueue argument can't be null.");
        if (thisN == thisArray.length) resize(2 * thisArray.length);
        thisArray[thisN++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Empty.");
        int index = StdRandom.uniform(thisN--);
        Item item = thisArray[index];
        thisArray[index] = thisArray[thisN];
        thisArray[thisN] = null;
        if (thisN > 0 && thisN == thisArray.length/4) resize(thisArray.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Empty.");
        return thisArray[StdRandom.uniform(thisN)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int i;
        private int[] index;
        public RandomIterator() {
            i = thisN - 1;
            index = new int[thisN];
            index = StdRandom.permutation(thisN);
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more item in iterator.");
            return thisArray[index[i--]];
        }

        public void remove() {
            throw new UnsupportedOperationException("No remove in Iterator.");
        }
    }

    private void resize(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("capacity must be positive integer.");
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i != thisN; ++i) {
            copy[i] = thisArray[i];
        }
        thisArray = copy;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
