package com.cssweb.network;


import com.cssweb.business.WorkerThread;
import com.cssweb.business.WorkerThreadPool;
import com.cssweb.msg.CustomMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;


public class NettyServerHandler extends SimpleChannelInboundHandler<CustomMessage> {

    private static final Logger logger = LogManager.getLogger(NettyServerHandler.class.getName());

    private BlockingQueue<CustomMessage> queue;

    public NettyServerHandler(BlockingQueue<CustomMessage> queue) {
        this.queue = queue;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, CustomMessage request) throws Exception {
        logger.info("channelRead0");

        request.setChannelHandlerContext(ctx);

        WorkerThread reqTask = new WorkerThread(request);
        WorkerThreadPool.getInstance().put(reqTask);

        //queue.offer(request);

        // ctx.writeAndFlush(request);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive");
        //ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
        ctx.close();
    }
}
