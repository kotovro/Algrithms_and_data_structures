package ru.cs.vsu.voronetskiy_k_v.Task03;

import java.lang.reflect.Array;

public class SimpleQueueC<T> implements ISimpleQueue {

    private int count;
    private SimpleNode head = null;
    private SimpleNode tail = null;

    public int count() {
        return count;
    }

    @Override
    public T[] toArray() {
        Class clazz = head.value.getClass();
        T[] res = (T[])Array.newInstance(clazz, count);
        int i = count;
        SimpleNode<T> tmp = head;
        while (i > 0) {
            res[i - 1] = tmp.value;
            tmp = tmp.next;
            i--;
        }
        return res;
    }


    private class SimpleNode<T> {
        private T value;
        private SimpleNode<T> next;



        private SimpleNode(T value, SimpleNode<T> next) {
            this.value = value;
            this.next = next;
        }
        public SimpleNode(T value) {
            this(value, null);
        }
    }

    @Override
    public void add(Object value) {
        SimpleNode temp  = new SimpleNode(value, null);
        if (count != 0) {
            tail.next = temp;
        }
        tail = temp;
        if (count == 0) {
            head = tail;
        }
        count++;
    }

    @Override
    public Object remove() {
        Object res = head.value;
        head = head.next;
        count--;
        return res;
    }

    @Override
    public Object element() {
        return head.value;
    }

   public SimpleQueueC(T[] array) {
        for (int i = 0; i < array.length; i++) {
           this.add(array[i]);
       }
   }
    public SimpleQueueC() {
    }
}
