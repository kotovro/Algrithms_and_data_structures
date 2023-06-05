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
    private boolean[][] adjMatrix;
    private int eCount = 0;
    private int vCount = 0;


    public boolean[][] getAdjMatrix() {
        return adjMatrix;
    }
    public AdjMatrixGraphExt reduceGraph(int[] redundantNodes) {
        AdjMatrixGraphExt res;
        List<Integer> listRedundantNodes = new LinkedList<>();
        for (int i = 0; i < redundantNodes.length; i++) {
            listRedundantNodes.add(redundantNodes[i]);
        }
        boolean[][] newMatrix = new boolean[adjMatrix.length - redundantNodes.length][adjMatrix.length - redundantNodes.length];
        int newEdges = 0;
        int newVertexes = adjMatrix.length - redundantNodes.length;
        int newI = -1;
        int newJ = -1;
        for (int i = 0; i < adjMatrix.length; i++) {
            if (!listRedundantNodes.contains(i)) {
                newI++;
                for (int j = i; j < adjMatrix.length; j++) {
                    if (!listRedundantNodes.contains(j)) {
                        newJ++;
                        newMatrix[newI][newJ] = adjMatrix[i][j];
                        if (adjMatrix[i][j]) {
                            newEdges++;
                        }
                    }
                }
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
        for (int i = 0; i < adjMatrix.length; i++) {
            if (teamList.contains(i)) {
                int newJ = 0;
                for (int j = i; j < adjMatrix.length; j++) {
                    if (teamList.contains(j)) {
                        newMatrix[teamIndex][newJ] = isAdj(i, j);
                        newJ++;
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
        while (teamIndex + newGraphIndex < adjMatrix.length) {
            if (teamList.contains(teamIndex + newGraphIndex)) {
                for (int i = 0; i < adjMatrix.length; i++) {
                    if (teamList.contains(i)) {
                        newMatrix[teamIndex + newGraphIndex][i] = this.isAdj(team[teamIndex], i);
                        newEdges++;
                    } else {
                        newMatrix[teamIndex + newGraphIndex][i] = false;
                    }
                }
                teamIndex++;
            }  else {
                int newJ = 0;
                for (int i = 0; i < adjMatrix.length; i++) {
                    if (!teamList.contains(i)) {
                        newMatrix[teamIndex + newGraphIndex][i] = newGraph.isAdj(newGraphIndex, newJ);
                        newJ++;
                    }
                }
                newGraphIndex++;
            }
        }
        return new AdjMatrixGraphExt(newMatrix, newEdges, vertexCount());
    }
}
