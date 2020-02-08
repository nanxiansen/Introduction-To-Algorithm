package com.nan.algorithm.linear.type;

import java.util.*;

/**
 * @author nanzhang
 * @date 2020/2/6
 */
public class MySkippedList<E extends Object> {

    private static final int MIN_LAYER_COUNT = 2;

    private static final int BASIC_LAYER_COUNT = 4;

    private static final int MAX_LAYER_COUNT = 30;

    private int layerCapacity;

    /**
     * In head.nextNodes, the max number of index which points to a nonNull node
     */
    private int currentMaxLayerIndex = 0;

    private int size = 0;

    static class Node<T> {
        T value;
        Node<T>[] nextNodes;
        Node<T>[] previousNodes;

        Node(T value, int count) {
            this.value = value;
            this.nextNodes = new Node[count];
            this.previousNodes = new Node[count];
        }

        Node(T value, Node<T>[] nextNodes, Node<T>[] previousNodes) {
            this.value = value;
            this.nextNodes = nextNodes;
            this.previousNodes = previousNodes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?> node = (Node<?>) o;
            return Objects.equals(value, node.value) &&
                    Arrays.equals(nextNodes, node.nextNodes) &&
                    Arrays.equals(previousNodes, node.previousNodes);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    private Node<E> head;

    public MySkippedList() {
        this(BASIC_LAYER_COUNT);
    }

    public MySkippedList(int layerCapacity) {
        if (layerCapacity > MAX_LAYER_COUNT) {
            this.layerCapacity = MAX_LAYER_COUNT;
        } else if (layerCapacity < MIN_LAYER_COUNT) {
            this.layerCapacity = MIN_LAYER_COUNT;
        } else {
            this.layerCapacity = layerCapacity;
        }
        head = new Node<>(null, layerCapacity);
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        Node<E> node = head.nextNodes[0];
        int currentIndex = 0;
        while (currentIndex++ != index) {
            node = node.nextNodes[0];
        }
        return node.value;
    }

    public void add(E e) {
        if (e == null) {
            return;
        }
        Node<E> insertedNode = new Node<>(e, generateLayerCount());
        // empty list
        if (head.nextNodes[currentMaxLayerIndex] == null) {
            head.nextNodes[0] = insertedNode;
            return;
        }
        // not empty list: iterate and compare layer by layer
        Node<E> currentNode = findNearByNodeForAdd(insertedNode);
        if (currentNode == null) {
            return;
        }
        Node<E> nextNode = currentNode.nextNodes[0];

        // nextNode == null说明，currentNode已经是最后一个元素了，可以直接将e插入到最后
        if (nextNode == null) {
            insertNotEnd(currentNode, insertedNode, 0);
            return;
        }

        // 最低层
        int compare = compare(nextNode.value, e);
        if (compare > 0) {
            // 从nextNode向后遍历
            while (nextNode != null && compare(nextNode.value, e) > 0) {
                currentNode = nextNode;
                nextNode = currentNode.nextNodes[0];
            }
        }

        // nextNode == null说明，currentNode已经是最后一个元素了，可以直接将e插入到最后
        if (nextNode == null) {
            insertNotEnd(currentNode, insertedNode, 0);
            return;
        }

        if (compare(nextNode.value, e) == 0) {
            while (nextNode != null && compare(nextNode.value, e) == 0) {
                if (nextNode.value == e) {
                    return;
                }
                currentNode = nextNode;
                nextNode = currentNode.nextNodes[0];
            }
        }
        insertNotEnd(currentNode, insertedNode, 0);
    }

    /**
     * @param insertedNode 要添加的元素
     * @return 如果上层表中找到了e元素的存在，则返回null
     */
    private Node<E> findNearByNodeForAdd(Node<E> insertedNode) {
        int maxLayer = insertedNode.nextNodes.length - 1;
        E e = insertedNode.value;
        Node<E> currentNode = head;
        Node<E> nextNode;
        // 维护删除操作对currentMaxLayerIndex的影响
        optimizeLayerIndex();
        for (int currentLayer = currentMaxLayerIndex; currentLayer > 0; currentLayer--) {
            nextNode = currentNode.nextNodes[currentLayer];
            if (nextNode == null) {
                if (maxLayer >= currentLayer) {
                    currentNode.nextNodes[currentLayer] = insertedNode;
                }
                continue;
            }
            // 新增元素要插入到nextNode的后面
            while (nextNode != null && compare(nextNode.value, e) > 0) {
                currentNode = nextNode;
                nextNode = currentNode.nextNodes[currentLayer];
            }

            if (nextNode == null) {
                if (maxLayer >= currentLayer) {
                    currentNode.nextNodes[currentLayer] = insertedNode;
                }
                continue;
            }

            Node<E> tempCur = currentNode;
            while (nextNode != null && compare(nextNode.value, e) == 0) {
                if (nextNode.value == e) {
                    return null;
                }
                tempCur = nextNode;
                nextNode = tempCur.nextNodes[currentLayer];
            }
            if (nextNode == null) {
                if (maxLayer >= currentLayer) {
                    tempCur.nextNodes[currentLayer] = insertedNode;
                }
                continue;
            }

            if (maxLayer >= currentLayer) {
                // 在currentNode和nextNode之间插入
                tempCur.nextNodes[currentLayer] = insertedNode;
                insertedNode.nextNodes[currentLayer] = nextNode;
            }
        }
        return currentNode;
    }

    /**
     * @param e e
     * @return 如果上层表中找到了e元素的存在，则返回null
     */
    private Node<E> findNearByNodeForRemove(E e) {
        Node<E> currentNode = head;
        Node<E> nextNode;
        // 维护删除操作对currentMaxLayerIndex的影响
        optimizeLayerIndex();
        for (int currentLayer = currentMaxLayerIndex; currentLayer > 0; currentLayer--) {
            nextNode = currentNode.nextNodes[currentLayer];
            if (nextNode == null) {
                continue;
            }
            // 新增元素要插入到nextNode的后面
            while (nextNode != null && compare(nextNode.value, e) > 0) {
                currentNode = nextNode;
                nextNode = currentNode.nextNodes[currentLayer];
            }
            if (nextNode == null) {
                continue;
            }

            while (nextNode != null && compare(nextNode.value, e) == 0) {
                if (nextNode.value == e) {
                    return nextNode;
                }
                nextNode = nextNode.nextNodes[currentLayer];
            }
        }
        return currentNode;
    }

    private void insertNotEnd(Node<E> currentNode, Node<E> insertedNode, int layer) {
        Node<E> nextNode = currentNode.nextNodes[layer];
        if (nextNode == null) {
            // nextNode == null说明，currentNode已经是最后一个元素了，可以直接将e插入到最后
            insertedNode.previousNodes[layer] = currentNode;
            currentNode.nextNodes[layer] = insertedNode;
        } else {
            // e应该插入到nextNode和currentNode之间
            insertedNode.nextNodes[layer] = nextNode;
            insertedNode.previousNodes[layer] = currentNode;
            nextNode.previousNodes[layer] = insertedNode;
            currentNode.nextNodes[layer] = insertedNode;
        }

        if (layer == 0) {
            maintainAfterAdd(insertedNode.nextNodes.length, insertedNode);
        }
    }

    private void maintainAfterAdd(int addedLayerCount, Node<E> insertedNode) {
        // 插入后的维护工作
        if (currentMaxLayerIndex < addedLayerCount) {
            currentMaxLayerIndex = addedLayerCount;
            head.nextNodes[currentMaxLayerIndex] = insertedNode;
        }
        size++;
    }

    public E remove(E e) {
        if (e == null) {
            return null;
        }
        Node<E> firstNode = head.nextNodes[currentMaxLayerIndex];
        // empty list
        if (firstNode == null) {
            return null;
        }
        Node<E> currentNode = findNearByNodeForRemove(e);
        if (currentNode == null) {
            return null;
        }
        while (compare(currentNode.value, e) >= 0) {
            if (currentNode.value == e) {
                // 逐层删除
                for (int index = 0; index < currentNode.nextNodes.length; index++) {
                    currentNode.previousNodes[index].nextNodes[index] = currentNode.nextNodes[index];
                    currentNode.nextNodes[index].previousNodes[index] = currentNode.previousNodes[index];
                }
                // 清理工作
                size--;
                return e;
            }
            if (currentNode.nextNodes[0] == null) {
                return null;
            }
            currentNode = currentNode.nextNodes[0];
        }
        return null;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int index = currentMaxLayerIndex; index > -1; index--) {
            Node<E> node = head.nextNodes[currentMaxLayerIndex];
            if (node == null) {
                currentMaxLayerIndex--;
                continue;
            }
            builder.append(String.format("layer %d: ", currentMaxLayerIndex));
            while (node != null) {
                builder.append(node.value.toString());
                node = node.nextNodes[index];
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * 元素大小比较
     *
     * @param e1 e1
     * @param e2 e2
     * @return 元素大小比较
     */
    private int compare(E e1, E e2) {
        Comparable<? super E> c1 = (Comparable<? super E>) e1;
        return c1.compareTo(e2);
    }

    private void optimizeLayerIndex() {
        Node<E> node = head.nextNodes[currentMaxLayerIndex];
        while (currentMaxLayerIndex > 0 && node == null) {
            currentMaxLayerIndex--;
            node = head.nextNodes[currentMaxLayerIndex];
        }
    }

    /**
     * 生成随机的层数
     *
     * @return 最高层的层数
     */
    private int generateLayerCount() {
        int count = 1;
        optimizeLayerIndex();

        // 一次最多增加一层
        int maxLayerCount = currentMaxLayerIndex + 1;
        if (head.nextNodes[currentMaxLayerIndex] != null &&
                head.nextNodes[currentMaxLayerIndex].nextNodes[currentMaxLayerIndex] != null) {
            maxLayerCount++;
        }
        maxLayerCount = Math.min(layerCapacity, maxLayerCount);

        for (; count < maxLayerCount + 1; count++) {
            if (halfPossibility()) {
                break;
            }
        }

        System.out.println(count);
        return count;
    }

    /**
     * 50%概率
     *
     * @return 50%概率
     */
    private static boolean halfPossibility() {
        int bound = new Random().nextInt(2);
        return bound == 0;
    }
}
