package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RunSolution {
    public static int runSolution(int[] inArray, String outFile) {
        SimpleLinkedList<Integer> temp = new SimpleLinkedList<>(ArrayUtils.toGeneric(inArray));
        SimpleLinkedList<Integer>.SimpleLinkedListNode<Integer> res = temp.getSolution(val ->
                Integer.compare(val, 0)
         );
        return res.value.intValue();
    }
    public static int runSolution(String inFile, String outFile) {
        int[] input = ArrayUtils.readIntArrayFromFile(inFile);
        SimpleLinkedList<Integer> temp = new SimpleLinkedList<>(ArrayUtils.toGeneric(input));
        SimpleLinkedList<Integer>.SimpleLinkedListNode<Integer> res = temp.getSolution(val ->
                Integer.compare(val, 0)
        );
        return res.value.intValue();
    }
}

