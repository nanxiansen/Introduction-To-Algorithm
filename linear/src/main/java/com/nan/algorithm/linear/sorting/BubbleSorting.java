package com.nan.algorithm.linear.sorting;

/**
 * @author nanzhang
 * @date 2020/2/9
 */
public class BubbleSorting extends BaseSorting {

    public BubbleSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortCore() {
        for (int index1 = 0; index1 < numbers.length - 1; index1++) {
            for (int index2 = numbers.length - 1; index2 > index1; index2--) {
                if (numbers[index2] < numbers[index2 - 1]) {
                    exchange(index2, index2 - 1);
                }
            }
        }
    }
}
