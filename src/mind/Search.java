package mind;
import movegen.*;

public class Search {
    public static int maxSearchDepth;
    public static boolean waitForStop;

    private final static int NullMinDepth = 5;
    private final static int LMRMinDepth = 2;
    private final static int LMRMoveWOReduction = 3;
    private final static int LMRPVReduction = 1;
    private final static int LMRNonPVDiv = 3;
    private final static int AspirationWindow = 25;
    private final static int INF = 999999;

    private static boolean stop;
    private static Move IDMove = null;
    private static int IDScore = 0;
    private static int selDepth;

    public Search(){}

    public static void itDeep(Board board){
        Limits.calcTime(board.getSideToPlay(), board.gamePly());
        Limits.startTime = System.currentTimeMillis();
        IDMove = null;
        IDScore = 0;
        selDepth = 0;
        stop = false;
        int alpha = -INF;
        int beta = INF;
        int depth = 1;
        while (depth <= maxSearchDepth || waitForStop) {
            long elapsed = System.currentTimeMillis() - Limits.startTime;
            if (stop || elapsed >= Limits.timeAllocated / 2 || isScoreCheckmate(IDScore))
                break;
            negaMaxRoot(board, depth, alpha, beta);
            if (IDScore <= alpha) {
                alpha = -INF;
            }
            else if (IDScore >= beta){
                beta = INF;
            }
            else {
                printInfo(board, depth);
                alpha = IDScore - AspirationWindow;
                beta = IDScore + AspirationWindow;
                depth++;
                Statistics.reset();
            }
        }
    }

    public static void negaMaxRoot(Board board, int depth, int alpha, int beta){
        MoveList moves = board.generateLegalMoves();
        if (moves.size() == 1) {
            IDMove = moves.get(0);
            IDScore = 0;
            return;
        }

        moves = MoveOrder.moveOrdering(board, moves, 0);
        int value;
        Move bestMove = null;
        for (Move move : moves){
            board.push(move);
            value = -negaMax(board, depth - 1, 1, -beta, -alpha, true);
            board.pop();
            if (stop || Limits.checkLimits()) {
                stop = true;
                break;
            }
            if (value > alpha){
                alpha = value;
                bestMove = move;
            }
        }
        if (bestMove == null)
            bestMove = moves.get(0);

        if (!stop) {
            TranspTable.set(board.hash(), alpha, depth, TTEntry.EXACT, bestMove);
            IDMove = bestMove;
            IDScore = alpha;
        }
    }

    public static int negaMax(Board board, int depth, int ply, int alpha, int beta, boolean canApplyNull){

        if (stop || Limits.checkLimits()) {
            stop = true;
            return 0;
        }

        // check if King is attacked. If so, we may be in checkmate. If we're not in checkmate, extend the
        // search depth by 1. We use inCheck to see if we're in checkmate or stalemate.

        Statistics.nodes++;
        // look for checkmate. If only in check, this will simply generate the moves.
        MoveList moves = board.generateLegalMoves();
        boolean inCheck = board.checkers() != 0;
        if (moves.size() == 0 && inCheck) {
            Statistics.leafs++;
            return -INF + ply;
        }

        if (board.isDraw(moves)) {
            Statistics.leafs++;
            return 0;
        }

        if (depth + (inCheck ? 1 : 0) <= 0) {
            Statistics.leafs++;
            return qSearch(board, depth, ply, alpha, beta);
        }

        // check to see if the board state has already been encountered
        int alphaOrig = alpha;
        final TTEntry ttEntry = TranspTable.get(board.hash());
        if (ttEntry != null && ttEntry.depth() >= depth) {
            Statistics.ttHits++;
            switch (ttEntry.flag()) {
                case TTEntry.EXACT:
                    return ttEntry.score();
                case TTEntry.UPPER_BOUND:
                    beta = Math.min(beta, ttEntry.score());
                    break;
                case TTEntry.LOWER_BOUND:
                    alpha = Math.max(alpha, ttEntry.score());
                    break;
            }
            if (alpha >= beta) {
                Statistics.leafs++;
                return ttEntry.score();
            }
        }

        // Do a null move check if allowed
        if (canApplyNullWindow(board, depth, canApplyNull)) {
            int r = depth > 6 ? 3 : 2;
            board.pushNull();
            int value = -negaMax(board, depth - r - 1, ply + 1, -beta, -beta + 1, false);
            board.popNull();
            if (value >= beta){
                Statistics.betaCutoffs++;
                return beta;
            }
        }

        int value;
        Move bestMove = null;
        MoveOrder.scoreMoves(board, moves, ply);
        for (int i = 0; i < moves.size(); i++){
            MoveOrder.SortNextBestMove(moves, i);
            Move move = moves.get(i);
            int reducedDepth = depth;
            if (canApplyLMR(board, depth, move, i, inCheck))
                reducedDepth = depth - 1;
            board.push(move);
            value = -negaMax(board, reducedDepth - 1, ply + 1, -beta, -alpha, true);
            board.pop();
            if (value >= beta){
                TranspTable.set(board.hash(), value, depth, TTEntry.LOWER_BOUND, move);
                if (move.flags() == Move.QUIET) {
                    MoveOrder.addKiller(board, move, ply);
                    MoveOrder.addHistory(board, move, depth);
                }
                Statistics.betaCutoffs++;
                return beta;
            }
            if (value > alpha) {
                bestMove = move;
                alpha = value;
            }

        }

        if (bestMove != null){
            int flag;
            if (alpha <= alphaOrig)
                flag = TTEntry.UPPER_BOUND;
            else
                flag = TTEntry.EXACT;
            TranspTable.set(board.hash(), alpha, depth, flag, bestMove);
        }
        return alpha;
    }

    public static int qSearch(Board board, int depth, int ply, int alpha, int beta){
        selDepth = Math.max(ply, selDepth);
        Statistics.qnodes++;
        if (stop || Limits.checkLimits()){
            stop = true;
            return 0;
        }

        if (board.kingAttacked() && board.generateLegalMoves().size() == 0) {
            Statistics.qleafs++;
            return -INF + ply;
        }

        int standPat = Evaluation.evaluateState(board);
        if (standPat >= beta) {
            Statistics.qleafs++;
            return beta;
        }

        if (alpha < standPat)
            alpha = standPat;

        MoveList moves = board.generateLegalQMoves();
        MoveOrder.scoreMoves(board, moves, ply);
        int value;
        for (int i = 0; i < moves.size(); i++){
            MoveOrder.SortNextBestMove(moves, i);
            Move move = moves.get(i);
            board.push(move);
            value = -qSearch(board, depth - 1, ply + 1, -beta, -alpha);
            board.pop();
            if (value >= beta) {
                Statistics.qbetaCutoffs++;
                return beta;
            }
            if (value > alpha)
                alpha = value;


        }
        return alpha;
    }

    public static boolean isScoreCheckmate(int score){
        return Math.abs(score) >= INF/2;
    }

    public static boolean canApplyNullWindow(Board board, int depth, boolean canApplyNull){
        return canApplyNull && depth >= NullMinDepth && board.phase() >= 12 && !board.kingAttacked();
    }

    public static boolean canApplyLMR(Board board, int depth, Move move, int moveIndex, boolean fromCheck){
        return depth >= LMRMinDepth && moveIndex >= LMRMoveWOReduction && move.flags() == Move.QUIET
                && !fromCheck && !board.kingAttacked();
    }

    public static String getPv(Board board, int depth){
        Move bestMove;
        if (TranspTable.get(board.hash()) == null || depth == 0)
            return "";
        else
            bestMove = TranspTable.get(board.hash()).move();
        board.push(bestMove);
        String pV = bestMove.uci() + " " + getPv(board, depth - 1);
        board.pop();
        return pV;
    }

    public static Move getMove(){
        if (IDMove != null)
            return IDMove;
        else
            return Move.nullMove();
    }

    public static int getScore(){
        return IDScore;
    }

    public static void stop(){
        stop = true;
    }

    public static void printInfo(Board board, int depth){
        System.out.print("info");
        System.out.print(" currmove " + IDMove.uci());
        System.out.print(" depth " + depth);
        System.out.print(" seldepth " + selDepth);
        System.out.print(" time " + Limits.timeElapsed());
        System.out.print(" score cp " + IDScore);
        System.out.print(" nodes " + Statistics.totalNodes());
        System.out.println(" pv " + getPv(board, depth));
    }
}