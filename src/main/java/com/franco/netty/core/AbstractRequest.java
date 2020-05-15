package com.franco.netty.core;

import com.franco.netty.message.RequestMessage;
import com.franco.spring.core.Request;
import com.franco.spring.servlet.ServletContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
* 远端请求抽象实现
* 借助Netty的Channel传输
*
* @author franco
*/
public abstract class AbstractRequest implements Request {

    /** 容器上下文 */
    protected ServletContext sc;
    protected Channel channel;
    protected ChannelHandlerContext ctx;
    protected RequestMessage message;
    /** 全局值 */
    private Object globalKeyValue;
    /** 创建时间 */
    private long createTime;

    public AbstractRequest(ServletContext sc, ChannelHandlerContext ctx, Channel channel, RequestMessage message) {
        this.sc = sc;
        this.ctx = ctx;
        this.channel = channel;
        this.message = message;
        this.globalKeyValue = message.getGlobalKeyValue();
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public String getCommand() {
        return message.getCommand();
    }

    @Override
    public byte[] getContent() {
        return message.getContent();
    }

    @Override
    public int getRequestId() {
        return message.getRequestId();
    }

    @Override
    public ServletContext getServletContext() {
        return sc;
    }

    @Override
    public Object getChannel() {
        return channel;
    }

    @Override
    public String getIp() {
        return getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isHttpLong() {
        // 这个方法似乎放在这里不恰当
        return false;
    }

    public long getCreateTime() {
        return createTime;
    }
}
