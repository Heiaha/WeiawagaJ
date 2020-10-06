package mind;
import movegen.*;

public class Search {
    final public static int MAX_SEARCH_DEPTH = 25;
    final public static int INF = 99999;
    public static boolean stop;
    public static Move IDMove = null;
    public static int IDScore = -INF;

    private final static int NullMinDepth = 5;
    private final static int LMRMinDepth = 2;
    private final static int LMRMoveWOReduction = 3;
    private final static int LMRPVReduction = 1;
    private final static int LMRNonPVDiv = 3;

    public Search(){}

    public static void itDeep(Board board){
        Limits.calcTime(board.getSideToPlay(), board.game_ply);
        Limits.startTime = System.currentTimeMillis();
        int searchDepth = MAX_SEARCH_DEPTH;
        if (Evaluation.getPhaseValue(board) > 10)
            searchDepth += 2;
        IDMove = null;
        IDScore = -INF;
        stop = false;
        for (int depth = 1; depth <= searchDepth; depth++) {
            negaMaxRoot(board, depth);
            System.out.print("info");
            System.out.print(" currmove " + IDMove.uci());
            System.out.print(" depth " + depth);
            System.out.print(" score cp " + IDScore);
            System.out.println(" nodes " + Statistics.totalNodes());
            Statistics.reset();
            long elapsed = System.currentTimeMillis() - Limits.startTime;
            if (elapsed >= Limits.timeAllocated/2 || isScoreCheckmate(IDScore))
                break;
        }
    }

    public static void negaMaxRoot(Board board, int depth){
        int alpha = -INF;
        int beta = INF;
        MoveList moves = board.generateLegalMoves();
        if (moves.size() == 1) {
            IDMove = moves.get(0);
            return;
        }

        moves = MoveOrder.moveOrdering(board, moves, 0);
        int value;
        Move bestMove = null;
        for (Move move : moves){
            board.push(move);
            value = -negamax(board, depth - 1, 1, -beta, -alpha, true);
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

        if (!stop){
            TranspTable.set(board.hash(), alpha, depth, TTEntry.EXACT, bestMove);
            IDMove = bestMove;
            IDScore = alpha;
        }
    }

    public static int negamax(Board board, int depth, int ply, int alpha, int beta, boolean pV){

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
        if (canApplyNullWindow(board, depth, pV)){
            int r = depth > 6 ? 3 : 2;
            board.pushNull();
            int value = -negamax(board, depth - r - 1, ply + 1, -beta, -beta + 1, false);

            board.popNull();
            if (value >= beta){
                Statistics.betaCutoffs++;
                return beta;
            }
        }

        int value;
        int moveIndex = 0;
        Move bestMove = null;
        moves = MoveOrder.moveOrdering(board, moves, ply);
        boolean pVS = true;
        for (Move move : moves){

            board.push(move);
            if (pVS) {
                value = -negamax(board, depth - 1, ply + 1, -beta, -alpha, true);
                pVS = false;
            }
            else{
                int reducedDepth = depth;
                if (canApplyLMR(board, depth, move, moveIndex, inCheck))
                    reducedDepth = LMRReducedDepth(depth, pV);
                value = -negamax(board, reducedDepth - 1, ply + 1, -alpha - 1, -alpha, false);
                if (value > alpha)
                    value = -negamax(board, depth - 1, ply  + 1,  -beta, -alpha, false);
            }
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
            moveIndex++;
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
        moves = MoveOrder.moveOrdering(board, moves, ply);
        int value;
        for (Move move : moves){
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

    public static boolean canApplyNullWindow(Board board, int depth, boolean pV){
        return !pV && depth >= NullMinDepth && !board.kingAttacked();
    }

    public static boolean canApplyLMR(Board board, int depth, Move move, int moveIndex, boolean fromCheck){
        return depth >= LMRMinDepth && moveIndex >= LMRMoveWOReduction && move.flags() == Move.QUIET
                && !fromCheck && !board.kingAttacked();
    }

    public static int LMRReducedDepth(int depth, boolean pV){
        return pV ?
            depth - LMRPVReduction :
            depth / LMRNonPVDiv;
    }
}