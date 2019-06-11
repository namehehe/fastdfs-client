package cn.wannengsou.filesystem.fdfs.client.selector;

/**
 * @program: fastdfs-client
 * @description: FastDFS连接选择器，主要用于选择tracker-group-storage, 以select开头的方法是选择权重最低的节点
 * @author: PanShaoJie
 * @create: 2019-05-09 16:59
 **/
public interface Selector<T> {

    T select();

    T selectByGroup(String group);

    void addNode(T node);

    void returnNode(T node);
}
