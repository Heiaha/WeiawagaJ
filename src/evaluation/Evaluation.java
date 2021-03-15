package evaluation;
import movegen.*;
import search.Score;


public class Evaluation {
    public static int whiteKingSq;
    public static int blackKingSq;
    public static long allWhitePieces;
    public static long allBlackPieces;
    public static long allPieces;


    public static void initEval( Board board){
        allWhitePieces = board.allPieces(Side.WHITE);
        allBlackPieces = board.allPieces(Side.BLACK);
        allPieces = allWhitePieces ^ allBlackPieces;
        whiteKingSq = Bitboard.lsb(board.bitboardOf(Piece.WHITE_KING));
        blackKingSq = Bitboard.lsb(board.bitboardOf(Piece.BLACK_KING));
    }

    public static boolean hasBishopPair(long bishopBb){
        return ((bishopBb & Bitboard.LIGHT_SQUARES) != 0) && ((bishopBb & Bitboard.DARK_SQUARES) != 0);
    }

    public static int pawnsShieldingKing(Board board, int side){
        int kingSq = side == Side.WHITE ? whiteKingSq : blackKingSq;
        long pawns = board.bitboardOf(side, PieceType.PAWN);
        return Bitboard.popcount(EConstants.PAWN_SHIELD_MASKS[side][kingSq] & pawns);
    }

    public static int pawnsOnSameColorSquare(Board board, int square, int side){
        return Bitboard.popcount(board.bitboardOf(side, PieceType.PAWN) &
                ((Bitboard.DARK_SQUARES & Square.getBb(square)) != 0 ? Bitboard.DARK_SQUARES : Bitboard.LIGHT_SQUARES));
    }

    public static int bishopScore(Board board, int side){
        long bishopsBb = board.bitboardOf(side, PieceType.BISHOP);
        int score = 0;
        if (hasBishopPair(bishopsBb))
            score += EConstants.BISHOP_SCORES[EConstants.IX_BISHOP_PAIR_VALUE];

        while (bishopsBb != 0) {
            int sq = Bitboard.lsb(bishopsBb);
            bishopsBb = Bitboard.extractLsb(bishopsBb);
            long attacks = Attacks.getBishopAttacks(sq, allPieces) & ~allPieces;

            if (Bitboard.popcount(attacks & Bitboard.CENTER) == 2)
                score += EConstants.BISHOP_SCORES[EConstants.IX_BISHOP_ATTACKS_CENTER];
            score += EConstants.BISHOP_SCORES[EConstants.IX_BISHOP_SAME_COLOR_PAWN_PENALTY]*pawnsOnSameColorSquare(board, sq, side);
        }
        return score;
    }

    public static int rookScore(Board board, int side){
        long rooksBb = board.bitboardOf(side, PieceType.ROOK);
        int ourKingSq = side == Side.WHITE ? whiteKingSq : blackKingSq;
        int score = 0;

        long ourPawns = board.bitboardOf(side, PieceType.PAWN);
        long enemyPawns = board.bitboardOf(Side.flip(side), PieceType.PAWN);

        while (rooksBb != 0){
            int sq = Bitboard.lsb(rooksBb);
            rooksBb = Bitboard.extractLsb(rooksBb);

            long rookFileBb = Square.getFileBb(sq);
            if ((ourPawns & rookFileBb) == 0){
                if ((enemyPawns & rookFileBb) == 0)
                    score += EConstants.ROOK_SCORES[EConstants.IX_ROOK_ON_OPEN_FILE];
                else
                    score += EConstants.ROOK_SCORES[EConstants.IX_ROOK_ON_SEMIOPEN_FILE];
            }

            int pieceMobility = Bitboard.popcount(Attacks.getRookAttacks(sq, allPieces) & ~allPieces);
            // check to see if the king has trapped a rook
            if (pieceMobility <= 3) {
                int kf = Square.getFile(ourKingSq);
                if ((kf < File.FILE_E) == (Square.getFile(sq) < kf))
                    score += EConstants.ROOK_SCORES[EConstants.IX_KING_TRAPPING_ROOK_PENALTY];
            }
        }
        return score;
    }

    public static int kingScore(Board board, int side){
        int score = 0;
        int pawnShield = pawnsShieldingKing(board, side);
        score += EConstants.KING_SCORES[EConstants.IX_KING_PAWN_SHIELD_BONUS]*pawnShield;
        return score;
    }

    public static int pSqScore(Board board, int side){
        int score = 0;
        for (int pt = PieceType.PAWN; pt <= PieceType.KING; pt++) {
            long bb = board.bitboardOf(side, pt);
            while (bb != 0){
                int sq = Bitboard.lsb(bb);
                bb = Bitboard.extractLsb(bb);
                score += EConstants.PIECE_TYPE_TABLES[pt][Square.relativeSquare(sq, side)];
            }
        }
        return score;
    }

    public static int materialScore(Board board, int side){
        int score = 0;
        for (int pt = PieceType.PAWN; pt <= PieceType.QUEEN; pt++) {
            long bb = board.bitboardOf(side, pt);
            score += EConstants.PIECE_TYPE_VALUES_TUNING[pt]*Bitboard.popcount(bb);
        }
        return score;
    }

    public static int evaluateState( Board board){
        initEval(board);
        int score = 0;
        score += board.materialScore();
        score += board.pSqScore();

        score += board.getSideToPlay() == Side.WHITE ? EConstants.TEMPO[0] : -EConstants.TEMPO[0];

        score += Pawns.evaluate(board, Side.WHITE);
        score -= Pawns.evaluate(board, Side.BLACK);

        score += bishopScore(board, Side.WHITE);
        score -= bishopScore(board, Side.BLACK);

        score += rookScore(board, Side.WHITE);
        score -= rookScore(board, Side.BLACK);

        score += kingScore(board, Side.WHITE);
        score -= kingScore(board, Side.BLACK);

        int finalScore = Score.eval(score, board.phase());
        return board.getSideToPlay() == Side.WHITE ? finalScore : -finalScore;
    }

    public static int evaluateForTune( Board board){
        initEval(board);
        int score = 0;

        score += materialScore(board, Side.WHITE);
        score -= materialScore(board, Side.BLACK);

        score += pSqScore(board, Side.WHITE);
        score -= pSqScore(board, Side.BLACK);

        score += board.getSideToPlay() == Side.WHITE ? EConstants.TEMPO[0] : -EConstants.TEMPO[0];

        score += Pawns.evaluate(board, Side.WHITE);
        score -= Pawns.evaluate(board, Side.BLACK);

        score += bishopScore(board, Side.WHITE);
        score -= bishopScore(board, Side.BLACK);

        score += rookScore(board, Side.WHITE);
        score -= rookScore(board, Side.BLACK);

        score += kingScore(board, Side.WHITE);
        score -= kingScore(board, Side.BLACK);

        return Score.eval(score, board.phase());
    }
}
