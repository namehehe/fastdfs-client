package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @program: fastdfs-client
 * @description: 下载文件任务
 * @author: PanShaoJie
 * @create: 2019-05-16 15:25
 **/
public class DownloadFileTask extends AbstractTask<byte[]> {
    public DownloadFileTask(HandlerObserver observer, String address, Object msg) {
        super(observer, address, msg);
    }

    public DownloadFileTask(HandlerObserver observer, String address, Object msg, Charset charset) {
        super(observer, address, msg, charset);
    }

    @Override
    protected byte[] decodeContent(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
