package cn.wannengsou.filesystem;

import cn.wannengsou.filesystem.fdfs.client.FastDFSClient;
import cn.wannengsou.filesystem.fdfs.client.FastDFSClientContext;
import cn.wannengsou.filesystem.fdfs.client.bean.FileInfo;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;

/**
 * @program: fastdfs-client
 * @description:
 * @author: PanShaoJie
 * @create: 2019-05-10 10:38
 **/
public class FileChannelTerm {

    @Test
    public void read(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        eventLoopGroup.submit(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();

            System.out.println("eeeeee");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readProperties() throws IOException, DocumentException {
        String propertiesPath = System.getProperty("user.dir") + "/src/main/resources/FastDFS-client.xml";
        System.out.println(propertiesPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(propertiesPath));
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Element tracker : elements){
            System.out.println(tracker.element("ip").getData());
            System.out.println(tracker.element("port").getData());
            System.out.println(tracker.element("charset").getData());
        }
    }

    @Test
    public void FastDFSClientTest() {
        try {
            FastDFSClientContext ctx = FastDFSClient.init(null);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadFileTest() {
        try {
            FastDFSClientContext ctx = FastDFSClient.init(null);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\书籍\\mybatis\\深入浅出MyBatis技术原理与实战 541832.pdf"));
                        System.out.println(ctx.uploadFile((byte) 0, fileInputStream, "pdf", fileInputStream.getChannel().size(), false));
                        cyclicBarrier.await();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            cyclicBarrier.await();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void appendFileTest(){
        try {
            FastDFSClientContext ctx = FastDFSClient.init();
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\yuejiayun_logo_2019.png"));

            ctx.appendFile(fileInputStream, fileInputStream.getChannel().size(), "group1", "M00/00/00/wKgQWFzi2NqAX0S9AAAvtBhmGvg635.png");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFileTest(){
        try {
            FastDFSClientContext ctx = FastDFSClient.init();
            byte[] bytes = ctx.downloadFile("group1", "M00/00/00/wKgQWFzi2NqAX0S9AAAvtBhmGvg635.png");
            File file = new File("C:\\Users\\Administrator\\Desktop\\yuejiayun_logo_206.png");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryFileInfoTest(){
        try {
            FastDFSClientContext ctx = FastDFSClient.init();
            FileInfo fileInfo = ctx.queryFileInfo("group1", "M00/00/00/wKgQWFzi2NqAX0S9AAAvtBhmGvg635.png");
            //M00/00/00/wKgQWF0A0JeAT7PKAAABhhBP-Og484.txt
            System.out.println(fileInfo);
            fileInfo = ctx.queryFileInfo("group1", "M00/00/00/wKgQWF0AzzuAaCaoAAAvtBhmGvg137.png");
            System.out.println(fileInfo);
            fileInfo = ctx.queryFileInfo("group1", "M00/00/00/wKgQWF0A0JeAT7PKAAABhhBP-Og484.txt");
            System.out.println(fileInfo);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
