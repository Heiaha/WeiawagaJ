package texel;

import search.Score;

import java.io.FileWriter;
import java.io.IOException;

public class Parameter {

    public static int MG = 0;
    public static int EG = 1;

    private int[] values;
    private int[] originalValues;
    private int[] bestValues;


    public String name;
    public int length;

    public Parameter(int[] values, String name){
        this.name = name;
        this.values = values;
        length = values.length;

        originalValues = new int[values.length];
        System.arraycopy(values, 0, originalValues, 0, values.length);

        bestValues = new int[values.length];
        System.arraycopy(values, 0, bestValues, 0, values.length);

    }

    public Parameter(int value, String name){
        this.name = name;
        this.values = new int[]{value};
        length = 1;

        originalValues = new int[values.length];
        System.arraycopy(values, 0, originalValues, 0, values.length);

        bestValues = new int[values.length];
        System.arraycopy(values, 0, bestValues, 0, values.length);

    }

    public void update(int i, int step, int phase){
        int score = bestValues[i];
        int mg = Score.mg(score);
        int eg = Score.eg(score);
        values[i] = phase == MG ? Score.score(mg + step, eg) : Score.score(mg, eg + step);;
    }

    public void markBest() {
        System.arraycopy(values, 0, bestValues, 0, values.length);
    }

    public void printAndSave() throws IOException {
        FileWriter writer = new FileWriter("results.txt", true);
        String output = "Name: " + name + "\n";
        for (int i = 0; i < bestValues.length; i++){
            output += "Score.score(" + Score.mg(bestValues[i]) + ", " + Score.eg(bestValues[i]) +"), ";
            if ((i + 1)%8 == 0)
                output += "\n";

        }
        output += "\n";
        System.out.println(output);
        writer.write(output);
        writer.flush();
    }
}
