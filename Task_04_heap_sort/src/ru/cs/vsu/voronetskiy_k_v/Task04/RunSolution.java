package src.ru.cs.vsu.voronetskiy_k_v.Task04;

import src.ru.cs.vsu.voronetskiy_k_v.Task04.utils.ArrayUtils;

public class RunSolution {


    public static int[] runSolution(String inFile, String outFile, int from, int to) {
        int[] input = ArrayUtils.readIntArrayFromFile(inFile);
        Integer[] arr = ArrayUtils.toGeneric(input);
        HeapSort.sort(arr, from, to);
        try {
            ArrayUtils.writeArrayToFile(outFile, input);
        } catch (Exception e) {
        }
        return ArrayUtils.toPrimitive(arr);
    }

    public static int[] runSolution(int[] inArray, int from, int to) {
        Integer[] arr = ArrayUtils.toGeneric(inArray);
        HeapSort.sort(arr, from, to);
        return ArrayUtils.toPrimitive(arr);
    }


}

