package mind;

import movegen.*;
import java.util.Comparator;


public class MoveOrder {
    private static final int[][] MvvLvaScores = new int[6][6];
    static {
        final int[] VictimScore = {100, 200, 300, 400, 500, 600};
        for (int attacker = PieceType.PAWN; attacker <= PieceType.KING; attacker++) {
            for (int victim = PieceType.PAWN; victim <= PieceType.KING; victim++) {
                MvvLvaScores[victim][attacker] = VictimScore[victim] + 6 - (VictimScore[attacker] / 100);
            }
        }
    }

    public static int getMvvLvaScore(Board board, Move move){
        return MvvLvaScores[board.pieceTypeAt(move.to())][board.pieceTypeAt(move.from())];
    }
    
    public static MoveList moveOrdering(final Board board, final MoveList moves){
        MoveList promotions = new MoveList();
        MoveList captures = new MoveList();
        MoveList quiet = new MoveList();
        for (Move move : moves){
            switch (move.flags()) {
                case Move.PC_BISHOP, Move.PC_KNIGHT, Move.PC_ROOK, Move.PC_QUEEN, Move.PR_BISHOP, Move.PR_KNIGHT, Move.PR_ROOK, Move.PR_QUEEN -> promotions.add(move);
                case Move.CAPTURE -> captures.add(move);
                case Move.QUIET, Move.EN_PASSANT, Move.DOUBLE_PUSH, Move.OO, Move.OOO -> quiet.add(move);
            }
        }

        Comparator<Move> compareByMvvLva = (Move move1, Move move2) -> Integer.compare(getMvvLvaScore(board, move2), getMvvLvaScore(board, move1));
        captures.sort(compareByMvvLva);
        MoveList sortedMoves = new MoveList();
        sortedMoves.addAll(captures);
        sortedMoves.addAll(promotions);
        sortedMoves.addAll(quiet);
        return sortedMoves;
    }
}
