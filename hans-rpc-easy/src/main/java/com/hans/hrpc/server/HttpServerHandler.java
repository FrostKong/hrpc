package com.hans.hrpc.server;

import com.hans.hrpc.model.RpcRequest;
import com.hans.hrpc.model.RpcResponse;
import com.hans.hrpc.serializer.Serializer;//奇怪，接口和实现都要import吗？
import com.hans.hrpc.serializer.JdkSerializer;
import com.hans.hrpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {


    @Override
    public void handle(HttpServerRequest req) {
        //指定序列化器
        final Serializer serializer = new JdkSerializer();//final 变量变常量

        //记录日志
        System.out.println("Received request:" + req.method() + " " + req.uri());

        //异步服务器请求处理
        req.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);//这里注意？rpcrequest的class
            } catch (Exception e) {
                e.printStackTrace();
            }
            //构造response对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(req, rpcResponse, serializer);
                return;
            }

            try {
                //获取调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());//拿implclass里面特定的方法
                //Object result=method.invoke(implClass.newInstance(),rpcRequest.getArgs());newInstance()已经弃用
                //使用constructor创建实例
                Constructor<?> constructor = implClass.getDeclaredConstructor();
                Object implInstance = constructor.newInstance();

                Object result = method.invoke(implInstance, rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());//这是给return的type

            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(req, rpcResponse, serializer);

        });

    }

    /**
     * 响应
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try{
            byte[] bytes = serializer.serialize(rpcResponse);//Unhandled exception: java.io.IOException 必须要抛错误
            httpServerResponse.end(Buffer.buffer(bytes));//开始发

        }catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());//一个空的结尾
        }
    }
}
