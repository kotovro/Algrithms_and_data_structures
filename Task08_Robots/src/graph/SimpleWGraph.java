package graph;

import java.util.Iterator;

public class SimpleWGraph extends AdjMatrixGraph implements WeightedGraph {



    public static class GraphEdge implements WeightedEdgeTo{
        private int vFrom;
        private int vTo;
        private double weight;
        public int to() {
            return vTo;
        }

        @Override
        public double weight() {
            return weight;
        }

        public GraphEdge(int v1, int v2, double weight) {
            vFrom = v1;
            vTo = v2;
            this.weight = weight;
        }

        public int from() {
            return vFrom;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

    }
    double[][] adjMatrix;

    public SimpleWGraph(double[][] arr) {
        this.adjMatrix = arr;
        this.vCount = arr.length;
        this.eCount = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (adjMatrix[i][j] > 0) {
                    eCount++;
                }
            }
        }
    }
    @Override
    public void addAdge(int v1, int v2, double weight) {
        if (adjMatrix[v1][v2] <= 0) {
            eCount++;
        }
        adjMatrix[v1][v2] = weight;
        adjMatrix[v2][v1] = weight;
    }


    @Override
    public Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v) {
        return new Iterable<WeightedEdgeTo>() {
            WeightedEdgeTo nextAdj = null;
            @Override
            public Iterator<WeightedEdgeTo> iterator() {
                for (int i = 0; i < adjMatrix.length; i++) {
                    if (adjMatrix[v][i] > 0) {
                        nextAdj = new GraphEdge(v, i, adjMatrix[v][i]);
                        break;
                    }
                }

                return new Iterator<WeightedEdgeTo>() {
                    @Override
                    public boolean hasNext() {
                        return nextAdj != null;
                    }

                    @Override
                    public WeightedEdgeTo next() {
                        WeightedEdgeTo result = nextAdj;
                        nextAdj = null;

                        for (int i = result.to() + 1; i < adjMatrix.length; i++) {
                            if (adjMatrix[v][i] > 0) {
                                nextAdj = new GraphEdge(v, i, adjMatrix[v][i]);
                                break;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }
    @Override
    public Double getWeight(int v1, int v2) {
        return (adjMatrix[v1][v2] > 0) ? adjMatrix[v1][v2] : null;
    }

    public void setWeight(GraphEdge edge) {
        adjMatrix[edge.from()][edge.to()] = edge.weight();
        adjMatrix[edge.to()][edge.from()] = edge.weight();
    }
    @Override
    public Iterable<Integer> adjacencies(int v) {
        return new Iterable<>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<>() {
                    Iterator<WeightedEdgeTo> iterator = adjacenciesWithWeights(v).iterator();
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Integer next() {
                        return iterator.next().to();
                    }
                };
            }
        };
    }
    public double[][] getAdjMatrix() {
        return this.adjMatrix;
    }


}
