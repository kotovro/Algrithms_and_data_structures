package ru.vsu.cs.course1.graph.demo;

import ru.vsu.cs.course1.graph.AdjMatrixGraph;
import ru.vsu.cs.course1.graph.Graph;

public class GraphSplitter {
    public static Graph splitter(int numberOfTeams, int maxDifference, boolean isDirectlyKnown,
                           AdjMatrixGraph graph) {
        int minAmount = (int) Math.ceil(((double)graph.vertexCount()) / (maxDifference * (numberOfTeams - 1) + 1));
//        int avgAmount = (int) Math.ceil(((double)graph.vertexCount()) / numberOfTeams);
        int maxAmount = (int) Math.floor(((double) maxDifference * graph.vertexCount()) / (numberOfTeams +
                maxDifference - 1));
        if (minAmount < 2) {
            minAmount = 2;
        }
        for (int num = minAmount; num <= maxAmount; num++) {
            int[] nodes = new int[num - 1];
            int code = gen_comb_norep_lex_init(nodes, graph.vertexCount()  - 1, num - 1);
            while(code > 0) {
                checkTeam(graph, nodes, isDirectlyKnown) {

                }
            }
            for (int i = 1; i <= graph.vertexCount(); i++) {
                for () {}
            }
            Graph result = GraphSplitter.splitter(numberOfTeams - 1, maxDifference, );
            GraphSplitter

        }
    }

    private static boolean checkTeam(boolean[][] matrix, int[] nodes, boolean isDirectlyKnown) {
        for (int i = 0; i < ; i++) {
            for ()
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
