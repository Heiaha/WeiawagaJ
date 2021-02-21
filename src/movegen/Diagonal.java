package movegen;

public class Diagonal {
    public final static int H1_H1 =  0, G1_H2 = 1, F1_H3 = 2, E1_H4 =  3, D1_H5 =  4, C1_H6 =  5, B1_H7 =  6,
                      H8_A1 =  7, G8_A2 = 8, F8_A3 = 9, E8_A4 = 10, D8_A5 = 11, C8_A6 = 12, B8_A7 = 13,
                      A8_A8 = 14;

    final static long[] BB_DIAGONALS = {
            0x0000000000000080L,
            0x0000000000008040L,
            0x0000000000804020L,
            0x0000000080402010L,
            0x0000008040201008L,
            0x0000804020100804L,
            0x0080402010080402L,
            0x8040201008040201L,
            0x4020100804020100L,
            0x2010080402010000L,
            0x1008040201000000L,
            0x0804020100000000L,
            0x0402010000000000L,
            0x0201000000000000L,
            0x0100000000000000L
    };

    public static long getBb(int diagonal){
        return BB_DIAGONALS[diagonal];
    }
}
