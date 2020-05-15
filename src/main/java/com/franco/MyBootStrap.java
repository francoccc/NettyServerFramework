package com.franco;

import com.franco.netty.decoder.TcpMessageDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.franco.wrapper.WrapperUtil;
import com.franco.wrapper.NettyWrapper;
import com.franco.spring.servlet.ServletConfig;
import com.franco.spring.servlet.ServletContext;
import com.franco.spring.servlet.ServletContextImpl;
import com.franco.spring.servlet.Servlet;
import com.franco.spring.session.SessionManager;
import com.franco.netty.handler.TcpServerHandler;


/**
 * 服务器引导
 *
 * @author franco
 */
public class MyBootStrap {


    /** 应用contenxt */
    private ApplicationContext applicationContext;

    /** 容器配置 */
    private ServletConfig sc;

    /** netty配置 */
    private NettyConfig nc;

    /** ServletContext */
    private ServletContext servletContext;

    /** servlet */
    private Servlet servlet;

    /** 服务器的配置路径 */
    private final static String CONF_NAME = "conf.xml";

    public void start() throws InterruptedException {
        WrapperUtil.setWrapper(new NettyWrapper());
        XmlConfig config = new XmlConfig(CONF_NAME);
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        sc = config.getServletConfig();
        nc = config.getNettyConfig();
        // servlet init
        servletContext = new ServletContextImpl(sc);
        servletContext.setAttribute("applicationContext", applicationContext);
        servletContext.setAttribute("nettyConfig", nc);
        SessionManager.getInstance().setServletConfig(sc);
        SessionManager.getInstance().startCheckThread();
        try {
            servlet = sc.getServletClass().getDeclaredConstructor(ServletContext.class,
                    ServletConfig.class).newInstance(servletContext, sc);
        }  catch (Exception e) {
            throw new RuntimeException("generate servlet fail");
        }
        servlet.init();

//        // zooKeeper
//        try {
//            CreateMaster createMaster = new CreateMaster("127.0.0.1:2181", 3000, servletContext);
//        } catch (IOException e) {
//            throw new RuntimeException("ZooKeeper init error");
//        }
        // netty init
        EventLoopGroup bossGroup = new NioEventLoopGroup((Integer) nc.getInitParameter("bossThread"));
        EventLoopGroup workerGroup = new NioEventLoopGroup((Integer) nc.getInitParameter("workerThread"));
        ServerBootstrap b = new ServerBootstrap();
        int tcpPort = (Integer) nc.getInitParameter("tcpPort");
        try {
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TcpMessageDecoder());
                            ch.pipeline().addLast(new TcpServerHandler(servlet));
                        }
                    }).childOption(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(tcpPort).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

        System.out.println("test");
    }
}
