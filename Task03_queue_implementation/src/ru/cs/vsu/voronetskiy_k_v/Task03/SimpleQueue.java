package ru.cs.vsu.voronetskiy_k_v.Task03;

public class SimpleQueue<T> extends SimpleLinkedList<T> {
    public T peek() throws Exception {
        return this.getFirst();
    }

    public T pop() throws Exception {
        T res = this.peek();
        this.removeFirst();
        return res;
    }

    public void push(T value) {
        this.addFirst(value);
    }

    public void exchange() throws Exception {
        T elem  = this.getLast();
        this.removeLast();
        this.addFirst(elem);
    }
   public SimpleQueue(T[] array) {
       super(array);
   }
    public SimpleQueue(T[] array, boolean isReverse) {
        for (T elem : array) {
            if (isReverse) {
                this.addFirst(elem);
            } else {
                this.addLast(elem);
            }
        }
    }

    public SimpleQueue() {
    }
}
