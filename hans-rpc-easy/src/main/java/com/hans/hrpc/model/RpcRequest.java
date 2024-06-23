package com.hans.hrpc.model;

import lombok.AllArgsConstructor;//这几个是干什么用的？
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ObjectOutputStream;
import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable{
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] args;//这个object又是哪里来的？
}
