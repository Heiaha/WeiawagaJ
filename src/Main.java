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
    private final static String POSITIONFEN = "position fen";
    private final static SearchThread thread = new SearchThread();


    public static void main(String[] args) throws IOException {

        FileWriter fr = new FileWriter("./uciCommands.txt", true);
        BufferedWriter bw = new BufferedWriter(fr);
        Scanner input_stream = new Scanner(System.in);
        Board board = new Board();

        while (true){
            String input = input_stream.nextLine();
            bw.write(input + "\n");
            if (input.equals(QUIT)){
                System.exit(0);
            }
            else if (input.equals(UCINEWGAME)){
                board = new Board();
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
                System.out.println(board.pSqScore().mg);
                System.out.println(board.pSqScore().eg);
            }
            else if (input.startsWith(POSITIONFEN)){
                String fenString = input.replace(POSITIONFEN, "");
                fenString = fenString.trim();
                board.setFen(fenString);
                System.out.println(board.pSqScore().mg);
                System.out.println(board.pSqScore().eg);
            }
            else if (input.startsWith(GOTHINK)){
                String[] command = input.split(" ");
                int moveTimeIndex = linearSearch(command, "movetime");
                if (input.contains("infinite"))
                    Limits.timeAllocated = Long.MAX_VALUE;
                if (moveTimeIndex != -1)
                    Limits.timeAllocated = Long.parseLong(command[moveTimeIndex + 1]);
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
                thread.startSearch(board);
            }
            else if (input.equals(STOP)){
                thread.stop();
            }
            bw.flush();
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
