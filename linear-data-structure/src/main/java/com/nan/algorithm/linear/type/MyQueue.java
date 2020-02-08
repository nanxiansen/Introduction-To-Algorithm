package com.nan.algorithm.linear.type;

import java.util.LinkedList;
import java.util.List;

/**
 * Basic implement of queue
 *
 * @author nanzhang
 * @date 2020/2/6
 */
public class MyQueue<E> {
    private List<E> list = new LinkedList<>();

    /**
     * 入队
     *
     * @param e 入队元素
     */
    public void push(E e) {
        if (e != null) {
            list.add(0, e);
        }
    }

    /**
     * 出队
     *
     * @return 出队元素
     */
    public E poll() {
        if (list.size() == 0) {
            return null;
        }
        E result = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return result;
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        MyQueue<String> myQueue = new MyQueue<>();
        myQueue.push("1");
        myQueue.push("2");
        myQueue.push("3");

        int count = myQueue.size();
        for (int i = 0; i < count; i++) {
            System.out.println(myQueue.poll());
        }
    }
}
