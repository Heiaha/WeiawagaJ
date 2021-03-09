package texel;

import evaluation.*;
import movegen.Board;
import movegen.PieceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tuner {
    public static List<Parameter> parameters = new ArrayList<Parameter>();
    public static Board[] positions;
    public static double[] results;
    public static String filename;

    public static void setup(String filepath) throws FileNotFoundException {
        filename = filepath;
        getDataset();
        parameters.add(new Parameter(EConstants.PIECE_TYPE_VALUES_TUNING, "Piece Type Values"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.PAWN], "Pawn PST"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.KNIGHT], "Knight PST"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.BISHOP], "Bishop PST"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.ROOK], "Rook PST"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.QUEEN], "Queen PST"));
        parameters.add(new Parameter(EConstants.PIECE_TYPE_TABLES[PieceType.KING], "King PST" ));
        parameters.add(new Parameter(EConstants.PAWN_SCORES, "Pawn Scores" ));
        parameters.add(new Parameter(EConstants.BISHOP_SCORES, "Bishop Scores"));
        parameters.add(new Parameter(EConstants.ROOK_SCORES, "Rook Scores"));
        parameters.add(new Parameter(EConstants.KING_SCORES, "King Scores"));
        parameters.add(new Parameter(EConstants.TEMPO, "Tempo"));


    }

    public static double findBestK() {
        double K = 1.0;
        double step = 0.001;
        boolean improved = true;
        double bestError = meanSquaredError(K);
        while (improved){
            improved = false;
            double newError = meanSquaredError(K + step);
            if (newError < bestError) {
                K += step;
                bestError = newError;
                improved = true;
            }
            else {
                newError = meanSquaredError(K - step);
                if (newError < bestError) {
                    K -= step;
                    bestError = newError;
                    improved = true;
                }
            }
            System.out.println("K = " + K);
        }
        return K;
    }

    public static void getDataset() throws FileNotFoundException {
        System.out.println("Loading dataset...");
        File fenFile = new File(filename);
        Scanner positionScanner = new Scanner(fenFile);
        int npositions = 0;
        while (positionScanner.hasNextLine()) {
            positionScanner.nextLine();
            npositions++;
        }
        positions = new Board[npositions];
        results = new double[npositions];
        positionScanner = new Scanner(fenFile);
        int i = 0;
        while (positionScanner.hasNextLine()) {
            String data = positionScanner.nextLine();
            String fen = data.split(",", 0)[0];
            double result = Double.parseDouble(data.split(",", 0)[1]);
            positions[i] = new Board(fen);
            results[i] = result;
            i++;
        }
    }

    public static double meanSquaredError(double K) {
        double error = 0;
        int nPositions = 0;
        for (int i = 0; i < positions.length; i++) {
            nPositions++;
            Board board = positions[i];
            double result = results[i];
            int score = Evaluation.evaluateForTune(board);

            double sigmoid = 1 / (1 + Math.pow(10, -K*score/400));
            error += Math.pow(result - sigmoid, 2);
        }
        error /= nPositions;
        return error;
    }

    public static void tune() throws IOException {

        double K = findBestK();
        int adjust_value = 1;
        boolean improved = true;
        double bestError = meanSquaredError(K);
        while (improved) {
            improved = false;
            for (Parameter param : parameters) {
                for (int phase = 0; phase <= 1; phase++) {
                    for (int i = 0; i < param.length; i++) {
                        param.update(i, adjust_value, phase);
                        double newError = meanSquaredError(K);
                        if (newError < bestError) {
                            bestError = newError;
                            param.markBest();
                            improved = true;
                            continue;
                        }
                        param.update(i, -adjust_value, phase);
                        newError = meanSquaredError(K);
                        if (newError < bestError) {
                            bestError = newError;
                            param.markBest();
                            improved = true;
                        }
                    }
                }
                System.out.println("Tuned parameter \"" + param.name + "\" with MSE = " + bestError);
            }
            for (Parameter param : parameters) {
                param.printAndSave();
            }
        }
    }
}

