package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.PtenudetailService;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import pub.platform.utils.ToolUtil;
import rfm.ta.common.enums.TaEnumArchivedFlag;
import rfm.ta.gateway.dep.model.txn.TOA2001001;
import rfm.ta.repository.dao.TaRsAccMapper;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccExample;
import rfm.ta.service.dep.DepService;

import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: ����2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccService {
    private static final Logger logger = LoggerFactory.getLogger(TaAccService.class);
    private static String DEP_CHANNEL_ID_UNIPAY = "100";

    @Autowired
    private TaRsAccMapper accountMapper;
    @Autowired
    private PtenudetailService ptenudetailService;
    @Autowired
    private DepService depService;

    public TaRsAcc selectedRecordByPkid(String pkId) {
        return accountMapper.selectByPrimaryKey(pkId);
    }

    public TaRsAcc selectCanRecvAccountByNo(String accountNo) {
        TaRsAccExample accountExample = new TaRsAccExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString())
        .andAccIdEqualTo(accountNo);
        List<TaRsAcc> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ���Ѽ���˻�����");
        }
        return accountList.get(0);
    }

    public TaRsAcc selectCanPayAccountByNo(String accountNo) {
        TaRsAccExample accountExample = new TaRsAccExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString()).
                andAccIdEqualTo(accountNo);
        List<TaRsAcc> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ��δ���Ƶ��Ѽ���˻�����ȷ�ϸ��˻��ѿ�����ܲ�δ���Ƹ��");
        }
        return accountList.get(0);
    }

    /**
     * �ж��˺��Ƿ��Ѵ���
     *
     * @param account
     * @return
     */
    public boolean isExistInDb(TaRsAcc account) {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andAccIdEqualTo(account.getAccId());
        return accountMapper.countByExample(example) >= 1;
    }

    /**
     * �Ƿ񲢷����³�ͻ
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaRsAcc act) {
        TaRsAcc actt = accountMapper.selectByPrimaryKey(act.getPkId());
        if (!act.getRecVersion().equals(actt.getRecVersion())) {
            return false;
        }
        return true;
    }

    /**
     * ��ѯ����δɾ������˻���¼
     *
     * @return
     */
    public List<TaRsAcc> qryAllRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public TaRsAcc qryRecord(String pkid){
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByPrimaryKey(pkid);
    }
    public List<TaRsAcc> qryAllLockRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<TaRsAcc> qryAllMonitRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString());
        return accountMapper.selectByExample(example);
    }

    /**
     * ��ѯ
     */
    public List<TaRsAcc> selectedRecordsByCondition(String strAccTypePara, String strAccIdPara, String strAccNamePara) {
        TaRsAccExample example = new TaRsAccExample();
        example.clear();
        TaRsAccExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        if (ToolUtil.getStrIgnoreNull(strAccTypePara).trim().length()!=0) {
            rsActCrit.andAccTypeEqualTo(strAccTypePara);
        }
        if (ToolUtil.getStrIgnoreNull(strAccIdPara).trim().length()!=0) {
            rsActCrit.andAccIdLike("%"+strAccIdPara+"%");
        }
        if (ToolUtil.getStrIgnoreNull(strAccNamePara).trim().length()!=0) {
            rsActCrit.andAccNameLike("%" + strAccNamePara + "%");
        }
        return accountMapper.selectByExample(example);
    }

    /**
     * ������¼
     *
     * @param account
     */
    public void insertRecord(TaRsAcc account) {
        if (isExistInDb(account)) {
            throw new RuntimeException("���˺��Ѵ��ڣ�������¼�룡");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String strLastUpdTimeTemp= ToolUtil.getStrLastUpdTime();
            account.setCreatedBy(om.getOperatorId());
            account.setCreatedTime(strLastUpdTimeTemp);
            account.setLastUpdBy(om.getOperatorId());
            account.setLastUpdTime(strLastUpdTimeTemp);
            accountMapper.insertSelective(account);
        }
    }

    /**
     * ͨ����������
     */
    public int updateRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // Ĭ���û�
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }

    /**
     * ͨ������ɾ��
     */
    public int deleteRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // Ĭ���û�
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            account.setDeletedFlag(TaEnumArchivedFlag.ARCHIVED_FLAG1.getCode());
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }

    /**
     * ����̩���������ϵͳ������ܽ���
     *
     * @param taRsAccPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxnMessage(TaRsAcc taRsAccPara) {
        try {
            String msgtxt = StringUtils.rightPad(taRsAccPara.getTradeId(), 4, ' ')       // 01   ���״���       4   2001
                          + StringUtils.rightPad(taRsAccPara.getBankId(), 2, ' ')        // 02   ������д���   2
                          + StringUtils.rightPad(taRsAccPara.getCityId(), 6, ' ')        // 03   ���д���       6
                          + StringUtils.rightPad(taRsAccPara.getBusiApplyId(), 14, ' ')  // 04   ���������   14
                          + StringUtils.rightPad(taRsAccPara.getAccType(), 1, ' ')       // 05   �ʻ����       1   0��Ԥ�ۼ�ܻ�
                          + StringUtils.rightPad(taRsAccPara.getAccId(), 30, ' ')        // 06   ���ר���˺�   30
                          + StringUtils.rightPad(taRsAccPara.getAccName(), 150, ' ')     // 07   ���ר������   150
                          + StringUtils.rightPad(taRsAccPara.getSerial(), 30, ' ')       // 08   ��ˮ��         30
                          + StringUtils.rightPad(taRsAccPara.getTradeDate(), 10, ' ')    // 09   ����           10  ��ϵͳ���ڼ���
                          + StringUtils.rightPad(taRsAccPara.getBranchId(), 30, ' ')     // 10   �����         30
                          + StringUtils.rightPad(taRsAccPara.getOperId(), 30, ' ')       // 11   ��Ա��         30
                          + StringUtils.rightPad(taRsAccPara.getInitiator(), 1, ' ');    // 12   ����         1   1_�������

            List<SelectItem> taAccStatusListTemp=ptenudetailService.getTaAccStatusList();
            // ö�ٱ��������ݿ��У����ñ�־
            taRsAccPara.setStatusFlag(taAccStatusListTemp.get(1).getValue().toString());
            updateRecord(taRsAccPara);

            //ͨ��MQ������Ϣ��DEP
            String msgid = depService.sendDepMessage(DEP_CHANNEL_ID_UNIPAY, msgtxt);
            handle1001Message(depService.recvDepMessage(msgid));
        } catch (Exception e) {
            logger.error("MQ��Ϣ����ʧ��", e);
            throw new RuntimeException("MQ��Ϣ����ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������ܽ��׽��
     *
     * @param message
     */
    @Transactional
    public void handle1001Message(String message) {
        logger.info(" ========��ʼ�����ص�100004��Ϣ==========");
        logger.info(message);

        /*TOA2001001 toa = TOA2001001.(message);
        if (toa != null) {
            String retcode_head = toa.INFO.RET_CODE;      //����ͷ������
            String req_sn = toa.INFO.REQ_SN;              //������ˮ��
            String batch_sn = req_sn.substring(0, 11);    //����������ˮ�� �õ����κ�
            String batch_detl_sn = req_sn.substring(11);  //����������ˮ�� �õ������ڵ�˳���
            FipCutpaydetlExample example = new FipCutpaydetlExample();
            example.createCriteria().andBatchSnEqualTo(batch_sn).andBatchDetlSnEqualTo(batch_detl_sn)
                    .andArchiveflagEqualTo("0").andDeletedflagEqualTo("0");
            List<FipCutpaydetl> cutpaydetlList = cutpaydetlMapper.selectByExample(example);
            if (cutpaydetlList.size() != 1) {
                logger.error("δ���ҵ���Ӧ�Ŀۿ��¼��" + req_sn);
                throw new RuntimeException("δ���ҵ���Ӧ�Ŀۿ��¼��" + req_sn);
            }
            FipCutpaydetl record = cutpaydetlList.get(0);

            if ("0000".equals(retcode_head)) { //����ͷ��0000�����������
                //�Ѳ��ҵ����ݿ��ж�Ӧ�ļ�¼�����Խ�����־��¼
                T100004Toa.Body.BodyDetail bodyDetail = toa.BODY.RET_DETAILS.get(0);
                String retcode_detl = bodyDetail.RET_CODE;
                if ("0000".equals(retcode_detl)) { //���׳ɹ���Ψһ��־
                    if (bodyDetail.ACCOUNT_NO.equals(record.getBiBankactno())) {
                        long recordAmt = record.getPaybackamt().multiply(new BigDecimal(100)).longValue();
                        long returnAmt = Integer.parseInt(bodyDetail.AMOUNT);
                        if (recordAmt == returnAmt) {
                            record.setBillstatus(BillStatus.CUTPAY_SUCCESS.getCode());
                            record.setDateBankCutpay(new Date());
                        } else {
                            logger.error("���ؽ�ƥ��");
                            appendNewJoblog(record.getPkid(), "fip_cutpaydetl", "��������", "���ؽ�ƥ��:" + returnAmt);
                        }
                    } else {
                        logger.error("�ʺŲ�ƥ��");
                        appendNewJoblog(record.getPkid(), "fip_cutpaydetl", "��������", "�ʺŲ�ƥ��" + bodyDetail.ACCOUNT_NO);
                    }
                } else {  //����ʧ��
                    record.setBillstatus(BillStatus.CUTPAY_FAILED.getCode());
                }
                record.setTxRetcode(String.valueOf(retcode_detl));
                record.setTxRetmsg(bodyDetail.ERR_MSG);
            } else if ("1002".equals(retcode_head)) {//�޷���ѯ���ý��ף������ط�  �ؼ���
                record.setBillstatus(BillStatus.RESEND_PEND.getCode());
                record.setTxRetcode(String.valueOf(retcode_head));
                record.setTxRetmsg(toa.INFO.ERR_MSG);
            } else { //����ѯ (TODO: δ���� 0001��0002)
                record.setBillstatus(BillStatus.CUTPAY_QRY_PEND.getCode());
                record.setTxRetcode(String.valueOf(retcode_head));
                record.setTxRetmsg(toa.INFO.ERR_MSG);
            }
            record.setRecversion(record.getRecversion() + 1);
            cutpaydetlMapper.updateByPrimaryKey(record);
        } else { //
            throw new RuntimeException("�ñʽ��׼�¼Ϊ�գ������ѱ�ɾ���� " + message);
        }*/
        logger.debug(" ................. �����ص���Ϣ����........");
    }

}
