package cn.wannengsou.filesystem.fdfs.client.exception;

/**
 * 非FastDFS本身的错误码抛出的异常，取服务端连接取不到时抛出的异常
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 11:41 <br/>
 */
public class FastDfsUnavailableException extends FastDfsException {
    public FastDfsUnavailableException(String message) {
        super("无法获取服务端连接资源：" + message);
    }

    public FastDfsUnavailableException(String message, Throwable t) {
        super("无法获取服务端连接资源：" + message, t);
    }
}