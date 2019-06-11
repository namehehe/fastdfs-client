package cn.wannengsou.filesystem.fdfs.client.task;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.CountDownLatch;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-10 17:35
 **/
public class RecvStatus {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    //承载的消息体
    private ByteBuf msg = null;

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public ByteBuf getMsg() {
        return msg;
    }

    public void setMsg(ByteBuf msg) {
        this.msg = msg;
    }
}
