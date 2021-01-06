import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Topological;
import java.util.HashMap;

public class WordNet {
    private HashMap<String, Bag<Integer>> thisHM;
    private int thisN;
    private String[] thisSynset;
    private Digraph thisG;
    private final SAP thisSap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Input of WordNet is null.");
        buildST(synsets); // thisHM; thisN; thisSynset; thisGloss;
        buildDG(hypernyms);
        thisSap = new SAP(thisG);
        Topological topo = new Topological(thisG);
        if (!topo.hasOrder())
            throw new IllegalArgumentException("Not a DAG.");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return thisHM.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Input of isNoun is null.");
        return thisHM.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Input is not a WordNet noun.");
        return thisSap.length(thisHM.get(nounA), thisHM.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Input is not a WordNet noun.");
        int ancestor = thisSap.ancestor(thisHM.get(nounA), thisHM.get(nounB));
        if (ancestor != -1)
            return thisSynset[ancestor];
        else
            return "";
    }

    private void buildST(String synsets) {
        thisHM = new HashMap<String, Bag<Integer>>();
        Queue<String> queue = new Queue<String>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] str = in.readLine().split(",");
            int id = Integer.parseInt(str[0]);
            queue.enqueue(str[1]);
            String[] nouns = str[1].split(" ");
            for (int i = 0; i < nouns.length; ++i) {
                String nounsi = nouns[i];
                Bag<Integer> bag;
                if (thisHM.containsKey(nounsi))
                    bag = thisHM.get(nounsi);
                else
                    bag = new Bag<Integer>();
                bag.add(id);
                thisHM.put(nounsi, bag);
            }
        }
        thisN = queue.size();
        if (thisN == 0)
            throw new IllegalArgumentException("Empty input.");
        thisSynset = new String[thisN];
        for (int i = 0; i < thisN; ++i) {
            thisSynset[i] = queue.dequeue();
        }
    }

    private void buildDG(String hypernyms) {
        thisG = new Digraph(thisN);
        In in = new In(hypernyms);
        boolean[] root = new boolean[thisN];
        while (in.hasNextLine()) {
            String[] str = in.readLine().split(",");
            int v = Integer.parseInt(str[0]);
            for (int i = 1; i < str.length; ++i) {
                thisG.addEdge(v, Integer.parseInt(str[i]));
                root[v] = true;
            }
        }
        int countRooted = 0;
        for (int i = 0; i < thisN; ++i) {
            if (!root[i])
                countRooted++;
        }
        if (countRooted != 1)
            throw new IllegalArgumentException("Not rooted.");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.sap(args[2], args[3]));
        StdOut.println(wordnet.distance(args[2], args[3]));
    }
}