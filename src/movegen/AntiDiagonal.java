package movegen;

public class AntiDiagonal {
    public final static int A1_A1 =  0, B1_A2 = 1, C1_A3 = 2, D1_A4 =  3, E1_A5 =  4, F1_A6 =  5, G1_A7 =  6,
                            H1_A8 =  7, B8_H2 = 8, C8_H3 = 9, D8_H4 = 10, E8_H5 = 11, F8_H6 = 12, G8_H7 = 13,
                            H8_H8 = 14;

    final static long[] BB_ANTIDIAGONALS = {
            0x0000000000000001L,
            0x0000000000000102L,
            0x0000000000010204L,
            0x0000000001020408L,
            0x0000000102040810L,
            0x0000010204081020L,
            0x0001020408102040L,
            0x0102040810204080L,
            0x0204081020408000L,
            0x0408102040800000L,
            0x0810204080000000L,
            0x1020408000000000L,
            0x2040800000000000L,
            0x4080000000000000L,
            0x8000000000000000L
    };

    public static long getBb(int antidiagonal){
        return BB_ANTIDIAGONALS[antidiagonal];
    }
}
