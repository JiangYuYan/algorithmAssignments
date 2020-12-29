import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private final int thisN;
    private final int thisNofFlowNet;
    private final int[] thisW; // wins
    private final int[] thisL; // losses
    private final int[] thisR; // remaining games
    private final int[][] thisG; // games left to play between i and j
    private final HashMap<String, Integer> thisTeams;
    private final String[] thisTeamNames;
    private int thisMaxWins;
    private int thisMaxWinsId;
    private final int fullRemaining;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        thisN = in.readInt();
        thisNofFlowNet = thisN * (thisN - 1) / 2 + thisN + 1;
        thisW = new int[thisN];
        thisL = new int[thisN];
        thisR = new int[thisN];
        thisG = new int[thisN][thisN];
        thisTeamNames = new String[thisN];
        thisTeams = new HashMap<String, Integer>(thisN);
        thisMaxWins = 0;
        for (int i = 0; i < thisN; ++i) {
            String team = in.readString();
            thisTeamNames[i] = team;
            thisTeams.put(team, i);
            thisW[i] = in.readInt();
            thisL[i] = in.readInt();
            thisR[i] = in.readInt();
            for (int j = 0; j < thisN; ++j)
                thisG[i][j] = in.readInt();
            if (thisW[i] > thisMaxWins) {
                thisMaxWins = thisW[i];
                thisMaxWinsId = i;
            } 
        }
        int temp = 0;
        for (int i = 1; i < thisN; ++i) {
            for (int j = 0; j < i; ++j) {
                temp += thisG[i][j];
            }
        }
        fullRemaining = temp;
    }

    // number of teams
    public              int numberOfTeams() {
        return thisN;
    }

    // all teams
    public Iterable<String> teams() {
        return thisTeams.keySet();
    }

    // number of wins for given team
    public              int wins(String team) {
        check(team);
        return thisW[thisTeams.get(team)];
    }

    // number of losses for given team
    public              int losses(String team) {
        check(team);
        return thisL[thisTeams.get(team)];
    }

    // number of remaining games for given team
    public              int remaining(String team) {
        check(team);
        return thisR[thisTeams.get(team)];
    }

    // number of remaining games between team1 and team2
    public              int against(String team1, String team2) {
        check(team1);
        check(team2);
        return thisG[thisTeams.get(team1)][thisTeams.get(team2)];
    }

    // is given team eliminated?
    public          boolean isEliminated(String team) {
        check(team);
        int id = thisTeams.get(team);
        if (thisW[id] + thisR[id] < thisMaxWins)
            return true;
        int remaining = fullRemaining;
        for (int i = 0; i < thisN; ++i) {
            remaining -= thisG[id][i];
        }
        FordFulkerson ford = getFordFulkerson(id);
        if (ford.value() < remaining)
            return true;
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) {
            HashSet<String> set = new HashSet<String>();
            int id = thisTeams.get(team);
            if (thisW[id] + thisR[id] < thisMaxWins) {
                set.add(thisTeamNames[thisMaxWinsId]);
            } else {
                FordFulkerson ford = getFordFulkerson(id);
                int thisV = thisN;
                for (int i = 1; i < thisN; ++i) {
                    for (int j = 0; j < i; ++j) {
                        if (i != id && j != id) {
                            if (ford.inCut(thisV)) {
                                // StdOut.println(thisV + " " + i + " " + j);
                                set.add(thisTeamNames[i]);
                                set.add(thisTeamNames[j]);
                            }
                        }
                        ++thisV;
                    }
                }
            }
            return set;
        } else {
            return null;
        }
        
    }

    private FordFulkerson getFordFulkerson(int index) {
        FlowNetwork fnw = new FlowNetwork(thisNofFlowNet);
        int thisV = thisN;
        for (int i = 1; i < thisN; ++i) {
            for (int j = 0; j < i; ++j) {
                if (i != index && j != index) {
                    fnw.addEdge(new FlowEdge(thisV, i, Double.POSITIVE_INFINITY));
                    fnw.addEdge(new FlowEdge(thisV, j, Double.POSITIVE_INFINITY));
                    fnw.addEdge(new FlowEdge(index, thisV, thisG[i][j]));
                }
                ++thisV;
            }
        }

        for (int i = 0; i < thisN; ++i) {
            if (i != index) {
                fnw.addEdge(new FlowEdge(i, thisNofFlowNet - 1, thisW[index] + thisR[index] - thisW[i]));
            }
        }
        // StdOut.println(thisV);
        // StdOut.println();
        // StdOut.println("Index = " + index);
        // StdOut.println(fnw.toString());
        return new FordFulkerson(fnw, index, thisNofFlowNet - 1);
    }

    private void check(String team) {
        if (!thisTeams.containsKey(team))
            throw new IllegalArgumentException("Invalid team.");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
