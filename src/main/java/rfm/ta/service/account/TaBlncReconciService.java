package rfm.ta.service.account;

import common.utils.ToolUtil;
import org.fbi.dep.model.txn.Tia900012701;
import org.fbi.dep.model.txn.Toa900012701;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.dep.DepMsgSendAndRecv;

import java.util.ArrayList;
import java.util.List;

/**
 * （泰）余额对账Service
 * Created by Thinkpad on 2015/8/14.
 */
@Service
public class TaBlncReconciService {
    private static final Logger logger = LoggerFactory.getLogger(TaBlncReconciService.class);

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * 发送监管账号到SBS查询余额
     *
     * @param taRsAccList
     */
    @Transactional
    public List<Toa900012701> sendAndRecvRealTimeTxn900012701(List<TaRsAcc> taRsAccList) {
        try {
            int pageCount = 0; // 总页数
            int pageSize = 3; // 每页记录个数
            int totalCount = taRsAccList.size(); // 总条数
            int loopCount = 0; // 循环条件

            Tia900012701 tia900012701Temp = new Tia900012701();
            tia900012701Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS; // 渠道
            tia900012701Temp.header.TX_CODE=EnuTaTxCode.TRADE_2701.getCode(); // 交易代码

            if(totalCount % pageSize == 0){
                pageCount = totalCount / pageSize;
            } else {
                pageCount = totalCount / pageSize + 1;
            }

            List<Toa900012701> toaParas = null;
            List<Tia900012701.BodyDetail> details = null;
            Tia900012701.BodyDetail bodyDetail = null;
            for(int i=0; i<pageCount;i++){
                details = new ArrayList<Tia900012701.BodyDetail>();
                if(i == pageCount-1){ // 最后一页的处理
                    loopCount = totalCount;
                } else{
                    loopCount = (i+1)*pageSize;
                }
                for(int j=i*pageSize;j<loopCount;j++){
                    bodyDetail = new Tia900012701.BodyDetail();
                    bodyDetail.setACTNM(taRsAccList.get(j).getAccId());
                    details.add(bodyDetail);
                }

                tia900012701Temp.body.DETAILS = details;

                //通过MQ发送信息到DEP
                String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012701Temp);
                Toa900012701 toaPara=(Toa900012701) depMsgSendAndRecv.recvDepMessage(strMsgid);
                toaParas.add(toaPara);
            }

            return toaParas;
        } catch (Exception e) {
            logger.error("发送监管账号到SBS查询余额失败", e);
            throw new RuntimeException("发送监管账号到SBS查询余额失败", e);
        }
    }
}
