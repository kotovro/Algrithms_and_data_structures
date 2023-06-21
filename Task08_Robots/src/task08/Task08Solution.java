package task08;

import graph.GraphAlgorithms;
import graph.SimpleWGraph;
import graph.WeightedGraph;

import java.util.Iterator;
import java.util.LinkedList;

public class Task08Solution {
    /** this class is a list which contains
     * all possible positions at one moment for one robot
    */
    public static class AllPositionsAtOneMoment extends LinkedList<Path>{
        boolean isSame(AllPositionsAtOneMoment posCmpTo) {
            if (posCmpTo.size() != this.size()) {
                return false;
            }
            for (Path p: this) {
                if (!posCmpTo.contains(p)) {
                    return false;
                }
            }
            return true;
        }
        public boolean contains(Path elem) {
            for (Path p: this) {
                if (p.curNode.isSame(elem.curNode)) {
                    return true;
                }
            }
            return false;
        }
        public boolean contains(Position node) {
            for (Path p: this) {
                if (p.curNode.isSame(node)) {
                    return true;
                }
            }
            return false;
        }
    }
    /** class which contains position of a one robot
        and a path to it from its starting node
     */
    public static class Path {
        public Path prevPass;
        public Position curNode;
        public Path(Path prev, Position cur) {
            prevPass = prev;
            curNode = cur;
        }
    }

    /**
      position of a robot in a graph with a remaining distance to target node
     */
    public static class Position {
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

        public boolean isSame(Position p) {
            return this.targetNode == p.targetNode &&
                    this.distance == p.distance;
                    //&& (this.distance == 0 || this.startNode == ((Position) p).startNode);
        }

        public boolean isMeetingPoint(Position p) {
            return this.distance == 0 && this.isSame(p);
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
        LinkedList<AllPositionsAtOneMoment> possiblePositionsR1allTime = new LinkedList<>();
        Path tmpPath = new Path(null, new Position(-1, start1Pos, 0));
        AllPositionsAtOneMoment tmpMoment = new AllPositionsAtOneMoment();
        tmpMoment.add(tmpPath);
        possiblePositionsR1allTime.add(tmpMoment);
        LinkedList<AllPositionsAtOneMoment> possiblePositionsR2allTime = new LinkedList<>();
        tmpPath = new Path(null, new Position(-1, start2Pos, 0));
        tmpMoment = new AllPositionsAtOneMoment();
        tmpMoment.add(tmpPath);
        possiblePositionsR2allTime.add(tmpMoment);
        LinkedList<AllPositionsAtOneMoment> possiblePositionsR3allTime = null;
        if (start3Pos >= 0) {
            possiblePositionsR3allTime = new LinkedList<>();
            tmpPath = new Path(null, new Position(-1, start3Pos, 0));
            tmpMoment = new AllPositionsAtOneMoment();
            tmpMoment.add(tmpPath);
            possiblePositionsR3allTime.add(tmpMoment);
        }
        while (!meeting) {
            AllPositionsAtOneMoment curPos1 = possiblePositionsR1allTime.getLast();
            AllPositionsAtOneMoment curPos2 = possiblePositionsR2allTime.getLast();
            LinkedList<Path[]> commonNodes = new LinkedList<>();

            for (Path pos1: curPos1) {
                Position lastPos1 = pos1.curNode;
                for (Path pos2: curPos2) {
                    Position lastPos2 = pos2.curNode;
                    if (lastPos1.isMeetingPoint(lastPos2)) {
                        Path[] path = new Path[3];
                        path[0] = pos1;
                        path[1] = pos2;
                        if (possiblePositionsR3allTime == null) {
                            // третьего робота нет
                            return path;
                        }
                        commonNodes.add(path);
                        break;
                    }
                }
            }
            if (commonNodes.size() > 0) {
                LinkedList<Path> curPos3 = possiblePositionsR3allTime.getLast();
                for (Path[] paths: commonNodes) {
                    Position lastPos1 = paths[0].curNode;
                    for (Path pos3: curPos3) {
                        Position lastPos3 = pos3.curNode;
                        if (lastPos1.isMeetingPoint(lastPos3)) {
                            paths[2] = pos3;
                            return paths;
                        }
                    }
                }
            }
            if (!doAllSteps(graph, possiblePositionsR1allTime, possiblePositionsR2allTime, possiblePositionsR3allTime,
                    speed1, speed2, speed3)) {
                return null;
            }
        }
        return null;
    }

    private static boolean doAllSteps(SimpleWGraph graph,
                               LinkedList<AllPositionsAtOneMoment> list1,
                               LinkedList<AllPositionsAtOneMoment> list2,
                               LinkedList<AllPositionsAtOneMoment> list3,
                               int speed1, int speed2, int speed3) {

        AllPositionsAtOneMoment newStepR1 = doOneStep(graph, list1, speed1);
        AllPositionsAtOneMoment newStepR2 = doOneStep(graph, list2, speed2);
        AllPositionsAtOneMoment newStepR3 = null;

        Iterator<AllPositionsAtOneMoment> iterator1 = list1.iterator();
        Iterator<AllPositionsAtOneMoment> iterator2 = list2.iterator();
        Iterator<AllPositionsAtOneMoment> iterator3 = null;

        if (list3 != null) {
            newStepR3 = doOneStep(graph, list3, speed3);
            iterator3 = list3.iterator();
        }

        while (iterator1.hasNext() && iterator2.hasNext() && (iterator3 == null || iterator3.hasNext())){
            AllPositionsAtOneMoment positions1 = iterator1.next();
            AllPositionsAtOneMoment positions2 = iterator2.next();
            AllPositionsAtOneMoment positions3 = null;
            if (iterator3 !=  null) {
                positions3 = iterator3.next();
            }

            if (positions1.isSame(newStepR1) && positions2.isSame(newStepR2)
                    && (iterator3 == null || positions3.isSame(newStepR3))) {
                    return false;
            }

        }
        list1.add(newStepR1);
        list2.add(newStepR2);
        if(list3 != null) {
            list3.add(newStepR3);
        }
        return true;
    }

    private static AllPositionsAtOneMoment doOneStep(SimpleWGraph graph, LinkedList<AllPositionsAtOneMoment> list, int speed) {
        AllPositionsAtOneMoment curMoment = list.getLast();
        AllPositionsAtOneMoment newMoment = new AllPositionsAtOneMoment();
        //list.add(newMoment);
        for(Path path: curMoment) {
            Position curPosition = path.curNode;
            int maxMovement = speed;
            if (curPosition.distance > 0) {
                int tmpDistance = curPosition.distance - speed;
                if (tmpDistance >= 0) {
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
                    if (!newMoment.contains(newPosition)) {
                        newMoment.add(new Path(path, newPosition));
                    }

                } else {
                    for (WeightedGraph.WeightedEdgeTo edge1: graph.adjacenciesWithWeights(edge.to())) {
                        //для общего случая нужна рекурсия, но по условию задачи максимальная скорость равна 2
                        Position newPosition1 = new Position(edge.to(),
                                edge1.to(), (int) Math.round(edge1.weight()) - 1);
                        if (!newMoment.contains(newPosition1)) {
                            newMoment.add(new Path(path, newPosition1));
                        }
                    }
                }
            }
        }
        return newMoment;
    }
}
