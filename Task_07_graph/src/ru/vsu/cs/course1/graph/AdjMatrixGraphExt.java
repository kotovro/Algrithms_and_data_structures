package ru.vsu.cs.course1.graph;

import ru.vsu.cs.course1.graph.AdjMatrixGraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AdjMatrixGraphExt extends AdjMatrixGraph {

    public AdjMatrixGraphExt(boolean[][] adjMatrix, int edgeCount, int vertexCount) {
        this.adjMatrix = adjMatrix;
        eCount = edgeCount;
        vCount = vertexCount;
    }
    public AdjMatrixGraphExt() {
        super();
    }



    public boolean[][] getAdjMatrix() {
        return adjMatrix;
    }
    public AdjMatrixGraphExt reduceGraph(int[] redundantNodes) {
        List<Integer> listRedundantNodes = new LinkedList<>();
        for (int i = 0; i < redundantNodes.length; i++) {
            listRedundantNodes.add(redundantNodes[i]);
        }
        boolean[][] newMatrix = new boolean[adjMatrix.length - redundantNodes.length][adjMatrix.length - redundantNodes.length];
        int newEdges = 0;
        int newVertexes = adjMatrix.length - redundantNodes.length;
        int newI = 0;

        for (int i = 0; i < adjMatrix.length-1; i++) {
            if (!listRedundantNodes.contains(i)) {

                int newJ = newI + 1;
                for (int j = i+1; j < adjMatrix.length; j++) {
                    if (!listRedundantNodes.contains(j)) {

                        newMatrix[newI][newJ] = isAdj(i, j);
                        newMatrix[newJ][newI] = isAdj(i, j);
                        if (isAdj(i, j)) {
                            newEdges++;
                        }
                        newJ++;
                    }
                }
                newI++;
            }
        }
    return new AdjMatrixGraphExt(newMatrix, newEdges, newVertexes);
    }

    public AdjMatrixGraphExt makeTeamGraph(int[] team) {
        boolean[][] newMatrix = new boolean[team.length][team.length];
        int teamIndex = 0;
        int newEdge = 0;
        List<Integer> teamList = new LinkedList<>();
        for (int i = 0; i < team.length; i++) {
            teamList.add(team[i]);
        }
        for (int i = 0; i < adjMatrix.length-1; i++) {
            if (teamList.contains(i)) {
                int newJ = teamIndex + 1;
                for (int j = i+1; j < adjMatrix.length; j++) {
                    if (teamList.contains(j)) {
                        newMatrix[teamIndex][newJ] = isAdj(i, j);
                        newMatrix[newJ][teamIndex] = isAdj(i, j);
                        newJ++;
                        if (isAdj(i, j))
                            newEdge++;
                    }
                }
                teamIndex++;
            }
        }
        return new AdjMatrixGraphExt(newMatrix, newEdge, team.length);
    }
    public AdjMatrixGraphExt merge(AdjMatrixGraphExt newGraph, int[] team) {
        List<Integer> teamList = new LinkedList<>();
        for (int i = 0; i < team.length; i++) {
            teamList.add(team[i]);
        }

        boolean[][] newMatrix = new boolean[adjMatrix.length][adjMatrix.length];
        int teamIndex = 0;
        int newEdges = newGraph.edgeCount();
        int newGraphIndex = 0;
        int curIndex = 0;
        while (curIndex < adjMatrix.length - 1) {
            if (teamList.contains(curIndex)) {
                for (int j = curIndex + 1; j < adjMatrix.length; j++) {
                    if (teamList.contains(j)) {
                        newMatrix[curIndex][j] = this.isAdj(team[teamIndex], j);
                        newMatrix[j][curIndex] = this.isAdj(team[teamIndex], j);
                        if (isAdj(teamIndex, j)) {
                            newEdges++;
                        }
                    }
                }
                teamIndex++;
            }  else {
                int newJ = 0;
                for (int j = 0; j < adjMatrix.length; j++) {
                    if (!teamList.contains(j)) {
                        newMatrix[curIndex][j] = newGraph.isAdj(newGraphIndex, newJ);
                        newJ++;
                    }
                }
                newGraphIndex++;
            }
            curIndex = teamIndex + newGraphIndex;
        }
        return new AdjMatrixGraphExt(newMatrix, newEdges, vertexCount());
    }
}
