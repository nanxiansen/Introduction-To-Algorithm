package com.nan.algorithm.linear.sorting;

/**
 * 排序：
 * 1）有序结果存放到numbers
 * 2）有序结果同步到sortedNumbers
 * 3）归并sortedNumbers中的有序结果，存放到numbers
 * 4）有序结果同步到sortedNumbers
 *
 * @author nanzhang
 * @date 2020/2/9
 */
public class MergeSorting extends BaseSorting {

    private Integer[] sortedNumbers;

    public MergeSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortCore() {
        this.sortedNumbers = new Integer[numbers.length];
        sortArray(0, numbers.length - 1);
    }

    private void sortArray(int begin, int end) {
        if (begin > end) {
            throw new RuntimeException("begin should not bigger than end");
        }
        if (begin == end) {
            sortedNumbers[begin] = numbers[begin];
            return;
        }
        if (begin == end - 1) {
            if (numbers[begin] > numbers[end]) {
                exchange(begin, end);
            }
            synToSorted(begin, end);
            return;
        }
        int middle = (begin + end) / 2;
        sortArray(begin, middle);
        sortArray(middle + 1, end);
        int index1 = begin;
        int index2 = middle + 1;
        for (int index = begin; index <= end; index++) {
            if (index1 > middle) {
                copyArray(index2, index, end - index2 + 1);
                break;
            }
            if (index2 > end) {
                copyArray(index1, index, middle - index1 + 1);
                break;
            }
            if (sortedNumbers[index1] < sortedNumbers[index2]) {
                numbers[index] = sortedNumbers[index1++];
            } else {
                numbers[index] = sortedNumbers[index2++];
            }
        }
        synToSorted(begin, end);
    }

    private void copyArray(int fromBegin, int toBegin, int length) {
        for (int index = 0; index < length; index++) {
            numbers[toBegin + index] = sortedNumbers[fromBegin + index];
        }
    }

    private void synToSorted(int begin, int end) {
        for (int index = begin; index <= end; index++) {
            sortedNumbers[index] = numbers[index];
        }
    }
}
