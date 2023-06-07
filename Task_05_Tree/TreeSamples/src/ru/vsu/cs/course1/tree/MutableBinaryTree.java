package ru.vsu.cs.course1.tree;

import java.awt.*;
import java.util.function.Function;

/**
 * Реализация простейшего бинарного дерева
 */
public class MutableBinaryTree<T> implements BinaryTree<T>, Cloneable {

    public class MutableTreeNode implements BinaryTree.TreeNode<T>, Cloneable {
        public T value;
        public MutableTreeNode left;
        public MutableTreeNode right;
        private boolean isSorted = false;
        private Color color = Color.BLACK;

        public MutableTreeNode(T value, MutableTreeNode left, MutableTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public MutableTreeNode(T value) {
            this(value, null, null);
        }

        @Override
        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }
        @Override
        public Color getColor() {
            if (this.isSorted) {
             return Color.GREEN;
            }
            return this.color;
        }
        public void setColor(Color color) {
            this.color = color;
        }

        public void setSorted(boolean b) {
            this.isSorted = b;
        }
        public boolean getSorted() {
            return this.isSorted;
        }
        //cloned node does not contain links to children nodes
        public MutableTreeNode clone() {
            MutableTreeNode cloned = new MutableTreeNode(value);
            cloned.setColor(color);
            cloned.setSorted(isSorted);
            return cloned;
        }
    }

    protected MutableTreeNode root = null;
    private boolean isMaxHeap = false;
    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public MutableBinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public MutableBinaryTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public MutableBinaryTree() {
        this((Function<String, T>) null);
    }
    public MutableBinaryTree(MutableTreeNode node) {
        root = node;
    }
    public void setIsMaxHeap(boolean b) {
        this.isMaxHeap = b;
    }
    public boolean getIsMaxHeap() {
        return this.isMaxHeap;
    }
    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private static class IndexWrapper {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        // пропускаем возможные пробелы
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private MutableTreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        MutableTreeNode parentNode = new MutableTreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        MutableTreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }
    //returns a root node of the cloned subtree
    private MutableBinaryTree<T>.MutableTreeNode cloneSubtree(MutableBinaryTree<T>.MutableTreeNode curNode) {
        MutableBinaryTree<T>.MutableTreeNode cloned = curNode.clone();
        if (curNode.getLeft() != null) {
            cloned.left = cloneSubtree((MutableTreeNode) curNode.getLeft());
        }
        if (curNode.getRight() != null) {
            cloned.right = cloneSubtree((MutableTreeNode) curNode.getRight());
        }
       return cloned;
    }
    public MutableBinaryTree<T> clone() {
        MutableBinaryTree<T> treeClone =
                new MutableBinaryTree<>(cloneSubtree(root));
        treeClone.setIsMaxHeap(isMaxHeap);
        return treeClone;
    }

}
