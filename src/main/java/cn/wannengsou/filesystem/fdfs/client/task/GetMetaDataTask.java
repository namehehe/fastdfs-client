package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import cn.wannengsou.filesystem.fdfs.client.utils.BytesUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @program: fastdfs-client
 * @description: 获取文件元数据任务
 * @author: PanShaoJie
 * @create: 2019-05-16 15:36
 **/
public class GetMetaDataTask extends AbstractTask<Map<String, String>> {
    public GetMetaDataTask(HandlerObserver observer, String address, Object msg) {
        super(observer, address, msg);
    }

    public GetMetaDataTask(HandlerObserver observer, String address, Object msg, Charset charset) {
        super(observer, address, msg, charset);
    }

    @Override
    protected Map<String, String> decodeContent(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        return BytesUtil.fromByte(bytes);
    }
}
