package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.bean.GroupState;
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
 * @description: 获取group的状态
 * @author: PanShaoJie
 * @create: 2019-05-16 16:00
 **/
public class GetListGroupStateTask extends AbstractTask<List<GroupState>> {
    public GetListGroupStateTask(HandlerObserver observer, String address, Object msg) {
        super(observer, address, msg);
    }

    public GetListGroupStateTask(HandlerObserver observer, String address, Object msg, Charset charset) {
        super(observer, address, msg, charset);
    }

    @Override
    protected List<GroupState> decodeContent(ByteBuf byteBuf) throws IOException {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return decodeGroup(bytes);
    }

    /**
     * 解析Group
     */
    private List<GroupState> decodeGroup(byte[] bs) throws IOException {
        // 获取对象转换定义
        ObjectMateData objectMateData = FastDfsParamMapperUtils.getObjectMap(GroupState.class);

        int fixFieldsTotalSize = objectMateData.getFieldsFixTotalSize();
        if (bs.length % fixFieldsTotalSize != 0) {
            throw new IOException("FixFieldsTotalSize=" + fixFieldsTotalSize + ", 但是数据长度=" + bs.length + ", 数据无效");
        }

        // 计算反馈对象数量
        int count = bs.length / fixFieldsTotalSize;
        int offset = 0;
        List<GroupState> results = new ArrayList<GroupState>(count);
        for (int i = 0; i < count; i++) {
            byte[] one = new byte[fixFieldsTotalSize];
            System.arraycopy(bs, offset, one, 0, fixFieldsTotalSize);
            results.add(FastDfsParamMapperUtils.map(one, GroupState.class, getCharset()));
            offset += fixFieldsTotalSize;
        }

        return results;
    }
}
