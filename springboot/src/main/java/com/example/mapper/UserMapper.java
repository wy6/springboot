package com.example.mapper;

import com.example.entity.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    void addUser(User user);

    void delUser(String id);

    List<User> queryUserByName(String userName);
}