package cn.wannengsou.filesystem.fdfs.client;

import cn.wannengsou.filesystem.fdfs.client.bean.GroupState;
import cn.wannengsou.filesystem.fdfs.client.bean.StorageState;
import cn.wannengsou.filesystem.fdfs.client.netty.config.ConnectionConfig;
import cn.wannengsou.filesystem.fdfs.client.netty.connection.NettyNioCollection;
import cn.wannengsou.filesystem.fdfs.client.netty.observer.HandlerObserver;
import cn.wannengsou.filesystem.fdfs.client.processer.InputProcesser;
import cn.wannengsou.filesystem.fdfs.client.processer.OutputProcesser;
import cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request.GetGroupListRequest;
import cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request.GetListStorageRequest;
import cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request.GetStorageNodeRequest;
import cn.wannengsou.filesystem.fdfs.client.selector.Selector;
import cn.wannengsou.filesystem.fdfs.client.selector.StorageSelector;
import cn.wannengsou.filesystem.fdfs.client.struct.GroupNode;
import cn.wannengsou.filesystem.fdfs.client.struct.Node;
import cn.wannengsou.filesystem.fdfs.client.struct.StorageNode;
import cn.wannengsou.filesystem.fdfs.client.struct.TrackerNode;
import cn.wannengsou.filesystem.fdfs.client.task.GetListGroupStateTask;
import cn.wannengsou.filesystem.fdfs.client.task.GetListStorageStateTask;
import cn.wannengsou.filesystem.fdfs.client.task.RecvStatus;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @program: fastdfs-client
 * @description: FastDFS客户端
 * @author: PanShaoJie
 * @create: 2019-05-16 16:11
 **/
public class FastDFSClient {

    //选择器
    private StorageSelector selector;

    //连接观察者
    private HandlerObserver observer;

    //上传任务执行器
    private OutputProcesser outputProcesser;

    //下载任务执行器
    private InputProcesser inputProcesser;

    private FastDFSClient(StorageSelector selector, HandlerObserver observer, OutputProcesser outputProcesser, InputProcesser inputProcesser) {
        this.selector = selector;
        this.observer = observer;
        this.outputProcesser = outputProcesser;
        this.inputProcesser = inputProcesser;
    }

    public static FastDFSClientContext init(String propertiesPath) throws DocumentException, BrokenBarrierException, InterruptedException {
        if(propertiesPath == null){
            propertiesPath = System.getProperty("user.dir") + "/src/main/resources/FastDFS-client.xml";
        }

        HandlerObserver observer = new HandlerObserver();

        InputProcesser inputProcesser = new InputProcesser();
        OutputProcesser outputProcesser = new OutputProcesser();

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(propertiesPath));
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        Set<TrackerNode> trackerNodes = new HashSet<>();

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        for (Element tracker : elements){
            String ip = String.valueOf(tracker.element("ip").getData());
            int port = Integer.valueOf(String.valueOf(tracker.element("port").getData()));
            String address = ip + ':' + port;

            TrackerNode trackerNode = new TrackerNode(Integer.valueOf(String.valueOf(tracker.element("weight").getData())), address,
                    Charset.forName((String) tracker.element("charset").getData()));
            //初始化连接
            new Thread(() -> {
                try {
                    NettyNioCollection.collection().config(new ConnectionConfig(ip, port)).observer(observer).init(cyclicBarrier);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();

            trackerNodes.add(trackerNode);
        }

        cyclicBarrier.await();
        StorageSelector storageSelector = initStorageSelector(trackerNodes, inputProcesser, observer);

        return new FastDFSClientContext(new FastDFSClient(storageSelector, observer, outputProcesser, inputProcesser));
    }

    public static FastDFSClientContext init() throws DocumentException, InterruptedException, BrokenBarrierException {
        return init(null);
    }

    private static StorageSelector initStorageSelector(Set<TrackerNode> trackerNodes, InputProcesser inputProcesser, HandlerObserver observer) throws BrokenBarrierException, InterruptedException {
        List<StorageNode> storageNodes = new LinkedList<>();
        try {

            for (TrackerNode trackerNode : trackerNodes) {

                List<GroupState> groupStates = inputProcesser.process(new GetListGroupStateTask(observer, trackerNode.getName(), new GetGroupListRequest()));

                for (GroupState groupState : groupStates) {
                    GroupNode groupNode = new GroupNode(0, groupState.getGroupName(), trackerNode);
                    trackerNode.addGroup(groupNode);

                    List<StorageState> storageStates = inputProcesser.process(new GetListStorageStateTask(observer, trackerNode.getName(),
                            new GetListStorageRequest(groupNode.getName()), trackerNode.getCharset()));
                    for (StorageState storageState : storageStates) {
                        StorageNode storageNode = new StorageNode(0, storageState.getIpAddr() + ':' + storageState.getStoragePort(), groupNode);
                        groupNode.addStorage(storageNode);
                        storageNodes.add(storageNode);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        PriorityBlockingQueue<StorageNode> priorityBlockingQueue = new PriorityBlockingQueue<>(storageNodes.size(), Comparator.comparingInt(o -> o
                .getWeight().get()));
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(storageNodes.size() + 1);
        for (StorageNode storageNode : storageNodes){
            String [] stringArr = storageNode.getName().split(":");
            new Thread(() -> {
                try {
                    NettyNioCollection.collection().observer(observer).config(new ConnectionConfig(stringArr[0], Integer.valueOf(stringArr[1]))).init(cyclicBarrier);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
            assert priorityBlockingQueue.add(storageNode);
        }
        cyclicBarrier.await();
        return new StorageSelector(priorityBlockingQueue, new StorageSelector.CustomicPolicy());
    }

    public HandlerObserver getObserver() {
        return observer;
    }

    public void setObserver(HandlerObserver observer) {
        this.observer = observer;
    }

    public OutputProcesser getOutputProcesser() {
        return outputProcesser;
    }

    public void setOutputProcesser(OutputProcesser outputProcesser) {
        this.outputProcesser = outputProcesser;
    }

    public InputProcesser getInputProcesser() {
        return inputProcesser;
    }

    public void setInputProcesser(InputProcesser inputProcesser) {
        this.inputProcesser = inputProcesser;
    }

    public StorageSelector getSelector() {
        return selector;
    }

    public void setSelector(StorageSelector selector) {
        this.selector = selector;
    }
}
