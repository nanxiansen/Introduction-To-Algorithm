package com.nan.algorithm.linear.sorting;

import java.util.Arrays;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public class HeapSorting extends BaseSorting {

    public HeapSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortArray() {
        buildMaxHeap(numbers, numbers.length);
        for (int index = numbers.length - 1; index > 0; index--) {
            exchange(numbers, 0, index);
            maintainMaxHeap(numbers, 0, index);
        }
    }

    private void buildMaxHeap(Integer[] array, int length) {
        int beginIndex = (length >> 1) - 1;
        for (int index = beginIndex; index >= 0; index--) {
            maintainMaxHeap(array, index, length);
        }
    }

    private void maintainMaxHeap(Integer[] array, int root, int length) {
        int leftIndex = getLeftIndex(root);
        int rightIndex = getRightIndex(root);
        int largestIndex = root;
        if (leftIndex < length && array[root] < array[leftIndex]) {
            largestIndex = leftIndex;
        }
        if (rightIndex < length && array[largestIndex] < array[rightIndex]) {
            largestIndex = rightIndex;
        }

        if (largestIndex != root) {
            exchange(array, root, largestIndex);
            maintainMaxHeap(array, largestIndex, length);
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
