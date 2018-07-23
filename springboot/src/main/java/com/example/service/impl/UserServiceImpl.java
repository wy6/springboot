package com.example.service.impl;

import com.example.entity.user.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    // 配置增、删、改事务类型，查用 @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTwoUser(User user) {
        userMapper.addUser(user);

        int a = 1 / 0;

        user.setAge(user.getAge() + 1);
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

    @Override
    public List<User> queryAllUserByPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return userMapper.queryAllUserByPage(page, pageSize);
    }

}
