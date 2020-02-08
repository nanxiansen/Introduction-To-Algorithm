package com.nan.algorithm.linear.sorting;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author nanzhang
 * @date 2020/2/8
 */
public class BaseSortingTest {

    private Integer[] array = {1, 10, 5, 7, 8, 2, 9, 6, 3, 3, 4, 11};

    @Test
    public void testHeapSorting() {
        BaseSorting sorting = new HeapSorting(array);
        System.out.println(Arrays.toString(sorting.numbers));
    }
}
