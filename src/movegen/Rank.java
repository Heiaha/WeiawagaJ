package movegen;

public class Rank {
    public final static int RANK_1 = 0, RANK_2 = 1, RANK_3 = 2, RANK_4 = 3, RANK_5 = 4, RANK_6 = 5, RANK_7 = 6, RANK_8 = 7;

    private final static long[] BB_RANKS = {0x00000000000000FFL, 0x000000000000FF00L, 0x0000000000FF0000L, 0x00000000FF000000L,
            0x000000FF00000000L, 0x0000FF0000000000L, 0x00FF000000000000L, 0xFF00000000000000L};

    public static long getBb(int rank){
        return BB_RANKS[rank];
    }

    public static int relativeRank(int rank, int side){
        return side == Side.WHITE ? rank : Rank.RANK_8 - rank;
    }

    public static long forwardRanksBb(int sq, int side){
        return side == Side.WHITE ? ~BB_RANKS[RANK_1] << 8 * relativeRank(Square.getRank(sq), side)
                                  : ~BB_RANKS[RANK_8] >>> 8 * relativeRank(Square.getRank(sq), side);
    }


}
