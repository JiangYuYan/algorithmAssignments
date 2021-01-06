import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet thisWordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        thisWordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String candidate = "";
        int dMax = 0;
        for (String s : nouns) {
            int d = accumulateDis(s, nouns);
            if (d > dMax) {
                dMax = d;
                candidate = s;
            }
        }
        return candidate;
    }

    private int accumulateDis(String noun, String[] nouns) {
        int distance = 0;
        for (String s : nouns)
            distance += thisWordnet.distance(noun, s);
        return distance;
    }
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}