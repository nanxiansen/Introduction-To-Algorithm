package com.nan.algorithm.linear.type;

import org.junit.Test;

/**
 * @author nanzhang
 * @date 2020/2/7
 */
public class MySkippedListTest {

    @Test
    public void test() {
        MySkippedList<String> list = new MySkippedList<>();
        for (int index = 0; index < 100; index++) {
            list.add(String.valueOf(index));
        }
        System.out.println(list);
    }
}
