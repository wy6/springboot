package com.example.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @Description: Iterator Test
 * @Author: WangY
 * @Date: Created in 2018-08-02 11:43
 * @Modified By：
 */


/**
 *  用Iterator接口遍历集合元素
 *     Iterator 主要用于遍历Collection集合中的元素，Iterator对象也被称为迭代器。
 * 	Iterator 仅用于遍历集合，必须依附于Collection对象。
 * 	使用 Iterator 对集合元素进行迭代时,并不是把集合元素本身传给迭代变量，而是把集合元素的值
 * 	传给了迭代变量，所以修改迭代变量的值对集合元素本身没有任何影响。
 *
 *  提供的方法：
 *  1.boolean hasNext()如果仍有元素可以迭代，则返回 true。
 * 	2.Object next()返回迭代的下一个元素。
 * 	3.void remove()删除集合里上一次next方法返回的元素。
 */
public class IteratorTest {
    public static void main(String[] args) {
        Collection books = new HashSet();
        books.add("A书");
        books.add("B书");
        books.add("C书");
        // 获取books集合对应的迭代器
        Iterator it = books.iterator();
        while (it.hasNext()) {
            // it.next()返回的是一个对象类型，需要强制转换
            String book = (String) it.next();
            System.out.println(book);
            if (book.equals("B书")) {
                // 从集合中删除上一次next方法返回的元素
                it.remove();
            }
            // book变量赋值，不会改变集合元素本身
            book = "C书";
        }
        System.out.println(books);

        /**
         * 控制台输出结果：
         * C书
         * B书
         * A书
         * [C书, A书]
         */
    }
}
