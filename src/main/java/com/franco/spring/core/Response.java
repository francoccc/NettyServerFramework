package com.franco.spring.core;

import com.franco.common.ServerProtocol;
import io.netty.channel.Channel;

import java.util.Map;

public interface Response {

    Channel getChannel();

    /**
     * 是否可以写
     * @return
     */
    boolean isWritable();

    /**
     * 写出数据
     * future模式
     * @param object
     */
    Object write(Object object);

    ServerProtocol getProtocol();

    void markClose();
}
