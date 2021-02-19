package mind;

import movegen.Move;
import java.util.HashMap;

public class TranspTable {
    private final static HashMap<Long, TTEntry> table = new HashMap<>();

    public static void set(long key_, int score_, int depth_, int flag_, Move bestMove_){
        table.put(key_, new TTEntry(key_, score_, depth_, flag_, bestMove_));
    }

    public static TTEntry probe(long key_){
        return table.get(key_);
    }

    public static void reset(){
        table.clear();
    }

}
