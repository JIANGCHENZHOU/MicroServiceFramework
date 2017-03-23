package com.cssweb.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//MessageToMessageEncoder<CustomMessage>
public class CustomEncoder extends MessageToByteEncoder<CustomMessage> {
    private static final Logger logger = LogManager.getLogger(CustomEncoder.class.getName());

    @Override
    protected void encode(ChannelHandlerContext ctx, CustomMessage response, ByteBuf out) throws Exception {
        logger.info("encode");


        // 发送消息头
        out.writeInt(response.getMsgHeader().getCrc());
        out.writeInt(response.getMsgHeader().getMsgBodySize());

        // 发送消息内容
        out.writeBytes(response.getMsgBody());
    }
}
