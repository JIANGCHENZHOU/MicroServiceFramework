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
            logger.info("��Ϣͷ���������Ѷ��ֽ���" + readBytes);
            return;
        }

        in.markReaderIndex();

        // ����Ϣͷ
        //byte[] msgHeaderBytes = new byte[CustomMessageHeader.MSG_HEADER_SIZE];
        //in.readBytes(msgHeaderBytes);

        CustomMessageHeader msgHeader = new CustomMessageHeader();
        msgHeader.setCrc(in.readInt());
        msgHeader.setMsgBodySize(in.readInt());

        logger.info("��Ϣ���ݴ�С" + msgHeader.getMsgBodySize());

        // �������������
        if (msgHeader.getMsgBodySize() >= 65536) {

        }


        // �ȴ�ֻ����Ϣ���ݽ������
        readBytes = in.readableBytes();
        if (readBytes < msgHeader.getMsgBodySize()) {
            in.resetReaderIndex();
            logger.info("��Ϣ���ݲ�����");
            return;
        }


        byte[] msgContent = new byte[msgHeader.getMsgBodySize()];
        in.readBytes(msgContent);


        // ������Ϣ
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMsgHeader(msgHeader);
        customMessage.setMsgBody(msgContent);
        customMessage.setChannelHandlerContext(ctx);

        out.add(customMessage);
    }
}
