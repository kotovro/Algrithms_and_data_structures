package ru.cs.vsu.voronetskiy_k_v.Task03;

import ru.cs.vsu.voronetskiy_k_v.Task03.utils.ArrayUtils;

public class RunSolution {
    public static SimpleQueue<Integer> runSolution(int[] inArray) {
        SimpleQueue<Integer> temp = new SimpleQueue<>(ArrayUtils.toGeneric(inArray), true);
        SimpleQueue<Integer> res = new SimpleQueue<>();
        while (temp.size() > 0) {
            try {
                Integer el = temp.pop();
                res.push(el);
                if (temp.size() > 0) {
                    res.exchange();
                }
            } catch (Exception e) {

            }
        }
        return res;
    }

    public static SimpleQueue<Integer> runSolution(String inFile, String outFile) {
        int[] input = ArrayUtils.readIntArrayFromFile(inFile);
        SimpleQueue<Integer> res = RunSolution.runSolution(input);

//        if (outFile != null && outFile.length() != 0) {
//            try {
//                ArrayUtils.writeArrayToFile(outFile, res);
//            } catch (Exception e) {
//                return null;
//            }
//        }
        return res;
    }
}
