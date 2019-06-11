package cn.wannengsou.filesystem.fdfs.client.protocol.tracker.request;

import cn.wannengsou.filesystem.fdfs.client.constant.CmdConstants;
import cn.wannengsou.filesystem.fdfs.client.protocol.BaseRequest;
import cn.wannengsou.filesystem.fdfs.client.protocol.ProtocolHead;

/**
 * 获取Group信息请求
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 15:06 <br/>
 */
public class GetGroupListRequest extends BaseRequest {
    public GetGroupListRequest() {
        head = new ProtocolHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
