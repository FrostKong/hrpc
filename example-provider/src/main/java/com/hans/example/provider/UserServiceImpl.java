package com.hans.example.provider;

import com.hans.example.common.service.UserService;
import com.hans.example.common.model.User;
public class UserServiceImpl implements UserService{
    public User getUser(User user){
        System.out.println("用户名:"+user.getName());
        return user;
    }
}
