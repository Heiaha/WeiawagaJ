package movegen;

public class Move {
    public final static int QUIET = 0b0000, DOUBLE_PUSH = 0b0001, OO = 0b0010, OOO = 0b0011,
        CAPTURE = 0b0100,  EN_PASSANT = 0b0101, PROMOTION = 0b1000,
        PR_KNIGHT = 0b1000, PR_BISHOP = 0b1001, PR_ROOK = 0b1010, PR_QUEEN = 0b1011,
        PC_KNIGHT = 0b1100, PC_BISHOP = 0b1101, PC_ROOK = 0b1110, PC_QUEEN = 0b1111, NULL = 0b1001;

    public final static Move nullMove = new Move(Square.NO_SQUARE, Square.NO_SQUARE, Move.NULL);
    private final int move;
    private int sortScore;

    public Move(){
        move = 0;
        sortScore = 0;
    }

    public Move(int m){
        move = m;
        sortScore = 0;
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

    public int flags() {
        return (move >>> 12) & 0xf;
    }

    public int move(){
        return move;
    }

    public int score(){
        return sortScore;
    }

    public boolean isCapture() {
        return ((move >>> 12 ) & CAPTURE) != 0;
    }

    public boolean isPromotion() {
        return ((move >>> 12 ) & PROMOTION) != 0;
    }

    public static Move nullMove(){
        return new Move(Square.NO_SQUARE, Square.NO_SQUARE, Move.NULL);
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && getClass() == other.getClass())
            return this.move == ((Move)other).move();
        return false;
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

    public void addToScore(int score){
        sortScore += score;
    }
}
