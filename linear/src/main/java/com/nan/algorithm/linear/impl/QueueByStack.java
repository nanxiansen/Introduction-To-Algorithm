package com.nan.algorithm.linear.impl;

import com.nan.algorithm.linear.type.MyStack;

/**
 * Using stacks to implement a queue
 *
 * @author nanzhang
 * @date 2020/2/6
 */
public class QueueByStack<E> {

    private MyStack<E> inStack = new MyStack<>();
    private MyStack<E> outStack = new MyStack<>();

    public void push(E e) {
        inStack.push(e);
    }

    public E pop() {
        if (outStack.size() == 0) {
            if (inStack.size() == 0) {
                return null;
            }
            int count = inStack.size();
            for (int index = 0; index < count; index++) {
                outStack.push(inStack.pop());
            }
        }
        return outStack.pop();
    }

    public int size() {
        return inStack.size() + outStack.size();
    }

    public static void main(String[] args) {
        QueueByStack<String > queue = new QueueByStack<>();
        queue.push("1");
        queue.push("2");
        queue.push("3");

        int count = queue.size();
        for (int index = 0; index < count - 1; index++) {
            System.out.println(queue.pop());
        }

        queue.push("4");
        queue.push("5");

        count = queue.size();
        for (int index = 0; index < count; index++) {
            System.out.println(queue.pop());
        }
    }
}
