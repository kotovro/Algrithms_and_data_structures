package ru.cs.vsu.voronetskiy_k_v.Task03;

import javax.swing.table.TableRowSorter;

public interface ISimpleQueue<T> {
    void add(T value);

    T remove() throws Exception;

    T element() throws Exception;

    int count();

    default boolean empty() {
        return count() == 0;
    }
    public T[] toArray();
}

