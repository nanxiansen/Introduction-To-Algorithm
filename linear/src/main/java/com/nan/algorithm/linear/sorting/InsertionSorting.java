package com.nan.algorithm.linear.sorting;

/**
 * @author nanzhang
 * @date 2020/2/9
 */
public class InsertionSorting extends BaseSorting {

    public InsertionSorting(Integer[] numbers) {
        super(numbers);
    }

    @Override
    protected void sortCore() {
        for (int index1 = 1; index1 < numbers.length; index1++) {
            if (numbers[index1] >= numbers[index1 - 1]) {
                continue;
            }
            int value = numbers[index1];
            int index2 = index1 - 1;
            while (index2 >= 0 && numbers[index2] > value) {
                numbers[index2 + 1] = numbers[index2];
                index2--;
            }
            numbers[index2 + 1] = value;
        }
    }
}
