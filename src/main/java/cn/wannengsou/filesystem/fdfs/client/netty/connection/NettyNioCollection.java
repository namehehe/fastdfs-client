package cn.wannengsou.filesystem.fdfs.client.netty.connection;

import cn.wannengsou.filesystem.fdfs.client.netty.config.ConnectionConfig;
import cn.wannengsou.filesystem.fdfs.client.netty.handler.FastDFSDecoder;
import cn.wannengsou.filesystem.fdfs.client.netty.handler.FastDFSEncoder;
import cn.wannengsou.filesystem.fdfs.client.netty.handler.FastDFSHandler;
import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @program: fastdfs-client
 * @description: netty连接客户端，连接fastdfs的tracker,storage.
 * @author: PanShaoJie
 * @create: 2019-05-09 15:52
 **/
public class NettyNioCollection {
    //config
    private ConnectionConfig connectionConfig;
    //observer
    private HandlerObserver observer;

    private NettyNioCollection(){
    }

    public static NettyNioCollection collection(){
        return new NettyNioCollection();
    }

    public void init(CyclicBarrier cyclicBarrier) throws InterruptedException, BrokenBarrierException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new FastDFSDecoder())
                                .addLast(new FastDFSEncoder())
                                .addLast(new FastDFSHandler(observer));
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(connectionConfig.getIpAddr(), connectionConfig.getPort()).sync();
        cyclicBarrier.await();
        channelFuture.channel().closeFuture().sync();
    }

    public NettyNioCollection observer(HandlerObserver observer){
        this.observer = observer;
        return this;
    }

    public NettyNioCollection config(ConnectionConfig connectionConfig){
        this.connectionConfig = connectionConfig;
        return this;
    }
}
