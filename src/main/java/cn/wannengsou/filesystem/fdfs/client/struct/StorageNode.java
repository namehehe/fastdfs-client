package cn.wannengsou.filesystem.fdfs.client.struct;

import java.nio.charset.Charset;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 15:10
 **/
public class StorageNode extends Node {

    private GroupNode groupNode;

    public StorageNode(int weight, String name) {
        super(weight, name);
    }

    public StorageNode(int weight, String name, GroupNode groupNode) {
        super(weight, name);
        this.groupNode = groupNode;
    }

    public GroupNode getGroupNode() {
        return groupNode;
    }

    public void setGroupNode(GroupNode groupNode) {
        this.groupNode = groupNode;
    }
}
