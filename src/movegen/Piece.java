package movegen;

public class Piece {

    public final static int WHITE_PAWN = 0;
    public final static int WHITE_KNIGHT = 1;
    public final static int WHITE_BISHOP = 2;
    public final static int WHITE_ROOK = 3;
    public final static int WHITE_QUEEN = 4;
    public final static int WHITE_KING = 5;
    public final static int BLACK_PAWN = 8;
    public final static int BLACK_KNIGHT = 9;
    public final static int BLACK_BISHOP = 10;
    public final static int BLACK_ROOK = 11;
    public final static int BLACK_QUEEN = 12;
    public final static int BLACK_KING = 13;
    public final static int NONE = 14;

    public final static int[] PIECES = { WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING,
                                         BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING };

    public final static int NPIECES = 15;

    public static int typeOf(int piece){
        return piece & 0b111;
    }

    public static int sideOf(int piece){
        return (piece & 0b1000) >>> 3;
    }

    public static int makePiece(int side, int pieceType){
        return (side << 3) + pieceType;
    }

    public static String getNotation(int piece){
        return switch (piece) {
            case WHITE_PAWN -> "P";
            case WHITE_KNIGHT -> "N";
            case WHITE_BISHOP -> "B";
            case WHITE_ROOK -> "R";
            case WHITE_QUEEN -> "Q";
            case WHITE_KING -> "K";
            case BLACK_PAWN -> "p";
            case BLACK_KNIGHT -> "n";
            case BLACK_BISHOP -> "b";
            case BLACK_ROOK -> "r";
            case BLACK_QUEEN -> "q";
            case BLACK_KING -> "k";
            default -> "";
        };

    }
}
