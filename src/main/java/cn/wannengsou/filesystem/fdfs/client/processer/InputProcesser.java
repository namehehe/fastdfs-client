package cn.wannengsou.filesystem.fdfs.client.processer;

import cn.wannengsou.filesystem.fdfs.client.task.AbstractTask;

import java.util.concurrent.*;

/**
 * @program: fastdfs-client
 * @description: 从Tracker读取信息的执行器
 * @author: PanShaoJie
 * @create: 2019-05-17 11:07
 **/
public class InputProcesser extends AbstractProcessor {

    @Override
    public <T> T process(AbstractTask<T> task) throws ExecutionException, InterruptedException {
        return executor.submit(task).get();
    }
}
