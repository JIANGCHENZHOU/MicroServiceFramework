package com.cssweb.msg;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chenhf on 14-1-4.
 */
public class CustomMessageHeader {
    private static final Logger logger = LogManager.getLogger(CustomMessageHeader.class.getName());

    public final static int MSG_HEADER_SIZE = 8;


    private int version;
    private int msgBodySize;
    private int crc;
    private byte zip;
    private byte msgType;
    private byte priority;


    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public int getMsgBodySize() {
        return msgBodySize;
    }

    public void setMsgBodySize(int msgBodySize) {
        this.msgBodySize = msgBodySize;
    }


/*
    public byte getZip() {
        return zip;
    }

    public void setZip(byte zip) {
        this.zip = zip;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }
*/



    /*

    private byte[] buffer;
    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public boolean encode() {
        // 可以使用java aio ByteBuffer
        // 可以使用netty ByteBuf
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(buf);

            byte[] temp = ByteArrayIntUtil.intToByteArrayN(msgContentSize, 4); // 新版本
            // byte[] temp =  Util.hton(msgContentSize); // 老版本

            out.write(temp, 0, 4);


            out.writeInt(crc);
            out.writeByte(zip);
            out.writeByte(msgType);
            out.writeInt(functionNo);


            buffer = buf.toByteArray();

            out.close();
            buf.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean decode(byte[] msgHeader) {
        try {

            ByteArrayInputStream buf = new ByteArrayInputStream(msgHeader);
            DataInputStream in = new DataInputStream(buf);

            byte[] size = new byte[4];

            in.readFully(size);

            msgContentSize = ByteArrayIntUtil.byteArrayToIntN(size); // 新版本

            //  msgContentSize = Util.ntoh(size); // 老版本
            //logger.info("消息内容大小" + msgContentSize);

            crc = in.readInt();
            zip = in.readByte();
            msgType = in.readByte();
            functionNo = in.readInt();

            in.close();
            buf.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

  */
}
