package cn.wannengsou.filesystem.fdfs.client.struct;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 15:10
 **/
public class GroupNode extends Node {

    private TrackerNode trackerNode = null;
    private Set<StorageNode> storageNodes = null;

    public GroupNode(int weight, String name) {
        super(weight, name);
    }

    public GroupNode(int weight, String name, TrackerNode trackerNode) {
        super(weight, name);
        this.trackerNode = trackerNode;
    }

    public GroupNode(int weight, String name, TrackerNode trackerNode, Set<StorageNode> storageNodes){
        this(weight, name);
        this.trackerNode = trackerNode;
        this.storageNodes = storageNodes;
    }

    public boolean addStorage(StorageNode storageNode){
        if(storageNodes == null){
            storageNodes = new HashSet<>();
        }

        return storageNodes.add(storageNode);
    }

    public TrackerNode getTrackerNode() {
        return trackerNode;
    }

    public void setTrackerNode(TrackerNode trackerNode) {
        this.trackerNode = trackerNode;
    }

    public Set<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    public void setStorageNodes(Set<StorageNode> storageNodes) {
        this.storageNodes = storageNodes;
    }
}
