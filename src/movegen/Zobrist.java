package movegen;
import java.util.Random;

public class Zobrist {
    public static long[][] ZOBRIST_TABLE = new long[15][64];
    public static long[] EN_PASSANT = new long[8];
    public static long SIDE = new Random().nextLong();
    static{
        for (int piece = 0; piece < 15; piece++){
            for (int sq = Square.A1; sq <= Square.H8; sq++)
                ZOBRIST_TABLE[piece][sq] = new Random().nextLong();
        }

        for (int file = File.FILE_A; file <= File.FILE_H; file++)
            EN_PASSANT[file] = new Random().nextLong();
    }
}
