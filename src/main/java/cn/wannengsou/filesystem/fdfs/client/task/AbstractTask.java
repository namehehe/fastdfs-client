package cn.wannengsou.filesystem.fdfs.client.task;

import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import cn.wannengsou.filesystem.fdfs.client.protocol.ProtocolHead;
import cn.wannengsou.filesystem.fdfs.client.utils.FastDfsParamMapperUtils;
import cn.wannengsou.filesystem.fdfs.client.utils.ReflectionsUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

/**
 * @program: fastdfs-client
 * @description: FastDFS的虚拟类，采用策略模式，自己实现对ByteBuf的解码
 * @author: PanShaoJie
 * @create: 2019-05-16 11:26
 **/
public abstract class AbstractTask<T> implements Callable<T> {

    private HandlerObserver observer = null;
    private Object msg = null;
    private String address = null;
    private Charset charset = Charset.defaultCharset();

    public AbstractTask(HandlerObserver observer, String address, Object msg) {
        this.observer = observer;
        this.address = address;
        this.msg = msg;
    }

    public AbstractTask(HandlerObserver observer, String address, Object msg, Charset charset) {
        this.observer = observer;
        this.address = address;
        this.msg = msg;
        this.charset = charset;
    }

    @Override
    public T call() {
        RecvStatus recvStatus = new RecvStatus();
        observer.write(address, msg, recvStatus);
        try {
            recvStatus.getCountDownLatch().await();

            return decode(observer.read(address));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T decode(RecvStatus recvStatus) throws IOException{
        ByteBuf byteBuf = recvStatus.getMsg();
//        decodeHead(byteBuf);
        return decodeContent(byteBuf);
    }

    protected void decodeHead(ByteBuf byteBuf) throws IOException {
        ProtocolHead head = ProtocolHead.createFromByteBuf(byteBuf);
        if(head.validateResponseHead()){
            long msgLen = head.getContentLength();

            if(byteBuf.readableBytes() < msgLen){
                throw new IOException("消息长度与响应头长度不符");
            }
        }
    }

    protected abstract T decodeContent(ByteBuf byteBuf) throws IOException;

    public HandlerObserver getObserver() {
        return observer;
    }

    public void setObserver(HandlerObserver observer) {
        this.observer = observer;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
