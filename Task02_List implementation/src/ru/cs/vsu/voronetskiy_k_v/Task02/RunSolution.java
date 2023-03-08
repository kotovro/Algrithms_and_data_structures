package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RunSolution {
    public static int[] runSolution(int[] inArray, String outFile) {
        int[] res = ArrayUtils.toPrimitive(
                Solution.process(
                            Arrays.stream(inArray).boxed().collect(Collectors.toCollection(LinkedList::new)))
                        .toArray(new Integer[0]));
        if (outFile != null && outFile.length() != 0) {
            try {
                ArrayUtils.writeArrayToFile(outFile, res);
            } catch (Exception e) {
                return null;
            }
        }
        return res;
    }

    public static int[] runSolution(String inFile, String outFile) {
        int[] input = ArrayUtils.readIntArrayFromFile(inFile);
        int[] res = ArrayUtils.toPrimitive(
                Solution.process(
                                Arrays.stream(input).boxed().collect(Collectors.toCollection(LinkedList::new)))
                        .toArray(new Integer[0]));

        if (outFile != null && outFile.length() != 0) {
            try {
                ArrayUtils.writeArrayToFile(outFile, res);
            } catch (Exception e) {
                return null;
            }
        }
        return res;
    }
}
