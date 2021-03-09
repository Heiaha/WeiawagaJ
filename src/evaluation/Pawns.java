package evaluation;

import movegen.*;

public class Pawns {

    public static int evaluate(final Board board, final int side){
        int score = 0;
        score += EConstants.PAWN_SCORES[EConstants.IX_PASSED_PAWN_VALUE] * nPassedPawns(board, side);
        score += EConstants.PAWN_SCORES[EConstants.IX_DOUBLED_PAWN_PENALTY] * nDoubledPawns(board.bitboardOf(side, PieceType.PAWN));
        score += EConstants.PAWN_SCORES[EConstants.IX_ISOLATED_PAWN_PENALTY] *  nIsolatedPawns(board.bitboardOf(side, PieceType.PAWN));
        return score;
    }

    public static int nPassedPawns(final Board board, final int side){
        long ourPawns = board.bitboardOf(side, PieceType.PAWN);
        long enemyPawns = board.bitboardOf(Side.flip(side), PieceType.PAWN);
        long fill = 0;
        fill |= Bitboard.shift(enemyPawns, Square.relative_dir(Square.SOUTH_WEST, side));
        fill |= Bitboard.shift(enemyPawns, Square.relative_dir(Square.SOUTH_EAST, side));
        fill = Bitboard.fill(fill, Square.relative_dir(Square.SOUTH, side));
        return Bitboard.popcount(ourPawns & ~fill);
    }

    public static int nDoubledPawns(final long pawnBb){
        long fill = Bitboard.shift(pawnBb, Square.NORTH);
        fill = Bitboard.fill(fill, Square.NORTH);
        return Bitboard.popcount(fill & pawnBb);
    }

    public static int nIsolatedPawns(final long pawnBb){
        return Bitboard.popcount(
                (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb, Square.EAST))) &
                        (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb,Square.WEST)))
        );
    }
}
