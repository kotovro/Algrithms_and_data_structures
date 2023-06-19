package graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphSplitter {
    public static boolean splitter(int desiredNumberOfSubgraphs, int maxDifference, boolean isNotDirectlyKnown,
                           AdjMatrixGraphExt graph, int curMinTeam, int curMaxTeam, boolean isInit) {
        if (isInit) {
            graph.initSubgraphMatrix();
        }
        if (desiredNumberOfSubgraphs == 1) {
            // too big last team (possible, it's a redundant check)
            if (curMinTeam != 10000 && graph.getFreeVertexesCount() > curMinTeam * maxDifference) {
                return false;
            }

            int[] notInTeams = graph.getFreeVertexes();

            if (isSubgraphValid(graph, notInTeams, 0, isNotDirectlyKnown)) {
                assignSubgraphNumber(graph, notInTeams, graph.getSubgraphsCount() + 1);
                return true;
            } else {
                return false;
            }
        }
        // because of the (N - x) / (M - 1) <= Kx, where x is minimum possible subgraph
        int minAmount = (int) Math.ceil(((double)graph.getFreeVertexesCount()) / (maxDifference * (desiredNumberOfSubgraphs - 1) + 1));

        if (minAmount < 2) {
            minAmount = 2;
        }

        minAmount = Math.max(minAmount, (int) Math.ceil(((double) curMaxTeam) / maxDifference));

        if (desiredNumberOfSubgraphs * minAmount > graph.getFreeVertexesCount()) {
            return false;
        }

        int maxAmount = Math.min(curMinTeam * maxDifference,
                //(N - x) / (M - 1) = x/K, where x is the largest team possible
                (int) Math.floor(((double) maxDifference * graph.getFreeVertexesCount()) / (desiredNumberOfSubgraphs +
                    maxDifference - 1)));

        int[] allFreeVertexes = graph.getFreeVertexes();

        // check if current vertex, which is now a root, has any free adjacent vertexes
        if (!graph.adjacenciesInSubgraph(0, 0).iterator().hasNext()) {
            return false;
        }

        for (int num = minAmount; num <= maxAmount; num++) {
            if (!checkTeamSize(graph.getFreeVertexesCount(), num, desiredNumberOfSubgraphs - 1, maxAmount)) {
                continue;
            }
            int[] estimatedSubgraphAddIndexes = new int[num - 1];
            int code = gen_comb_norep_lex_init(estimatedSubgraphAddIndexes, allFreeVertexes.length  - 1, num - 1);

            while (code > 0) {
                int[] estimatedSubgraph = getEstimatedSubgraph(allFreeVertexes, estimatedSubgraphAddIndexes, num);
                int subgraphNumber = graph.getSubgraphsCount() + 1;
                assignSubgraphNumber(graph, estimatedSubgraph, subgraphNumber);

                if (isSubgraphValid(graph, estimatedSubgraph, subgraphNumber, isNotDirectlyKnown)) {

                    if (splitter(
                            desiredNumberOfSubgraphs - 1,
                            maxDifference,
                            isNotDirectlyKnown,
                            graph,
                            Math.min(curMinTeam,
                                estimatedSubgraph.length),
                            Math.max(curMaxTeam, estimatedSubgraph.length),
                            false)) {

                        return true;
                    }
                }
                assignSubgraphNumber(graph, estimatedSubgraph, 0);
                code = gen_comb_norep_lex_next(estimatedSubgraphAddIndexes, allFreeVertexes.length - 1, num - 1);
            }
        }
        return false;
    }
    private static void assignSubgraphNumber(AdjMatrixGraphExt graph, int[] subgraph, int number) {
        for (int i = 0; i < subgraph.length; i++) {
            graph.setVertexSubgraph(subgraph[i], number);
        }
    }
    /**
    *  Will try to construct estimated subgraph
    * only with the first free vertex because
    * all others will be the same.
    * So, it will be a first free vertex + estimatedSubgraphAdd
     */
    private static int[] getEstimatedSubgraph(int[] freeVertexes, int[] add, int length) {
        int[] res = new int[length];
        res[0] = freeVertexes[0];
        for (int i = 0; i < add.length; i++) {
            res[i + 1] = freeVertexes[add[i] + 1];
        }

        return res;
    }
    private static int[] reconstructNodes(int[] nodes) {
        int[] res = new int[nodes.length + 1];
        res[0] = 0;
        for(int i = 0; i < nodes.length; i++) {
            res[i + 1] = nodes[i] + 1;
        }
        return res;
    }
    private static boolean checkTeamSize(int freeCount, int teamSize, int teamsCount, int maxSize) {
        double avgRestTeamsMembers = ((double)freeCount - teamSize) / teamsCount;
        return (avgRestTeamsMembers >= 2) && (avgRestTeamsMembers <= maxSize);
    }
    private static boolean isSubgraphValid(AdjMatrixGraphExt graph, int[] subgraph, int subgraphNumber,  boolean isNotDirectlyKnown) {
        if (!isNotDirectlyKnown) {
            for (int i = 0; i < subgraph.length - 1; i++) {
                for (int j = i + 1; j < subgraph.length; j++) {
                    if (!graph.isAdj(subgraph[i], subgraph[j])) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            List<Integer> teamGraphList = new LinkedList<>();
            for (Integer node: bfsInSubgraph(graph, subgraph, subgraphNumber, 0)) {
               teamGraphList.add(node);
            }
            for (int i = 0; i < subgraph.length; i++) {
                if (!teamGraphList.contains(subgraph[i])) {
                    return false;
                }
            }
            return true;
        }

    }

    private static int gen_comb_norep_lex_init(int[] vector, int n, int k)
    {
        int j; //index

    //test for special cases
        if(k > n)
            return -1;

        if(k == 0)
            return 0;

    //initialize: vector[0, ..., k - 1] are 0, ..., k - 1
        for(j = 0; j < k; j++)
            vector[j] = j;

        return 1;
    }

    private static int gen_comb_norep_lex_next(int[] vector, int n, int k)
    {
        int j; //index

    //easy case, increase rightmost element
        if(vector[k - 1] < n - 1)
        {
            vector[k - 1]++;
            return 1;
        }

    //find rightmost element to increase
        for(j = k - 2; j >= 0; j--)
            if(vector[j] < n - k + j)
                break;

    //terminate if vector[0] == n - k
        if(j < 0)
            return 0;

    //increase
        vector[j]++;

    //set right-hand elements
        while(j < k - 1)
        {
            vector[j + 1] = vector[j] + 1;
            j++;
        }

        return 1;
    }
    public static Iterable<Integer> bfsInSubgraph(AdjMatrixGraphExt graph, int[] subgraph, int subgraphNumber, int from) {
        return new Iterable<Integer>() {
            private Queue<Integer> queue = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                queue = new LinkedList<>();
                queue.add(from);
                visited = new boolean[graph.getSubgraphVertexCount(subgraphNumber)];
                visited[from] = true;

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return ! queue.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = queue.remove();
                        for (Integer adj : graph.adjacenciesInSubgraph(result, subgraphNumber)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                queue.add(adj);
                            }
                        }
                        return subgraph[result];
                    }
                };
            }
        };
    }
}