package com.example.service.impl;

import com.example.entity.user.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018/7/21 14:54
 * @Modified By：
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public void delUser(String id) {
        userMapper.delUser(id);
    }

    @Override
    public List<User> queryUserName(String userName) {
        return userMapper.queryUserByName(userName);
    }
}
