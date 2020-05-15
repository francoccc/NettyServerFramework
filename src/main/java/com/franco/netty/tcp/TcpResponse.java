package com.franco.netty.tcp;

import com.franco.common.ServerProtocol;
import com.franco.spring.core.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.Map;

public class TcpResponse implements Response {

    private Channel channel;

    private boolean close = false;

    public TcpResponse(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isWritable() {
        return channel.isWritable();
    }

    public Object write(Object object) {
        ChannelFuture future = null;
        if(channel.isWritable()) {
            future = channel.pipeline().writeAndFlush(object);
        }
        if(close) {
            if (null == future) {
                channel.close();
            } else {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
        return null;
    }

    public ServerProtocol getProtocol() {
        return ServerProtocol.TCP;
    }

    public void markClose() {
        close = true;
    }
}
