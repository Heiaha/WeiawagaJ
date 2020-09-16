package mind;
import movegen.*;

public class Evaluation {
    public static int whiteKingSq;
    public static int blackKingSq;
    public static long allWhitePieces;
    public static long allBlackPieces;
    public static long allPieces;

    final static int TOTAL_PHASE = 20;
    final static int[] PIECE_PHASES = {0, 1, 1, 2, 4};

    public final static Score[] PIECE_TYPE_VALUES = {
            new Score(100, 110), // PAWN
            new Score(320, 320), // KNIGHT
            new Score(330, 330), // BISHOP
            new Score(500, 500), // ROOK
            new Score(900, 900)  // QUEEN
    };

    //pawn scoring
    final static Score PASSED_PAWN_VALUE = new Score(10, 70);
    final static Score DOUBLED_PAWN_PENALTY = new Score(-20, -30);
    final static Score ISOLATED_PAWN_PENALTY = new Score(-15, -30);

    //king scoring
    final static Score KING_PAWN_SHIELD_BONUS = new Score(10, 0);

    //rook scoring
    final static Score KING_TRAPPING_ROOK_PENALTY = new Score(-50, 0);

    //bishop scoring
    final static Score BISHOP_SAME_COLOR_PAWN_PENALTY = new Score(-3, -7); // per pawn
    final static Score BISHOP_ATTACKS_CENTER = new Score(30, 40);
    final static Score BISHOP_PAIR_VALUE = new Score(45, 55);

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

    public static void initEval(final Board board){
        allWhitePieces = board.allPieces(Side.WHITE);
        allBlackPieces = board.allPieces(Side.BLACK);
        allPieces = allWhitePieces | allBlackPieces;
        whiteKingSq = Bitboard.lsb(board.bitboardOf(Piece.WHITE_KING));
        blackKingSq = Bitboard.lsb(board.bitboardOf(Piece.BLACK_KING));
    }

    public static int getPhaseValue(final Board board){
        int phase = TOTAL_PHASE;
        for (int pieceType = PieceType.PAWN; pieceType <= PieceType.QUEEN; pieceType++){
            long whitePieces = board.bitboardOf(Side.WHITE, pieceType);
            long blackPieces = board.bitboardOf(Side.BLACK, pieceType);
            phase -= PIECE_PHASES[pieceType]*Bitboard.popcount(whitePieces | blackPieces);
        }
        return phase;
    }

    public static boolean hasBishopPair(final long bishopBb){
        return ((bishopBb & Bitboard.LIGHT_SQUARES) != 0) && ((bishopBb & Bitboard.DARK_SQUARES) != 0);
    }

    public static int isolatedPawns(final long pawnBb){
        return Bitboard.popcount(
                (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb, Square.EAST))) &
                    (pawnBb & ~Bitboard.fileFill(Bitboard.shift(pawnBb,Square.WEST)))
        );
    }

    public static int doubledPawns(final long pawnBb){
        long fill = Bitboard.shift(pawnBb, Square.NORTH);
        fill = Bitboard.fill(fill, Square.NORTH);
        return Bitboard.popcount(fill & pawnBb);
    }

    public static int passedPawns(final Board board, final int side){
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
        return Bitboard.popcount(Bitboard.PAWN_SHIELD_MASKS[side][kingSq] & pawns);
    }

    public static int pawnsOnSameColorSquare(final Board board, int square, int side){
        return Bitboard.popcount(board.bitboardOf(side, PieceType.PAWN) & ((Bitboard.DARK_SQUARES & Square.getBb(square)) != 0 ? Bitboard.DARK_SQUARES : Bitboard.LIGHT_SQUARES));
    }

    public static int evaluateState(final Board board){
        Score score = new Score();
        int phase = TOTAL_PHASE;
        int sq;
        initEval(board);

        // EVALUATE PAWN STRUCTURE
        int passedPawnDiff = passedPawns(board, Side.WHITE) - passedPawns(board, Side.BLACK);
        score.add(PASSED_PAWN_VALUE, passedPawnDiff);

        int doubledPawnDiff = doubledPawns(board.bitboardOf(Piece.WHITE_PAWN)) - doubledPawns(board.bitboardOf(Piece.BLACK_PAWN));
        score.add(DOUBLED_PAWN_PENALTY, doubledPawnDiff);

        int isolatedPawnDiff = isolatedPawns(board.bitboardOf(Piece.WHITE_PAWN)) - isolatedPawns(board.bitboardOf(Piece.BLACK_PAWN));
        score.add(ISOLATED_PAWN_PENALTY, isolatedPawnDiff);

        if (hasBishopPair(Piece.WHITE_BISHOP)){
            score.add(BISHOP_PAIR_VALUE);
        }
        if (hasBishopPair(Piece.BLACK_BISHOP)){
            score.sub(BISHOP_PAIR_VALUE);
        }

        //EVALUATE KING POSITION
        int pawnShieldDiff = pawnsShieldingKing(board, Side.WHITE)
                - pawnsShieldingKing(board, Side.BLACK);
        score.add(KING_PAWN_SHIELD_BONUS, pawnShieldDiff);

        score.add(PIECE_TABLES[PieceType.KING][whiteKingSq]);
        score.sub(PIECE_TABLES[PieceType.KING][Square.squareMirror(blackKingSq)]);

        long whitePieces;
        long blackPieces;
        for (int pieceType = PieceType.PAWN; pieceType <= PieceType.QUEEN; pieceType++){
            whitePieces = board.bitboardOf(Side.WHITE, pieceType);
            blackPieces = board.bitboardOf(Side.BLACK, pieceType);
            phase -= PIECE_PHASES[pieceType]*Bitboard.popcount(whitePieces | blackPieces);
            score.add(PIECE_TYPE_VALUES[pieceType], Bitboard.popcount(whitePieces));
            score.sub(PIECE_TYPE_VALUES[pieceType], Bitboard.popcount(blackPieces));

            while (whitePieces != 0){
                sq = Bitboard.lsb(whitePieces);
                whitePieces = Bitboard.extractLsb(whitePieces);
                score.add(PIECE_TABLES[pieceType][sq]);

                // make sure the rook isn't trapped by the king.
                switch (pieceType) {
                    case PieceType.BISHOP -> {
                        score.add(BISHOP_SAME_COLOR_PAWN_PENALTY, pawnsOnSameColorSquare(board, sq, Side.WHITE));
                        if (Bitboard.popcount(Attacks.getBishopAttacks(sq, allPieces) & Bitboard.CENTER) == 2)
                            score.add(BISHOP_ATTACKS_CENTER);
                    }
                    case PieceType.ROOK -> {
                        int mobility = Bitboard.popcount(Attacks.getRookAttacks(sq, allPieces));
                        if (mobility <= 3) {
                            int kf = Square.getFile(whiteKingSq);
                            if ((kf < File.FILE_E) == (Square.getFile(sq) < kf))
                                score.add(KING_TRAPPING_ROOK_PENALTY);
                        }
                    }
                }
            }

            while (blackPieces != 0){
                sq = Bitboard.lsb(blackPieces);
                blackPieces = Bitboard.extractLsb(blackPieces);
                score.sub(PIECE_TABLES[pieceType][Square.squareMirror(sq)]);

                // make sure the rook isn't trapped by the king.
                switch (pieceType) {
                    case PieceType.BISHOP -> {
                        score.sub(BISHOP_SAME_COLOR_PAWN_PENALTY, pawnsOnSameColorSquare(board, sq, Side.BLACK));
                        if (Bitboard.popcount(Attacks.getBishopAttacks(sq, allPieces) & Bitboard.CENTER) == 2)
                            score.sub(BISHOP_ATTACKS_CENTER);
                    }
                    case PieceType.ROOK -> {
                        int mobility = Bitboard.popcount(Attacks.getRookAttacks(sq, allPieces));
                        if (mobility <= 3) {
                            int kf = Square.getFile(blackKingSq);
                            if ((kf < File.FILE_E) == (Square.getFile(sq) < kf))
                                score.sub(KING_TRAPPING_ROOK_PENALTY);
                        }
                    }
                }
            }
        }

        int finalScore = score.eval(phase);
        return board.getSideToPlay() == Side.WHITE ? finalScore : -finalScore;
    }
}
