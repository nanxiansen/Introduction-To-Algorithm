package com.nan.algorithm.linear.type;

import org.junit.Test;

/**
 * @author nanzhang
 * @date 2020/2/7
 */
public class MySkippedListTest {

    @Test
    public void test() {
        MySkippedList<Integer> list = new MySkippedList<>();
        for (int index = 0; index < 100; index++) {
            list.add(index);
        }
        System.out.println(list);

        for (int i = 80; i < 99; i++) {
            list.add(i);
        }
        System.out.println(list);

        for (int i = 20; i < 30; i++) {
            list.remove(i);
        }
        System.out.println(list);
    }
}
