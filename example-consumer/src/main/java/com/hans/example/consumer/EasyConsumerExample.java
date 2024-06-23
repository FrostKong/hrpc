package com.hans.example.consumer;

import com.hans.example.common.service.UserService;
import com.hans.example.common.model.User;
import com.hans.hrpc.proxy.ServiceProxyFactory;
public class EasyConsumerExample {//提供什么？
    public static void main(String[] args){
        //静态代理
        //UserService userService=new UserServiceProxy();
        //动态代理
        UserService userService= ServiceProxyFactory.getProxy(UserService.class);
        User user=new User();
        user.setName("hans");
        //调用
        User newUser=userService.getUser(user);//为何又弄一个newuser
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("user==null");
        }


    }
}