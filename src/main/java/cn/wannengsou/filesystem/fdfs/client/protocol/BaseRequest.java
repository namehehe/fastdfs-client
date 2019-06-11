package cn.wannengsou.filesystem.fdfs.client.protocol;

import cn.wannengsou.filesystem.fdfs.client.mapper.ObjectMateData;
import cn.wannengsou.filesystem.fdfs.client.utils.FastDfsParamMapperUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * FastDFS操作请求 基类
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 1:00 <br/>
 */
public abstract class BaseRequest {
    /**
     * 报文头
     */
    protected ProtocolHead head;

    /**
     * 发送文件流
     */
    protected InputStream inputFile;

    /**
     * 获取报文头(包内可见)
     */
    ProtocolHead getHead() {
        return head;
    }

    /**
     * 获取报文头
     */
    byte[] getHeadByte(Charset charset) {
        // 设置报文长度
        head.setContentLength(getBodyLength(charset));
        // 返回报文byte
        return head.toByte();
    }

    public byte[] encodeParam() throws IOException {
        byte[] params = encodeParam(Charset.defaultCharset());
        if(inputFile == null){
            return params;
        }
        return ArrayUtils.addAll(params, getFileByte());
    }

    private byte[] getFileByte() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = inputFile.read(bytes)) != -1){
            byteArrayOutputStream.write(bytes, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 打包参数
     */
    public byte[] encodeParam(Charset charset) {
        byte[] paramByte = FastDfsParamMapperUtils.toByte(this, charset);
        return ArrayUtils.addAll(getHeadByte(charset), paramByte);
    }

    /**
     * 获取参数域长度
     */
    private long getBodyLength(Charset charset) {
        ObjectMateData objectMateData = FastDfsParamMapperUtils.getObjectMap(this.getClass());
        return objectMateData.getFieldsSendTotalByteSize(this, charset) + getFileSize();
    }

    InputStream getInputFile() {
        return inputFile;
    }

    public long getFileSize() {
        return 0;
    }
}
