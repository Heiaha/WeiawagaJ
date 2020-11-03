package mind;

public class Statistics {
    public static int leafs = 0;
    public static int qleafs = 0;
    public static int betaCutoffs = 0;
    public static int qbetaCutoffs = 0;
    public static int ttHits = 0;
    public static int nodes = 0;
    public static int qnodes = 0;

    public static int totalNodes(){
        return nodes + qnodes;
    }

    public static float branchingRatio(){
        if (nodes != leafs)
            return (float)nodes / (nodes - leafs);
        return 0;
    }

    public static float qBranchingRatio(){
        if (qnodes != qleafs)
            return (float)qnodes / (qnodes - qleafs);
        return 0;
    }

    public static void reset(){
        leafs = 0;
        qleafs = 0;
        betaCutoffs = 0;
        qbetaCutoffs = 0;
        ttHits = 0;
        nodes = 0;
        qnodes = 0;
    }


}
