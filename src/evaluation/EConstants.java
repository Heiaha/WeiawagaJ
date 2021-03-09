package evaluation;

import movegen.*;
import search.Score;

import java.util.Arrays;

public class EConstants {
    public static int TOTAL_PHASE = 24;
    public static int[] PIECE_PHASES = {0, 1, 1, 2, 4, 0};

    public static int[] TEMPO = {Score.score(18, 17),};

    public static int[] PIECE_TYPE_VALUES = {
            Score.score(109, 114),
            Score.score(379, 316),
            Score.score(367, 323),
            Score.score(496, 536),
            Score.score(1025, 1022),
            Score.score(0, 0)
    };

    public  static int[] PIECE_TYPE_VALUES_TUNING = Arrays.copyOfRange(PIECE_TYPE_VALUES, 0, 5);


    //pawn scoring
    public static int[] PAWN_SCORES = {Score.score(-6, 33), Score.score(-1, -17), Score.score(-21, 4),};
    public static int IX_PASSED_PAWN_VALUE = 0;
    public static int IX_DOUBLED_PAWN_PENALTY = 1;
    public static int IX_ISOLATED_PAWN_PENALTY = 2;


    //bishop scoring
    public static int[] BISHOP_SCORES = {Score.score(-2, -8), Score.score(30, 11), Score.score(54, 59),};
    public static int IX_BISHOP_SAME_COLOR_PAWN_PENALTY = 0; // per pawn
    public static int IX_BISHOP_ATTACKS_CENTER = 1;
    public static int IX_BISHOP_PAIR_VALUE = 2;

    //rook scoring
    public static int[] ROOK_SCORES = {Score.score(-26, -36), Score.score(50, 22), Score.score(16, 28),};
    public static int IX_KING_TRAPPING_ROOK_PENALTY = 0;
    public static int IX_ROOK_ON_OPEN_FILE = 1;
    public static int IX_ROOK_ON_SEMIOPEN_FILE = 2;

    //king scoring
    public static int[] KING_SCORES = {Score.score(18, -6),};
    public static int IX_KING_PAWN_SHIELD_BONUS = 0;

    public static int[][] PIECE_TYPE_TABLES = {
            {
                    Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46), Score.score(-57, -46),
                    Score.score(-36, -25), Score.score(-27, -37), Score.score(-37, -26), Score.score(-56, -21), Score.score(-50, -21), Score.score(-1, -32), Score.score(2, -42), Score.score(-28, -44),
                    Score.score(-24, -38), Score.score(-30, -36), Score.score(-14, -44), Score.score(-26, -32), Score.score(-12, -36), Score.score(-6, -42), Score.score(5, -51), Score.score(-12, -49),
                    Score.score(-26, -28), Score.score(-26, -32), Score.score(-17, -40), Score.score(1, -40), Score.score(4, -40), Score.score(-1, -44), Score.score(-22, -38), Score.score(-29, -38),
                    Score.score(-13, -6), Score.score(-3, -16), Score.score(-1, -24), Score.score(12, -35), Score.score(12, -35), Score.score(6, -31), Score.score(-6, -19), Score.score(-30, -19),
                    Score.score(7, 51), Score.score(2, 51), Score.score(10, 43), Score.score(16, 33), Score.score(20, 23), Score.score(22, 20), Score.score(4, 42), Score.score(-10, 42),
                    Score.score(52, 116), Score.score(52, 116), Score.score(40, 96), Score.score(40, 92), Score.score(38, 92), Score.score(44, 92), Score.score(38, 98), Score.score(38, 116),
                    Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54), Score.score(-46, -54),
            },
            {
                    Score.score(-93, -66), Score.score(-47, -81), Score.score(-66, -61), Score.score(-52, -55), Score.score(-36, -57), Score.score(-46, -59), Score.score(-40, -79), Score.score(-60, -93),
                    Score.score(-59, -75), Score.score(-57, -59), Score.score(-30, -39), Score.score(-24, -36), Score.score(-19, -32), Score.score(-2, -47), Score.score(-32, -51), Score.score(-30, -79),
                    Score.score(-48, -57), Score.score(-25, -34), Score.score(-6, -33), Score.score(-4, -18), Score.score(6, -22), Score.score(2, -34), Score.score(3, -41), Score.score(-36, -55),
                    Score.score(-40, -51), Score.score(-16, -37), Score.score(1, -16), Score.score(3, -9), Score.score(17, -17), Score.score(8, -17), Score.score(2, -26), Score.score(-28, -53),
                    Score.score(-32, -51), Score.score(-3, -28), Score.score(9, -16), Score.score(34, -11), Score.score(26, -11), Score.score(26, -10), Score.score(2, -25), Score.score(-22, -44),
                    Score.score(-53, -64), Score.score(5, -38), Score.score(13, -18), Score.score(22, -17), Score.score(22, -23), Score.score(17, -24), Score.score(7, -40), Score.score(-23, -68),
                    Score.score(-67, -70), Score.score(-47, -50), Score.score(7, -40), Score.score(1, -32), Score.score(-15, -40), Score.score(7, -40), Score.score(-24, -56), Score.score(-48, -80),
                    Score.score(-78, -92), Score.score(-66, -76), Score.score(-56, -48), Score.score(-46, -66), Score.score(-30, -52), Score.score(-58, -72), Score.score(-60, -82), Score.score(-78, -92),
            },
            {
                    Score.score(-44, -40), Score.score(-14, -17), Score.score(-31, -25), Score.score(-29, -13), Score.score(-23, -17), Score.score(-26, -22), Score.score(-32, -26), Score.score(-40, -46),
                    Score.score(-6, -30), Score.score(-3, -21), Score.score(6, -12), Score.score(-13, -2), Score.score(-5, 0), Score.score(5, -12), Score.score(19, -21), Score.score(-17, -34),
                    Score.score(-14, -18), Score.score(-4, -4), Score.score(0, -12), Score.score(2, 8), Score.score(2, 12), Score.score(1, -16), Score.score(5, -7), Score.score(-5, -17),
                    Score.score(-25, -15), Score.score(-3, -1), Score.score(1, 9), Score.score(17, 11), Score.score(19, 3), Score.score(-1, 5), Score.score(-5, -9), Score.score(-11, -19),
                    Score.score(-15, -11), Score.score(-14, 6), Score.score(5, 4), Score.score(16, 9), Score.score(16, 9), Score.score(11, 6), Score.score(-5, -2), Score.score(-14, -11),
                    Score.score(-30, -11), Score.score(4, -5), Score.score(-5, -18), Score.score(8, -1), Score.score(6, -7), Score.score(-1, -16), Score.score(0, -1), Score.score(-16, -9),
                    Score.score(-30, -19), Score.score(-20, -23), Score.score(-18, -3), Score.score(-20, -21), Score.score(-2, -9), Score.score(0, -9), Score.score(-14, -23), Score.score(-30, -31),
                    Score.score(-40, -43), Score.score(-22, -21), Score.score(-30, -29), Score.score(-26, -21), Score.score(-24, -19), Score.score(-28, -23), Score.score(-18, -21), Score.score(-40, -43),
            },
            {
                    Score.score(-27, -17), Score.score(-31, -11), Score.score(-25, -17), Score.score(-17, -22), Score.score(-15, -26), Score.score(-11, -15), Score.score(-41, -15), Score.score(-15, -29),
                    Score.score(-48, -23), Score.score(-37, -24), Score.score(-43, -22), Score.score(-41, -20), Score.score(-35, -26), Score.score(-15, -28), Score.score(-35, -26), Score.score(-48, -21),
                    Score.score(-48, -23), Score.score(-35, -21), Score.score(-37, -27), Score.score(-41, -25), Score.score(-32, -29), Score.score(-26, -24), Score.score(-26, -24), Score.score(-43, -25),
                    Score.score(-47, -14), Score.score(-38, -15), Score.score(-34, -15), Score.score(-29, -21), Score.score(-24, -26), Score.score(-30, -20), Score.score(-16, -22), Score.score(-37, -23),
                    Score.score(-33, -13), Score.score(-32, -14), Score.score(-14, -13), Score.score(-14, -22), Score.score(-13, -22), Score.score(-7, -11), Score.score(-27, -17), Score.score(-24, -12),
                    Score.score(-23, -9), Score.score(-12, -11), Score.score(-11, -15), Score.score(-9, -17), Score.score(-15, -24), Score.score(-2, -12), Score.score(0, -8), Score.score(-17, -16),
                    Score.score(-3, -4), Score.score(0, -5), Score.score(6, -9), Score.score(6, -12), Score.score(10, -19), Score.score(16, -4), Score.score(-4, -4), Score.score(7, -9),
                    Score.score(2, -10), Score.score(0, -11), Score.score(-5, -13), Score.score(3, -18), Score.score(3, -15), Score.score(1, -13), Score.score(-7, -13), Score.score(-5, -14),
            },
            {
                    Score.score(-22, -27), Score.score(-24, -28), Score.score(-16, -27), Score.score(3, -25), Score.score(-20, -21), Score.score(-25, -32), Score.score(-25, -32), Score.score(-35, -42),
                    Score.score(-25, -32), Score.score(-15, -22), Score.score(5, -22), Score.score(-5, -18), Score.score(3, -18), Score.score(1, -22), Score.score(-15, -22), Score.score(-13, -26),
                    Score.score(-25, -22), Score.score(-8, -17), Score.score(-8, -3), Score.score(-6, -10), Score.score(-6, -9), Score.score(-4, 2), Score.score(3, -5), Score.score(-7, -15),
                    Score.score(-15, -19), Score.score(-15, -7), Score.score(-10, -4), Score.score(-8, 10), Score.score(4, 8), Score.score(0, 8), Score.score(1, 3), Score.score(-10, -5),
                    Score.score(-20, -19), Score.score(-15, -14), Score.score(-10, -5), Score.score(-6, 5), Score.score(14, 7), Score.score(14, 5), Score.score(7, 0), Score.score(0, -5),
                    Score.score(-25, -26), Score.score(-15, -18), Score.score(0, -9), Score.score(8, 5), Score.score(16, 5), Score.score(16, 5), Score.score(11, 0), Score.score(1, -10),
                    Score.score(-25, -28), Score.score(-15, -18), Score.score(-5, -2), Score.score(5, 0), Score.score(1, 0), Score.score(11, 0), Score.score(11, 0), Score.score(1, -10),
                    Score.score(-35, -34), Score.score(-7, -12), Score.score(-3, -10), Score.score(-2, -5), Score.score(2, -5), Score.score(-3, -10), Score.score(-3, -10), Score.score(-13, -20),
            },
            {
                    Score.score(5, -71), Score.score(19, -51), Score.score(-2, -41), Score.score(-16, -42), Score.score(14, -43), Score.score(-9, -39), Score.score(25, -51), Score.score(19, -77),
                    Score.score(6, -51), Score.score(15, -34), Score.score(-19, -12), Score.score(-21, -14), Score.score(-21, -10), Score.score(-21, -10), Score.score(21, -28), Score.score(17, -44),
                    Score.score(-17, -42), Score.score(-29, -16), Score.score(-39, -4), Score.score(-41, 4), Score.score(-41, 6), Score.score(-41, 2), Score.score(-13, -10), Score.score(-23, -30),
                    Score.score(-35, -44), Score.score(-31, -20), Score.score(-51, 6), Score.score(-61, 14), Score.score(-61, 14), Score.score(-49, 7), Score.score(-35, -7), Score.score(-39, -33),
                    Score.score(-31, -29), Score.score(-33, -1), Score.score(-47, 9), Score.score(-69, 15), Score.score(-69, 15), Score.score(-43, 15), Score.score(-31, 3), Score.score(-27, -21),
                    Score.score(-23, -17), Score.score(-33, 3), Score.score(-39, 11), Score.score(-67, 5), Score.score(-65, 5), Score.score(-35, 29), Score.score(-33, 3), Score.score(-23, -17),
                    Score.score(-27, -21), Score.score(-33, -7), Score.score(-35, -3), Score.score(-49, 5), Score.score(-51, 1), Score.score(-39, 3), Score.score(-39, -7), Score.score(-33, -17),
                    Score.score(-45, -73), Score.score(-41, -39), Score.score(-41, -31), Score.score(-61, -30), Score.score(-67, -28), Score.score(-49, -18), Score.score(-47, -28), Score.score(-37, -38),
            }
    };

    public static int[] PIECE_VALUES = new int[Piece.NPIECES];
    public static int[][] PIECE_TABLES = new int[Piece.NPIECES][64];
    static {
        for (int pc = Piece.WHITE_PAWN; pc <= Piece.WHITE_KING; pc++){
            PIECE_VALUES[pc] = PIECE_TYPE_VALUES[pc];
            PIECE_VALUES[Piece.flip(pc)] = -PIECE_TYPE_VALUES[pc];
            for (int sq = Square.A1; sq <= Square.H8; sq++) {
                PIECE_TABLES[pc][sq] = PIECE_TYPE_TABLES[pc][sq];
                PIECE_TABLES[Piece.flip(pc)][sq] = -PIECE_TYPE_TABLES[pc][Square.squareMirror(sq)];
            }
        }
    }

    public static long[][] PAWN_SHIELD_MASKS = new long[2][64];
    static {
        for (int sq = Square.A1; sq <= Square.H8; sq++) {
            long sq_bb = Square.getBb(sq);
            PAWN_SHIELD_MASKS[Side.WHITE][sq] = Bitboard.shift(sq_bb, Square.NORTH) | Bitboard.shift(sq_bb, Square.NORTH_EAST) | Bitboard.shift(sq_bb, Square.NORTH_WEST);
            PAWN_SHIELD_MASKS[Side.BLACK][sq] = Bitboard.shift(sq_bb, Square.SOUTH) | Bitboard.shift(sq_bb, Square.SOUTH_EAST) | Bitboard.shift(sq_bb, Square.SOUTH_WEST);
        }
    }
}
