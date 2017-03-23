package com.cssweb.network;

import com.cssweb.business.Business;
import com.cssweb.business.WorkerThreadPool;
import com.cssweb.config.Config;


import com.cssweb.test.HelloService;
import com.cssweb.test.HelloServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NettyServer {
    private static final Logger logger = LogManager.getLogger(NettyServer.class.getName());

    private final int port;
    private Business business = new Business();


    public static Map<String, Object> services = new ConcurrentHashMap<>();



    public NettyServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup acceptorThreadPool = new NioEventLoopGroup();
        EventLoopGroup IOThreadPool = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(acceptorThreadPool, IOThreadPool)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 8192)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyServerInitializer(business.getQueue()));

            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            acceptorThreadPool.shutdownGracefully();
            IOThreadPool.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        Config.getInstance().readConfigXML();
        String server = Config.getInstance().getServer();
        String[] tmp = server.split(":");
        int port = Integer.parseInt(tmp[1]);

        WorkerThreadPool.getInstance().init(10);

        try {
            HelloService helloService = new HelloServiceImpl();


            String interfaceName = helloService.getClass().getAnnotation(RpcService.class).value().getName();
            System.out.println("className = " + helloService.getClass());
            System.out.println("interfaceName = " + interfaceName);
            services.put(interfaceName, helloService);


            new NettyServer(port).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
