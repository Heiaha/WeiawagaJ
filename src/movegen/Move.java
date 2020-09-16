package movegen;

public class Move {
    public final static int QUIET = 0b0000, DOUBLE_PUSH = 0b0001, OO = 0b0010, OOO = 0b0011,
        CAPTURE = 0b1000, CAPTURES = 0b1111, EN_PASSANT = 0b1010, PROMOTIONS = 0b0111,
        PR_KNIGHT = 0b0100, PR_BISHOP = 0b0101, PR_ROOK = 0b0110, PR_QUEEN = 0b0111,
        PC_KNIGHT = 0b1100, PC_BISHOP = 0b1101, PC_ROOK = 0b1110, PC_QUEEN = 0b1111, NULL = 0b1001;

    private final int move;

    public Move(){
        move = 0;
    }

    public Move(int m){
        move = m;
    }

    public Move(int from, int to){
        move = (from << 6) | to;
    }

    public Move(int from, int to, int flags){
        move = (flags << 12) | (from << 6) | to;
    }

    public int to(){
        return move & 0x3f;
    }

    public int from(){
        return (move >>> 6) & 0x3f;
    }

    public int toFrom(){
        return move & 0xffff;
    }

    public int flags(){
        return (move >>> 12) & 0xf;
    }

    public int move(){
        return move;
    }

    public static Move nullMove(){
        return new Move(Square.NO_SQUARE, Square.NO_SQUARE, Move.NULL);
    }


    public String uci(){
        String promo = switch (this.flags()) {
            case Move.PC_BISHOP, Move.PR_BISHOP -> "b";
            case Move.PC_KNIGHT, Move.PR_KNIGHT -> "n";
            case Move.PC_ROOK, Move.PR_ROOK -> "r";
            case Move.PC_QUEEN, Move.PR_QUEEN -> "q";
            default -> "";
        };
        return Square.getName(this.from()) + Square.getName(this.to()) + promo;
    }
}
