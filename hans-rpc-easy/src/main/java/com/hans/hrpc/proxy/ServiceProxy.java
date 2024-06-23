package com.hans.hrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.hans.hrpc.model.RpcRequest;
import com.hans.hrpc.model.RpcResponse;
import com.hans.hrpc.serializer.Serializer;
import com.hans.hrpc.serializer.JdkSerializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

/**
 * 动态代理
 */
public class ServiceProxy implements InvocationHandler{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Serializer serializer = new JdkSerializer();//final 变量变常量
        //全部都new出来合不合适
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())//创建带有一个元素的Class数组
                .args(args)//同上
                .build();
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")//返回存进来，httpResponse昙花一现，try完关闭
                    .body(bytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();//类型转换，为什么是user类型？这个要问yupi
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
