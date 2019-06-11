package cn.wannengsou.filesystem.fdfs.client.netty.handler;

import cn.wannengsou.filesystem.fdfs.client.protocol.BaseRequest;
import cn.wannengsou.filesystem.fdfs.client.utils.BytesUtil;
import cn.wannengsou.filesystem.fdfs.client.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @program: fastdfs-client
 * @description: FastDFS的解码器，用来转化FastDFS传输有特定的私有协议
 * @author: PanShaoJie
 * @create: 2019-05-09 16:14
 **/
public class FastDFSEncoder extends MessageToByteEncoder<BaseRequest> {

    /**
     * 本质上是调用{@link BaseRequest#encodeParam(Charset)}
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, BaseRequest msg, ByteBuf out) throws IOException {
        byte[] bytes = msg.encodeParam();
        out.writeBytes(bytes);
//        if(out.isReadable()){
//            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");
//        }
    }
}
