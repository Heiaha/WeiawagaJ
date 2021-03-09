import evaluation.Evaluation;
import search.*;
import movegen.*;
import texel.Tuner;

import java.io.IOException;
import java.util.Scanner;

public class UCIClient {
    private final static String ENGINENAME = "Weiawaga";
    private final static String AUTHOR = "Mal";
    private final static String UCI = "uci";
    private final static String GOTHINK = "go";
    private final static String MOVEFROMSTART = "position startpos moves ";
    private final static String ISREADY = "isready";
    private final static String STOP = "stop";
    private final static String QUIT = "quit";
    private final static String UCINEWGAME = "ucinewgame";
    private final static String POSITIONFEN = "position fen";
    private final static String TUNE = "tune";
    private final static int DEFAULT_MAX_SEARCH_DEPTH = 99;


    private final static SearchThread thread = new SearchThread();
    private static Board board = new Board();

    public static void run() throws IOException {
        System.out.println("Weiawaga v2.0, March 8, 2021");
        System.out.println("Homepage and source code: https://github.com/Heiaha/Weiawaga");
        Scanner inputStream = new Scanner(System.in);

        while (true) {
            String input = inputStream.nextLine();
            if (input.equals(QUIT)) {
                System.exit(0);
            } else if (input.equals(UCINEWGAME)) {
                board = new Board();
                TranspTable.reset();
            } else if (input.equals(UCI)) {
                board = new Board();
                System.out.println("id name " + ENGINENAME);
                System.out.println("id author " + AUTHOR);
                System.out.println("uciok");
            } else if (input.equals(ISREADY)) {
                System.out.println("readyok");
            } else if (input.startsWith(MOVEFROMSTART) || input.startsWith(POSITIONFEN)) {
                parse(input);
            } else if (input.startsWith(GOTHINK)) {
                go(input.split(" "));
            } else if (input.equals(STOP)) {
                thread.stop();
            }
            else if (input.equals("evaluate"))
                System.out.println(Evaluation.evaluateForTune(board));
            else if (input.startsWith(TUNE)){
                String file = input.split(" ")[1];
                Tuner.setup(file);
                Tuner.tune();
            }
        }
    }

    public static void go(String[] parameters){
        Search.maxSearchDepth = DEFAULT_MAX_SEARCH_DEPTH;
        Search.waitForStop = false;
        long whiteTime = getParameter(parameters, "wtime", Long.MAX_VALUE);
        long blackTime = getParameter(parameters, "btime", Long.MAX_VALUE);
        long winc = getParameter(parameters, "winc", 0L);
        long binc = getParameter(parameters, "binc", 0L);
        int depth = (int)getParameter(parameters, "depth", 0L);
        long moveTime = getParameter(parameters, "movetime", 0L);
        boolean infiniteFlag = getFlag(parameters, "infinite");

        if (whiteTime != Long.MAX_VALUE){
            Limits.time[Side.WHITE] = whiteTime;
        }
        if (blackTime != Long.MAX_VALUE){
            Limits.time[Side.BLACK] = blackTime;
        }

        if (winc != 0){
            Limits.increment[Side.WHITE] = winc;
        }
        if (binc != 0){
            Limits.increment[Side.BLACK] = binc;
        }

        Limits.calcTime(board.getSideToPlay(), board.gamePly());
        if (moveTime != 0){
            Limits.setTime(moveTime);
        }
        if (depth != 0) {
            Search.maxSearchDepth = depth;
        }

        if (infiniteFlag){
            Limits.time[Side.WHITE] = Long.MAX_VALUE;
            Limits.time[Side.BLACK] = Long.MAX_VALUE;
            Limits.setTime(Long.MAX_VALUE);
            Search.waitForStop = true;
        }
        thread.startSearch(board);
    }


    private static long getParameter(String[] parameters, String name, long defaultValue){
        for (int i = 0; i < parameters.length; i++){
            if (parameters[i].equals(name)) {
                return Long.parseLong(parameters[i + 1]);
            }
        }
        return defaultValue;
    }

    private static boolean getFlag(String[] parameters, String flagName){
        for (String element : parameters){
            if (flagName.equals(element))
                return true;
        }
        return false;
    }

    public static void parse(String command){
        board = new Board();
        if (command.contains("startpos")){
            String movesString = command.replace(MOVEFROMSTART, "");
            String[] movesStringArray = movesString.split(" ", 0);
            for (String move : movesStringArray){
                board.pushFromString(move);
            }
        }
        else if (command.contains("fen")){
            String fen = command.replace(POSITIONFEN, "").trim();
            board.setFen(fen);
        }
    }
}
