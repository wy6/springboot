package com.example.test;

/**
 * @Description: Collection Test.
 * @Author: WangY
 * @Date: Created in 2018-08-02 11:46
 * @Modified By：
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 1.Collection 接口是List、Set、Queue接口的父接口，定义了如下操作集合元素的方法：
 *   1.1 boolean add(Object o)
 *           向集合里添加一个元素，如果集合对象被添加操作改变则返回true；
 *   1.2 boolean addAll(Collection c)
 *           将指定 collection 中的所有元素都添加到此 collection 中，如果集合对象被添加返回true;
 *   1.3 void clear()
 *           移除此 collection 中的所有元素,将集合长度变为0；
 *   1.4 boolean contains(Object o)
 *           如果此 collection 包含指定的元素，则返回 true。
 *   1.5 boolean containsAll(Collection<?> c)
 *           如果此 collection 包含指定 collection 中的所有元素，则返回 true。
 *   1.6 boolean equals(Object o)
 *           比较此 collection 与指定对象是否相等。
 *   1.7 int hashCode()
 *           返回此 collection 的哈希码值。
 *   1.8 boolean isEmpty()
 *           如果此 collection 不包含元素，则返回 true。
 *   1.9 Iterator<E> iterator()
 *           返回一个Iterator对象，用于遍历集合里的元素；
 *   1.10 boolean remove(Object o)
 *           从此 collection 中移除指定元素的单个实例，如果存在的话返回true;
 *   1.11 boolean removeAll(Collection c)
 *           移除此 collection 中那些也包含在指定 collection 中的所有元素,如果存在的话返回true;
 *   1.12 boolean retainAll(Collection c)
 *           仅保留此 collection 中那些也包含在指定 collection 的元素,如果存在的话返回true;
 *   1.13 int size()
 *           返回此 collection 中的元素个数；
 *   1.14 Object[] toArray()
 *           该方法把集合转换成一个数组，所有的集合元素变成对应的数组元素；
 */
public class CollectionTest {

    public static void main(String[] args) {
        Collection c = new ArrayList();
        // 添加元素
        c.add("孙悟空");
        c.add(6);
        // 输出集合元素个数
        System.out.println("c的集合个数为：" + c.size());
        // 移除指定元素
        c.remove(6);
        System.out.println("c的集合个数为：" + c.size());
        // 判断集合是否包含某个元素
        System.out.println("判断集合是否包含某个元素：" + c.contains("孙悟空"));
        c.add("Java EE");
        System.out.println("c的集合元素：" + c);
        Collection books = new HashSet();
        books.add("孙悟空");
        books.add("Java讲义");
        // c集合是否完全包含books集合
        System.out.println("c集合是否完全包含books集合" + c.containsAll(books));
        // 用c集合减去books集合里的元素
        c.removeAll(books);
        System.out.println("c的集合元素：" + c);
        // 删除c集合里的所有元素
        c.clear();
        System.out.println("c的集合元素：" + c);
        // books 集合里面只剩下c集合里也包含的元素
        books.removeAll(c);
        System.out.println("books的集合元素：" + books);

        /**
         * 控制台输出结果
         * c的集合个数为：2
         * c的集合个数为：1
         * 判断集合是否包含某个元素：true
         * c的集合元素：[孙悟空, Java EE]
         * c集合是否完全包含books集合false
         * c的集合元素：[Java EE]
         * c的集合元素：[]
         * books的集合元素：[孙悟空, Java讲义]
         */
    }

}
