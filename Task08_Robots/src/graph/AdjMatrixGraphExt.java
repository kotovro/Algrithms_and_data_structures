package graph;

import java.util.Arrays;
import java.util.Iterator;

/**
    Graph, that can be separated into sub-graphs
*/
public class AdjMatrixGraphExt extends AdjMatrixGraph {
    private int[] subgraphMatrix;
    public AdjMatrixGraphExt(boolean[][] adjMatrix, int edgeCount, int vertexCount) {
        this.adjMatrix = adjMatrix;
        eCount = edgeCount;
        vCount = vertexCount;
    }
    public AdjMatrixGraphExt() {
        super();
    }
    public Iterable<Integer> adjacenciesInSubgraph(int v, int subgraphNumber) {
        return new Iterable<Integer>() {
            Integer nextAdj = null;

            @Override
            public Iterator<Integer> iterator() {
                int[] subgraph = getSubgraphVertexes(subgraphNumber);
                for (int i = 0; i < subgraph.length; i++) {
                    if (adjMatrix[subgraph[v]][subgraph[i]]) {
                        nextAdj = i;
                        break;
                    }
                }

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return nextAdj != null;
                    }

                    @Override
                    public Integer next() {
                        Integer result = nextAdj;
                        nextAdj = null;
                        int[] subgraph = getSubgraphVertexes(subgraphNumber);

                        for (int i = result + 1; i < subgraph.length; i++) {
                            if (adjMatrix[subgraph[v]][subgraph[i]]) {
                                nextAdj = i;
                                break;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }


    /**
     * Returns vertexes count in subgraph with certain number
     * @return  vertexes count
     */
    public int getSubgraphVertexCount(int subgraphNumber) {
        if (subgraphMatrix == null || subgraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        return (int)Arrays.stream(subgraphMatrix).filter(el -> el == subgraphNumber).count();
    }

    /**
     * Free vertex is one that doesn't belong to any of sub-graphs
     * @return  free vertexes count
     */
    public int getFreeVertexesCount() {
        return getSubgraphVertexCount(0);
    }
    /**
     * Returns all vertexes of a certain subgraph
     */
    public int[] getSubgraphVertexes(int subgraphNumber) {
        if (subgraphMatrix == null || subgraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        int vertexCount = getSubgraphVertexCount(subgraphNumber);
        int[] subgraph = new int[vertexCount];
        int j = 0;
        for (int i = 0; i < vCount; i++) {
            if (subgraphMatrix[i] == subgraphNumber) {
                subgraph[j] = i;
                j++;
            }
        }
        return subgraph;
    }
    /**
     * Free vertex is one that doesn't belong to any of sub-graphs
     * @return  all free vertexes of graph
     */
    public int[] getFreeVertexes() {
        return getSubgraphVertexes(0);
    }
    public void  initSubgraphMatrix() {
        subgraphMatrix = new int[vCount];
        for (int i = 0; i < vCount; i++) {
            subgraphMatrix[i] = 0;
        }
    }
    public int getSubgraphsCount() {
        if (subgraphMatrix == null || subgraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        return Arrays.stream(subgraphMatrix).reduce((x, y) -> x > y ? x : y).getAsInt();
    }
    public void setVertexSubgraph(int vertex, int subgraph){
        if (subgraphMatrix == null || subgraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        subgraphMatrix[vertex] = subgraph;
    }
    public int getVertexSubgraph(int vertex){
        if (subgraphMatrix == null || subgraphMatrix.length == 0) {
            initSubgraphMatrix();
        }
        return subgraphMatrix[vertex];
    }
    final String lineBreak = System.getProperty("line.separator");
    public String toStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(vCount).append(lineBreak);
        sb.append(eCount).append(lineBreak);
        for (int i = 0; i < vCount - 1; i++) {
            for (int j = i + 1; j < vCount; j++) {
                if(adjMatrix[i][j]) {
                    sb.append(String.format("%d %d", i, j)).append(lineBreak);
                }
            }
        }
        return sb.toString();
    }

    /**
     * All of this stuff to the rest of the class used for graphviz dot notation
     *
     */
    private String getVertexColor(int vertex) {
        int index = getVertexSubgraph(vertex);
        if (index > colors.length) {
            index %= colors.length;
        }
        return colors[index];
    }
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(lineBreak);
        for (int v1 = 0; v1 < this.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                String style1 = (getVertexSubgraph(v1) != 0) ? "style=filled" : "";
                String style2 = (getVertexSubgraph(v2) != 0) ? "style=filled" : "";
                sb.append(String.format("  { %d [color=%s %s] } %s { %d [color=%s %s] }",
                        v1, getVertexColor(v1), style1, (isDigraph ? "->" : "--"), v2, getVertexColor(v2), style2))
                        .append(lineBreak);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(lineBreak);
            }
        }
        sb.append("}").append(lineBreak);

        return sb.toString();
    }
    private static String[] colors = new String[] {
            "black",
            "aquamarine",
            "bisque",
            //"blue",
            //"blueviolet",
            "brown",
            "burlywood",
            "cadetblue",
            "chartreuse",
            //"chocolate",
            "coral",
            "crimson",
            "cyan",
            //"darkblue",
            //"darkcyan",
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
