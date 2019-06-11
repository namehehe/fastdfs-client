package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.bean.StorageState;
import cn.wannengsou.filesystem.fdfs.client.mapper.ObjectMateData;
import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import cn.wannengsou.filesystem.fdfs.client.utils.FastDfsParamMapperUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: fastdfs-client
 * @description: 获取storage的状态
 * @author: PanShaoJie
 * @create: 2019-05-16 15:51
 **/
public class GetListStorageStateTask extends AbstractTask<List<StorageState>> {
    public GetListStorageStateTask(HandlerObserver observer, String address, Object msg) {
        super(observer, address, msg);
    }

    public GetListStorageStateTask(HandlerObserver observer, String address, Object msg, Charset charset) {
        super(observer, address, msg, charset);
    }

    @Override
    protected List<StorageState> decodeContent(ByteBuf byteBuf) throws IOException {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return decodeGroup(bytes);
    }

    /**
     * 解析Group
     */
    private List<StorageState> decodeGroup(byte[] bs) throws IOException {
        // 获取对象转换定义
        ObjectMateData objectMateData = FastDfsParamMapperUtils.getObjectMap(StorageState.class);

        int fixFieldsTotalSize = objectMateData.getFieldsFixTotalSize();
        if (bs.length % fixFieldsTotalSize != 0) {
            throw new IOException("FixFieldsTotalSize=" + fixFieldsTotalSize + ", 但是数据长度=" + bs.length + ", 数据无效");
        }

        // 计算反馈对象数量
        int count = bs.length / fixFieldsTotalSize;
        int offset = 0;
        List<StorageState> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            byte[] one = new byte[fixFieldsTotalSize];
            System.arraycopy(bs, offset, one, 0, fixFieldsTotalSize);
            results.add(FastDfsParamMapperUtils.map(one, StorageState.class, getCharset()));
            offset += fixFieldsTotalSize;
        }

        return results;
    }
}
