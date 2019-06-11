package cn.wannengsou.filesystem.fdfs.client.netty.config;

/**
 * @program: fastdfs-client
 * @description: 连接参数，如ip，端口等
 * @author: PanShaoJie
 * @create: 2019-05-09 16:39
 **/
public class ConnectionConfig {

    private String ipAddr;
    private int port;

    public ConnectionConfig(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
