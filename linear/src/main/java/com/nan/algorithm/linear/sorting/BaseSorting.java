package com.nan.algorithm.linear.sorting;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public abstract class BaseSorting {

    private static final int MAX_LENGTH_NON_SORT = 2;

    protected Integer[] numbers;

    public BaseSorting(Integer[] numbers) {
        this.numbers = numbers;
        if (needSort()) {
            sortCore();
        }
    }

    /**
     * 用着构造器中的，用来排序的方法。这是不同排序算法的核心
     */
    protected abstract void sortCore();

    public Integer[] sort() {
        return this.numbers;
    }

    protected void exchange(int index1, int index2) {
        if (index1 == index2 || index1 >= numbers.length || index2 >= numbers.length || index1 < 0 || index2 < 0) {
            return;
        }
        int temp = numbers[index1];
        numbers[index1] = numbers[index2];
        numbers[index2] = temp;
    }

    protected boolean needSort() {
        if (numbers.length <= 1) {
            return false;
        }
        if (numbers.length == MAX_LENGTH_NON_SORT) {
            if (numbers[0] > numbers[1]) {
                exchange(0, 1);
                return false;
            }
        }
        return true;
    }
}
