package cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request;

import cn.wannengsou.filesystem.fdfs.client.constant.CmdConstants;
import cn.wannengsou.filesystem.fdfs.client.protocol.BaseRequest;
import cn.wannengsou.filesystem.fdfs.client.protocol.ProtocolHead;

/**
 * 获取存储节点请求
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 15:23 <br/>
 */
public class GetStorageNodeRequest extends BaseRequest {
    private static final byte withoutGroupCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;

    /**
     * 获取存储节点
     */
    public GetStorageNodeRequest() {
        super();
        this.head = new ProtocolHead(withoutGroupCmd);
    }
}
