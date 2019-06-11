package cn.wannengsou.filesystem.fdfs.client.selector;

import cn.wannengsou.filesystem.fdfs.client.bean.StorageSimpleNode;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @program: fastdfs-client
 * @description: 采用FastDFS默认的负载均衡规则选取storage
 * @author: PanShaoJie
 * @create: 2019-05-16 18:04
 **/
public class DefaultStorageSelector extends AbstractSelector<StorageSimpleNode> {

    //private PriorityBlockingQueue<TrackerNode> trackerNodes = getNodes();

    public DefaultStorageSelector(AbstractPolicy<StorageSimpleNode> abstractPolicy) {
        super(abstractPolicy);
    }

    public DefaultStorageSelector(PriorityBlockingQueue<StorageSimpleNode> trackers, AbstractPolicy<StorageSimpleNode> abstractPolicy) {
        super(trackers, abstractPolicy);
    }

    @Override
    public StorageSimpleNode select() {
        return policy.select();
    }

    @Override
    public StorageSimpleNode selectByGroup(String group) {
        return null;
    }
}
