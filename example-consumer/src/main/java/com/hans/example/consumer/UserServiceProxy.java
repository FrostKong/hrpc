package com.hans.example.consumer;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.hans.example.common.service.UserService;
import com.hans.example.common.model.User;
import com.hans.hrpc.model.RpcRequest;
import com.hans.hrpc.model.RpcResponse;
import com.hans.hrpc.serializer.Serializer;
import com.hans.hrpc.serializer.JdkSerializer;

import java.io.IOException;

/**
 * 静态代理
 */
public class UserServiceProxy implements UserService {
    public User getUser(User user) {
        final Serializer serializer = new JdkSerializer();//final 变量变常量
        //全部都new出来合不合适
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})//创建带有一个元素的Class数组
                .args(new Object[]{user})//同上
                .build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")//返回存进来，httpResponse昙花一现，try完关闭
                    .body(bytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();//类型转换，为什么是user类型？这个要问yupi
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
