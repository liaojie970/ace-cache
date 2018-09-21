package com.ace.cache.test.service.impl;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.ace.cache.parser.ICacheResultParser;
import com.ace.cache.test.cache.MyKeyGenerator;
import com.ace.cache.test.entity.User;
import com.ace.cache.test.service.UserService;
import com.ace.cache.utils.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Ace on 2017/5/21.
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    @Cache(key = "user{1}")
    public User get(String account) {
        System.out.println("从方法内读取....");
        User user = new User("Ace", 24, account);
        return user;
    }

    @Override
    @Cache(key = "user:list")
    public List<User> getLlist() {
        System.out.println("从方法内读取....");
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:set", parser = SetCacheResultParser.class)
    public Set<User> getSet() {
        System.out.println("从方法内读取....");
        Set<User> users = new HashSet<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:map", parser = UserMapCacheResultParser.class)
    public Map<String, User> getMap() {
        System.out.println("从方法内读取....");
        Map<String, User> users = new HashMap<String, User>();
        for (int i = 0; i < 20; i++) {
            User user = new User("Ace", i, "ace");
            users.put(user.getAccount() + i, user);
        }
        return users;
    }

    @Override
    @CacheClear(pre = "user")
    public void save(User user) {

    }

    @Override
    @Cache(key = "user", generator = MyKeyGenerator.class)
    public User get(int age) {
        System.out.println("从方法内读取....");
        User user = new User("Ace", age, "Ace");
        return user;
    }

    /**
     * 对map返回结果做处理
     *
     * @Created by Ace on 2017/5/22.
     */
    public static class UserMapCacheResultParser implements ICacheResultParser {
        @Override
        public Object parse(String value, Type returnType, Class<?>... origins) {
            try {
                return JacksonUtils.getInstance().readValue(value, new TypeReference<HashMap<String, User>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 对set返回结果做处理
     *
     * @Created by Ace on 2017/5/22.
     */
    public static class SetCacheResultParser implements ICacheResultParser {
        @Override
        public Object parse(String value, Type returnType, Class<?>... origins) {
            try {
                return JacksonUtils.getInstance().readValue(value, new TypeReference<HashMap<String, User>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}