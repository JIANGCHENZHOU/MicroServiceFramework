package com.cssweb.network;

import com.cssweb.msg.CustomDecoder;
import com.cssweb.msg.CustomEncoder;
import com.cssweb.msg.CustomMessage;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;


public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LogManager.getLogger(NettyServerInitializer.class.getName());

    private BlockingQueue<CustomMessage> queue;

    public NettyServerInitializer(BlockingQueue<CustomMessage> queue) {
        this.queue = queue;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        logger.info("initChannel");

        ChannelPipeline pipeline = ch.pipeline();

        // Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast("deflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast("inflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
        pipeline.addLast("decoder", new CustomDecoder());
        pipeline.addLast("encoder", new CustomEncoder());

        // and then business logic.
        // Please note we create a handler for every new channel
        // because it has stateful properties.
        pipeline.addLast("handler", new NettyServerHandler(queue));
    }
}
