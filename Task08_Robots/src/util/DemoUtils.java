package util;

import graph.SimpleWGraph;
import task08.Task08Solution;

import java.awt.geom.Point2D;
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
    public static Point2D solveQuadraticSystem(Point2D start, Point2D end, double len0, double len1, double difX, double difY) {
        double x0 = start.getX();
        double x1 = end.getX();
        double y0 = start.getY();
        double y1 = end.getY();
        double const1 = (x1 * x1 - x0 * x0 + y1 * y1 - y0 * y0 + len0 * len0 - len1 * len1) / (2 * (x1 - x0));
        double const2 = (y1 - y0) / (x1 - x0);
        double a = 1 + const2 * const2;
        double b = 2 * (const2 * x0 - const1 * const2 - y0);
        double c = const1 * const1 - 2 * x0 * const1 + x0 * x0 + y0 * y0 - len0 * len0;
        double discr = b * b - 4 * a * c;
        double resY1 = (-b + Math.sqrt(Math.abs(discr))) / (2 * a);
        double resX1 = const1 - const2 * resY1;
        return new Point2D.Double(resX1 + difX, resY1 + difY);
    }
}
