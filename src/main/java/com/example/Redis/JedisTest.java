package com.example.Redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-24 16:48
 * @Modified By：
 */

public class JedisTest {

    // 测试连通性
    @Test
    public void jedisTest() {

        Jedis jedis = new Jedis("192.168.1.104", 6379);
        
        System.out.println("JedisTest.jedisTest.20:" + jedis.ping());
    }

    // 五大数据类型
    @Test
    public void Test() {

        Jedis jedis = new Jedis("192.168.1.104", 6379);

        // String 类型
        jedis.set("name", "tom");
        System.out.println("JedisTest.Test.35:添加tom个人信息-set" + jedis.get("name"));

        jedis.mset("sex", "boy", "age", "13");
        System.out.println("JedisTest.Test.35:增加多个key-mset" + jedis.mget("name", "age"));

        jedis.incr("age");
        System.out.println("JedisTest.Test.41:增加年龄-incr" + jedis.get("age"));

        Set<String> keys = jedis.keys("*");
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            System.out.println("JedisTest.Test.36:" + key);
        }
    }

}
