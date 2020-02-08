package com.nan.algorithm.linear.sorting;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public class BaseSortingTest {

    private Integer[] array = {1, 100, 5, 88, 89, 2, 99, 46, 33, 31, 102};

    @Test
    public void testHeapSorting() {
        BaseSorting sorting = new HeapSorting(array);
        System.out.println(Arrays.toString(sorting.numbers));
    }
}
