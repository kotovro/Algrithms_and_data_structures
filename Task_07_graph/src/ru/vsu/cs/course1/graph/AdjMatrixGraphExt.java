package ru.vsu.cs.course1.graph;

import java.util.LinkedList;
import java.util.List;

public class AdjMatrixGraphExt extends AdjMatrixGraph {

    private int[] subGraphMatrix;
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
    private void  initSubgraphMatrix() {
        subGraphMatrix = new int[vCount];
        for (int i = 0; i < vCount; i++) {
            subGraphMatrix[i] = 0;
        }
    }
    public void setNodeTeam(int node, int team){
        if (subGraphMatrix == null || subGraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        subGraphMatrix[node] = team;
    }
    public int getNodeTeam(int node){
        if (subGraphMatrix == null || subGraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        return subGraphMatrix[node];
    }
    private String getNodeColor(int node) {
        int index = getNodeTeam(node);
        if (index > colors.length) {
            index %= colors.length;
        }
        return colors[index];
    }
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < this.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                sb.append(String.format("  { %d [color=%s] } %s { %d [color=%s] }", v1, getNodeColor(v1), (isDigraph ? "->" : "--"), v2, getNodeColor(v2))).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
    private static String[] colors = new String[] {
            "aquamarine",
            "bisque",
            "blue",
            "blueviolet",
            "brown",
            "burlywood",
            "cadetblue",
            "chartreuse",
            "chocolate",
            "coral",
            "crimson",
            "cyan",
            "darkblue",
            "darkcyan",
            "darkgoldenrod",
            "darkgray",
            "darkgreen",
            "darkgrey",
            "darkkhaki",
            "darkmagenta",
            "darkolivegreen",
            "darkorange",
            "darkorchid",
            "darkred",
            "darksalmon",
            "darkseagreen",
            "darkslateblue",
            "darkslategray",
            "darkslategrey",
            "darkturquoise",
            "darkviolet",
            "deeppink",
            "deepskyblue",
            "dimgray",
            "dimgrey",
            "dodgerblue",
            "firebrick",
            "floralwhite",
            "forestgreen",
            "fuchsia",
            "gainsboro",
            "ghostwhite",
            "gold",
            "goldenrod",
            "gray",
            "grey",
            "green",
            "greenyellow",
            "honeydew",
            "hotpink",
            "indianred",
            "indigo",
            "ivory",
            "khaki",
            "lavender",
            "lavenderblush",
            "lawngreen",
            "lemonchiffon",
            "lightblue",
            "lightcoral",
            "lightcyan",
            "lightgoldenrodyellow",
            "lightgray",
            "lightgreen",
            "lightgrey",
            "lightpink",
            "lightsalmon",
            "lightseagreen",
            "lightskyblue",
            "lightslategray",
            "lightslategrey",
            "lightsteelblue",
            "lightyellow",
            "lime",
            "limegreen",
            "linen",
            "magenta",
            "maroon",
            "mediumaquamarine",
            "mediumblue",
            "mediumorchid",
            "mediumpurple",
            "mediumseagreen",
            "mediumslateblue",
            "mediumspringgreen",
            "mediumturquoise",
            "mediumvioletred",
            "midnightblue",
            "mintcream",
            "mistyrose",
            "moccasin",
            "navajowhite",
            "navy",
            "oldlace",
            "olive",
            "olivedrab",
            "orange",
            "orangered",
            "orchid",
            "palegoldenrod",
            "palegreen",
            "paleturquoise",
            "palevioletred",
            "papayawhip",
            "peachpuff",
            "peru",
            "pink",
            "plum",
            "powderblue",
            "purple",
            "red",
            "rosybrown",
            "royalblue",
            "saddlebrown",
            "salmon",
            "sandybrown",
            "seagreen",
            "seashell",
            "sienna",
            "silver",
            "skyblue",
            "slateblue",
            "slategray",
            "slategrey",
            "snow",
            "springgreen",
            "steelblue",
            "tan",
            "teal",
            "thistle",
            "tomato",
            "turquoise",
            "violet",
            "wheat",
            "white",
            "whitesmoke",
            "yellow",
            "yellowgreen"
    };
}
