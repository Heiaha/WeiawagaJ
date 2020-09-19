import mind.*;
import movegen.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static String ENGINENAME = "Weiawaga";
    private final static String AUTHOR = "Mal";

    private final static String UCI = "uci";
    private final static String GOTHINK = "go";
    private final  static String MOVEFROMSTART = "position startpos moves ";
    private final static String ISREADY = "isready";
    private final static String STOP = "stop";
    private final static String QUIT = "quit";
    private final static String UCINEWGAME = "ucinewgame";



    public static void main(String[] args) throws IOException {

        FileWriter fr = new FileWriter("./uciCommands.txt", true);
        BufferedWriter bw = new BufferedWriter(fr);
        Scanner input_stream = new Scanner(System.in);
        Search searcher = new Search();
        Board board = new Board();

        while (true){
            String input = input_stream.nextLine();
            bw.write(input + "\n");
            if (input.equals(QUIT)){
                System.exit(0);
            }
            else if (input.equals(UCI)){
                board = new Board();
                System.out.println("id name " + ENGINENAME);
                System.out.println("id author " + AUTHOR);
                System.out.println("uciok");
            }
            else if (input.equals(ISREADY)){
                board = new Board();
                System.out.println("readyok");
            }
            else if (input.startsWith(MOVEFROMSTART)){
                board = new Board();
                String movesString = input.replace(MOVEFROMSTART, "");
                String[] movesStringArray = movesString.split(" ", 0);

                //correct for duplicates in lichess api (https://github.com/ShailChoksi/lichess-bot/issues/214)
                List<String> correctedMoveStrings = new ArrayList<>();
                correctedMoveStrings.add(movesStringArray[0]);
                for (int i = 1; i < movesStringArray.length; i++){
                    if (!movesStringArray[i].equals(movesStringArray[i - 1]))
                        correctedMoveStrings.add(movesStringArray[i]);
                }

                for (String move_string : correctedMoveStrings){
                    board.pushFromString(move_string);
                }
            }
            else if (input.startsWith(GOTHINK)){
                String[] command = input.split(" ");
                int wtimeIndex = linearSearch(command, "wtime");
                if (wtimeIndex != -1)
                    Limits.time[Side.WHITE] = Long.parseLong(command[wtimeIndex + 1]);
                int btimeIndex = linearSearch(command, "btime");
                if (btimeIndex != -1)
                    Limits.time[Side.BLACK] = Long.parseLong(command[btimeIndex + 1]);
                int wincIndex = linearSearch(command, "winc");
                if (wincIndex != -1)
                    Limits.increment[Side.WHITE] = Long.parseLong(command[wincIndex + 1]);
                int bincIndex = linearSearch(command, "binc");
                if (bincIndex != -1)
                    Limits.increment[Side.BLACK] = Long.parseLong(command[bincIndex + 1]);

                long startTime = System.currentTimeMillis();
                searcher.itDeep(board);
                long endTime = System.currentTimeMillis();
                Move best_move = searcher.IDMove;
                int best_value = searcher.IDScore;

                System.out.println("info score cp " + best_value);
                System.out.println("bestmove " + best_move.uci());
                TranspTable.reset();
                System.gc();
            }
            bw.flush();
        }

    }

    public static void printMoves(Board board, int depth){
        int alpha = -Search.INF;
        int beta = Search.INF;
        Search searcher = new Search();
        int score;
        for (Move move : board.generateLegalMoves()){
            board.push(move);
            score = -searcher.negamax(board, depth, -beta, -alpha);
            board.pop();
            if (move.flags() == Move.OO || move.flags() == Move.OOO)
                System.out.print("Castling ");
            System.out.print(move.uci() + " ");
            System.out.println(score);
        }
    }

    public static int linearSearch(Object[] arr, Object val){
        for (int i = 0; i < arr.length; i++){
            if (arr[i].equals(val))
                return i;
        }
        return -1;
    }

    private static long printPerft(Board board, int depth) {
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

    private static long perft(Board board, int depth) {
        MoveList moves = board.generateLegalMoves();
        if (depth == 1) {
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
