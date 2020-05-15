package com.franco.netty.handler;

import com.franco.common.NettyConstants;
import com.franco.netty.message.RequestMessage;
import com.franco.netty.tcp.TcpRequest;
import com.franco.netty.tcp.TcpResponse;
import com.franco.spring.servlet.Servlet;
import com.franco.spring.core.Session;
import com.franco.spring.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * TcpServerHandler
 *
 * @author franco
 */
public class TcpServerHandler extends SimpleChannelInboundHandler {

    private Servlet servlet;

    public TcpServerHandler() {

    }

    public TcpServerHandler(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestMessage m = (RequestMessage) msg;
//        ActionMapUtil.invoke(m.getHeader().getCammand(), ctx, m);
        String sessionId = ctx.channel().attr(NettyConstants.SESSON_ID).get();
        SessionManager.getInstance().access(sessionId);
        m.setSessionId(sessionId);
        TcpRequest request = new TcpRequest(servlet.getServletContext(), ctx, ctx.channel(), m);
        TcpResponse response = new TcpResponse(ctx.channel());
        servlet.service(request, response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String content="我收到连接";
//        Header header=new Header((byte)0, (byte)1, (byte)1, (byte)1, (byte)0, "713f17ca614361fb257dc6741332caf2",content.getBytes("UTF-8").length, "test@hello");
//        Message message=new Message(header,content);
//        ctx.writeAndFlush(message);
        String sessionId = ctx.channel().attr(NettyConstants.SESSON_ID).get();
        Session session = SessionManager.getInstance().getSession(sessionId);
        if (session == null || !session.isValid()) {
            session = SessionManager.getInstance().getSession(sessionId, true);
        }
        ctx.channel().attr(NettyConstants.SESSON_ID).set(session.getId());
        super.channelActive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
