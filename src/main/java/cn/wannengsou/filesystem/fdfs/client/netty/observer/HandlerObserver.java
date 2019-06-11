package cn.wannengsou.filesystem.fdfs.client.netty.observer;

import cn.wannengsou.filesystem.fdfs.client.task.RecvStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @program: fastdfs-client
 * @description: handler的观察者，主要用来观察handler初始化、发送、接收时出现的状态，需要内聚一个ctx管理器，目前用一个map去管理ctx
 * @author: PanShaoJie
 * @create: 2019-05-09 16:47
 **/
public class HandlerObserver {

    //此map用来保存远程服务器的连接信息，key为:本地ip + : + port(e.g:127.0.0.1:53556)，value为链路的上下文
    private ConcurrentMap<String, ChannelHandlerContext> nodeMapper = new ConcurrentHashMap<>();
    //此map用来保存发送消息到服务器后，服务器响应信息的承载体，key为tracker或者storage服务器地址，value为承载体
    private ConcurrentMap<ChannelId, RecvStatus> messages = new ConcurrentHashMap<>();

    public void register(ChannelHandlerContext ctx){
        nodeMapper.putIfAbsent(getRemoteAddress(ctx), ctx);
    }

    public boolean contains(String address){
        return nodeMapper.keySet().contains(address);
    }

    public ChannelHandlerContext getCtx(String address){
        return nodeMapper.get(address);
    }

    public void notifyRThread(ChannelHandlerContext ctx, ByteBuf msg){
        RecvStatus recvStatus = messages.get(ctx.channel().id());
        if(recvStatus != null) {
            recvStatus.setMsg(msg);
            recvStatus.getCountDownLatch().countDown();
        }
    }

    public void notifyClose(ChannelHandlerContext ctx){
        messages.remove(ctx.channel().id());
        nodeMapper.remove(getRemoteAddress(ctx));
    }

    public void notifyClose(ChannelHandlerContext ctx, Throwable cause){

    }

    public RecvStatus read(String code) throws InterruptedException {
        return messages.remove(nodeMapper.get(code).channel().id());
    }

    public void write(String address, Object msg, RecvStatus status){
        messages.putIfAbsent(nodeMapper.get(address).channel().id(), status);
        getCtx(address).writeAndFlush(msg);
    }

    private String getLocalAddress(ChannelHandlerContext ctx){
        return StringUtils.substring(ctx.channel().localAddress().toString(), 1);
    }

    private String getRemoteAddress(ChannelHandlerContext ctx){
        return StringUtils.substring(ctx.channel().remoteAddress().toString(), 1);
    }
}
