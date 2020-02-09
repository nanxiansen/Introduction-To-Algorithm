package com.nan.algorithm.linear.sorting;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public class HeapSorting extends BaseSorting {

    public HeapSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortCore() {
        buildMaxHeap(numbers.length);
        for (int index = numbers.length - 1; index > 0; index--) {
            exchange(numbers, 0, index);
            maintainMaxHeap(0, index);
        }
    }

    private void buildMaxHeap(int length) {
        int beginIndex = (length >> 1) - 1;
        for (int index = beginIndex; index >= 0; index--) {
            maintainMaxHeap(index, length);
        }
    }

    private void maintainMaxHeap(int root, int length) {
        int leftIndex = getLeftIndex(root);
        int rightIndex = getRightIndex(root);
        int largestIndex = root;
        if (leftIndex < length && numbers[root] < numbers[leftIndex]) {
            largestIndex = leftIndex;
        }
        if (rightIndex < length && numbers[largestIndex] < numbers[rightIndex]) {
            largestIndex = rightIndex;
        }

        if (largestIndex != root) {
            exchange(numbers, root, largestIndex);
            maintainMaxHeap(largestIndex, length);
        }
    }

    private int getLeftIndex(int root) {
        return 1 + (root << 1);
    }

    private int getRightIndex(int root) {
        return 2 + (root << 1);
    }

    private void exchange(Integer[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}
