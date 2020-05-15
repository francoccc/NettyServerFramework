package com.franco.netty.tcp;

import com.franco.common.ServerProtocol;
import com.franco.netty.core.AbstractRequest;
import com.franco.netty.message.RequestMessage;
import com.franco.spring.core.RequestUtil;
import com.franco.spring.core.Push;
import com.franco.spring.core.Request;
import com.franco.spring.servlet.ServletContext;
import com.franco.spring.core.Session;
import com.franco.spring.session.SessionManager;
import com.google.common.base.Strings;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;


/**
 * TCP请求
 *
 * @author franco
 */
public class TcpRequest extends AbstractRequest {

    private Map<String, String[]> paramMap;
    private byte[] content;  //
    private Object[] args;
    private long createTime;

    public TcpRequest(ServletContext sc, ChannelHandlerContext ctx, Channel channel, RequestMessage message) {
        super(sc, ctx, channel, message);
        // 此处访问session 创建

    }

    private volatile boolean parse = false;
    private void parseParam(byte[] bytes) {
        if(parse || bytes == null) {
            return;
        }
        String content = new String(bytes);
        if(Strings.isNullOrEmpty(content)) {
            return;
        }
        try {
            this.paramMap = new HashMap<String, String[]>();
            RequestUtil.parseParamWithoutDecode(content, paramMap);
        } catch (Exception e) {
            // Ignore
        }
        parse = true;
    }

    public Map<String, String[]> getParameterMap() {
        parseParam(content);
        return paramMap;
    }

    public String[] getParameterValues(String name) {
        parseParam(content);
        return paramMap == null ? null : paramMap.get(name);
    }

    public Object[] getRequestArgs() {
        return args;
    }

    public void setRequestArgs(Object[] args) {
        this.args = args;
    }

    public ServerProtocol getServerProtocol() {
        return ServerProtocol.TCP;
    }

    public Session getSession() {
        return SessionManager.getInstance().getSession(message.getSessionId());
    }

    public Session getSession(boolean allowCreate) {
        return null;
    }

    public Session getNewSession() {
        return null;
    }

    public void pushAndClose(Object buffer) {
        if(channel.isWritable()) {
            ChannelFuture future = channel.write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public Push newPush() {
        return new TcpPush(channel);
    }

    public long createTime() {
        return createTime;
    }
}
