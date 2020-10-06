package movegen;

public class UndoInfo {
    public long entry;
    public int captured;
    public int epsq;
    public int move;
    public long hash;
    public long materialHash;
    public int halfmoveCounter;
    public int pliesFromNull;
    public UndoInfo(){
        entry = 0;
        captured = Piece.NONE;
        epsq = Square.NO_SQUARE;
        move = 0;
        hash = 0;
        halfmoveCounter = 0;
        pliesFromNull = 0;
    }
}
