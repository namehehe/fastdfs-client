package cn.wannengsou.filesystem.fdfs.client;

import cn.wannengsou.filesystem.fdfs.client.bean.*;
import cn.wannengsou.filesystem.fdfs.client.protocol.storage.enums.StorageMetadataSetType;
import cn.wannengsou.filesystem.fdfs.client.protocol.storage.request.*;
import cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request.*;
import cn.wannengsou.filesystem.fdfs.client.struct.StorageNode;
import cn.wannengsou.filesystem.fdfs.client.task.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @program: fastdfs-client
 * @description: FastDFS客户端上下文
 * @author: PanShaoJie
 * @create: 2019-05-16 16:10
 **/
public class FastDFSClientContext {

    private FastDFSClient client;

    public FastDFSClientContext(FastDFSClient client) {
        this.client = client;
    }

    public StorageSimpleNode getStorageNodeInfo(String groupName) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        StorageSimpleNode result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(), new GetStorageNodeByGroupNameRequest(groupName), StorageSimpleNode.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public List<StorageState> getStorageNodeListInfo(String groupName) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        List<StorageState> result = client.getInputProcesser().process(new GetListStorageStateTask(client.getObserver(), node.getName(), new GetListStorageRequest(groupName)));
        client.getSelector().returnNode(node);
        return result;
    }

    public List<StorageState> getStorageNodeListInfo(String groupName, String storageIpAddr) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        List<StorageState> result = client.getInputProcesser().process(new GetListStorageStateTask(client.getObserver(), node.getName(), new GetListStorageRequest(groupName, storageIpAddr)));
        client.getSelector().returnNode(node);
        return result;
    }

    public List<GroupState> getGroupListInfo() throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        List<GroupState> result = client.getInputProcesser().process(new GetListGroupStateTask(client.getObserver(), node.getName(), new GetGroupListRequest()));
        client.getSelector().returnNode(node);
        return result;
    }

    public StorageNodeInfo getFetchStorageInfo(String groupName, String path, boolean toUpdate) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        StorageNodeInfo result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(), new GetFetchStorageRequest(groupName, path, toUpdate), StorageNodeInfo.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public Void deleteStorage(String groupName, String ipAddr) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        client.getSelector().returnNode(node);
        return client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(), new DeleteStorageRequest(groupName, ipAddr), Void.class));
    }

    /**
     * 从文件上传命令<br/>
     * <pre>
     * 使用背景
     * 使用FastDFS存储一个图片的多个分辨率的备份时，希望只记录源图的FID，
     * 并能将其它分辨率的图片与源图关联。可以使用从文件方法
     * 名词注解:
     *   主从文件是指文件ID有关联的文件，一个主文件可以对应多个从文件
     *   主文件ID = 主文件名 + 主文件扩展名
     *   从文件ID = 主文件名 + 从文件后缀名 + 从文件扩展名
     * 以缩略图场景为例：主文件为原始图片，从文件为该图片的一张或多张缩略图
     * 流程说明：
     *  1.先上传主文件（即：原文件），得到主文件FID
     *  2.然后上传从文件（即：缩略图），指定主文件FID和从文件后缀名，上传后得到从文件FID。
     *
     * 注意:
     *   FastDFS中的主从文件只是在文件ID上有联系。FastDFS server端没有记录主从文件对应关系，
     *   因此删除主文件，FastDFS不会自动删除从文件。删除主文件后，从文件的级联删除，需要由应用端来实现。
     *
     * @param fileSize
     * @param masterFilename
     * @param prefixName
     * @param fileExtName
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public StorePath uploadSlaveFile(long fileSize, String masterFilename, String prefixName, String fileExtName) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        StorePath storePath = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new UploadSlaveFileRequest(null, fileSize, masterFilename, prefixName, fileExtName), StorePath.class));
        storePath.setTrackerAddr(node.getName());
        client.getSelector().returnNode(node);
        return storePath;
    }

    public StorePath uploadFile(byte storeIndex, InputStream inputStream, String fileExtName, long fileSize, boolean isAppenderFile) throws
            ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        StorePath storePath = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new UploadFileRequest(storeIndex, inputStream, fileExtName, fileSize, isAppenderFile), StorePath.class));
        storePath.setTrackerAddr(node.getName());
        client.getSelector().returnNode(node);
        return storePath;
    }

    public Void truncateFile(String path, long fileSize) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Void result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new TruncateRequest(path, fileSize), Void.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public Void setMetadata(String groupName, String path, Map<String, String> metaData, StorageMetadataSetType type) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Void result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new SetMetadataRequest(groupName, path, metaData, type), Void.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public FileInfo queryFileInfo(String groupName, String path) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        FileInfo fileInfo = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new QueryFileInfoRequest(groupName, path), FileInfo.class));
        client.getSelector().returnNode(node);
        return fileInfo;
    }

    public Void modifyFile(String path, long fileSize, long fileOffset) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Void result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new ModifyRequest(null, fileSize, path, fileOffset), Void.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public Map<String, String> getMetadata(String groupName, String path) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Map<String, String> result = client.getInputProcesser().process(new GetMetaDataTask(client.getObserver(), node.getName(),
                new GetMetadataRequest(groupName, path)));
        client.getSelector().returnNode(node);
        return result;
    }

    public byte[] downloadFile(String groupName, String path, long fileOffset, long fileSize) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        byte[] result = client.getInputProcesser().process(new DownloadFileTask(client.getObserver(), node.getName(),
                new DownloadFileRequest(groupName, path, fileOffset, fileSize)));
        client.getSelector().returnNode(node);
        return result;
    }

    public byte[] downloadFile(String groupName, String path) throws ExecutionException, InterruptedException {
        return downloadFile(groupName, path, 0, 0);
    }

    public Void deleteFile(String groupName, String path) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Void result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new DeleteFileRequest(groupName, path), Void.class));
        client.getSelector().returnNode(node);
        return result;
    }

    public Void appendFile(InputStream inputStream, long fileSize, String group, String path) throws ExecutionException, InterruptedException {
        StorageNode node = client.getSelector().select();
        Void result = client.getInputProcesser().process(new FastDFSTask<>(client.getObserver(), node.getName(),
                new AppendFileRequest(inputStream, fileSize, path), Void.class));
        client.getSelector().returnNode(node);
        return result;
    }
}
