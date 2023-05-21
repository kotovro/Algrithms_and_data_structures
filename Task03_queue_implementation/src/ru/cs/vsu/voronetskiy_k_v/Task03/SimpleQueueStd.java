package ru.cs.vsu.voronetskiy_k_v.Task03;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;


public class SimpleQueueStd<T>  implements ISimpleQueue<T> {

    private LinkedList<T> list;

    public SimpleQueueStd() {
        list = new LinkedList<>();
    }

    public SimpleQueueStd(T[] array) {
        list = new LinkedList<>();
        list.addAll(Arrays.asList(array));
    }

    @Override
    public void add(T value) {
        list.add((T)value);
    }

    @Override
    public T remove() throws Exception {
        return list.remove();
    }

    @Override
    public T element() throws Exception {
        return list.element();
    }

    @Override
    public int count() {
        return list.size();
    }

    @Override
    public T[] toArray() {
        Class clazz = list.element().getClass();
        int size = count();
        T[] res = (T[]) Array.newInstance(clazz, size);
        int i = size;
        while (i > 0) {
            res[i - 1] = list.get(size - i);
            i--;
        }
        return res;
    }


}
