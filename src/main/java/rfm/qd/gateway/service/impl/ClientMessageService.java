package rfm.qd.gateway.service.impl;

import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.CommonRes;
import rfm.qd.gateway.service.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-18
 * Time: 上午3:20
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ClientMessageService implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageService.class);

    public CommonRes transMsgToBean(String message) {
        return (CommonRes) BaseBean.toObject(CommonRes.class, message);
    }

    /**
     *
     * @param message
     * @return
     */
    @Override
    public String handleMessage(String message) {
        String responseMsg = null;
        CommonRes resBean = transMsgToBean(message);
        if ("0000".equalsIgnoreCase(resBean.head.RetCode)) {
            logger.info("【客户端】发送报文后接收响应：发送成功");
            responseMsg = "数据发送成功！";
        } else {
            logger.error("【客户端】发送报文后接收响应：" + resBean.head.RetCode + resBean.head.RetMsg);
            responseMsg = resBean.head.RetCode + resBean.head.RetMsg;
        }
        return responseMsg;
    }
}
