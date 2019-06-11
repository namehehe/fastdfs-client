package cn.wannengsou.filesystem.fdfs.client.processer;

import cn.wannengsou.filesystem.fdfs.client.task.AbstractTask;

import java.util.concurrent.*;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 11:09
 **/
public abstract class AbstractProcessor {

    protected ExecutorService executor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20), new ThreadPoolExecutor
            .CallerRunsPolicy());

    public <T> T process(AbstractTask<T> task) throws ExecutionException, InterruptedException {
        return executor.submit(task).get();
    }
}
