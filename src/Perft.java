import movegen.Board;
import movegen.Move;
import movegen.MoveList;

public class Perft {

    public static long printPerft(Board board, int depth) {
        final long start_time = System.currentTimeMillis();
        long nodes = 0;
        MoveList moves = board.generateLegalMoves();
        for (Move move : moves){
            System.out.print(move.uci() + " ");
            board.push(move);
            long move_nodes = perft(board, depth - 1);
            board.pop();
            nodes += move_nodes;
            System.out.println(move_nodes);
        }
        final long end_time = System.currentTimeMillis();
        double time_taken = (end_time - start_time)/1000.0;
        System.out.println("Nodes: " + nodes);
        System.out.println("Time: " + time_taken + "s");
        System.out.println("NPS: " + nodes/time_taken);
        return nodes;
    }

    public static long perft(Board board, int depth) {
        MoveList moves = board.generateLegalMoves();
        if (depth == 0) {
            return moves.size();
        }
        long nodes = 0;
        for (Move move : moves) {
            board.push(move);
            nodes += perft(board, depth - 1);
            board.pop();
        }
        return nodes;
    }

}
