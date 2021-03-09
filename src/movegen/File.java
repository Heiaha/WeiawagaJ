package movegen;

public class File {
    public final static int FILE_A = 0, FILE_B = 1, FILE_C = 2, FILE_D = 3, FILE_E = 4, FILE_F = 5, FILE_G = 6, FILE_H = 7;

    private final static long[] BB_FILES = {
            0x0101010101010101L,
            0x0202020202020202L,
            0x0404040404040404L,
            0x0808080808080808L,
            0x1010101010101010L,
            0x2020202020202020L,
            0x4040404040404040L,
            0x8080808080808080L
    };

    private final static long[] BB_ADJACENT = {
            0x0202020202020202L,
            0x0505050505050505L,
            0x0A0A0A0A0A0A0A0AL,
            0x1414141414141414L,
            0x2828282828282828L,
            0x5050505050505050L,
            0xA0A0A0A0A0A0A0A0L,
            0x4040404040404040L
    };

    public static long getBb(int file){
        return BB_FILES[file];
    }

    public static int relativeFile(int file, int side){
        return side == Side.WHITE ? file : FILE_H - file;
    }

    public static long getAdjacentBb(int file){
        return BB_ADJACENT[file];
    }

    public static long forwardFileBb(int sq, int side){
        return Rank.forwardRanksBb(sq, side) & Square.getFileBb(sq);
    }
    public static long backwardFileBb(int sq, int side) {
        return Rank.forwardRanksBb(sq, Side.flip(side)) & Square.getFileBb(sq);
    }

}
