package movegen;
import java.util.Random;

public class Zobrist {
    public static long[][] ZOBRIST_TABLE = new long[15][64];
    static{
        for (int piece = 0; piece < 15; piece++){
            for (int sq = Square.A1; sq <= Square.H8; sq++)
                ZOBRIST_TABLE[piece][sq] = new Random().nextLong();
        }
    }
}
