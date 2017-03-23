package com.cssweb.msg;

import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by chenhf on 14-1-4.
 */
public class CustomMessage {
    private static final Logger logger = LogManager.getLogger(CustomMessage.class.getName());

    private CustomMessageHeader msgHeader = null;
    private byte[] msgBody = null;
    private ChannelHandlerContext channelHandlerContext = null;



    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }
    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public CustomMessageHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(CustomMessageHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }
}
