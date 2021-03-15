package search;
import evaluation.EConstants;

public class Score {

    public static int score(int mg, int eg){
        return (mg << 16) + eg;
    }

    public static int mg(int score) {
        return (score + 0x8000) >> 16;
    }

    public static int eg(int score) {
        return (short)(score & 0xffff);
    }

    public static int eval(int score, int phase){
        phase = (phase*256 + (EConstants.TOTAL_PHASE / 2)) / EConstants.TOTAL_PHASE;
        return ((mg(score)*(256 - phase)) + (eg(score) * phase))/256;
    }

}