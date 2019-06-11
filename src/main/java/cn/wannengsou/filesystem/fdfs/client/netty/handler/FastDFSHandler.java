package cn.wannengsou.filesystem.fdfs.client.netty.handler;

import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * @program: fastdfs-client
 * @description: 用来处理上传、下载、查询等操作
 * @author: PanShaoJie
 * @create: 2019-05-09 16:04
 **/
public class FastDFSHandler extends ChannelHandlerAdapter {

    private HandlerObserver observer;

    public FastDFSHandler(HandlerObserver observer) {
        this.observer = observer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.fireChannelRead(msg);
        //如果读取的消息内容为ByteBuf则通知，否则直接抛弃
        if(msg instanceof ByteBuf) {
            observer.notifyRThread(ctx, (ByteBuf)msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        observer.register(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        observer.notifyClose(ctx, cause);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        observer.notifyClose(ctx);
    }
}
