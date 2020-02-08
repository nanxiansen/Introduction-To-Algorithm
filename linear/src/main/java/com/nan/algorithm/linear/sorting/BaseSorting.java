package com.nan.algorithm.linear.sorting;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public abstract class BaseSorting {

    protected Integer[] numbers;

    public BaseSorting(Integer[] numbers) {
        this.numbers = numbers;
        sortArray();
    }

    /**
     * 用着构造器中的，用来排序的方法。这是不同排序算法的核心
     */
    protected abstract void sortArray();

    public Integer[] sort() {
        return this.numbers;
    }
}
