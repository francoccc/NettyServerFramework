package com.franco.netty.tcp;

import com.franco.common.ServerProtocol;
import com.franco.spring.core.Push;
import com.franco.spring.core.Session;
import com.franco.wrapper.WrapperUtil;
import io.netty.channel.Channel;

/**
 * TcpPush
 */
public class TcpPush implements Push {

    /** TcpChannel */
    private Channel channel;

    public TcpPush(Channel channel) {
        this.channel = channel;
    }

    public void push(Session session, String command, byte[] body) {
        if(isPushable()) {
            channel.pipeline().writeAndFlush(WrapperUtil.wrapper(0, command, body));
        }
    }

    public void push(Session session, Object buffer) {
        if(isPushable()) {
            channel.pipeline().writeAndFlush(buffer);
        } else {
            // Session save
        }
    }

    public void push(String command, byte[] body) {
        if(isPushable()) {
            channel.pipeline().writeAndFlush(WrapperUtil.wrapper(0, command, body));
        }
    }

    public boolean isPushable() {
        return this.channel != null && channel.isWritable();
    }

    public void clear() {
        if(channel != null) {
            channel.close();
        }
    }

    public void discard() {
        clear();
    }

    public void heartbeat() {
        return;
    }

    public ServerProtocol getPushProtocol() {
        return ServerProtocol.TCP;
    }
}
