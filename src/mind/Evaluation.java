package mind;
import movegen.*;

public class Evaluation {
    public static int whiteKingSq;
    public static int blackKingSq;
    public static long allWhitePieces;
    public static long allBlackPieces;
    public static long allPieces;

    final static int TOTAL_PHASE = 24;
    final static int[] PIECE_PHASES = {0, 1, 1, 2, 4};

    final static Score TEMPO = new Score(20, 10);

    public final static Score[] PIECE_TYPE_VALUES = {
            new Score(70, 90), // PAWN
            new Score(325, 325), // KNIGHT
            new Score(325, 325), // BISHOP
            new Score(500, 500), // ROOK
            new Score(975, 975)  // QUEEN
    };

    //pawn scoring
    final static Score PASSED_PAWN_VALUE = new Score(10, 50);
    final static Score DOUBLED_PAWN_PENALTY = new Score(-20, -10);
    final static Score ISOLATED_PAWN_PENALTY = new Score(-20, -10);

    //king scoring
    final static Score KING_PAWN_SHIELD_BONUS = new Score(10, 0);

    //rook scoring
    final static Score KING_TRAPPING_ROOK_PENALTY = new Score(-25, 0);

    //bishop scoring
    final static Score BISHOP_SAME_COLOR_PAWN_PENALTY = new Score(-3, -7); // per pawn
    final static Score BISHOP_ATTACKS_CENTER = new Score(30, 40);
    final static Score BISHOP_PAIR_VALUE = new Score(45, 55);

    //mobility
    final static Score MOBILITY_BONUS = new Score(4, 1);

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

    public static Score pawnStructure(Board board, int side){
        Score pawnScore = new Score();
        pawnScore.add(PASSED_PAWN_VALUE, passedPawns(board, side));
        pawnScore.add(DOUBLED_PAWN_PENALTY, doubledPawns(board.bitboardOf(side, PieceType.PAWN)));
        pawnScore.add(ISOLATED_PAWN_PENALTY, isolatedPawns(board.bitboardOf(side, PieceType.PAWN)));
        return pawnScore;
    }

    public static int pawnsShieldingKing(final Board board, int side){
        int kingSq = side == Side.WHITE ? whiteKingSq : blackKingSq;
        long pawns = board.bitboardOf(side, PieceType.PAWN);
        return Bitboard.popcount(Bitboard.PAWN_SHIELD_MASKS[side][kingSq] & pawns);
    }

    public static int pawnsOnSameColorSquare(final Board board, int square, int side){
        return Bitboard.popcount(board.bitboardOf(side, PieceType.PAWN) & ((Bitboard.DARK_SQUARES & Square.getBb(square)) != 0 ? Bitboard.DARK_SQUARES : Bitboard.LIGHT_SQUARES));
    }

    public static int mobilityofPiece(int sq, int pieceType, int side){
        long ourPieces = side == Side.WHITE ? allWhitePieces : allBlackPieces;
        return switch (pieceType) {
            case PieceType.BISHOP -> Bitboard.popcount(Attacks.getBishopAttacks(sq, allPieces) & ~ourPieces);
            case PieceType.KNIGHT -> Bitboard.popcount(Attacks.getKnightAttacks(sq) & ~ourPieces);
            case PieceType.ROOK -> Bitboard.popcount(Attacks.getRookAttacks(sq, allPieces) & ~ourPieces);
            case PieceType.QUEEN -> Bitboard.popcount((Attacks.getRookAttacks(sq, allPieces) | Attacks.getBishopAttacks(sq, allPieces)) & ~ourPieces);
            default -> 0;
        };
    }

    public static int evaluateState(final Board board){
        Score score = new Score();
        int phase = TOTAL_PHASE;
        int sq;
        initEval(board);

        if (board.getSideToPlay() == Side.WHITE)
            score.add(TEMPO);
        else
            score.sub(TEMPO);

        // EVALUATE PAWN STRUCTURE
        score.add(pawnStructure(board, Side.WHITE));
        score.sub(pawnStructure(board, Side.BLACK));

        if (hasBishopPair(Piece.WHITE_BISHOP))
            score.add(BISHOP_PAIR_VALUE);

        if (hasBishopPair(Piece.BLACK_BISHOP))
            score.sub(BISHOP_PAIR_VALUE);


        //EVALUATE KING POSITION
        int pawnShieldDiff = pawnsShieldingKing(board, Side.WHITE)
                - pawnsShieldingKing(board, Side.BLACK);
        score.add(KING_PAWN_SHIELD_BONUS, pawnShieldDiff);

        score.add(PIECE_TABLES[PieceType.KING][whiteKingSq]);
        score.sub(PIECE_TABLES[PieceType.KING][Square.squareMirror(blackKingSq)]);

        long whitePieces;
        long blackPieces;
        int mobility;
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
                mobility = mobilityofPiece(sq, pieceType, Side.WHITE);
                score.add(MOBILITY_BONUS, mobility);
                switch (pieceType) {
                    case PieceType.BISHOP -> {
                        score.add(BISHOP_SAME_COLOR_PAWN_PENALTY, pawnsOnSameColorSquare(board, sq, Side.WHITE));
                        if (Bitboard.popcount(Attacks.getBishopAttacks(sq, allPieces) & Bitboard.CENTER) == 2)
                            score.add(BISHOP_ATTACKS_CENTER);
                    }
                    case PieceType.ROOK -> {
                        // make sure the rook isn't trapped by the king.
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
                mobility = mobilityofPiece(sq, pieceType, Side.BLACK);
                score.sub(MOBILITY_BONUS, mobility);
                switch (pieceType) {
                    case PieceType.BISHOP -> {
                        score.sub(BISHOP_SAME_COLOR_PAWN_PENALTY, pawnsOnSameColorSquare(board, sq, Side.BLACK));
                        if (Bitboard.popcount(Attacks.getBishopAttacks(sq, allPieces) & Bitboard.CENTER) == 2)
                            score.sub(BISHOP_ATTACKS_CENTER);
                    }
                    case PieceType.ROOK -> {
                        // make sure the rook isn't trapped by the king.
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
