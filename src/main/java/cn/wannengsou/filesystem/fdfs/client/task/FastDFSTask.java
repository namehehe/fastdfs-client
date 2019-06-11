package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import cn.wannengsou.filesystem.fdfs.client.utils.FastDfsParamMapperUtils;
import cn.wannengsou.filesystem.fdfs.client.utils.ReflectionsUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-20 14:43
 **/
public class FastDFSTask<T> extends AbstractTask<T> {

    private Class<T> genericType = null;

    public FastDFSTask(HandlerObserver observer, String address, Object msg, Class<T> genericType) {
        super(observer, address, msg);
        this.genericType = genericType;
    }

    public FastDFSTask(HandlerObserver observer, String address, Object msg, Charset charset, Class<T> genericType) {
        super(observer, address, msg, charset);
        this.genericType = genericType;
    }

    protected T decodeContent(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return FastDfsParamMapperUtils.map(bytes, genericType);
    }
}
