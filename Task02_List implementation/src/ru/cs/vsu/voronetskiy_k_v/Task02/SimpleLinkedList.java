package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

public class SimpleLinkedList<T> implements Iterable<T> {

    public static class SimpleLinkedListException extends Exception {
        public SimpleLinkedListException(String message) {
            super(message);
        }
    }

    public class SimpleLinkedListNode<T> {
        public T value;
        public SimpleLinkedListNode<T> next;

        public SimpleLinkedListNode(T value, SimpleLinkedListNode<T> next) {
            this.value = value;
            this.next = next;
        }

        public SimpleLinkedListNode(T value) {
            this(value, null);
        }
    }

    private SimpleLinkedListNode<T> head = null;
    private SimpleLinkedListNode<T> tail = null;
    private int count = 0;


    public void addFirst(T value) {
        head = new SimpleLinkedListNode<>(value, head);
        if (count == 0) {
            tail = head;
        }
        count++;
    }

    public void addLast(T value) {
        SimpleLinkedListNode<T> temp = new SimpleLinkedListNode<>(value);
        if (count == 0) {
            head = tail = temp;
        } else {
            tail.next = temp;
            tail = temp;
        }
        count++;
    }

    private void checkEmpty() throws SimpleLinkedListException {
        if (count == 0) {
            throw new SimpleLinkedListException("Empty list");
        }
    }

    private SimpleLinkedListNode<T> getNode(int index) {
        int i = 0;
        for (SimpleLinkedListNode<T> curr = head; curr != null; curr = curr.next, i++) {
            if (i == index) {
                return curr;
            }
        }
        return null;
    }

    public T removeFirst() throws SimpleLinkedListException {
        checkEmpty();

        T value = head.value;
        head = head.next;
        if (count == 1) {
            tail = null;
        }
        count--;
        return value;
    }

    public T removeLast() throws SimpleLinkedListException {
//        checkEmpty();
//
//        T value = tail.value;
//        if (count == 1) {
//            head = tail = null;
//        } else {
//            SimpleLinkedListNode<T> prev = getNode(count - 2);
//            prev.next = null;
//            tail = prev;
//        }
//        count--;
//        return value;
        return remove(count - 1);
    }

    public T remove(int index) throws SimpleLinkedListException {
        checkEmpty();
        if (index < 0 || index >= count) {
            throw new SimpleLinkedListException("Incorrect index");
        }

        T value;
        if (index == 0) {
            value = head.value;
            head = head.next;
        } else {
            SimpleLinkedListNode<T> prev = getNode(index - 1);
            SimpleLinkedListNode<T> curr = prev.next;
            value = curr.value;
            prev.next = curr.next;
            if (index == count - 1) {
                tail = prev;
            }
        }
        count--;
        return value;
    }

    public void insert(int index, T value) throws SimpleLinkedListException {
        if (index < 0 || index > count) {
            throw new SimpleLinkedListException("Incorrect index");
        }
        if (index == 0) {
            addFirst(value);
        } else {
            SimpleLinkedListNode<T> prev = getNode(index - 1);
            prev.next = new SimpleLinkedListNode<>(value, prev.next);
            if (index == count) {
                tail = prev.next;
            }
        }
        count++;
    }

    public int size() {
        return count;
    }

    public T getFirst() throws SimpleLinkedListException {
        checkEmpty();

        return head.value;
    }

    public T getLast() throws SimpleLinkedListException {
        checkEmpty();

        return tail.value;
    }

    public T get(int index) throws SimpleLinkedListException {
        if (index < 0 || index >= count) {
            throw new SimpleLinkedListException("Incorrect index");
        }
        return getNode(index).value;
    }

    public SimpleLinkedList(T[] array) {
        for (int i = 0; i < array.length; i++) {
            this.addLast(array[i]);
        }
    }

    public T[] listToArray() {
        T value = this.head.value;
        T[] res  = (T[]) Array.newInstance(value.getClass(), this.size());
        int i = 0;
        for (T node: this) {
            res[i] = node;
            i++;
        }
        return res;
    }

    public int[] listToIntArray(SimpleLinkedList<Integer> list) {
        int[] res = new int[list.size()];
        int i = 0;
        for (Integer node: list) {
            res[i] = node;
            i++;
        }
        return res;
    }

//    public SimpleLinkedListNode<T> getSolution(Function<T, Integer> comparator) {
//        SimpleLinkedListNode<T> curItem = this.head;
//        int maxNegativeCount = 0;
//        int curNegativeCount = 0;
//        SimpleLinkedListNode<T> candidate = null;
//        SimpleLinkedListNode<T> res = null;
//        while (curItem.next != null) {
//            int comparisonRes = comparator.apply(curItem.value);
//            if (comparisonRes >= 0) {
//                if (candidate != null && curNegativeCount >= maxNegativeCount) {
//                    res = candidate;
//                    maxNegativeCount = curNegativeCount;
//                }
//                candidate = comparisonRes > 0 ? curItem : null;
//                curNegativeCount = 0;
//            } else if (candidate != null) {
//                curNegativeCount += 1;
//            }
//            curItem = curItem.next;
//        }
//        if (candidate != null && curNegativeCount >= maxNegativeCount) {
//            res = candidate;
//        }
//        return res;
//    }

    public SimpleLinkedList<Double> getSolution() {
        T value = this.head.value;
        Double[] resArray = (Double[]) Array.newInstance(value.getClass(), this.size());
        int i = 0;
        for (T el: this)
        {
            if (i > 0)
                resArray[i] = (resArray[i - 1] + (Double)el);
            else
                resArray[i] = (Double) el;
            i += 1;
        }
        return new SimpleLinkedList<>(resArray);
    }

    @Override
    public Iterator<T> iterator() {
        class SimpleLinkedListIterator implements Iterator<T> {
            SimpleLinkedListNode<T> curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T value = curr.value;
                curr = curr.next;
                return value;
            }
        }

        return new SimpleLinkedListIterator();
    }

}
