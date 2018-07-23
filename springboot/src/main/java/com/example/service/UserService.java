package com.example.service;

import com.example.entity.user.User;

import java.util.List;

public interface UserService {

    public void addUser(User user);

    public void delUser(String id);

    public List<User> queryUserName(String userName);

}
