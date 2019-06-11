package cn.wannengsou.filesystem.fdfs.client.selector;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-17 09:47
 **/
public abstract class AbstractPolicy<T>{

    public abstract T select();

    public abstract int calculateWeight();
}
