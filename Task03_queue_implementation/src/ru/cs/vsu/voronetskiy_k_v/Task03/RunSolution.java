package ru.cs.vsu.voronetskiy_k_v.Task03;

import ru.cs.vsu.voronetskiy_k_v.Task03.utils.ArrayUtils;

public class RunSolution {

    public static int[] runSolution(String inFile, String outFile) {
        int[] input = ArrayUtils.readIntArrayFromFile(inFile);
        int[] res = RunSolution.runSolutionCustom(input);
        return res;
    }
    public static int[] runSolutionCustom(int[] inArray) {
        SimpleQueueC<Integer> solutionQueue = new SimpleQueueC<>();
        return runSolution(solutionQueue, inArray);
    }

    public static int[] runSolutionStd(int[] inArray) {
        SimpleQueueStd<Integer> solutionQueue = new SimpleQueueStd<>();
        return runSolution(solutionQueue, inArray);
    }
    public static int[] runSolution(ISimpleQueue<Integer> queue, int[] inArray) {
        for (int i = inArray.length - 1; i > -1; i--) {
            try {
                if (queue.count() > 0) {
                    Integer tmp = (Integer) queue.remove();
                    queue.add(tmp);
                }
                queue.add(inArray[i]);
            } catch (Exception e) {
                
            }
        }
        return ArrayUtils.toPrimitive((Integer[]) queue.toArray());
    }

    public static int[] runCheckC(int[] arr) {
        SimpleQueueC<Integer> queue = new SimpleQueueC<>(ArrayUtils.toGeneric(arr));
        return runCheck(queue);
    }

    public static int[] runCheckStd(int[] arr) {
        SimpleQueueStd<Integer> queue = new SimpleQueueStd<>(ArrayUtils.toGeneric(arr));
        return runCheck(queue);
    }
    public static int[] runCheck(ISimpleQueue<Integer> queue) {
        int[] res = new int[queue.count()];
        int i = 0;
        try {
            while (queue.count() > 0) {
                res[i] = (Integer) queue.remove();
                i++;
                if (queue.count() > 0) {
                    queue.add(queue.remove());
                }
            }
        } catch (Exception ex ) {
        }

        return res;
    }
    public static <T extends ISimpleQueue<T>> void sort(T[] data, boolean[] fixed) {

    }


}

