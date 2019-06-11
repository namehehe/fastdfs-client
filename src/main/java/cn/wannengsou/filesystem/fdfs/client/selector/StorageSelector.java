package cn.wannengsou.filesystem.fdfs.client.selector;

import cn.wannengsou.filesystem.fdfs.client.struct.Node;
import cn.wannengsou.filesystem.fdfs.client.struct.StorageNode;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 09:35
 **/
public class StorageSelector extends AbstractSelector<StorageNode> {

    private PriorityBlockingQueue<StorageNode> storages = getNodes();
//    private PriorityBlockingQueue<Node> storages = new PriorityBlockingQueue<>();   //原型
//    private PriorityBlockingQueue<Node> storagesDuplicate = new PriorityBlockingQueue<>();  //副本

    public StorageSelector(AbstractPolicy<StorageNode> policy) {
        super(policy);
    }

    public StorageSelector(PriorityBlockingQueue<StorageNode> storages, AbstractPolicy<StorageNode> policy) {
        super(storages, policy);
    }

    @Override
    public StorageNode select() {
        if(storages.isEmpty()){
            try {
                int n = 3;
                while (n-- > 0) {
                    Thread.currentThread().wait(2000L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            return storages.poll();
        }
        return null;
    }

    @Override
    public StorageNode selectByGroup(String group) {
        return null;
    }

    //节点权重读写采用无锁化
    private void calculateWeight(Node node){
        for(;;){
            AtomicInteger weightAtm = node.getWeight();
            int weight = weightAtm.get();

            if(weightAtm.compareAndSet(policy.calculateWeight(), weight)){
                break;
            }
        }
    }

    @Override
    public void addNode(StorageNode node) {
        storages.add(node);
    }

    @Override
    public void returnNode(StorageNode node) {
        calculateWeight(node);
        storages.add(node);
    }

    public static class CustomicPolicy extends AbstractPolicy<StorageNode>{

        @Override
        public StorageNode select() {
            return null;
        }

        @Override
        public int calculateWeight() {
            return 0;
        }
    }
}
