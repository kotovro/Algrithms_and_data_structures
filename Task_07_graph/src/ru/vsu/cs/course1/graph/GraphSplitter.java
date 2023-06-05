package ru.vsu.cs.course1.graph;

import java.util.LinkedList;
import java.util.List;

public class GraphSplitter {
    public static AdjMatrixGraphExt splitter(int numberOfTeams, int maxDifference, boolean isNotDirectlyKnown,
                           AdjMatrixGraphExt graph, int curMinTeam, int curMaxTeam) {
        if (numberOfTeams == 1) {
            // too big last team
            if (graph.vertexCount() > curMinTeam * maxDifference) {
                return null;
            }

            int[] team = new int[graph.vertexCount()];
            for (int i = 0; i < graph.vertexCount(); i++) {
                team[i] = i;
            }
            if (checkTeam(graph, team, isNotDirectlyKnown)) {
                return graph;
            } else {
                return null;
            }
        }
        int minAmount = (int) Math.ceil(((double)graph.vertexCount()) / (maxDifference * (numberOfTeams - 1) + 1));

        if (minAmount < 2) {
            minAmount = 2;
        }
        minAmount = Math.max(minAmount, (int) Math.ceil(((double) curMaxTeam) / maxDifference));

        if (numberOfTeams * minAmount > graph.vertexCount()) {
            return null;
        }

        int maxAmount = Math.min(curMinTeam * maxDifference,
                (int) Math.floor(((double) maxDifference * graph.vertexCount()) / (numberOfTeams +
                    maxDifference - 1)));

        for (int num = minAmount; num <= maxAmount; num++) {
            int[] nodes = new int[num - 1];
            int code = gen_comb_norep_lex_init(nodes, graph.vertexCount()  - 1, num - 1);
            while(code > 0) {
                int[] team = reconstructNodes(nodes);
                if (checkTeam(graph, team, isNotDirectlyKnown)) {
                    AdjMatrixGraphExt newGraph = splitter(
                            numberOfTeams - 1,
                            maxDifference,
                            isNotDirectlyKnown,
                            graph.reduceGraph(team),
                            Math.min(curMinTeam,
                                team.length),
                            Math.max(curMaxTeam, team.length));
                    if (newGraph != null) {
                        return graph.merge(newGraph, team);
                    }
                }
                code = gen_comb_norep_lex_next(nodes, graph.vertexCount() - 1, num - 1);
            }
        }
        return null;
    }

    private static int[] reconstructNodes(int[] nodes) {
        int[] res = new int[nodes.length + 1];
        res[0] = 0;
        for(int i = 0; i < nodes.length; i++) {
            res[i + 1] = nodes[i] + 1;
        }
        return res;
    }
    private static boolean checkTeam(AdjMatrixGraphExt graph, int[] nodes, boolean isNotDirectlyKnown) {
        if(!isNotDirectlyKnown) {
            for (int i = 0; i < nodes.length - 1; i++) {
                for (int j = i + 1; j < nodes.length; j++) {
                    if (!graph.isAdj(nodes[i], nodes[j])) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            List<Integer> teamGraphList = new LinkedList<>();
            for (Integer node: GraphAlgorithms.bfs(graph.makeTeamGraph(nodes), 0)) {
               teamGraphList.add(node);
            }
            for (int i = 0; i < nodes.length; i++) {
                if (!teamGraphList.contains(i)) {
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
}
