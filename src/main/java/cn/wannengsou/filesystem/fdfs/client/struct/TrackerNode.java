package cn.wannengsou.filesystem.fdfs.client.struct;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 15:10
 **/
public class TrackerNode extends Node {

    private Set<GroupNode> groupNodes = null;
    private Charset charset = null;

    public TrackerNode(int weight, String name, Charset charset) {
        super(weight, name);
        this.charset = charset;
    }

    public TrackerNode(int weight, String name, Charset charset, Set<GroupNode> groupNodes) {
        this(weight, name, charset);
        this.groupNodes = groupNodes;
    }

    public boolean addGroup(GroupNode group){
        if(groupNodes == null){
            groupNodes = new HashSet<>();
        }

        return groupNodes.add(group);
    }

    public Set<GroupNode> getGroupNodes() {
        return groupNodes;
    }

    public void setGroupNodes(Set<GroupNode> groupNodes) {
        this.groupNodes = groupNodes;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
