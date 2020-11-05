package mind;
import movegen.*;

public class Limits {
    public static long[] time = {Long.MAX_VALUE, Long.MAX_VALUE};
    public static long[] increment = {0, 0};
    public static long timeAllocated = Long.MAX_VALUE;
    public static long startTime;
    public static int limitCheckCount = 4096;

    public final static int overhead = 100;

    public static boolean checkLimits(){
        if (--limitCheckCount > 0)
            return false;

        limitCheckCount = 4096;

        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed >= timeAllocated;
    }

    public static void calcTime(int activeSide, int game_ply){
        if (timeAllocated != Long.MAX_VALUE)
            return;
        long ourTime = time[activeSide];
        long theirTime = time[Side.flip(activeSide)];
        if (ourTime == Long.MAX_VALUE) {
            return;
        }

        float timeRatio = ourTime/(float)theirTime;
        timeRatio = Math.min(timeRatio, 2);
        timeRatio = Math.max(timeRatio, 1);

        float phaseFactor = phaseFactor(game_ply);

        timeAllocated = (long)(ourTime*timeRatio*phaseFactor/30.0 + increment[activeSide] - overhead);
    }

    public static void setTime(long time){
        timeAllocated = time;
    }

    public static void resetTime(){
        time[Side.WHITE] = Long.MAX_VALUE;
        time[Side.BLACK] = Long.MAX_VALUE;
        increment[Side.WHITE] = 0;
        increment[Side.BLACK] = 0;
        timeAllocated = Long.MAX_VALUE;
    }

    public static long timeElapsed(){
        return System.currentTimeMillis() - startTime;
    }

    public static float phaseFactor(int ply){
        return (float)(0.8/(1 + Math.exp(-(ply - 20)/5.0)) + 0.2);
    }
}


