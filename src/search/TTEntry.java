package search;
import movegen.*;

public class TTEntry {
    public final static byte EXACT = 0, LOWER_BOUND = 1, UPPER_BOUND = 2;
    public final static int SIZE = 10; // in bytes

    private final int score;
    private final byte depth, flag;
    private final int bestMove;

    public TTEntry(int score, int depth, int flag, Move bestMove){
        this.score = score;
        this.depth = (byte)depth;
        this.flag = (byte)flag;
        this.bestMove = bestMove.move();
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
