package com.nan.algorithm.linear.type;

import java.util.LinkedList;
import java.util.List;

/**
 * Basic implement of stack
 *
 * @author nanzhang
 * @date 2020/2/6
 */
public class MyStack<E> {

    private List<E> list = new LinkedList<>();

    /**
     * 入栈
     *
     * @param e 入栈元素
     */
    public void push(E e) {
        if (e != null) {
            list.add(0, e);
        }
    }

    /**
     * 出栈
     *
     * @return 出栈元素
     */
    public E pop() {
        if (list.size() == 0) {
            return null;
        }
        E result = list.get(0);
        list.remove(0);
        return result;
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        MyStack<String> myStack = new MyStack<>();
        myStack.push("1");
        myStack.push("2");
        myStack.push("3");

        int count = myStack.size();
        for (int i = 0; i < count; i++) {
            System.out.println(myStack.pop());
        }
    }
}
