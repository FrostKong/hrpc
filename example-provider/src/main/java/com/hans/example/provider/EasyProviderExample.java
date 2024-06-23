package com.hans.example.provider;

import com.hans.example.common.service.UserService;
import com.hans.hrpc.registry.LocalRegistry;
import com.hans.hrpc.server.HttpServer;
import com.hans.hrpc.server.VertxHttpServer;
public class EasyProviderExample {//提供什么？
    public static void main(String[] args){
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //提供服务
        HttpServer httpServer=new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
