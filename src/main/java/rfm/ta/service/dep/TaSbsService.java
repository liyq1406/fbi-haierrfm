package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.EnuTaFdcTxCode;
import rfm.ta.common.enums.EnuTaSbsTxCode;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.his.TaTxnSbsService;

import java.util.ArrayList;
import java.util.List;

/**����SBS���˽���
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaSbsService {
    private static final Logger logger = LoggerFactory.getLogger(TaSbsService.class);

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    // ����SBSϵͳ������˽���
    /**
     * ����SBSϵͳ������˽���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900010002(TaRsAccDtl taTxnFdcPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(taTxnFdcPara.getTxCode());
            taTxnSbsPara.setSpvsnAccId(taTxnFdcPara.getSpvsnAccId().trim());// �����˺�
            taTxnSbsPara.setGerlAccId(taTxnFdcPara.getGerlAccId().trim()); // �տ��˺�
            taTxnSbsPara.setTxAmt(taTxnFdcPara.getTxAmt());                // ���׽��
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn().substring(8,26));// ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(taTxnFdcPara.getTxDate());              // ��������
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));         // ����ʱ��
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());              // ��Ա��
            taTxnSbsService.insertRecord(taTxnSbsPara);

            Tia900010002 tia900010002Temp=new Tia900010002();
            tia900010002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;

            if( EnuTaFdcTxCode.TRADE_2002.getCode().equals(taTxnSbsPara.getTxCode())||
                EnuTaFdcTxCode.TRADE_2111.getCode().equals(taTxnSbsPara.getTxCode())||
                EnuTaFdcTxCode.TRADE_2211.getCode().equals(taTxnSbsPara.getTxCode())) {
                // ��һ���˻�����ܻ�(������ˣ���������������������
                tia900010002Temp.body.OUT_ACC_ID = taTxnSbsPara.getGerlAccId();
                tia900010002Temp.body.IN_ACC_ID = taTxnSbsPara.getSpvsnAccId();
            }else if(EnuTaFdcTxCode.TRADE_2011.getCode().equals(taTxnSbsPara.getTxCode())||
                      EnuTaFdcTxCode.TRADE_2102.getCode().equals(taTxnSbsPara.getTxCode())||
                      EnuTaFdcTxCode.TRADE_2202.getCode().equals(taTxnSbsPara.getTxCode())) {
                // �Ӽ�ܻ���һ���˻� (����������������ˣ��������ˣ�
                tia900010002Temp.body.OUT_ACC_ID = taTxnSbsPara.getSpvsnAccId();
                tia900010002Temp.body.IN_ACC_ID = taTxnSbsPara.getGerlAccId();
            }

            tia900010002Temp.body.TX_AMT=taTxnSbsPara.getTxAmt();
            tia900010002Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900010002Temp.body.TX_TIME=taTxnSbsPara.getTxTime();
            tia900010002Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900010002Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900010002Temp.header.TX_CODE=EnuTaSbsTxCode.TRADE_0002.getCode();

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("����ʧ��", e);
            throw new RuntimeException("����ʧ��", e);
        }
    }

    // ��ѯSBS�ռ��ܱ���
    /**
     * ��ѯSBS�ռ��ܱ���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012601(TaTxnFdc taTxnFdcPara) {
        try {
            Tia900012601 tia900012601Temp=new Tia900012601();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaFdcTxCode.TRADE_2601.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());      // ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(taTxnFdcPara.getTxDate());    // ��������
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());    // ��Ա��

            tia900012601Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012601Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012601Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012601Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012601Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012601Temp);
            TOA toaPara = depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("��SBS�ռ��������˲�ѯʧ��", e);
            throw new RuntimeException("��SBS�ռ��������˲�ѯʧ��", e);
        }
    }

    // ��ѯSBS�ռ������ˮ
    /**
     * ��ѯSBS�ռ������ˮ
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public List<TaRsAccDtl> sendAndRecvRealTimeTxn900012602(TaTxnFdc taTxnFdcPara) {
        try {
            List<TaRsAccDtl> taRsAccDtlListTemp = null;

            // ���ն�����ϸ��ѯ
            Tia900012602 tia900012602Temp=new Tia900012602();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaFdcTxCode.TRADE_2602.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());           // ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // ��������
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());         // ��Ա��

            tia900012602Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012602Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012602Temp.body.BEGNUM="0";
            tia900012602Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012602Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012602Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012602Temp);
            Toa900012602 toa900012602Temp = (Toa900012602) depMsgSendAndRecv.recvDepMessage(strMsgid);

            if (toa900012602Temp != null && toa900012602Temp.body != null) {
                if (EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900012602Temp.header.RETURN_CODE)) {
                    taRsAccDtlListTemp = new ArrayList<TaRsAccDtl>();
                    // ����״�����
                    taRsAccDtlListTemp.addAll(fromBodyDetailsToTaRsAccDtls(toa900012602Temp.body.DETAILS));
                    String totcnt = toa900012602Temp.body.TOTCNT;
                    String curcnt = toa900012602Temp.body.CURCNT;
                    if (!totcnt.isEmpty() && totcnt != "") {
                        //��Ϊ totcnt��ȫ�ֱ����������ڵ�һ�β�ѯ֮�󣬷���ڶ��ν���ʱtotcnt�Ͳ�Ϊ�գ�����Ҫ�ڵ�һ�η�����ʱ���
                        int m = Integer.parseInt(totcnt) / Integer.parseInt(curcnt);
                        int n = Integer.parseInt(totcnt) % Integer.parseInt(curcnt);
                        if(n!=0){
                            m=m+1;
                        }
                        for (int j = 1; j <= m; j++) {
                            tia900012602Temp.body.BEGNUM= j * Integer.parseInt(curcnt) + 1 + "";
                            strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012602Temp);
                            toa900012602Temp = (Toa900012602) depMsgSendAndRecv.recvDepMessage(strMsgid);
                            if (EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900012602Temp.header.RETURN_CODE)) {
                                taRsAccDtlListTemp.addAll(fromBodyDetailsToTaRsAccDtls(toa900012602Temp.body.DETAILS));
                                curcnt = toa900012602Temp.body.CURCNT;
                            }
                        }
                    }
                } else if("W107".equalsIgnoreCase(toa900012602Temp.header.RETURN_CODE)) { // δ�ҵ��κμ�¼
                    taRsAccDtlListTemp = new ArrayList<TaRsAccDtl>();
                } else {
                    MessageUtil.addError(toa900012602Temp.header.RETURN_MSG);
                }
            }
            return taRsAccDtlListTemp;
        } catch (Exception e) {
            logger.error("��SBS�ռ��������˲�ѯʧ��", e);
            throw new RuntimeException("��SBS�ռ��������˲�ѯʧ��", e);
        }
    }

    private List<TaRsAccDtl> fromBodyDetailsToTaRsAccDtls(List<Toa900012602.Body.BodyDetail> bodyDetailListPara){
        List<TaRsAccDtl> taRsAccDtlListTemp=new ArrayList<TaRsAccDtl>();
        for(Toa900012602.Body.BodyDetail bdUnit:bodyDetailListPara){
            TaRsAccDtl taRsAccDtlTemp=new TaRsAccDtl();
            taRsAccDtlTemp.setReqSn(bdUnit.FBTIDX);
            taRsAccDtlTemp.setSpvsnAccId(bdUnit.ACTNUM);
            taRsAccDtlTemp.setTxAmt(bdUnit.TXNAMT);
            taRsAccDtlTemp.setGerlAccId(bdUnit.BENACT);
            taRsAccDtlTemp.setTxDate(bdUnit.ERYTIM);
            taRsAccDtlListTemp.add(taRsAccDtlTemp);
        }
        return taRsAccDtlListTemp;
    }

    // ���ͼ���˺ŵ�SBS��ѯ���
    /**
     * ���ͼ���˺ŵ�SBS��ѯ���
     *
     * @param taRsAccList
     */
    @Transactional
    public List<Toa900012701> sendAndRecvRealTimeTxn900012701(List<TaRsAcc> taRsAccList) {
        try {
            int pageCount = 0; // ��ҳ��
            int pageSize = 50; // ÿҳ��¼����
            int totalCount = taRsAccList.size(); // ������
            int loopCount = 0; // ѭ������

            Tia900012701 tia900012701Temp = new Tia900012701();
            tia900012701Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS; // ����
            tia900012701Temp.header.TX_CODE=EnuTaFdcTxCode.TRADE_2701.getCode(); // ���״���

            if(totalCount % pageSize == 0){
                pageCount = totalCount / pageSize;
            } else {
                pageCount = totalCount / pageSize + 1;
            }

            List<Toa900012701> toaParas = new ArrayList<Toa900012701>();
            List<Tia900012701.BodyDetail> details = null;
            Tia900012701.BodyDetail bodyDetail = null;
            for(int i=0; i<pageCount;i++){
                details = new ArrayList<Tia900012701.BodyDetail>();
                if(i == pageCount-1){ // ���һҳ�Ĵ���
                    loopCount = totalCount;
                } else{
                    loopCount = (i+1)*pageSize;
                }
                for(int j=i*pageSize;j<loopCount;j++){
                    bodyDetail = new Tia900012701.BodyDetail();
                    bodyDetail.setACTNM(taRsAccList.get(j).getSpvsnAccId());
                    details.add(bodyDetail);
                }

                String strTotcnt="000000"+String.valueOf(ToolUtil.getIntIgnoreNull(details.size()));
                strTotcnt=strTotcnt.substring(strTotcnt.length()-6,strTotcnt.length());
                tia900012701Temp.body.TOTCNT =strTotcnt;
                tia900012701Temp.body.DETAILS = details;

                //ͨ��MQ������Ϣ��DEP
                String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012701Temp);
                Toa900012701 toaPara=(Toa900012701) depMsgSendAndRecv.recvDepMessage(strMsgid);
                toaParas.add(toaPara);
            }

            return toaParas;
        } catch (Exception e) {
            logger.error("���ͼ���˺ŵ�SBS��ѯ���ʧ��", e);
            throw new RuntimeException("���ͼ���˺ŵ�SBS��ѯ���ʧ��", e);
        }
    }
}
