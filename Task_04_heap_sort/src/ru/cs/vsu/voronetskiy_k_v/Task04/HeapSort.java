package src.ru.cs.vsu.voronetskiy_k_v.Task04;

public class HeapSort {
    public static <T extends Comparable<T>> void sort(T[] data, int from, int to) {
            int N = to - from;

            // Build heap (rearrange array)
            for (int i = N / 2 - 1 + from; i >= from; i--) {
                makeMaxHeap(data, N + from, i, from);
            }
            // One by one extract an element from heap
            for (int i = to - 1; i > from; i--) {
                // Move current root to end
                T temp = data[from];
                data[from] = data[i];
                data[i] = temp;

                // make max heap on the reduced heap
                makeMaxHeap(data, i, from, from);

            }
        }

        // To heapify a subtree rooted with node i which is
        // an index in arr[]. n is size of heap
        private static <T extends Comparable<T>> void makeMaxHeap(T[] arr, int N, int i, int from)
        {
            int largest = i; // Initialize largest as root
            int l = 2 * (i - from) + 1 + from; // left = 2 * i + 1
            int r = 2 * (i - from) + 2 + from; // right = 2*i + 2

            // If left child is larger than root
            if (l < N  && arr[l].compareTo(arr[largest]) > 0)
                largest = l;

            // If right child is larger than largest so far
            if (r < N  && arr[r].compareTo(arr[largest]) > 0)
                largest = r;

            // If largest is not root
            if (largest != i) {
                T swap = arr[i];
                arr[i] = arr[largest];
                arr[largest] = swap;

                // Recursively make max heap for the affected subtree
                makeMaxHeap(arr, N, largest, from);
            }
        }

}
