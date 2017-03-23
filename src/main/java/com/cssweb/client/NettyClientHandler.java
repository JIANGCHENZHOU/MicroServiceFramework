package com.cssweb.client;

import com.cssweb.msg.CustomMessage;
import com.cssweb.msg.CustomMessageHeader;
import com.cssweb.msg.RpcRequest;
import com.cssweb.msg.RpcResponse;
import com.cssweb.serialization.protostuff.SerializationUtil;
import com.cssweb.test.HelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by chenh on 2016/12/26.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<CustomMessage> {
    private static final Logger logger = LogManager.getLogger(NettyClientHandler.class.getName());


    ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channelActive");

        this.ctx = ctx;
/*



            CustomMessageHeader msgHeader = new CustomMessageHeader();
            msgHeader.setCrc(0);
            msgHeader.setMsgBodySize(msgBody.length);
            //msgHeader.setZip((byte) 0);
            //msgHeader.setMsgType((byte) 0);



            CustomMessage request = new CustomMessage();
            request.setMsgHeader(msgHeader);
            request.setMsgBody(msgBody);

            ctx.write(request);
            ctx.flush();
*/
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, final CustomMessage msg) throws Exception {
        logger.info("channelRead0");

        RpcResponse rpcRes = SerializationUtil.deserialize(msg.getMsgBody(), RpcResponse.class);


        logger.info("Ó¦´ðÏûÏ¢£º" + rpcRes.getResult());

        //ByteBufUtil.hexDump()
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("exceptionCaught");
        ctx.close();
    }

    public void test() {
        HelloService hello = refer(HelloService.class);
        String result = hello.sayHello("chf");
        System.out.println("result = " + result);
    }


    public <T> T refer(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                RpcRequest rpcReq = new RpcRequest();
                rpcReq.setRequestId(UUID.randomUUID().toString());
                rpcReq.setClassName(method.getDeclaringClass().getName());
                rpcReq.setMethodName(method.getName());
                rpcReq.setParameterTypes(method.getParameterTypes());
                rpcReq.setParameters(args);

                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //ObjectOutputStream oos = new ObjectOutputStream(baos);
                //oos.writeObject(request);

                //byte[] msgBody = baos.toByteArray();
                byte[] msgBody = SerializationUtil.serialize(rpcReq);

                CustomMessageHeader msgHeader = new CustomMessageHeader();
                msgHeader.setCrc(0);
                msgHeader.setMsgBodySize(msgBody.length);
                //msgHeader.setZip((byte) 0);
                //msgHeader.setMsgType((byte) 0);

                CustomMessage msg = new CustomMessage();
                msg.setMsgHeader(msgHeader);
                msg.setMsgBody(msgBody);

                ctx.write(msg);
                ctx.flush();

                String str = "tmp";
                return str;
            }
        });
    }
}
