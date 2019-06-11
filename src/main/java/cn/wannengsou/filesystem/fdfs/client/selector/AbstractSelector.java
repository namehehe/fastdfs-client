package cn.wannengsou.filesystem.fdfs.client.selector;

import cn.wannengsou.filesystem.fdfs.client.struct.Node;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @program: fastdfs-client
 * @description: 选择器虚拟类，负责选择节点并且操作节点权重
 * @author: PanShaoJie
 * @create: 2019-05-09 17:59
 **/
public abstract class AbstractSelector<T> implements Selector<T > {
    private PriorityBlockingQueue<T> nodes = null;
    AbstractPolicy<T> policy = null;

    public AbstractSelector(AbstractPolicy<T> policy) {
        this.policy = policy;
        this.nodes = new PriorityBlockingQueue<>();
    }

    public AbstractSelector(PriorityBlockingQueue<T> nodes, AbstractPolicy<T> policy) {
        this.nodes = nodes;
        this.policy = policy;
    }

    @Override
    public void addNode(T node) {
        throw new UnsupportedOperationException("不支持此方法");
    }

    @Override
    public void returnNode(T node) {
        throw new UnsupportedOperationException("不支持此方法");
    }

    public PriorityBlockingQueue<T> getNodes() {
        return nodes;
    }

    public void setNodes(PriorityBlockingQueue<T> nodes) {
        this.nodes = nodes;
    }

    public AbstractPolicy<T> getPolicy() {
        return policy;
    }

    public void setPolicy(AbstractPolicy<T> policy) {
        this.policy = policy;
    }
}
