package cn.wannengsou.filesystem.fdfs.client.utils;

import io.netty.buffer.ByteBuf;

/**
 * @program: fastdfs-client
 * @description: ByteBuf与int, long转换的工具类
 * @author: PanShaoJie
 * @create: 2019-05-16 14:54
 **/
public class ByteBufUtil {

    /**
     * 从byteBuf的readerIndex指针开始读，注意此方法会导致byteBuf的读指针位置改变
     *
     * @param byteBuf
     * @return
     */
    public static long bytebuf2long(ByteBuf byteBuf){
        long res = 0;
        byte b;
        for(int i = 0; i < 8; i++){
            b = byteBuf.readByte();
            res = res | ((long) (b >= 0 ? b : 256 + b)) << (56 - i * 8);
        }
        return res;
    }
}
