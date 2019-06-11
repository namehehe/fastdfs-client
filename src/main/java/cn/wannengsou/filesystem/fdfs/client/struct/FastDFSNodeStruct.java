package cn.wannengsou.filesystem.fdfs.client.struct;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @program: fastdfs-client
 * @description: FastDFS的节点结构，即tracker-group-storage三者之间的关系视图
 * @author: PanShaoJie
 * @create: 2019-05-09 17:27
 **/
public class FastDFSNodeStruct {

    private FastDFSNodeStruct parent = null;
    private PriorityBlockingQueue<FastDFSNodeStruct> childrens = null;
    private Node node = null;

    public FastDFSNodeStruct(Node node) {
        this.node = node;
    }

    private final static int DEFAULS_INITIALIZE_CAPACITY = 3;

    private FastDFSNodeStruct addChildren(Node childrenNode){
        if(childrens == null){
            childrens= new PriorityBlockingQueue<>(DEFAULS_INITIALIZE_CAPACITY, (o1, o2) -> o1.getNode().getWeight().get() > o2.getNode().getWeight().get() ? 1 : 0);
        }
        childrens.add(new FastDFSNodeStruct(childrenNode));
        return this;
    }

    public FastDFSNodeStruct getParent() {
        return parent;
    }

    public void setParent(FastDFSNodeStruct parent) {
        this.parent = parent;
    }

    public PriorityBlockingQueue<FastDFSNodeStruct> getChildrens() {
        return childrens;
    }

    public void setChildrens(PriorityBlockingQueue<FastDFSNodeStruct> childrens) {
        this.childrens = childrens;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
