package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.util.List;


public class Solution {
    public static int findMax(List<Integer> list) {
        Integer maxEl = Integer.MIN_VALUE;
        for (Integer el: list) {
            if (el > maxEl) {
                maxEl = el;
            }
        }
        return maxEl;
    }
    public static int findMin(List<Integer> list) {
        Integer minEl = Integer.MAX_VALUE;
        for (Integer el: list) {
            if (el < minEl) {
                minEl = el;
            }
        }
        return minEl;
    }
    public static int lastIndexOf(List<Integer> list, int value) {
        for (int i = list.size() - 1; i >= 0 ; i--) {
            if (list.get(i) == value) {
                return i;
            }
        }
        return -1;
    }
    public static int firstIndexOf(List<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == value) {
                return i;
            }
        }
        return -1;
    }
    public static List<Integer> process(List<Integer> list) {
        int maxEl = findMax(list);
        int minEl = findMin(list);
        int indexOfLastMin = lastIndexOf(list, minEl);
        int indexOfFirstMax = firstIndexOf(list, maxEl);
        int changeFrom = Math.min(indexOfFirstMax, indexOfLastMin);
        int changeTo = Math.max(indexOfLastMin, indexOfFirstMax);

        if (list.size() != 1) {
            int i = 1;
            while (changeFrom + i < changeTo - i) {
                Integer tmp = list.get(changeTo - i);
                Integer tmp2 = list.get(changeFrom + i);
                list.set(changeFrom + i, tmp);
                list.set(changeTo - i, tmp2);
                i++;
            }
        }
        return list;
    }
}
