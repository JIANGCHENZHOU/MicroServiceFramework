package com.cssweb.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chenh on 2016/12/26.
 */
public class NettyClient {
    private static final Logger logger = LogManager.getLogger(NettyClient.class.getName());

    private String ip;
    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer());

            // Make a new connection.
            ChannelFuture f = b.connect(ip, port).sync();

            // Get the handler instance to retrieve the answer.
            NettyClientHandler handler = (NettyClientHandler) f.channel().pipeline().last();
            handler.test();

            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new NettyClient("127.0.0.1", 6000).run();
    }
}
