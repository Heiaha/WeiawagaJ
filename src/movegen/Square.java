package movegen;

import java.util.HashMap;
import java.util.Map;


public class Square {
    public static int A1 =  0, B1 =  1, C1 =  2, D1 =  3, E1 =  4, F1 =  5, G1 =  6, H1 =  7,
                      A2 =  8, B2 =  9, C2 = 10, D2 = 11, E2 = 12, F2 = 13, G2 = 14, H2 = 15,
                      A3 = 16, B3 = 17, C3 = 18, D3 = 19, E3 = 20, F3 = 21, G3 = 22, H3 = 23,
                      A4 = 24, B4 = 25, C4 = 26, D4 = 27, E4 = 28, F4 = 29, G4 = 30, H4 = 31,
                      A5 = 32, B5 = 33, C5 = 34, D5 = 35, E5 = 36, F5 = 37, G5 = 38, H5 = 39,
                      A6 = 40, B6 = 41, C6 = 42, D6 = 43, E6 = 44, F6 = 45, G6 = 46, H6 = 47,
                      A7 = 48, B7 = 49, C7 = 50, D7 = 51, E7 = 52, F7 = 53, G7 = 54, H7 = 55,
                      A8 = 56, B8 = 57, C8 = 58, D8 = 59, E8 = 60, F8 = 61, G8 = 62, H8 = 63,
                      NO_SQUARE = 64;

    private final static String[] SQUARE_NAMES = {
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
    };



    public final static int NORTH = 8, SOUTH = -8, EAST = 1, WEST = -1, NORTH_EAST = NORTH + EAST, NORTH_WEST = NORTH + WEST,
            SOUTH_WEST = SOUTH + WEST, SOUTH_EAST = SOUTH + EAST, NORTH_NORTH = NORTH + NORTH, SOUTH_SOUTH = SOUTH + SOUTH;

    public final static long[] BB_SQUARES = new long[65];
    static {
        for (int sq = A1; sq <= H8; sq++){
            BB_SQUARES[sq] = 1L << sq;
        }
        BB_SQUARES[64] = 0L;
    }

    public final static Map<String, Integer> NAME_MAP = new HashMap<>();
    static{
        for (int sq = Square.A1; sq <= Square.H8; sq++)
            NAME_MAP.put(SQUARE_NAMES[sq], sq);
    }

    private final static int[] SQ_MIRROR = new int[64];
    static{
        for (int sq = A1; sq <= H8; sq++){
            SQ_MIRROR[sq] = sq ^ 0x38;
        }
    }

    public final static int[][] CDISTANCE = new int[64][64];
    public final static int[][] MDISTANCE = new int[64][64];
    static{
        for (int sq1 = Square.A1; sq1 <= Square.H8; sq1++){
            for (int sq2 = Square.A1; sq2 <= Square.H8; sq2++){
                CDISTANCE[sq1][sq2] = Math.max(Math.abs(Square.getFile(sq1) - Square.getFile(sq2)), Math.abs(Square.getRank(sq1) - Square.getRank(sq2)));
                MDISTANCE[sq1][sq2] = Math.abs(Square.getFile(sq1) - Square.getFile(sq2)) - Math.abs(Square.getRank(sq1) - Square.getRank(sq2));
            }
        }
    }

    public static int getSquareFromName(String st){
        return NAME_MAP.get(st);
    }

    public static int encode(int rank, int file) { return (rank << 3) + file; }

    public static long getBb(int square){
        return BB_SQUARES[square];
    }

    public static int getRank(int square){
        return square >>> 3;
    }

    public static int getFile(int square){
        return square & 7;
    }

    public static long getFileBb(int square){
        return File.getBb(square & 7);
    }

    public static long getAdjacentFileBb(int square){ return File.getAdjacentBb(square & 7 ); }

    public static int getDiagonal(int square){
        return 7 + getRank(square) - getFile(square);
    }

    public static int getAntiDiagonal(int square){
        return getRank(square) + getFile(square);
    }

    public static int relative_dir(int direction, int side){
        return side == Side.WHITE ? direction : -direction;
    }

    public static String getName(int square){
        return SQUARE_NAMES[square];
    }

    public static int squareMirror(int sq){
        return SQ_MIRROR[sq];
    }

    public static int relativeSquare(int sq, int side){
        return side == Side.WHITE ? sq : SQ_MIRROR[sq];
    }

    public static int getCDistance(int sq1, int sq2){
        return CDISTANCE[sq1][sq2];
    }

    public static int getMDistance(int sq1, int sq2){
        return MDISTANCE[sq1][sq2];
    }
}



