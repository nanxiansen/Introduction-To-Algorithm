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
     * 注意：currentMaxLayerIndex指的是最大层的索引值，相当于层数-1
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
        // empty list
        if (currentMaxLayerIndex == 0 && head.nextNodes[0] == null) {
            head.nextNodes[0] = new Node<>(e, 1);
            size++;
            return;
        }

        // 维护删除操作对currentMaxLayerIndex的影响
        optimizeLayerIndex();
        // find near by node
        Map<Integer, Node<E>> beforeNodeMap = new HashMap<>();
        Node<E> currentNode = iterateToNearBy(e, beforeNodeMap);

        // 底层，先向后遍历
        while (currentNode.nextNodes[0] != null && compare(currentNode.nextNodes[0].value, e) > 0) {
            currentNode = currentNode.nextNodes[0];
        }
        beforeNodeMap.put(0, currentNode);

        // check if already in
        if (alreadyIn(currentNode, e)) {
            return;
        }

        // insert layer by layer
        Node<E> insertedNode = new Node<>(e, generateMaxLayerIndex() + 1);
        for (int layer = 0; layer < insertedNode.nextNodes.length; layer++) {
            Node<E> beforeNode = beforeNodeMap.get(layer);
            if (beforeNode == null) {
                beforeNode = head;
            }
            insertAfter(beforeNode, insertedNode, layer);
        }

        // maintain
        doMaintainWork(insertedNode);
    }

    /**
     * 在高层(0层之上)向后迭代，找到在底层中，目标位置左边、高层中有的且离目标位置比较近的节点
     *
     * @param e 插入值
     * @return 离目标较近的节点
     */
    private Node<E> iterateToNearBy(E e, Map<Integer, Node<E>> beforeNodeMap) {
        Node<E> currentNode = head;
        for (int currentLayer = currentMaxLayerIndex; currentLayer > 0; currentLayer--) {
            if (currentNode.nextNodes[currentLayer] == null) {
                beforeNodeMap.put(currentLayer, currentNode);
                continue;
            }
            // 向后遍历
            while (currentNode.nextNodes[currentLayer] != null
                    && compare(currentNode.nextNodes[currentLayer].value, e) > 0) {
                currentNode = currentNode.nextNodes[currentLayer];
            }
            beforeNodeMap.put(currentLayer, currentNode);
        }
        return currentNode;
    }

    /**
     * 待插入节点是否已经存在于跳表之中
     *
     * @param beginNode 从beginNode的下一个节点开始判断，
     * @param e         待插入节点
     * @return 存在
     */
    private boolean alreadyIn(Node<E> beginNode, E e) {
        Node<E> nextNode = beginNode.nextNodes[0];
        if (beginNode.nextNodes[0] == null) {
            return false;
        }
        int compare = compare(beginNode.nextNodes[0].value, e);
        if (compare > 0) {
            throw new RuntimeException("beginNode.nextNodes[0] should be the first node which matches compare(node, e) == 0");
        } else if (compare < 0) {
            return false;
        }
        while (nextNode != null && compare(nextNode.value, e) == 0) {
            if (nextNode.value == e) {
                return true;
            }
            nextNode = nextNode.nextNodes[0];
        }
        return false;
    }

    /**
     * 在currentNode后面插入insertedNode，层数为layer
     *
     * @param currentNode  currentNode
     * @param insertedNode insertedNode
     * @param layer        layer
     */
    private void insertAfter(Node<E> currentNode, Node<E> insertedNode, int layer) {
        Node<E> nextNode = null;
        nextNode = currentNode.nextNodes[layer];
        if (nextNode == null) {
            // currentNode是最后一个元素了
            insertedNode.previousNodes[layer] = currentNode;
        } else {
            // e应该插入到nextNode和currentNode之间
            insertedNode.nextNodes[layer] = nextNode;
            insertedNode.previousNodes[layer] = currentNode;
            nextNode.previousNodes[layer] = insertedNode;
        }
        currentNode.nextNodes[layer] = insertedNode;
    }

    /**
     * 插入后的维护工作
     *
     * @param insertedNode 新插入的元素
     */
    private void doMaintainWork(Node<E> insertedNode) {
        int addedMaxLayerIndex = insertedNode.nextNodes.length - 1;
        // 插入后的维护工作
        if (currentMaxLayerIndex < addedMaxLayerIndex) {
            currentMaxLayerIndex = addedMaxLayerIndex;
            head.nextNodes[currentMaxLayerIndex] = insertedNode;
        }
        size++;
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

    public E remove(E e) {
        if (e == null) {
            return null;
        }
        // empty list
        if (head.nextNodes[currentMaxLayerIndex] == null) {
            return null;
        }

        // 维护以前的删除操作对currentMaxLayerIndex的影响
        optimizeLayerIndex();
        // find near by node
        Map<Integer, Node<E>> beforeNodeMap = new HashMap<>();
        Node<E> currentNode = iterateAndCheck(e, beforeNodeMap);

        if (currentNode == null) {
            return null;
        }
        while (compare(currentNode.value, e) >= 0) {
            if (currentNode.value == e) {
                // 逐层删除
                for (int index = 0; index < currentNode.nextNodes.length; index++) {
                    currentNode.previousNodes[index].nextNodes[index] = currentNode.nextNodes[index];
                    if (currentNode.nextNodes[index] != null) {
                        currentNode.nextNodes[index].previousNodes[index] = currentNode.previousNodes[index];
                    }
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

    /**
     * 在高层(0层之上)向后迭代，找到在底层中，目标位置左边、高层中有的且离目标位置比较近的节点
     *
     * @param e 插入值
     * @return 离目标较近的节点
     */
    private Node<E> iterateAndCheck(E e, Map<Integer, Node<E>> beforeNodeMap) {
        Node<E> currentNode = head;
        for (int currentLayer = currentMaxLayerIndex; currentLayer > 0; currentLayer--) {
            if (currentNode.nextNodes[currentLayer] == null) {
                beforeNodeMap.put(currentLayer, currentNode);
                continue;
            }
            // 向后遍历
            while (currentNode.nextNodes[currentLayer] != null
                    && compare(currentNode.nextNodes[currentLayer].value, e) >= 0) {
                currentNode = currentNode.nextNodes[currentLayer];
                if (currentNode.value == e) {
                    return currentNode;
                }
            }
            beforeNodeMap.put(currentLayer, currentNode);
        }
        return currentNode;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int currentLayer = currentMaxLayerIndex; currentLayer > -1; currentLayer--) {
            Node<E> node = head.nextNodes[currentLayer];
            if (node == null) {
                if (currentMaxLayerIndex > 0) {
                    currentMaxLayerIndex--;
                    continue;
                } else {
                    break;
                }
            }
            builder.append(String.format("layer %d: ", currentLayer));
            while (node != null) {
                builder.append(node.value.toString());
                builder.append(",");
                node = node.nextNodes[currentLayer];
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
     * 生成随机的最高层的层数
     *
     * @return 最高层的层数
     */
    private int generateMaxLayerIndex() {
        optimizeLayerIndex();

        // 一次最多增加一层
        int maxLayerCount = currentMaxLayerIndex + 1;
        if (head.nextNodes[currentMaxLayerIndex] != null) {
            if (head.nextNodes[currentMaxLayerIndex].nextNodes[currentMaxLayerIndex] != null) {
                maxLayerCount++;
            }
        }
        maxLayerCount = Math.min(layerCapacity, maxLayerCount);

        int count = 1;
        for (; count < maxLayerCount; count++) {
            // 高一层的概率是1/4
            if (halfPossibility() || halfPossibility()) {
                break;
            }
        }
        return count - 1;
    }

    private static boolean halfPossibility() {
        return new Random().nextInt(2) == 0;
    }
}
