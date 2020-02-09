package com.nan.algorithm.linear.sorting;

import java.util.Arrays;

/**
 * 注意事项：
 * 1、如果最后一个元素是最大的，就将对除了最后一个元素的数组进行排序；
 * 2、如果最后一个元素是最大的，就交换第一个和最后一个，然后将对除了第一个元素的数组进行排序;
 * 3、每次排序，最后一个元素要特殊处理，如果不是最大和最小的情况，再去检查是否进行交换.
 *
 * @author nanzhang
 * @date 2020/2/9
 */
public class QuickSorting extends BaseSorting {

    public QuickSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortCore() {
        sortArray(0, numbers.length - 1);
    }

    private void sortArray(int begin, int end) {
        System.out.println(Arrays.toString(numbers));
        if (begin > end - 1) {
            return;
        }
        if (begin == end - 1) {
            if (numbers[begin] > numbers[end]) {
                exchange(begin, end);
            }
            return;
        }
        int firstBigIndex = -1;
        // if there is anyone bigger than numbers[end]
        boolean hasBigger = false;
        for (int index = begin; index < end; index++) {
            if (numbers[index] <= numbers[end]) {
                if (firstBigIndex < begin) {
                    continue;
                }
                exchange(firstBigIndex, index);
                firstBigIndex++;
                continue;
            }
            hasBigger = true;
            if (firstBigIndex < begin) {
                firstBigIndex = index;
            }
        }

        // if numbers[end] is the biggest one or the smallest one
        if (firstBigIndex < begin) {
            if (hasBigger) {
                // if numbers[end] is the smallest one, exchange begin and end, and then sort the elements of the array without the first one
                exchange(begin, end);
                sortArray(begin + 1, end);
            } else {
                // if numbers[end] is the biggest one, sort the elements of the array without the last one
                sortArray(begin, end - 1);
            }
            return;
        }

        if (numbers[end] < numbers[firstBigIndex]) {
            exchange(end, firstBigIndex);
        }
        sortArray(begin, firstBigIndex);
        sortArray(firstBigIndex + 1, end);
    }

    private void exchange(int index1, int index2) {
        if (index1 == index2 || index1 >= numbers.length || index2 >= numbers.length || index1 < 0 || index2 < 0) {
            return;
        }
        int temp = numbers[index1];
        numbers[index1] = numbers[index2];
        numbers[index2] = temp;
    }
}
