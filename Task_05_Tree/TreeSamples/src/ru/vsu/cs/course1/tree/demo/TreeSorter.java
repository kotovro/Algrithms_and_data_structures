package ru.vsu.cs.course1.tree.demo;

import ru.vsu.cs.course1.tree.BinaryTree;
import ru.vsu.cs.course1.tree.BinaryTreeAlgorithms;
import ru.vsu.cs.course1.tree.MutableBinaryTree;
import ru.vsu.cs.util.SwingUtils;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class  TreeSorter<T extends Comparable<T>>  {
    private MutableBinaryTree<T>.MutableTreeNode lastNode = null;
    private Stack<MutableBinaryTree<T>.MutableTreeNode> maxHeapQueue;
    private Stack<MutableBinaryTree<T>.MutableTreeNode> sortQueue;

    public TreeSorter() {
        maxHeapQueue = new Stack<>();
        sortQueue = new Stack<>();
    }
    private void getAnimationFrame(MutableBinaryTree<T> tree) {
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


// идея следующая: будем формировать список из деревьев, которые будем передавать в графику
//
    public Queue<MutableBinaryTree<T>> sort(MutableBinaryTree<T> tree) {
        Queue<MutableBinaryTree<T>> result = new LinkedList<>();
        maxHeapQueue.clear();
        sortQueue.clear();
        tree.setIsMaxHeap(false);

        for (BinaryTree.TreeNode<T> node : BinaryTreeAlgorithms.byLevelNodes(tree.getRoot())) {
            sortQueue.push((MutableBinaryTree<T>.MutableTreeNode) node);
        }

        while (!maxHeapQueue.isEmpty() || !sortQueue.isEmpty()) {
            getAnimationFrame(tree);
            result.add(tree.clone());
        }
        return result;
    }
    public Queue<MutableBinaryTree<T>> makeMaxHeap(MutableBinaryTree<T> tree) {
        Queue<MutableBinaryTree<T>> result = new LinkedList<>();
        maxHeapQueue.clear();
        sortQueue.clear();
        tree.setIsMaxHeap(false);

        for (BinaryTree.TreeNode<T> node : BinaryTreeAlgorithms.byLevelNodes(tree.getRoot())) {
            ((MutableBinaryTree<Integer>.MutableTreeNode) node).setSorted(false);
            maxHeapQueue.push((MutableBinaryTree<T>.MutableTreeNode) node);
        }
        while (!maxHeapQueue.isEmpty()) {
            getAnimationFrame(tree);
            result.add(tree.clone());
        }
        getAnimationFrame(tree);
        MutableBinaryTree<T> finall = tree.clone();
        finall.setIsMaxHeap(true);
        result.add(finall);

        return result;
    }
}
