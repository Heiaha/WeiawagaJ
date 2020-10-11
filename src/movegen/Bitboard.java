package movegen;

public class Bitboard {

    /**
     * The constant lightSquares.
     */
    public static final long LIGHT_SQUARES = 0x55AA55AA55AA55AAL;
    /**
     * The constant darkSquares.
     */
    public static final long DARK_SQUARES = 0xAA55AA55AA55AA55L;


    private final static long WHITE_OO_MASK = 0x90L;
    private final static long WHITE_OOO_MASK = 0x11L;

    private final static long WHITE_OO_BLOCKERS_AND_ATTACKERS_MASK = 0x60L;
    private final static long WHITE_OOO_BLOCKERS_AND_ATTACKERS_MASK = 0xeL;

    private final static long BLACK_OO_MASK = 0x9000000000000000L;
    private final static long BLACK_OOO_MASK = 0x1100000000000000L;

    private final static long BLACK_OO_BLOCKERS_AND_ATTACKERS_MASK = 0x6000000000000000L;
    private final static long BLACK_OOO_BLOCKERS_AND_ATTACKERS_MASK = 0xE00000000000000L;
    private final static long ALL_CASTLING_MASK = 0x9100000000000091L;


    public final static long[][] BB_RAYS = new long[8][64];
    static {
        for (int sq = Square.A1; sq <= Square.H8; sq++){
            // NORTH
            BB_RAYS[0][sq] = 0x0101010101010100L << sq;

            // SOUTH
            BB_RAYS[1][sq] = 0x0080808080808080L >>> (63 - sq);

            // EAST
            BB_RAYS[2][sq] = 2 * (( 1L << (sq | 7)) - (1L << sq));

            // WEST
            BB_RAYS[3][sq] = (1L << sq) - (1L << (sq & 56));

            //NORTH WEST
            BB_RAYS[4][sq] = shift(0x0102040810204000L, Square.WEST, 7 - Square.getFile(sq)) << (Square.getRank(sq) * 8);

            //NORTH EAST
            BB_RAYS[5][sq] = shift(0x8040201008040200L, Square.EAST, Square.getFile(sq)) << (Square.getRank(sq) * 8);

            //SOUTH WEST
            BB_RAYS[6][sq] = shift(0x0040201008040201L, Square.WEST, 7 - Square.getFile(sq)) >>> ((7 - Square.getRank(sq)) * 8);

            //SOUTH EAST
            BB_RAYS[7][sq] = shift(0x0002040810204080L, Square.EAST, Square.getFile(sq)) >>> ((7 - Square.getRank(sq)) * 8);
        }
    }

    //initialize square between
    public final static long[][] BB_SQUARES_BETWEEN = new long[64][64];
    static {
        long sqs;
        for (int sq1 = Square.A1; sq1 <= Square.H8; sq1++){
            for (int sq2 = Square.A1; sq2 <= Square.H8; sq2++){
                sqs = Square.getBb(sq1) | Square.getBb(sq2);
                if (Square.getFile(sq1) == Square.getFile(sq2) || Square.getRank(sq1) == Square.getRank(sq2)) {
                    BB_SQUARES_BETWEEN[sq1][sq2] =
                            Attacks.getRookAttacksSlow(sq1, sqs) & Attacks.getRookAttacksSlow(sq2, sqs);
                }
                else if (Square.getDiagonal(sq1) == Square.getDiagonal(sq1) || Square.getAntiDiagonal(sq1) == Square.getAntiDiagonal(sq2)) {
                    BB_SQUARES_BETWEEN[sq1][sq2] =
                            Attacks.getBishopAttacksSlow(sq1, sqs) & Attacks.getBishopAttacksSlow(sq2, sqs);
                }
            }
        }
    }

    public final static long[][] BB_LINES = new long[64][64];
    static{
        for (int sq1 = Square.A1; sq1 <= Square.H8; sq1++){
            for (int sq2 = Square.A1; sq2 <= Square.H8; sq2++){
                if (Square.getFile(sq1) == Square.getFile(sq2) || Square.getRank(sq1) == Square.getRank(sq2))
                    BB_LINES[sq1][sq2] =
                            Attacks.getRookAttacksSlow(sq1, 0) & Attacks.getRookAttacksSlow(sq2, 0);
                else if (Square.getDiagonal(sq1) == Square.getDiagonal(sq1) || Square.getAntiDiagonal(sq1) == Square.getAntiDiagonal(sq2))
                    BB_LINES[sq1][sq2] =
                            Attacks.getBishopAttacksSlow(sq1, 0) & Attacks.getBishopAttacksSlow(sq2, 0);
            }
        }
    }

    public static long between(int sq1, int sq2){
        return BB_SQUARES_BETWEEN[sq1][sq2];
    }

    public static long line(int sq1, int sq2){
        return BB_LINES[sq1][sq2];
    }

    public static final long CENTER = 0x1818000000L;


    public static long ooMask(int side){
        return side == Side.WHITE ? WHITE_OO_MASK : BLACK_OO_MASK;
    }

    public static long oooMask(int side){
        return side == Side.WHITE ? WHITE_OOO_MASK : BLACK_OOO_MASK;
    }

    public static long ooBlockersMask(int side){
        return side == Side.WHITE ? WHITE_OO_BLOCKERS_AND_ATTACKERS_MASK :
                BLACK_OO_BLOCKERS_AND_ATTACKERS_MASK;
    }

    public static long oooBlockersMask(int side){
        return side == Side.WHITE ? WHITE_OOO_BLOCKERS_AND_ATTACKERS_MASK :
                BLACK_OOO_BLOCKERS_AND_ATTACKERS_MASK;
    }

    public static long ignoreOOODanger(int side){
        return side == Side.WHITE ? 0x2 : 0x200000000000000L;
    }

    public static int lsb(long bb){
        return Long.numberOfTrailingZeros(bb);
    }

    public static int msb(long bb){
        return 63 - Long.numberOfLeadingZeros(bb);
    }

    public static long extractLsb(long bb){
        return bb & (bb - 1);
    }

    public static int popcount(long bb){
        return Long.bitCount(bb);
    }

    public static long shift (long bb, int direction){
        return switch (direction) {
            case Square.NORTH -> bb << 8;
            case Square.SOUTH -> bb >>> 8;
            case Square.EAST -> (bb & ~File.getBb(File.FILE_H)) << 1;
            case Square.WEST -> (bb & ~File.getBb(File.FILE_A)) >>> 1;
            case Square.NORTH_EAST -> (bb & ~File.getBb(File.FILE_H)) << 9;
            case Square.NORTH_WEST -> (bb & ~File.getBb(File.FILE_A)) << 7;
            case Square.SOUTH_EAST -> (bb & ~File.getBb(File.FILE_H)) >>> 7;
            case Square.SOUTH_WEST -> (bb & ~File.getBb(File.FILE_A)) >>> 9;
            default -> bb;
        };
    }

    public static long shift(long bb, int direction, int n){
        long result = bb;
        switch (direction) {
            case Square.NORTH:
                for (int i = 0; i < n; i++)
                    result = result << 8;
                break;
            case Square.SOUTH:
                for (int i = 0; i < n; i++)
                    result = result >>> 8;
                break;
            case Square.EAST:
                for (int i = 0; i < n; i++)
                    result = (result << 1) & ~File.getBb(File.FILE_A);
                break;
            case Square.WEST:
                for (int i = 0; i < n; i++)
                    result = (result >>> 1) & ~File.getBb(File.FILE_H);
                break;
            case Square.NORTH_EAST:
                for (int i = 0; i < n; i++)
                    result = (result & ~File.getBb(File.FILE_H)) << 9;
                break;
            case Square.NORTH_WEST:
                for (int i = 0; i < n; i++)
                    result = (result & ~File.getBb(File.FILE_A)) << 7;
                break;
            case Square.SOUTH_EAST:
                for (int i = 0; i < n; i++)
                    result = (result & ~File.getBb(File.FILE_H)) >>> 7;
                break;
            case Square.SOUTH_WEST:
                for (int i = 0; i < n; i++)
                    result = (result & ~File.getBb(File.FILE_A)) >>> 9;
                break;
        };
        return result;
    }

    public static long getRay(int direction, int sq){
        return switch (direction){
            case Square.NORTH -> BB_RAYS[0][sq];
            case Square.SOUTH -> BB_RAYS[1][sq];
            case Square.EAST -> BB_RAYS[2][sq];
            case Square.WEST -> BB_RAYS[3][sq];
            case Square.NORTH_WEST -> BB_RAYS[4][sq];
            case Square.NORTH_EAST -> BB_RAYS[5][sq];
            case Square.SOUTH_WEST -> BB_RAYS[6][sq];
            case Square.SOUTH_EAST -> BB_RAYS[7][sq];
            default -> 0;
        };
    }

    public static long reverse(long b){
        b = (b & 0x5555555555555555L) << 1 | (b >>> 1) & 0x5555555555555555L;
        b = (b & 0x3333333333333333L) << 2 | (b >>> 2) & 0x3333333333333333L;
        b = (b & 0x0f0f0f0f0f0f0f0fL) << 4 | (b >>> 4) & 0x0f0f0f0f0f0f0f0fL;
        b = (b & 0x00ff00ff00ff00ffL) << 8 | (b >>> 8) & 0x00ff00ff00ff00ffL;

        return (b << 48) | ((b & 0xffff0000) << 16) |
                ((b >>> 16) & 0xffff0000) | (b >>> 48);
    }

    public static long fill(long bb, int direction){
        switch(direction){
            case Square.NORTH:
                bb |= (bb <<  8);
                bb |= (bb << 16);
                bb |= (bb << 32);
                return bb;
            case Square.SOUTH:
                bb |= (bb >>>  8);
                bb |= (bb >>> 16);
                bb |= (bb >>> 32);
                return bb;
            default:
                return 0L;
        }
    }

    public static long fileFill(long bb){
        return fill(bb, Square.NORTH) | fill(bb, Square.SOUTH);
    }


    public static void printBitboard(long bb){
        System.out.println();
        for (int i = 56; i >= 0; i -= 8){
            for (int j = 0; j < 8; j++){
                System.out.print((((bb >>> (i + j)) & 1)) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }







}
