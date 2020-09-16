/// unfinished, for future use.


package mind;

import movegen.*;

public class TTEntry {
    public final static int EXACT = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;

    private final long key;
    private final int score, depth, flag;
    private final int bestMove;

    public TTEntry(long key_, int score_, int depth_, int flag_, Move bestMove_){
        key = key_;
        score = score_;
        depth = depth_;
        flag = flag_;
        bestMove = bestMove_.move();
    }

    public long key(){
        return key;
    }

    public int score(){
        return score;
    }

    public int depth(){
        return depth;
    }

    public int flag(){
        return flag;
    }

    public Move move(){
        return new Move(bestMove);
    }

}
