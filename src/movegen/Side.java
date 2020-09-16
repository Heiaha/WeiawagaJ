package movegen;

public class Side {
    public final static int WHITE = 0;
    public final static int BLACK = 1;

    public static int flip(int side){
        return WHITE == side ? BLACK : WHITE;
    }

}
