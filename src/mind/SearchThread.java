package mind;
import movegen.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchThread implements Runnable {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Board board;

    public void startSearch(Board board){
        this.board = board;
        Thread worker = new Thread(this);
        worker.start();
    }

    public void run(){
        running.set(true);
        Search.itDeep(board);
        System.out.println("info score cp " + Search.getScore());
        System.out.println("bestmove " + Search.getMove().uci());
        TranspTable.reset();
        System.gc();
        Limits.resetTime();
        running.set(false);
    }

    public Move getMove(){
        return Search.getMove();
    }

    public int getScore(){
        return Search.getScore();
    }

    public void stop(){
        Search.stop();
        Search.waitForStop = false;
        TranspTable.reset();
        MoveOrder.clearKillers();
        MoveOrder.clearHistory();
        System.gc();
        Limits.resetTime();
        running.set(false);
    }
}
