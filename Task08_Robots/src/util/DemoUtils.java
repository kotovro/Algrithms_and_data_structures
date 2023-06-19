package util;

import graph.SimpleWGraph;

import java.util.Random;

public class DemoUtils {
    public static SimpleWGraph createTestGraph(int vCount) {
        Random rnd = new Random();
        double[][] adjMatrix = new double[vCount][vCount];
        for (int i = 0; i < vCount - 1; i++) {
            for (int j = i + 1; j < vCount; j++) {
                adjMatrix[i][j] = rnd.nextInt(-20, 10);
                adjMatrix[j][i] = adjMatrix[i][j];
            }
        }
        return new SimpleWGraph(adjMatrix);
    }
}
