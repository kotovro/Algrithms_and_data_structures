package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RunSolution {
    public static SimpleLinkedList<Double> runSolution(double[] inArray, String outFile) {
        SimpleLinkedList<Double> temp = new SimpleLinkedList<>(ArrayUtils.toGeneric(inArray));
        SimpleLinkedList<Double> res = temp.getSolution();
        return res;
    }
    public static SimpleLinkedList<Double> runSolution(String inFile, String outFile) {
        double[] input = ArrayUtils.readDoubleArrayFromFile(inFile);
        SimpleLinkedList<Double> temp = new SimpleLinkedList<>(ArrayUtils.toGeneric(input));
        SimpleLinkedList<Double> res = temp.getSolution();
        try {
            ArrayUtils.writeArrayToFile(outFile, ArrayUtils.toDoubleArray(res.toString()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}

