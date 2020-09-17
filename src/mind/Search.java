package mind;
import movegen.*;

public class Search {
    final public static int MAX_SEARCH_DEPTH = 9;
    final public static int INF = 99999;
    public static boolean stop;
    public Move IDMove = null;
    public int IDScore = -INF;
    public static int nodes = 0;

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
        for (int depth = 1; depth <= searchDepth; depth++) {
            negaMaxRoot(board, depth);
            System.out.print("info");
            System.out.print(" currmove " + IDMove.uci());
            System.out.print(" depth " + depth);
            System.out.print(" score cp " + IDScore);
            System.out.println(" nodes " + nodes);
            nodes = 0;
            long elapsed = System.currentTimeMillis() - Limits.startTime;
            if (elapsed >= Limits.timeAllocated/2)
                break;
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
        int value;
        Move bestMove = null;
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

    public int negamax(Board board, int depth, int alpha, int beta){
        if (stop || Limits.checkLimits()) {
            stop = true;
            return INF;
        }

        if (board.isThreefoldOrFiftyMove())
            return 0;

        // check to see if the board state has already been encountered

        int alphaOrig = alpha;
        final TTEntry ttEntry = TranspTable.get(board.hash());
        if (ttEntry != null && ttEntry.depth() >= depth) {
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
            if (alpha >= beta)
                return ttEntry.score();
        }

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
        Move bestMove = null;
        moves = MoveOrder.moveOrdering(board, moves);
        for (Move move : moves){
            board.push(move);
            value = -negamax(board, depth - 1 + checkExtension, -beta, -alpha);
            board.pop();
            if (value >= beta){
                TranspTable.set(board.hash(), value, depth, TTEntry.LOWER_BOUND, move);
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

    public int qSearch(Board board, int alpha, int beta, int depth){
        nodes++;
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
