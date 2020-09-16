package mind;
import movegen.*;

public class Search {
    final public static int MAX_SEARCH_DEPTH = 10;
    final public static int INF = 99999;
    public static boolean stop;
    public Move IDMove = null;
    public int IDScore = -INF;

    public Search(){}

    public void itDeep(Board board){
        Limits.calcTime(board.getSideToPlay(), board.game_ply);
        Limits.startTime = System.currentTimeMillis();
        int searchDepth = MAX_SEARCH_DEPTH;
        if (Evaluation.getPhaseValue(board) > 10)
            searchDepth += 2;
        IDMove = null;
        IDScore = -INF;
        stop = false;
        if (Limits.timeAllocated == Long.MAX_VALUE)
            negaMaxRoot(board, searchDepth);
        else {
            for (int depth = 1; depth <= searchDepth; depth++) {
                negaMaxRoot(board, depth);
                if (stop || (System.currentTimeMillis() - Limits.startTime) >= Limits.timeAllocated)
                    break;
            }
        }
    }

    public void negaMaxRoot(Board board, int depth){
        int alpha = -INF;
        int beta = INF;
        MoveList moves = board.generateLegalMoves();
        if (moves.size() == 1) {
            IDMove = moves.get(0);
            return;
        }

        moves = MoveOrder.moveOrdering(board, moves);
        if (IDMove != null){
            moves.remove(IDMove);
            moves.add(0, IDMove);
        }

        int value;
        for (Move move : moves){
            board.push(move);
            value = -negamax(board, depth - 1, -beta, -alpha);
            board.pop();
            if (stop || Limits.checkLimits()) {
                stop = true;
                break;
            }
            if (value > alpha){
                alpha = value;
                IDScore = alpha;
                IDMove = move;
            }
        }
    }

    public int negamax(Board board, int depth, int alpha, int beta){
        if (stop || Limits.checkLimits()) {
            stop = true;
            return INF;
        }

        if (board.isThreefoldOrFiftyMove())
            return 0;

        // check if King is attacked. If so, we may be in checkmate. If we're not in checkmate, extend the
        // search depth by 1. We use checkExtension to see if we're in checkmate or stalemate.
        int checkExtension = board.kingAttacked() ? 1 : 0;
        if (depth + checkExtension <= 0)
            return qSearch(board, alpha, beta, depth);

        // look for checkmate. If only in check, this will simply generate the moves.
        MoveList moves = board.generateLegalMoves();
        if (moves.size() == 0)
            return checkExtension == 1 ? -INF - depth : 0;

        int value;
        moves = MoveOrder.moveOrdering(board, moves);
        for (Move move : moves){
            board.push(move);
            value = -negamax(board, depth - 1 + checkExtension, -beta, -alpha);
            board.pop();
            if (value >= beta){
                return beta;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        return alpha;
    }

    public int qSearch(Board board, int alpha, int beta, int depth){
        if (stop || Limits.checkLimits()){
            stop = true;
            return 0;
        }

        if (board.kingAttacked() && board.generateLegalMoves().size() == 0)
            return -INF - depth;

        int standPat = Evaluation.evaluateState(board);
        if (standPat >= beta)
            return beta;
        if (alpha < standPat)
            alpha = standPat;

        MoveList moves = board.generateLegalQMoves();
        moves = MoveOrder.moveOrdering(board, moves);
        int value;
        for (Move move : moves){
            board.push(move);
            value = -qSearch(board, -beta, -alpha, depth - 1);
            board.pop();
            if (value >= beta)
                return beta;
            if (value > alpha)
                alpha = value;
        }
        return alpha;
    }




}
