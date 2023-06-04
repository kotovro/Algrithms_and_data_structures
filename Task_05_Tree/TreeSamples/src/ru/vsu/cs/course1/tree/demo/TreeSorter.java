package ru.vsu.cs.course1.tree.demo;

import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.course1.tree.MutableBinaryTree;

import java.awt.*;
import java.util.Comparator;
import java.util.Stack;

public class  TreeSorter<T extends Comparable<T>>  {
    private MutableBinaryTree<T>.MutableTreeNode lastNode = null;
    public Stack<MutableBinaryTree<T>.MutableTreeNode> maxHeapQueue;
    public Stack<MutableBinaryTree<T>.MutableTreeNode> sortQueue;

    public TreeSorter() {
        maxHeapQueue = new Stack<>();
        sortQueue = new Stack<>();
    }
    public void makeMaxHeap(MutableBinaryTree<T> tree) {
        MutableBinaryTree<T>.MutableTreeNode curNode = null;
        if (!sortQueue.isEmpty() && maxHeapQueue.isEmpty()) {
            MutableBinaryTree<T>.MutableTreeNode root =
                    ((MutableBinaryTree<T>.MutableTreeNode) tree.getRoot());
            MutableBinaryTree<T>.MutableTreeNode last = sortQueue.pop();
            if (last != root) {
                T swap = last.getValue();
                last.setValue(root.getValue());
                root.setValue(swap);

                maxHeapQueue.push(root);
            }
            last.setSorted(true);
        }
        if (lastNode != null) {
             lastNode.setColor(Color.BLACK);
        }
        if (maxHeapQueue.isEmpty()) {
//            if (isMakingMaxHeap) {
//                tree.setIsMaxHeap(true);
//                isMakingMaxHeap = false;
//            }
//            timer.stop();
//            repaintTree();
            return;
        } else {
            curNode = maxHeapQueue.pop();
            lastNode = curNode;
            curNode.setColor(Color.BLUE);
        }
        MutableBinaryTree<T>.MutableTreeNode left =
                (MutableBinaryTree<T>.MutableTreeNode) curNode.getLeft();
        MutableBinaryTree<T>.MutableTreeNode right =
                (MutableBinaryTree<T>.MutableTreeNode) curNode.getRight();
        if (right != null && !right.getSorted() || left != null && !left.getSorted()) {

            MutableBinaryTree<T>.MutableTreeNode largest = curNode;
            // If left child is larger than root
            if (left != null && !left.getSorted() && left.getValue().compareTo(largest.getValue()) > 0)
                largest = left;

            // If right child is larger than largest so far
            if (right != null && !right.getSorted() && right.getValue().compareTo(largest.getValue()) > 0)
                largest = right;

            // If largest is not root
            if (largest != curNode) {
                T swap = curNode.getValue();
                curNode.setValue(largest.getValue());
                largest.setValue(swap);

                largest.setColor(Color.GRAY);
                maxHeapQueue.push(largest);
            }
        }
    }
}
