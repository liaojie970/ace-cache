package com.ace.cache.test.unit;

import com.ace.cache.test.CacheTest;
import com.ace.cache.test.entity.User;
import com.ace.cache.test.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by Ace on 2017/5/21.
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = CacheTest.class) // 指定我们SpringBoot工程的Application启动类
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGetUser() {
        userService.get("test");
        userService.get("test");
    }

    @Test
    public void testGetList() {
        userService.getLlist();
        userService.getLlist();
    }

    @Test
    public void testGetSet() {
        userService.getSet();
        userService.getSet();
    }

    @Test
    public void testGetMap() {
        userService.getMap();
        userService.getMap();
    }

    @Test
    public void testSave() {
        userService.save(new User("ace", 25, "ace"));
    }

    @Test
    public void testByKeyGenerator() {
        userService.get(28);
        userService.get(28);
    }

    @Test
    public void testSerialize() {
        String s = "helloworld!";
        byte[] bt = s.getBytes();
        getArrayList(bt);//这一行会出错
    }

    public ArrayList<String> getArrayList(byte[] bt) {
        ArrayList<String> list = new ArrayList<String>();
        ObjectInputStream objIps;

        //注意这里，ObjectInputStream 对以前使用 ObjectOutputStream
        // 写入的基本数据和对象进行反序列化。问题就在这里，你是直接将byte[]数组传递过来，
        // 而这个byte数组不是使用ObjectOutputStream类写入的。所以问题解决的办法就是：用输出流得到byte[]数组。
        try {
            objIps = new ObjectInputStream(new ByteArrayInputStream(bt));
            list = (ArrayList<String>) objIps.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}



