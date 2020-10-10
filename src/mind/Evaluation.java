package mind;
import movegen.*;

public class Evaluation {
    public static int whiteKingSq;
    public static int blackKingSq;
    public static long allWhitePieces;
    public static long allBlackPieces;
    public static long allPieces;

    final static int TOTAL_PHASE = 24;
    public final static int[] PIECE_PHASES = {0, 1, 1, 2, 4, 0};

    final static Score TEMPO = new Score(20, 10);

    public final static Score[] PIECE_TYPE_VALUES = {
            new Score(70, 90),   // PAWN
            new Score(325, 325), // KNIGHT
            new Score(325, 325), // BISHOP
            new Score(500, 500), // ROOK
            new Score(975, 975), // QUEEN
            new Score(0, 0)      // KING
    };

    //pawn scoring
    final static Score PASSED_PAWN_VALUE = new Score(10, 50);
    final static Score DOUBLED_PAWN_PENALTY = new Score(-20, -10);
    final static Score ISOLATED_PAWN_PENALTY = new Score(-20, -10);

    //king scoring
    final static Score KING_PAWN_SHIELD_BONUS = new Score(10, 0);

    //rook scoring
    final static Score KING_TRAPPING_ROOK_PENALTY = new Score(-25, 0);
    final static Score ROOK_ON_OPEN_FILE = new Score(20, 0);
    final static Score ROOK_ON_SEMIOPEN_FILE = new Score(10, 0);

    //bishop scoring
    final static Score BISHOP_SAME_COLOR_PAWN_PENALTY = new Score(-3, -7); // per pawn
    final static Score BISHOP_ATTACKS_CENTER = new Score(30, 40);
    final static Score BISHOP_PAIR_VALUE = new Score(50, 50);

    //7th
    final static Score ROOK_ON_7TH = new Score(20, 40);
    final static Score QUEEN_ON_7TH = new Score(10, 20);

    //Queen dist from king
    final static int QUEEN_DIST_FROM_KING_INIT = 10;

    public final static int[] KNIGHT_OUTPOST_SCORE = {
            0, 0, 0,  0,  0, 0, 0, 0,
            0, 0, 0,  0,  0, 0, 0, 0,
            0, 0, 0,  0,  0, 0, 0, 0,
            0, 2, 5, 10, 10, 5, 2, 0,
            0, 2, 5, 10, 10, 5, 2, 0,
            0, 0, 4,  5,  5, 4, 0, 0,
            0, 0, 0,  0,  0, 0, 0, 0,
            0, 0, 0,  0,  0, 0, 0, 0,
    };

    public final static Score[][] PIECE_TABLES = {
        {
            new Score(0, 0),   new Score(0, 0),    new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),    new Score(0, 0),
            new Score(5, -15), new Score(10, -15), new Score(10, -15),  new Score(-20, -20), new Score(-20, -20), new Score(10, -15),  new Score(10, -15), new Score(5, -15),
            new Score(5, -5),  new Score(-5, -5),  new Score(-10, -10), new Score(0, -10),   new Score(0, -10),   new Score(-10, -10), new Score(-5, -5),  new Score(5, -5),
            new Score(0, 0),   new Score(0, 0),    new Score(0, 0),     new Score(20, 20),   new Score(20, 20),   new Score(0, 0),     new Score(0, 0),    new Score(0, 0),
            new Score(5, 20),  new Score(5, 20),   new Score(10, 20),   new Score(25, 25),   new Score(25, 25),   new Score(10, 20),   new Score(5, 20),   new Score(5, 20),
            new Score(10, 30), new Score(10, 30),  new Score(20, 40),   new Score(30, 45),   new Score(30, 45),   new Score(20, 40),   new Score(10, 40),  new Score(10, 30),
            new Score(50, 90), new Score(50, 90),  new Score(50, 90),   new Score(50, 90),   new Score(50, 90),   new Score(50, 90),   new Score(50, 90),  new Score(50, 90),
            new Score(0, 0),   new Score(0, 0),    new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),    new Score(0, 0),
        },
        {
            new Score(-50, -50), new Score(-40, -40), new Score(-30, -30), new Score(-30, -30), new Score(-30, -30), new Score(-30, -30), new Score(-40, -40), new Score(-50, -50),
            new Score(-40, -40), new Score(-20, -20), new Score(0, 0),     new Score(5, 5),     new Score(5, 5),     new Score(0, 0),     new Score(-20, -20), new Score(-40, -40),
            new Score(-30, -30), new Score(5, 5),     new Score(10, 10),   new Score(15, 15),   new Score(15, 15),   new Score(10, 10),   new Score(5, 5),     new Score(-30, -30),
            new Score(-30, -30), new Score(0, 0),     new Score(15, 15),   new Score(20, 20),   new Score(20, 20),   new Score(15, 15),   new Score(0, 0),     new Score(-30, -30),
            new Score(-30, -30), new Score(5, 5),     new Score(15, 15),   new Score(20, 20),   new Score(20, 20),   new Score(15, 15),   new Score(5, 5),     new Score(-30, -30),
            new Score(-30, -30), new Score(0, 0),     new Score(10, 10),   new Score(15, 15),   new Score(15, 15),   new Score(10, 10),   new Score(0, 0),     new Score(-30, -30),
            new Score(-40, -40), new Score(-20, -20), new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(-20, -20), new Score(-40, -40),
            new Score(-50, -50), new Score(-40, -40), new Score(-30, -30), new Score(-30, -30), new Score(-30, -30), new Score(-30, -30), new Score(-40, -40), new Score(-50, -50),
        },
        {
            new Score(-20, -20), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-20, -20),
            new Score(-10, -10), new Score(5, 5),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(5, 5),     new Score(-10, -10),
            new Score(-10, -10), new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(-10, -10),
            new Score(-10, -10), new Score(0, 0),     new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(10, 10),   new Score(0, 0),     new Score(-10, -10),
            new Score(-10, -10), new Score(5, 5),     new Score(5, 5),     new Score(10, 10),   new Score(10, 10),   new Score(5, 5),     new Score(5, 5),     new Score(-10, -10),
            new Score(-10, -10), new Score(0, 0),     new Score(5, 5),     new Score(10, 10),   new Score(10, 10),   new Score(5, 5),     new Score(0, 0),     new Score(-10, -10),
            new Score(-10, -10), new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(-10, -10),
            new Score(-20, -20), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-10, -10), new Score(-20, -20),
        },
        {
            new Score(0, 0),   new Score(0, 0),   new Score(10, 0),  new Score(20, 5),  new Score(20, 5),  new Score(10, 0),  new Score(0, 0),    new Score(0, 0),
            new Score(-5, -5), new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(-5, -5),
            new Score(-5, -5), new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(-5, -5),
            new Score(-5, -5), new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(-5, -5),
            new Score(-5, -5), new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(-5, -5),
            new Score(-5, -5), new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(-5, -5),
            new Score(5, 5),   new Score(10, 10), new Score(10, 10), new Score(10, 10), new Score(10, 10), new Score(10, 10), new Score(10, 10),  new Score(5, 5),
            new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),   new Score(0, 0),    new Score(0, 0),
        },
        {
            new Score(-20, -20), new Score(-10, -10), new Score(-10, -10), new Score(-5, -5), new Score(-5, -5), new Score(-10, -10), new Score(-10, -10), new Score(-20, -20),
            new Score(-10, -10), new Score(0, 0),     new Score(0, 0),     new Score(0, 0),   new Score(0, 0),   new Score(0, 0),     new Score(0, 0),     new Score(-10, -10),
            new Score(-10, -10), new Score(5, 5),     new Score(5, 5),     new Score(5, 5),   new Score(5, 5),   new Score(5, 5),     new Score(0, 0),     new Score(-10, -10),
            new Score(0, 0),     new Score(0, 0),     new Score(5, 5),     new Score(5, 5),   new Score(5, 5),   new Score(5, 5),     new Score(0, 0),     new Score(-5, -5),
            new Score(-5, -5),   new Score(0, 0),     new Score(5, 5),     new Score(5, 5),   new Score(5, 5),   new Score(5, 5),     new Score(0, 0),     new Score(-5, -5),
            new Score(-10, -10), new Score(0, 0),     new Score(5, 5),     new Score(5, 5),   new Score(5, 5),   new Score(5, 5),     new Score(0, 0),     new Score(-10, -10),
            new Score(-10, -10), new Score(0, 0),     new Score(0, 0),     new Score(0, 0),   new Score(0, 0),   new Score(0, 0),     new Score(0, 0),     new Score(-10, -10),
            new Score(-20, -20), new Score(-10, -10), new Score(-10, -10), new Score(-5, -5), new Score(-5, -5), new Score(-10, -10), new Score(-10, -10), new Score(-20, -20),
        },
        {
            new Score(20, -50),  new Score(30, -30),  new Score(10, -30),  new Score(0, -30),   new Score(0, -30),   new Score(10, -30),  new Score(30, -30),  new Score(20, -50),
            new Score(20, -30),  new Score(20, -30),  new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(0, 0),     new Score(20, -30),  new Score(20, -30),
            new Score(-10, -30), new Score(-20, -10), new Score(-20, 20),  new Score(-20, 30),  new Score(-20, 30),  new Score(-20, 20),  new Score(-20, -10), new Score(-10, -30),
            new Score(-20, -30), new Score(-30, -10), new Score(-30, 30),  new Score(-40, 40),  new Score(-40, 40),  new Score(-30, 30),  new Score(-30, -10), new Score(-20, -30),
            new Score(-30, -30), new Score(-40, -10), new Score(-40, 30),  new Score(-50, 40),  new Score(-50, 40),  new Score(-40, 30),  new Score(-40, -10), new Score(-30, -30),
            new Score(-30, -30), new Score(-40, -10), new Score(-40, 20),  new Score(-50, 30),  new Score(-50, 30),  new Score(-40, 20),  new Score(-40, -10), new Score(-30, -30),
            new Score(-30, -30), new Score(-40, -20), new Score(-40, -10), new Score(-50, 0),   new Score(-50, 0),   new Score(-40, -10), new Score(-40, -20), new Score(-30, -30),
            new Score(-30, -50), new Score(-40, -40), new Score(-40, -30), new Score(-50, -20), new Score(-50, -20), new Score(-40, -30), new Score(-40, -40), new Score(-30, -50),
        }
    };

    public static long[][] PAWN_SHIELD_MASKS = new long[2][64];
    static {
        for (int sq = Square.A1; sq <= Square.H8; sq++) {
            long sq_bb = Square.getBb(sq);
            PAWN_SHIELD_MASKS[Side.WHITE][sq] = ((sq_bb << 8) | ((sq_bb << 7) & ~File.getBb(File.FILE_H)) |
                    ((sq_bb << 9) & ~File.getBb(File.FILE_A))) & Rank.getBb(Rank.RANK_2);
            PAWN_SHIELD_MASKS[Side.BLACK][sq] = ((sq_bb >>> 8) | ((sq_bb >>> 7) & ~File.getBb(File.FILE_A)) |
                    ((sq_bb >>> 9) & ~File.getBb(File.FILE_H))) & Rank.getBb(Rank.RANK_7);
        }
    }

    public static void initEval(final Board board){
        allWhitePieces = board.allPieces(Side.WHITE);
        allBlackPieces = board.allPieces(Side.BLACK);
        allPieces = allWhitePieces | allBlackPieces;
        whiteKingSq = Bitboard.lsb(board.bitboardOf(Piece.WHITE_KING));
        blackKingSq = Bitboard.lsb(board.bitboardOf(Piece.BLACK_KING));
    }

    public static boolean hasBishopPair(final long bishopBb){
        return ((bishopBb & Bitboard.LIGHT_SQUARES) != 0) && ((bishopBb & Bitboard.DARK_SQUARES) != 0);
    }

    public static int nIsolatedPawns(final long pawnBb){
        return Bitboard.popcount(
                (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb, Square.EAST))) &
                    (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb,Square.WEST)))
        );
    }

    public static int nDoubledPawns(final long pawnBb){
        long fill = Bitboard.shift(pawnBb, Square.NORTH);
        fill = Bitboard.fill(fill, Square.NORTH);
        return Bitboard.popcount(fill & pawnBb);
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

    public static int pawnsShieldingKing(final Board board, int side){
        int kingSq = side == Side.WHITE ? whiteKingSq : blackKingSq;
        long pawns = board.bitboardOf(side, PieceType.PAWN);
        return Bitboard.popcount(PAWN_SHIELD_MASKS[side][kingSq] & pawns);
    }

    public static int pawnsOnSameColorSquare(final Board board, int square, int side){
        return Bitboard.popcount(board.bitboardOf(side, PieceType.PAWN) &
                ((Bitboard.DARK_SQUARES & Square.getBb(square)) != 0 ? Bitboard.DARK_SQUARES : Bitboard.LIGHT_SQUARES));
    }

    public static Score pawnScore(Board board, int side){
        Score pawnScore = new Score();
        pawnScore.add(PASSED_PAWN_VALUE, nPassedPawns(board, side));
        pawnScore.add(DOUBLED_PAWN_PENALTY, nDoubledPawns(board.bitboardOf(side, PieceType.PAWN)));
        pawnScore.add(ISOLATED_PAWN_PENALTY, nIsolatedPawns(board.bitboardOf(side, PieceType.PAWN)));
        return pawnScore;
    }

    public static Score knightScore(Board board, int side){
        long knightBb = board.bitboardOf(side, PieceType.KNIGHT);
        long attacks;
        int sq;
        int mobility = 0;
        Score score = new Score();

        while (knightBb != 0){
            sq = Bitboard.lsb(knightBb);
            knightBb = Bitboard.extractLsb(knightBb);
            mobility += Bitboard.popcount(Attacks.getKnightAttacks(sq) & ~allPieces);

            int outpostSq = side == Side.WHITE ? sq : Square.squareMirror(sq);
            if (board.pieceAt(sq + Square.relative_dir(Square.SOUTH_EAST, side)) == Piece.makePiece(side, PieceType.PAWN)) {
                score.add(KNIGHT_OUTPOST_SCORE[outpostSq]);
            }
            if (board.pieceAt(sq + Square.relative_dir(Square.SOUTH_WEST, side)) == Piece.makePiece(side, PieceType.PAWN)) {
                score.add(KNIGHT_OUTPOST_SCORE[outpostSq]);
            }
        }
        score.add((mobility - 4) * 4);
        return score;
    }

    public static Score bishopScore(Board board, int side){
        long bishopsBb = board.bitboardOf(side, PieceType.BISHOP);
        int sq;
        long attacks;
        int mobility = 0;
        Score score = new Score();
        if (hasBishopPair(bishopsBb))
            score.add(BISHOP_PAIR_VALUE);

        while (bishopsBb != 0) {
            sq = Bitboard.lsb(bishopsBb);
            bishopsBb = Bitboard.extractLsb(bishopsBb);
            attacks = Attacks.getBishopAttacks(sq, allPieces) & ~allPieces;
            mobility += Bitboard.popcount(attacks);
            if (Bitboard.popcount(attacks & Bitboard.CENTER) == 2)
                score.add(BISHOP_ATTACKS_CENTER);
            score.add(BISHOP_SAME_COLOR_PAWN_PENALTY, pawnsOnSameColorSquare(board, sq, side));
        }
        score.add((mobility - 6) * 5);
        return score;
    }

    public static Score rookScore(Board board, int side){
        long rooksBb = board.bitboardOf(side, PieceType.ROOK);
        int ourKingSq = side == Side.WHITE ? whiteKingSq : blackKingSq;
        int sq;
        int mobility = 0;
        int pieceMobility;
        Score score = new Score();

        // check for rooks on the 7th
        long ourPawns = board.bitboardOf(side, PieceType.PAWN);
        long enemyPawns = board.bitboardOf(Side.flip(side), PieceType.PAWN);
        long sevBb = File.getBb(Rank.relative_rank(7, side));
        if ((sevBb & enemyPawns & rooksBb) != 0)
            score.add(ROOK_ON_7TH);

        while (rooksBb != 0){
            sq = Bitboard.lsb(rooksBb);
            rooksBb = Bitboard.extractLsb(rooksBb);

            long rookFileBb = Square.getFileBb(sq);
            if ((ourPawns & rookFileBb) == 0){
                if ((enemyPawns & rookFileBb) == 0)
                    score.add(ROOK_ON_OPEN_FILE);
                else
                    score.add(ROOK_ON_SEMIOPEN_FILE);
            }

            pieceMobility = Bitboard.popcount(Attacks.getRookAttacks(sq, allPieces) & ~allPieces);
            mobility += pieceMobility;

            // check to see if the king has trapped a rook
            if (pieceMobility <= 3) {
                int kf = Square.getFile(ourKingSq);
                if ((kf < File.FILE_E) == (Square.getFile(sq) < kf))
                    score.add(KING_TRAPPING_ROOK_PENALTY);
            }
        }
        score.add((mobility - 7) * 2, (mobility - 7) * 4);
        return score;
    }

    public static Score queenScore(Board board, int side){
        long queensBb = board.bitboardOf(side, PieceType.QUEEN);
        Score score = new Score();

        long enemyPawns = board.bitboardOf(Side.flip(side), PieceType.PAWN);
        long sevBb = File.getBb(Rank.relative_rank(7, side));
        if ((sevBb & enemyPawns & queensBb) != 0)
            score.add(QUEEN_ON_7TH);
        return score;
    }

    public static Score kingScore(Board board, int side){
        Score score = new Score();
        int pawnShield = pawnsShieldingKing(board, side);
        score.add(KING_PAWN_SHIELD_BONUS, pawnShield);
        return score;
    }

    public static int evaluateState(final Board board){
        initEval(board);
        Score score = new Score();
        score.add(board.materialScore());
        score.add(board.pSqScore());

        if (board.getSideToPlay() == Side.WHITE)
            score.add(TEMPO);
        else
            score.sub(TEMPO);

        score.add(pawnScore(board, Side.WHITE));
        score.sub(pawnScore(board, Side.BLACK));

        score.add(knightScore(board, Side.WHITE));
        score.sub(knightScore(board, Side.BLACK));

        score.add(bishopScore(board, Side.WHITE));
        score.sub(bishopScore(board, Side.BLACK));

        score.add(rookScore(board, Side.WHITE));
        score.sub(rookScore(board, Side.BLACK));

        score.add(queenScore(board, Side.WHITE));
        score.sub(queenScore(board, Side.BLACK));

        score.add(kingScore(board, Side.WHITE));
        score.sub(kingScore(board, Side.BLACK));

        int finalScore = score.eval(board.phase());
        return board.getSideToPlay() == Side.WHITE ? finalScore : -finalScore;
    }
}
