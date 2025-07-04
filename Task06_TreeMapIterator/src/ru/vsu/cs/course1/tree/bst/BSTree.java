package ru.vsu.cs.course1.tree.bst;

import ru.vsu.cs.course1.tree.BinaryTree;

import java.util.Iterator;
import java.util.Stack;

/**
 * Интерфейс для двоичного дерева поиска (BinarySearchTree) с реализацией по
 * умолчанию многих типичных для таких деревьев методов
 *
 * @param <T>
 */
public interface BSTree<T extends Comparable<? super T>> extends BinaryTree<T> {

    /**
     * Поиск TreeNode по значению
     *
     * @param value Значение для поиска
     * @return Узел, содержащий искомый элемент
     */
    default BinaryTree.TreeNode<T> getNode(T value) {
        return BSTreeAlgorithms.getNode(getRoot(), value);
    }

    /**
     * Поиск знаяения, равного значению (не обязательного того же самого)
     *
     * @param value Значение для поиска
     * @return Искомое значение
     */
    default T get(T value) {
        BinaryTree.TreeNode<T> valueNode = getNode(value);
        return (valueNode == null) ? null : valueNode.getValue();
    }

    /**
     * Проверка, содержится ли значение value (или равное value) в дереве
     *
     * @param value Значение для поиска
     * @return true/false
     */
    default boolean contains(T value) {
        return getNode(value) != null;
    }

    /**
     * Добавление элемента в дерево (возвращать старое значение нажно для
     * эффективной реализации словаря Map из стандартной библиотеки)
     *
     * @param value Добавляемое значение
     * @return Старое значение, равное value, если было
     */
    T put(T value);

    /**
     * Удаление значения из дерева (возвращать старое значение нажно для
     * эффективной реализации словаря Map из стандартной библиотеки)
     *
     * @param value Удаляемое значение
     * @return Старое значение, равное value, если было
     */
    T remove(T value);

    /**
     * Очистка дерева (удаление всех элементов)
     */
    void clear();

    /**
     * Размер дерева
     *
     * @return Кол-во элементов в дереве
     */
    int size();

    /**
     * Поиск минимального TreeNode
     *
     * @return Узел, содержащий минимальный элемент
     */
    default BinaryTree.TreeNode<T> getMinNode() {
        return BSTreeAlgorithms.getMinNode(getRoot());
    }

    /**
     * Поиск минимального значение
     *
     * @return Минимальное значение
     */
    default T getMin() {
        BinaryTree.TreeNode<T> minNode = getMinNode();
        return (minNode == null) ? null : minNode.getValue();
    }

    /**
     * Поиск максимального TreeNode
     *
     * @return Узел, содержащий минимальный элемент
     */
    default BinaryTree.TreeNode<T> getMaxNode() {
        return BSTreeAlgorithms.getMaxNode(getRoot());
    }

    /**
     * Поиск максимального значение
     *
     * @return Минимальное значение
     */
    default T getMax() {
        BinaryTree.TreeNode<T> minNode = getMinNode();
        return (minNode == null) ? null : minNode.getValue();
    }

    /**
     * Поиск TreeNode с наибольшим значением, меньшим или равным value
     *
     * @param value Параметр
     * @return Узел, содержащий искомый элемент
     */
    default BinaryTree.TreeNode<T> getFloorNode(T value) {
        return BSTreeAlgorithms.getFloorNode(getRoot(), value);
    }

    /**
     * Поиск наибольшего значения, меньшего или равного value
     *
     * @param value Параметр
     * @return Искомое значение
     */
    default T getFloor(T value) {
        BinaryTree.TreeNode<T> floorNode = getFloorNode(value);
        return (floorNode == null) ? null : floorNode.getValue();
    }

    /**
     * Поиск TreeNode с наименьшим значением, большим или равным value
     *
     * @param value Параметр
     * @return Узел, содержащий искомый элемент
     */
    default BinaryTree.TreeNode<T> getCeilingNode(T value) {
        return BSTreeAlgorithms.getCeilingNode(getRoot(), value);
    }

    /**
     * Поиск наименьшего значения, меньше или равного value
     *
     * @param value Параметр
     * @return Искомое значение
     */
    default T getCeiling(T value) {
        BinaryTree.TreeNode<T> ceilingNode = getCeilingNode(value);
        return (ceilingNode == null) ? null : ceilingNode.getValue();
    }
    default Iterable<T> iterateFromTo(T valueFrom, T valueTo) {
        if (valueTo.compareTo(valueFrom) < 0) {
            //either reduce the amount of checks in the first node, because we do need to compare elements with
            //From element, compare only with next
            throw new IllegalArgumentException("The end value has to be greater than initial one!");
        }
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private Stack<TreeNode<T>> stack = new Stack<>();
                    private TreeNode<T> next = findNextNode(getRoot());
                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public T next() {
                        TreeNode<T> tmp = findNextNode(next.getRight());
                        if (tmp != null) {
                            stack.push(tmp);
                        }
                        TreeNode<T> result = next;
                        if (stack.isEmpty()) {
                            next = null;
                        } else {
                            next = stack.pop();
                        }
                        return result.getValue();
                    }
                    private TreeNode<T> findNextNode(TreeNode<T> node) {
                        if (node == null) {
                            return null;
                        }
                        int cmp = (next == null) ? valueFrom.compareTo(node.getValue()) : -1;
                        if (cmp == 0) {
                            return node;
                        } else if (cmp > 0) {
                            return findNextNode(node.getRight());
                        } else {
                            BinaryTree.TreeNode<T> res = findNextNode(node.getLeft());
                            if (res != null) {
                                if (node.getValue().compareTo(valueTo) <= 0) {
                                    stack.push(node);
                                }
                                return res;
                            } else {
                                if (node.getValue().compareTo(valueTo) <= 0) {
                                    return node;
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                };
            }
        };
    }
}
