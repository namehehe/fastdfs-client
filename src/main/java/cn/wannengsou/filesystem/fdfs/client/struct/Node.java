package cn.wannengsou.filesystem.fdfs.client.struct;

import java.nio.charset.Charset;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: fastdfs-client
 * @description: 树节点，拥有节点名称（注意，此处需要根据名称去区分tracker、group、storage）与权重属性，默认采用ip地址+端口的形式（即：192.168.1.106:23000）
 * @author: PanShaoJie
 * @create: 2019-05-09 17:34
 **/
public class Node {

    private AtomicInteger weight = null;
    private String name = null;


    public Node(int weight, String name) {
        this.weight = new AtomicInteger(weight);
        this.name = name;
    }

    public AtomicInteger getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight.set(weight);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            return true;
        } else if(obj == null || !this.getClass().equals(obj.getClass())){
            return false;
        }
        Node tmp = (Node) obj;
        if(tmp.getName() == null && name == null){
            return true;
        }else
            return name != null && name.equals(tmp.getName());
    }
}
