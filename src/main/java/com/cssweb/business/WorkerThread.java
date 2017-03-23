package com.cssweb.business;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


import com.cssweb.msg.CustomMessage;
import com.cssweb.msg.CustomMessageHeader;
import com.cssweb.msg.RpcRequest;
import com.cssweb.msg.RpcResponse;
import com.cssweb.network.NettyServer;
import com.cssweb.serialization.protostuff.SerializationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class WorkerThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(WorkerThread.class.getName());


    private CustomMessage msg;

    public WorkerThread(CustomMessage msg) {
        this.msg = msg;
    }


    @Override
    public void run() {



        try {
            //把字节数组转换成RpcRequest
            //ByteArrayInputStream bis = new ByteArrayInputStream(msg.getMsgBody());
            //bjectInputStream ois = new ObjectInputStream(bis);
            //RpcRequest req = (RpcRequest) ois.readObject();

            RpcRequest rpcReq = SerializationUtil.deserialize(msg.getMsgBody(), RpcRequest.class);


            //通过反射调用方法
            Object service = NettyServer.services.get(rpcReq.getClassName());
            String methodName = rpcReq.getMethodName();
            Class<?>[] parameterTypes = rpcReq.getParameterTypes();
            Object[] arguments = rpcReq.getParameters();

            Method method = service.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(service, arguments);

            //输出result
            RpcResponse rpcRes = new RpcResponse();
            rpcRes.setResult(result);


            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //ObjectOutputStream oos = new ObjectOutputStream(baos);
            //oos.writeObject(res);

            //byte[] msgBody = baos.toByteArray();

            byte[] msgBody = SerializationUtil.serialize(rpcRes);


            CustomMessageHeader msgHeader = new CustomMessageHeader();
            msgHeader.setCrc(0);
            msgHeader.setMsgBodySize(msgBody.length);

            //msgHeader.setZip((byte) 0);
            //msgHeader.setMsgType((byte) 0);

            CustomMessage response = new CustomMessage();
            response.setMsgHeader(msgHeader);
            response.setMsgBody(msgBody);

            msg.getChannelHandlerContext().writeAndFlush(response);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
