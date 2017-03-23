package com.cssweb.msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class CustomDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LogManager.getLogger(CustomDecoder.class.getName());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        logger.info("decode");


        int readBytes = in.readableBytes();
        if (readBytes < CustomMessageHeader.MSG_HEADER_SIZE) {
            logger.info("消息头不完整，已读字节数" + readBytes);
            return;
        }

        in.markReaderIndex();

        // 读消息头
        //byte[] msgHeaderBytes = new byte[CustomMessageHeader.MSG_HEADER_SIZE];
        //in.readBytes(msgHeaderBytes);

        CustomMessageHeader msgHeader = new CustomMessageHeader();
        msgHeader.setCrc(in.readInt());
        msgHeader.setMsgBodySize(in.readInt());

        logger.info("消息内容大小" + msgHeader.getMsgBodySize());

        // 超出包最大限制
        if (msgHeader.getMsgBodySize() >= 65536) {

        }


        // 等待只到消息内容接收完成
        readBytes = in.readableBytes();
        if (readBytes < msgHeader.getMsgBodySize()) {
            in.resetReaderIndex();
            logger.info("消息内容不完整");
            return;
        }


        byte[] msgContent = new byte[msgHeader.getMsgBodySize()];
        in.readBytes(msgContent);


        // 构建消息
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMsgHeader(msgHeader);
        customMessage.setMsgBody(msgContent);
        customMessage.setChannelHandlerContext(ctx);

        out.add(customMessage);
    }
}
