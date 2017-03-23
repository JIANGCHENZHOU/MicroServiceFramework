package com.cssweb.client;

import com.cssweb.msg.CustomDecoder;
import com.cssweb.msg.CustomEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chenh on 2016/12/26.
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LogManager.getLogger(NettyClientInitializer.class.getName());

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("initChannel");

        ChannelPipeline pipeline = ch.pipeline();

        // Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast("deflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast("inflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
        pipeline.addLast("decoder", new CustomDecoder());
        pipeline.addLast("encoder", new CustomEncoder());


        pipeline.addLast("handler", new NettyClientHandler());
    }
}
