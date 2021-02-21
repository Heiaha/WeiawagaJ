package mind;

import movegen.Move;
import java.util.HashMap;

public class TranspTable {
    private final static HashMap<Long, TTEntry> table = new HashMap<>();

    public static void set(long key, int score, int depth, int flag, Move bestMove){
        table.put(key, new TTEntry(score, depth, flag, bestMove));
    }

    public static TTEntry probe(long key){
        return table.get(key);
    }

    public static void reset(){
        table.clear();
    }
}