# fastdfs-client
FastDFS java Client，contains usually usage of FastDFS(such as: upload、download、monitor、manage)

FastDFS的java客户端，包括了常用的操作，如上传文件，下载文件，监控tracker和storage的状态和管理

连接摒弃了原来客户端的socket，改用netty进行连接