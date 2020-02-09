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
        BaseSorting sorting = new HeapSorting(Arrays.copyOf(array, array.length));
        System.out.println("HeapSorting: " + Arrays.toString(sorting.numbers));
    }

    @Test
    public void testQuickSorting() {
        BaseSorting sorting = new QuickSorting(Arrays.copyOf(array, array.length));
        System.out.println("QuickSorting: " + Arrays.toString(sorting.numbers));
    }
}
