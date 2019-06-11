package cn.wannengsou.filesystem.fdfs.client.netty.handler;

import cn.wannengsou.filesystem.fdfs.client.protocol.ProtocolHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-09 16:29
 **/
public class FastDFSDecoder extends ByteToMessageDecoder{

    private long length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            //解析报文头，解决粘包/拆包问题
            if (in.readableBytes() <= 0) {
                return;
            }
            if(length == 0){
                ProtocolHead head = ProtocolHead.createFromByteBuf(in);
                length = head.getContentLength();
            }
            if(length <= in.readableBytes()) {
                setSingleDecode(true);
                out.add(in);
                length = 0;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
