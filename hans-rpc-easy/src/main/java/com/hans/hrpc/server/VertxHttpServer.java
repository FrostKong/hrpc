package com.hans.hrpc.server;

import io.vertx.core.Vertx;
import com.hans.hrpc.server.*;
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建实例
        Vertx vertx=Vertx.vertx();
        //创建http服务器
        io.vertx.core.http.HttpServer server=vertx.createHttpServer();
        //监听，处理请求
        server.requestHandler(new HttpServerHandler());//请求处理器的绑定
        server.listen(port, http -> {
            //启动http服务器并监听
            if (http.succeeded()) {
                System.out.println("HTTP server started on port "+port);
            } else {
                System.err.println("Failed to start server"+http.cause());
            }
        });
    }
}
