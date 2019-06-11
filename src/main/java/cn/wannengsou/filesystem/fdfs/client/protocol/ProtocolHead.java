package cn.wannengsou.filesystem.fdfs.client.protocol;

import cn.wannengsou.filesystem.fdfs.client.constant.CmdConstants;
import cn.wannengsou.filesystem.fdfs.client.constant.OtherConstants;
import cn.wannengsou.filesystem.fdfs.client.exception.FastDfsServerException;
import cn.wannengsou.filesystem.fdfs.client.utils.ByteBufUtil;
import cn.wannengsou.filesystem.fdfs.client.utils.BytesUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * FastDFS 协议头(协议头一共10位) 用于解析报文头
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 1:02 <br/>
 */
public class ProtocolHead {

    /**
     * 报文长度
     */
    private static final int HEAD_LENGTH = OtherConstants.FDFS_PROTO_PKG_LEN_SIZE + 2;

    /**
     * 报文内容长度1-7位
     */
    private long contentLength = 0;

    /**
     * 报文类型8位
     */
    private byte cmd;

    /**
     * 处理状态9位
     */
    private byte status = 0;

    /**
     * 请求报文构造函数
     */
    public ProtocolHead(byte cmd) {
        this.cmd = cmd;
    }

    /**
     * 返回报文构造函数
     *
     * @param contentLength 报文内容长度1-7位
     * @param cmd           报文类型8位
     * @param status        处理状态9位
     */
    private ProtocolHead(long contentLength, byte cmd, byte status) {
        this.contentLength = contentLength;
        this.cmd = cmd;
        this.status = status;
    }

    public static ProtocolHead createFromByteBuf(ByteBuf byteBuf) throws IOException {
        //检查byteBuf的可读长度是否>=响应头长度
        int bytes;
        if((bytes = byteBuf.readableBytes()) <= HEAD_LENGTH){
            throw new IOException("接收数据包大小不等于请求头中指定的大小 " + bytes + " != " + HEAD_LENGTH);
        }

        long returnContentLength = ByteBufUtil.bytebuf2long(byteBuf);
        byte returnCmd = byteBuf.readByte();
        byte returnStatus = byteBuf.readByte();

        // 返回解析出来的ProtoHead
        return new ProtocolHead(returnContentLength, returnCmd, returnStatus);
    }

    /**
     * toByte
     */
    byte[] toByte() {
        byte[] header;
        byte[] hex_len;
        header = new byte[HEAD_LENGTH];
        Arrays.fill(header, (byte) 0);
        hex_len = BytesUtil.long2buff(contentLength);
        System.arraycopy(hex_len, 0, header, 0, hex_len.length);
        header[OtherConstants.PROTO_HEADER_CMD_INDEX] = cmd;
        header[OtherConstants.PROTO_HEADER_STATUS_INDEX] = status;
        return header;
    }

    /**
     * 验证服务端返回报文有效性
     *
     * @return 返回true表示有效
     * @throws IOException 操作异常
     */
    public boolean validateResponseHead() throws IOException {
        // 检查是否是正确反馈报文
        if (cmd != CmdConstants.FDFS_PROTO_CMD_RESP) {
            throw new IOException("接收命令: [" + cmd + "] 不正确, 应该是: [" + CmdConstants.FDFS_PROTO_CMD_RESP + "]");
        }
        // 获取处理错误状态
        if (status != 0) {
            throw FastDfsServerException.byCode(status);
        }
        if (contentLength < 0) {
            throw new IOException("接收内容长度小于0: " + contentLength + " < 0!");
        }
        return true;
    }

    public long getContentLength() {
        return contentLength;
    }

    void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public byte getCmd() {
        return cmd;
    }

    public byte getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ProtoHead [contentLength=" + contentLength + ", cmd=" + cmd + ", status=" + status + "]";
    }
}
