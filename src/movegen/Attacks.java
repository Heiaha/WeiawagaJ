package movegen;

public class Attacks {

    private final static long[] WHITE_PAWN_ATTACKS = {
        0x0000000000000200L, 0x0000000000000500L, 0x0000000000000a00L, 0x0000000000001400L,
        0x0000000000002800L, 0x0000000000005000L, 0x000000000000a000L, 0x0000000000004000L,
        0x0000000000020000L, 0x0000000000050000L, 0x00000000000a0000L, 0x0000000000140000L,
        0x0000000000280000L, 0x0000000000500000L, 0x0000000000a00000L, 0x0000000000400000L,
        0x0000000002000000L, 0x0000000005000000L, 0x000000000a000000L, 0x0000000014000000L,
        0x0000000028000000L, 0x0000000050000000L, 0x00000000a0000000L, 0x0000000040000000L,
        0x0000000200000000L, 0x0000000500000000L, 0x0000000a00000000L, 0x0000001400000000L,
        0x0000002800000000L, 0x0000005000000000L, 0x000000a000000000L, 0x0000004000000000L,
        0x0000020000000000L, 0x0000050000000000L, 0x00000a0000000000L, 0x0000140000000000L,
        0x0000280000000000L, 0x0000500000000000L, 0x0000a00000000000L, 0x0000400000000000L,
        0x0002000000000000L, 0x0005000000000000L, 0x000a000000000000L, 0x0014000000000000L,
        0x0028000000000000L, 0x0050000000000000L, 0x00a0000000000000L, 0x0040000000000000L,
        0x0200000000000000L, 0x0500000000000000L, 0x0a00000000000000L, 0x1400000000000000L,
        0x2800000000000000L, 0x5000000000000000L, 0xa000000000000000L, 0x4000000000000000L,
        0x0, 0x0, 0x0, 0x0,
        0x0, 0x0, 0x0, 0x0,
    };
    private final static long[] BLACK_PAWN_ATTACKS = {
        0x0, 0x0, 0x0, 0x0,
        0x0, 0x0, 0x0, 0x0,
        0x00000000000002L, 0x00000000000005L, 0x0000000000000aL, 0x00000000000014L,
        0x00000000000028L, 0x00000000000050L, 0x000000000000a0L, 0x00000000000040L,
        0x00000000000200L, 0x00000000000500L, 0x00000000000a00L, 0x00000000001400L,
        0x00000000002800L, 0x00000000005000L, 0x0000000000a000L, 0x00000000004000L,
        0x00000000020000L, 0x00000000050000L, 0x000000000a0000L, 0x00000000140000L,
        0x00000000280000L, 0x00000000500000L, 0x00000000a00000L, 0x00000000400000L,
        0x00000002000000L, 0x00000005000000L, 0x0000000a000000L, 0x00000014000000L,
        0x00000028000000L, 0x00000050000000L, 0x000000a0000000L, 0x00000040000000L,
        0x00000200000000L, 0x00000500000000L, 0x00000a00000000L, 0x00001400000000L,
        0x00002800000000L, 0x00005000000000L, 0x0000a000000000L, 0x00004000000000L,
        0x00020000000000L, 0x00050000000000L, 0x000a0000000000L, 0x00140000000000L,
        0x00280000000000L, 0x00500000000000L, 0x00a00000000000L, 0x00400000000000L,
        0x02000000000000L, 0x05000000000000L, 0x0a000000000000L, 0x14000000000000L,
        0x28000000000000L, 0x50000000000000L, 0xa0000000000000L, 0x40000000000000L
    };
    private final static long[][] PAWN_ATTACKS = new long[2][64];
    static{
        for (int sq = Square.A1; sq <= Square.H8; sq++){
            PAWN_ATTACKS[Side.WHITE][sq] = WHITE_PAWN_ATTACKS[sq];
            PAWN_ATTACKS[Side.BLACK][sq] = BLACK_PAWN_ATTACKS[sq];
        }
    }

    private final static long[] KNIGHT_ATTACKS = {
        0x0000000000020400L, 0x0000000000050800L, 0x00000000000a1100L, 0x0000000000142200L,
        0x0000000000284400L, 0x0000000000508800L, 0x0000000000a01000L, 0x0000000000402000L,
        0x0000000002040004L, 0x0000000005080008L, 0x000000000a110011L, 0x0000000014220022L,
        0x0000000028440044L, 0x0000000050880088L, 0x00000000a0100010L, 0x0000000040200020L,
        0x0000000204000402L, 0x0000000508000805L, 0x0000000a1100110aL, 0x0000001422002214L,
        0x0000002844004428L, 0x0000005088008850L, 0x000000a0100010a0L, 0x0000004020002040L,
        0x0000020400040200L, 0x0000050800080500L, 0x00000a1100110a00L, 0x0000142200221400L,
        0x0000284400442800L, 0x0000508800885000L, 0x0000a0100010a000L, 0x0000402000204000L,
        0x0002040004020000L, 0x0005080008050000L, 0x000a1100110a0000L, 0x0014220022140000L,
        0x0028440044280000L, 0x0050880088500000L, 0x00a0100010a00000L, 0x0040200020400000L,
        0x0204000402000000L, 0x0508000805000000L, 0x0a1100110a000000L, 0x1422002214000000L,
        0x2844004428000000L, 0x5088008850000000L, 0xa0100010a0000000L, 0x4020002040000000L,
        0x0400040200000000L, 0x0800080500000000L, 0x1100110a00000000L, 0x2200221400000000L,
        0x4400442800000000L, 0x8800885000000000L, 0x100010a000000000L, 0x2000204000000000L,
        0x0004020000000000L, 0x0008050000000000L, 0x00110a0000000000L, 0x0022140000000000L,
        0x0044280000000000L, 0x0088500000000000L, 0x0010a00000000000L, 0x0020400000000000L
    };

    private final static long[] ADJACENT_ATTACKS = {
        0x0000000000000302L, 0x0000000000000705L, 0x0000000000000e0aL, 0x0000000000001c14L,
        0x0000000000003828L, 0x0000000000007050L, 0x000000000000e0a0L, 0x000000000000c040L,
        0x0000000000030203L, 0x0000000000070507L, 0x00000000000e0a0eL, 0x00000000001c141cL,
        0x0000000000382838L, 0x0000000000705070L, 0x0000000000e0a0e0L, 0x0000000000c040c0L,
        0x0000000003020300L, 0x0000000007050700L, 0x000000000e0a0e00L, 0x000000001c141c00L,
        0x0000000038283800L, 0x0000000070507000L, 0x00000000e0a0e000L, 0x00000000c040c000L,
        0x0000000302030000L, 0x0000000705070000L, 0x0000000e0a0e0000L, 0x0000001c141c0000L,
        0x0000003828380000L, 0x0000007050700000L, 0x000000e0a0e00000L, 0x000000c040c00000L,
        0x0000030203000000L, 0x0000070507000000L, 0x00000e0a0e000000L, 0x00001c141c000000L,
        0x0000382838000000L, 0x0000705070000000L, 0x0000e0a0e0000000L, 0x0000c040c0000000L,
        0x0003020300000000L, 0x0007050700000000L, 0x000e0a0e00000000L, 0x001c141c00000000L,
        0x0038283800000000L, 0x0070507000000000L, 0x00e0a0e000000000L, 0x00c040c000000000L,
        0x0302030000000000L, 0x0705070000000000L, 0x0e0a0e0000000000L, 0x1c141c0000000000L,
        0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L, 0xc040c00000000000L,
        0x0203000000000000L, 0x0507000000000000L, 0x0a0e000000000000L, 0x141c000000000000L,
        0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L
    };

    private final static long[] ROOK_MAGICS = {
        0x0080001020400080L, 0x0040001000200040L, 0x0080081000200080L, 0x0080040800100080L,
        0x0080020400080080L, 0x0080010200040080L, 0x0080008001000200L, 0x0080002040800100L,
        0x0000800020400080L, 0x0000400020005000L, 0x0000801000200080L, 0x0000800800100080L,
        0x0000800400080080L, 0x0000800200040080L, 0x0000800100020080L, 0x0000800040800100L,
        0x0000208000400080L, 0x0000404000201000L, 0x0000808010002000L, 0x0000808008001000L,
        0x0000808004000800L, 0x0000808002000400L, 0x0000010100020004L, 0x0000020000408104L,
        0x0000208080004000L, 0x0000200040005000L, 0x0000100080200080L, 0x0000080080100080L,
        0x0000040080080080L, 0x0000020080040080L, 0x0000010080800200L, 0x0000800080004100L,
        0x0000204000800080L, 0x0000200040401000L, 0x0000100080802000L, 0x0000080080801000L,
        0x0000040080800800L, 0x0000020080800400L, 0x0000020001010004L, 0x0000800040800100L,
        0x0000204000808000L, 0x0000200040008080L, 0x0000100020008080L, 0x0000080010008080L,
        0x0000040008008080L, 0x0000020004008080L, 0x0000010002008080L, 0x0000004081020004L,
        0x0000204000800080L, 0x0000200040008080L, 0x0000100020008080L, 0x0000080010008080L,
        0x0000040008008080L, 0x0000020004008080L, 0x0000800100020080L, 0x0000800041000080L,
        0x00FFFCDDFCED714AL, 0x007FFCDDFCED714AL, 0x003FFFCDFFD88096L, 0x0000040810002101L,
        0x0001000204080011L, 0x0001000204000801L, 0x0001000082000401L, 0x0001FFFAABFAD1A2L
    };

    private final static long[] BISHOP_MAGICS = {
        0x0002020202020200L, 0x0002020202020000L, 0x0004010202000000L, 0x0004040080000000L,
        0x0001104000000000L, 0x0000821040000000L, 0x0000410410400000L, 0x0000104104104000L,
        0x0000040404040400L, 0x0000020202020200L, 0x0000040102020000L, 0x0000040400800000L,
        0x0000011040000000L, 0x0000008210400000L, 0x0000004104104000L, 0x0000002082082000L,
        0x0004000808080800L, 0x0002000404040400L, 0x0001000202020200L, 0x0000800802004000L,
        0x0000800400A00000L, 0x0000200100884000L, 0x0000400082082000L, 0x0000200041041000L,
        0x0002080010101000L, 0x0001040008080800L, 0x0000208004010400L, 0x0000404004010200L,
        0x0000840000802000L, 0x0000404002011000L, 0x0000808001041000L, 0x0000404000820800L,
        0x0001041000202000L, 0x0000820800101000L, 0x0000104400080800L, 0x0000020080080080L,
        0x0000404040040100L, 0x0000808100020100L, 0x0001010100020800L, 0x0000808080010400L,
        0x0000820820004000L, 0x0000410410002000L, 0x0000082088001000L, 0x0000002011000800L,
        0x0000080100400400L, 0x0001010101000200L, 0x0002020202000400L, 0x0001010101000200L,
        0x0000410410400000L, 0x0000208208200000L, 0x0000002084100000L, 0x0000000020880000L,
        0x0000001002020000L, 0x0000040408020000L, 0x0004040404040000L, 0x0002020202020000L,
        0x0000104104104000L, 0x0000002082082000L, 0x0000000020841000L, 0x0000000000208800L,
        0x0000000010020200L, 0x0000000404080200L, 0x0000040404040400L, 0x0002020202020200L
    };

    public final static int[] BISHOP_SHIFTS = new int[64];
    final static long[] BISHOP_ATTACK_MASKS = new long[64];
    final static long[][] BISHOP_ATTACKS = new long[64][512];

    //initialize bishop magics
    static {
        long edges, subset, index;

        for (int sq = Square.A1; sq <= Square.H8; sq++){
            edges = ((Rank.getBb(Rank.RANK_1) | Rank.getBb(Rank.RANK_8)) & ~Rank.getBb(Square.getRank(sq))) |
                    ((File.getBb(File.FILE_A) | File.getBb(File.FILE_H)) & ~File.getBb(Square.getFile(sq)));
            BISHOP_ATTACK_MASKS[sq] = (Diagonal.getBb(Square.getDiagonal(sq)) ^ AntiDiagonal.getBb(Square.getAntiDiagonal(sq))) & ~edges;
            BISHOP_SHIFTS[sq] = 64 - Bitboard.popcount(BISHOP_ATTACK_MASKS[sq]);

            subset = 0;
            do {
                index = subset;
                index *= BISHOP_MAGICS[sq];
                index >>>= BISHOP_SHIFTS[sq];
                BISHOP_ATTACKS[sq][(int)index] = getBishopAttacksSlow(sq, subset);
                subset = (subset - BISHOP_ATTACK_MASKS[sq]) & BISHOP_ATTACK_MASKS[sq];
            } while(subset != 0L);
        }
    }

    final static int[] ROOK_SHIFTS = new int[64];
    final static long[] ROOK_ATTACK_MASKS = new long[64];
    final static long[][] ROOK_ATTACKS = new long[64][4096];

    //initialize rook magics
    static {
        long edges, subset, index;

        for (int sq = Square.A1; sq <= Square.H8; sq++){
            edges = ((Rank.getBb(Rank.RANK_1) | Rank.getBb(Rank.RANK_8)) & ~Rank.getBb(Square.getRank(sq))) |
                    ((File.getBb(File.FILE_A) | File.getBb(File.FILE_H)) & ~File.getBb(Square.getFile(sq)));
            ROOK_ATTACK_MASKS[sq] = (Rank.getBb(Square.getRank(sq)) ^ File.getBb(Square.getFile(sq))) & ~edges;
            ROOK_SHIFTS[sq] = 64 - Bitboard.popcount(ROOK_ATTACK_MASKS[sq]);

            subset = 0;
            do {
                index = subset;
                index *= ROOK_MAGICS[sq];
                index >>>= ROOK_SHIFTS[sq];
                ROOK_ATTACKS[sq][(int)index] = getRookAttacksSlow(sq, subset);
                subset = (subset - ROOK_ATTACK_MASKS[sq]) & ROOK_ATTACK_MASKS[sq];
            } while (subset != 0);
        }
    }

    public static long getBishopAttacks(int sq, long occ){
        return BISHOP_ATTACKS[sq][(int)(((occ & BISHOP_ATTACK_MASKS[sq]) * BISHOP_MAGICS[sq])
                >>> BISHOP_SHIFTS[sq])];
    }

    public static long getRookAttacks(int sq, long occ){
        return ROOK_ATTACKS[sq][(int)(((occ & ROOK_ATTACK_MASKS[sq]) * ROOK_MAGICS[sq])
                >>> ROOK_SHIFTS[sq])];
    }

    public static long getKnightAttacks(int sq){
        return KNIGHT_ATTACKS[sq];
    }

    public static long getKingAttacks(int sq){
        return ADJACENT_ATTACKS[sq];
    }

    public static long getRookAttacksSlow(int sq, long blockers){

        long attacks = 0;
        attacks |= Bitboard.getRay(Square.NORTH, sq);
        if ((Bitboard.getRay(Square.NORTH, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.NORTH, Bitboard.lsb(Bitboard.getRay(Square.NORTH, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.SOUTH, sq);
        if ((Bitboard.getRay(Square.SOUTH, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.SOUTH, Bitboard.msb(Bitboard.getRay(Square.SOUTH, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.EAST, sq);
        if ((Bitboard.getRay(Square.EAST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.EAST, Bitboard.lsb(Bitboard.getRay(Square.EAST, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.WEST, sq);
        if ((Bitboard.getRay(Square.WEST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.WEST, Bitboard.msb(Bitboard.getRay(Square.WEST, sq) & blockers)));

        return attacks;
    }

    public static long getBishopAttacksSlow(int sq, long blockers){

        long attacks = 0;

        attacks |= Bitboard.getRay(Square.NORTH_WEST, sq);
        if ((Bitboard.getRay(Square.NORTH_WEST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.NORTH_WEST, Bitboard.lsb(Bitboard.getRay(Square.NORTH_WEST, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.NORTH_EAST, sq);
        if ((Bitboard.getRay(Square.NORTH_EAST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.NORTH_EAST, Bitboard.lsb(Bitboard.getRay(Square.NORTH_EAST, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.SOUTH_EAST, sq);
        if ((Bitboard.getRay(Square.SOUTH_EAST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.SOUTH_EAST, Bitboard.msb(Bitboard.getRay(Square.SOUTH_EAST, sq) & blockers)));

        attacks |= Bitboard.getRay(Square.SOUTH_WEST, sq);
        if ((Bitboard.getRay(Square.SOUTH_WEST, sq) & blockers) != 0L)
            attacks &= ~(Bitboard.getRay(Square.SOUTH_WEST, Bitboard.msb(Bitboard.getRay(Square.SOUTH_WEST, sq) & blockers)));

        return attacks;
    }

    public static long pawnAttacks(long bb, int side){
        return side == Side.WHITE ? Bitboard.shift(bb, Square.NORTH_WEST) | Bitboard.shift(bb, Square.NORTH_EAST) :
                                    Bitboard.shift(bb, Square.SOUTH_WEST) | Bitboard.shift(bb, Square.SOUTH_EAST);
    }

    public static long pawnAttackSpan(int sq, int side) {
        return Rank.forwardRanksBb(sq, side) & Square.getAdjacentFileBb(sq);
    }

    public static long pawnDoubleAttacks(long bb, int side) {
        return side == Side.WHITE ? Bitboard.shift(bb, Square.NORTH_WEST) & Bitboard.shift(bb, Square.NORTH_EAST) :
                                    Bitboard.shift(bb, Square.SOUTH_WEST) & Bitboard.shift(bb, Square.SOUTH_EAST);
    }


    public static long pawnAttacks(int square, int side){
        return PAWN_ATTACKS[side][square];
    }

    // Sliding attacks from a given square & axis taking into account blocking pieces.
    // Uses hyperbola quintessence
    // https://www.chessprogramming.org/Hyperbola_Quintessence#:~:text=Hyperbola%20Quintessence%20applies%20the%20o,flip%20aka%20x86%2D64%20bswap%20.
    public static long slidingAttacks(int square, long occ, long mask){
        return (((mask & occ) - Square.getBb(square) * 2) ^
                Bitboard.reverse(Bitboard.reverse(mask & occ) - Bitboard.reverse(Square.getBb(square)) * 2)) & mask;
    }

    public static long attacks(int pieceType, int square, long occ){
        return switch (pieceType) {
            case PieceType.ROOK -> getRookAttacks(square, occ);
            case PieceType.BISHOP -> getBishopAttacks(square, occ);
            case PieceType.QUEEN -> getBishopAttacks(square, occ) | getRookAttacks(square, occ);
            case PieceType.KING -> getKingAttacks(square);
            case PieceType.KNIGHT -> getKnightAttacks(square);
            default -> 0L;
        };
    }
}
