/// unfinished, for future use.


package mind;

import movegen.Move;

public class TranspTable {
//    private final static int tableSize = 0x10000000;
    private final static int tableSize = 10;
    private final static int mod = tableSize - 1;
    private static final TTEntry[] table = new TTEntry[tableSize];


    public static void set(long key_, int score_, int depth_, int flag_, Move bestMove_){
        table[(int)(key_ & mod)] = new TTEntry(key_, score_, depth_, flag_, bestMove_);

    }

    public static TTEntry get(long key_){
        int tableKey = (int)(key_ & mod);
        if (table[tableKey] == null || table[tableKey].key() != key_)
            return null;
        return table[tableKey];
    }

    public static void reset(){
        for (int i = 0; i < tableSize; i++)
            table[i] = null;
    }

}
