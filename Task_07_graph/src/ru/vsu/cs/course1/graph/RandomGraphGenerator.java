package ru.vsu.cs.course1.graph;

import java.util.Random;

public class RandomGraphGenerator {
    public static AdjMatrixGraphExt generateGraph(int vCount, double probability) {
        boolean[][] adjMatrix = new boolean[vCount][vCount];
        Random rand = new Random();
        int edgeCount = 0;
        for (int i = 0; i < vCount - 1; i++) {
            for (int j = i + 1; j < vCount; j++) {
                if (rand.nextDouble() <= probability) {
                    adjMatrix[i][j] = true;
                    adjMatrix[j][i] = true;
                    edgeCount++;
                }
            }
        }
        return new AdjMatrixGraphExt(adjMatrix, edgeCount, vCount);
    }
}
