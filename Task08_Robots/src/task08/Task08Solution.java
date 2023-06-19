package task08;

import graph.GraphAlgorithms;
import graph.SimpleWGraph;
import graph.WeightedGraph;

import java.util.LinkedList;
import java.util.List;

public class Task08Solution {
    public static class Path {
        public Path prevPass;
        public Position curNode;
        public Path(Path prev, Position cur) {
            prevPass = prev;
            curNode = cur;
        }
    }
    public static class Position implements Comparable{
        private int targetNode = -1;
        private int distance = 0;
        private int startNode = -1;

        public Position(int startNode, int targetNode, int distance)  {
            this.startNode = startNode;
            this.targetNode = targetNode;
            this.distance = distance;
        }

        public int getStartNode() {
            return startNode;
        }
        public int getTargetNode() {
            return targetNode;
        }
        public int getDistance() {
            return distance;
        }

        @Override
        public boolean equals(Object p) {
            if (p.getClass() != Position.class) {
                return false;
            }
            return this.targetNode == ((Position) p).targetNode &&
                    this.distance == ((Position) p).distance;
                    //&& (this.distance == 0 || this.startNode == ((Position) p).startNode);
        }
        @Override
        public int compareTo(Object p) {
            if (this.distance == 0 &&
                    ((Position) p).distance == 0 &&
                this.targetNode == (((Position) p).targetNode))  {
                return 0;
            } else return -1;
        }
    }
    public static Path[] getSolution(SimpleWGraph graph,
                                                    int start1Pos,
                                                    int start2Pos,
                                                    int start3Pos,
                                                    int speed1,
                                                    int speed2,
                                                    int speed3) {
        boolean foundPos2 = false;
        boolean foundPos3 = (start3Pos >= 0) ? false : true;
        for(Integer node: GraphAlgorithms.bfs(graph, start1Pos)) {
            if (node.equals(start2Pos)) {
                foundPos2 = true;
            }
            if (node.equals(start3Pos)) {
                foundPos3 = true;
            }
            if (foundPos2 && foundPos3) {
                break;
            }
        }
        if (!foundPos2 || !foundPos3) {
            return null;
        }
        boolean meeting = false;
        LinkedList<LinkedList<Path>> possiblePositionsR1allTime = new LinkedList<>();
        Path tmp = new Path(null, new Position(-1, start1Pos, 0));
        LinkedList<Path> tmp2 = new LinkedList<>();
        tmp2.add(tmp);
        possiblePositionsR1allTime.add(tmp2);
        LinkedList<LinkedList<Path>> possiblePositionsR2allTime = new LinkedList<>();
        tmp = new Path(null, new Position(-1, start2Pos, 0));
        tmp2.add(tmp);
        possiblePositionsR2allTime.add(tmp2);
        LinkedList<LinkedList<Path>> possiblePositionsR3allTime = null;
        if (start3Pos >= 0) {
            possiblePositionsR3allTime = new LinkedList<>();
            tmp = new Path(null, new Position(-1, start3Pos, 0));
            tmp2.add(tmp);
            possiblePositionsR3allTime.add(tmp2);
        }
        while (!meeting) {
            LinkedList<Path> curPos1 = possiblePositionsR1allTime.getLast();
            LinkedList<Path> curPos2 = possiblePositionsR2allTime.getLast();
            LinkedList<Path[]> commonNodes = new LinkedList<>();

            for (Path pos1: curPos1) {
                Position lastPos1 = pos1.curNode;
                for (Path pos2: curPos2) {
                    Position lastPos2 = pos2.curNode;
                    if (lastPos1.compareTo(lastPos2) == 0) {
                        Path[] path = new Path[3];
                        path[0] = pos1;
                        path[1] = pos2;
                        commonNodes.add(path);
                        break;
                    }
                }
            }
            if (commonNodes.size() > 0) {
                if (possiblePositionsR3allTime == null) {
                    // третьего робота нет
                    return commonNodes.getLast();
                }
                LinkedList<Path> curPos3 = possiblePositionsR3allTime.getLast();
                for (Path[] paths: commonNodes) {
                    Position lastPos1 = paths[0].curNode;
                    for (Path pos3: curPos3) {
                        Position lastPos3 = pos3.curNode;
                        if (lastPos1.compareTo(lastPos3) == 0) {
                            paths[2] = pos3;
                            return paths;
                        }
                    }
                }
            }
            doAllSteps(graph, possiblePositionsR1allTime, possiblePositionsR2allTime, possiblePositionsR3allTime,
                    speed1, speed2, speed3);
        }
        return null;
    }

    private static void doAllSteps(SimpleWGraph graph, LinkedList<LinkedList<Path>> list1,
                               LinkedList<LinkedList<Path>> list2,
                               LinkedList<LinkedList<Path>> list3,
                               int speed1, int speed2, int speed3) {
        doOneStep(graph, list1, speed1);
        doOneStep(graph, list2, speed2);
        if (list3 != null) {
            doOneStep(graph, list3, speed3);
        }
    }
    private static void doOneStep(SimpleWGraph graph, LinkedList<LinkedList<Path>> list, int speed) {
        LinkedList<Path> curMoment = list.getLast();
        LinkedList<Path> newMoment = new LinkedList<>();
        list.add(newMoment);
        for(Path path: curMoment) {
            Position curPosition = path.curNode;
            int maxMovement = speed;
            if (curPosition.distance > 0) {
                int tmpDistance = curPosition.distance - speed;
                if (tmpDistance > 0) {
                    Position newPosition = new Position(curPosition.startNode, curPosition.targetNode,
                            tmpDistance);
                    Path newPath = new Path(path, newPosition);
                    newMoment.add(newPath);
                    continue;
                } else {
                   maxMovement = tmpDistance * (-1);
                }
            }
            for (WeightedGraph.WeightedEdgeTo edge: graph.adjacenciesWithWeights(curPosition.targetNode)) {
                //для общего случая нужна рекурсия, но по условию задачи максимальная скорость равна 2
                if (edge.weight() >= maxMovement ) {
                    Position newPosition = new Position(curPosition.targetNode,
                            edge.to(),(int) Math.round(edge.weight()) - maxMovement);
                    if (isUniqueNode(newPosition, newMoment)) {
                        newMoment.add(new Path(path, newPosition));
                    }

                } else {
                    for (WeightedGraph.WeightedEdgeTo edge1: graph.adjacenciesWithWeights(edge.to())) {
                        //для общего случая нужна рекурсия, но по условию задачи максимальная скорость равна 2
                        Position newPosition1 = new Position(edge.to(),
                                edge1.to(), (int) Math.round(edge1.weight()) - 1);
                        if (isUniqueNode(newPosition1, newMoment)) {
                            newMoment.add(new Path(path, newPosition1));
                        }
                    }
                }
            }
        }
    }

    private static boolean isUniqueNode(Position newPosition, LinkedList<Path> newMoment) {
        for(Path path:newMoment) {
            if (path.curNode.equals(newPosition)) {
                return false;
            }
        }
        return true;
    }
}
