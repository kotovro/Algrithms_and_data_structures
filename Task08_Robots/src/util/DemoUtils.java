package util;

import graph.SimpleWGraph;
import task08.Task08Solution;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class DemoUtils {
    public static SimpleWGraph createTestGraph(int vCount, double probability, int minWeight, int maxWeight) {
        double[][] adjMatrix = new double[vCount][vCount];

        Random rnd = new Random();
        int weightsDif = maxWeight - minWeight;
        int weightsCount = weightsDif + 1;
        int maxRandom = weightsCount + 1;
        int optionsCount = (int) Math.ceil((double) weightsCount / probability);
        int minRandom = maxRandom - optionsCount;

        for (int i = 0; i < vCount - 1; i++) {
            for (int j = i + 1; j < vCount; j++) {
                int weight = rnd.nextInt(minRandom, maxRandom);
                if (weight > 0) {
                    adjMatrix[i][j] = weight + minWeight - 1;
                    adjMatrix[j][i] = adjMatrix[i][j];
                }
            }
        }
        return new SimpleWGraph(adjMatrix);
    }

    public static SimpleWGraph removeNode(SimpleWGraph graph, int removeNodeIndex) {
        double[][] resMatrix = new double[graph.vertexCount() - 1][graph.vertexCount() - 1];

        for (int i = 0; i < graph.vertexCount() - 1; i++) {
            if (i == removeNodeIndex) {
                continue;
            }

            for (int j = i + 1; j < graph.vertexCount(); j++) {
                if (j == removeNodeIndex) {
                    continue;
                }
                int newI = (i >= removeNodeIndex) ? i - 1 : i;
                int newJ = (j >= removeNodeIndex) ? j - 1 : j;
                Double weight = graph.getWeight(i, j);
                resMatrix[newI][newJ] = (weight == null) ? 0 : weight;
                resMatrix[newJ][newI] = resMatrix[newI][newJ];
            }
        }
        return new SimpleWGraph(resMatrix);
    }
    public static SimpleWGraph addNode(SimpleWGraph graph) {
        double[][] resMatrix = new double[graph.vertexCount() + 1][graph.vertexCount() + 1];

        for (int i = 0; i < graph.vertexCount() - 1; i++) {
            for (int j = i + 1; j < graph.vertexCount(); j++) {
                Double weight = graph.getWeight(i, j);
                resMatrix[i][j] = (weight == null) ? 0 : weight;
                resMatrix[j][i] = resMatrix[i][j];
            }
        }
        return new SimpleWGraph(resMatrix);
    }

    public static LinkedList<Task08Solution.Position[]> repack(Task08Solution.Path[] path) {
        LinkedList<Task08Solution.Position[]> res = new LinkedList<>();
        for(Task08Solution.Path p: path) {
            LinkedList<Task08Solution.Position> list = new LinkedList<>();
            res.add(listToArrayReversed(repack(p, list)));
        }
        return res;
    }
    public static LinkedList<Task08Solution.Position> repack(Task08Solution.Path path, LinkedList<Task08Solution.Position> result) {
        if (path != null) {
            result.add(path.curNode);
            return repack(path.prevPass, result);
        }
        return result;
    }
    public static  Task08Solution.Position[] listToArrayReversed(LinkedList<Task08Solution.Position> list) {
        Iterator<Task08Solution.Position> iterator = list.descendingIterator();
        Task08Solution.Position[] result = new Task08Solution.Position[list.size()];
        int i = 0;
        while (iterator.hasNext()) {
            result[i] = iterator.next();
            i++;
        }
        return result;
    }
}
