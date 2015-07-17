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
 * Time: 下午2:04
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
            throw new RuntimeException("没有查询到已监管账户！！");
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
            throw new RuntimeException("没有查询到未限制的已监管账户！请确认该账户已开启监管并未限制付款！");
        }
        return accountList.get(0);
    }

    /**
     * 判断账号是否已存在
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
     * 是否并发更新冲突
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
     * 查询所有未删除监管账户记录
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
     * 查询
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
     * 新增记录
     *
     * @param account
     */
    public void insertRecord(TaRsAcc account) {
        if (isExistInDb(account)) {
            throw new RuntimeException("该账号已存在，请重新录入！");
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
     * 通过主键更新
     */
    public int updateRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // 默认用户
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("账户并发更新冲突！ActPkid=" + account.getPkId());
        }
    }

    /**
     * 通过主键删除
     */
    public int deleteRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // 默认用户
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            account.setDeletedFlag(TaEnumArchivedFlag.ARCHIVED_FLAG1.getCode());
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("账户并发更新冲突！ActPkid=" + account.getPkId());
        }
    }

    /**
     * 发送泰安房产监管系统建立监管交易
     *
     * @param taRsAccPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxnMessage(TaRsAcc taRsAccPara) {
        try {
            String msgtxt = StringUtils.rightPad(taRsAccPara.getTradeId(), 4, ' ')       // 01   交易代码       4   2001
                          + StringUtils.rightPad(taRsAccPara.getBankId(), 2, ' ')        // 02   监管银行代码   2
                          + StringUtils.rightPad(taRsAccPara.getCityId(), 6, ' ')        // 03   城市代码       6
                          + StringUtils.rightPad(taRsAccPara.getBusiApplyId(), 14, ' ')  // 04   监管申请编号   14
                          + StringUtils.rightPad(taRsAccPara.getAccType(), 1, ' ')       // 05   帐户类别       1   0：预售监管户
                          + StringUtils.rightPad(taRsAccPara.getAccId(), 30, ' ')        // 06   监管专户账号   30
                          + StringUtils.rightPad(taRsAccPara.getAccName(), 150, ' ')     // 07   监管专户户名   150
                          + StringUtils.rightPad(taRsAccPara.getSerial(), 30, ' ')       // 08   流水号         30
                          + StringUtils.rightPad(taRsAccPara.getTradeDate(), 10, ' ')    // 09   日期           10  送系统日期即可
                          + StringUtils.rightPad(taRsAccPara.getBranchId(), 30, ' ')     // 10   网点号         30
                          + StringUtils.rightPad(taRsAccPara.getOperId(), 30, ' ')       // 11   柜员号         30
                          + StringUtils.rightPad(taRsAccPara.getInitiator(), 1, ' ');    // 12   发起方         1   1_监管银行

            List<SelectItem> taAccStatusListTemp=ptenudetailService.getTaAccStatusList();
            // 枚举变量在数据库中，启用标志
            taRsAccPara.setStatusFlag(taAccStatusListTemp.get(1).getValue().toString());
            updateRecord(taRsAccPara);

            //通过MQ发送信息到DEP
            String msgid = depService.sendDepMessage(DEP_CHANNEL_ID_UNIPAY, msgtxt);
            handle1001Message(depService.recvDepMessage(msgid));
        } catch (Exception e) {
            logger.error("MQ消息发送失败", e);
            throw new RuntimeException("MQ消息发送失败", e);
        }
    }

    /**
     * 处理泰安房产监管系统建立监管交易结果
     *
     * @param message
     */
    @Transactional
    public void handle1001Message(String message) {
        logger.info(" ========开始处理返回的100004消息==========");
        logger.info(message);

        /*TOA2001001 toa = TOA2001001.(message);
        if (toa != null) {
            String retcode_head = toa.INFO.RET_CODE;      //报文头返回码
            String req_sn = toa.INFO.REQ_SN;              //交易流水号
            String batch_sn = req_sn.substring(0, 11);    //解析交易流水号 得到批次号
            String batch_detl_sn = req_sn.substring(11);  //解析交易流水号 得到批次内的顺序号
            FipCutpaydetlExample example = new FipCutpaydetlExample();
            example.createCriteria().andBatchSnEqualTo(batch_sn).andBatchDetlSnEqualTo(batch_detl_sn)
                    .andArchiveflagEqualTo("0").andDeletedflagEqualTo("0");
            List<FipCutpaydetl> cutpaydetlList = cutpaydetlMapper.selectByExample(example);
            if (cutpaydetlList.size() != 1) {
                logger.error("未查找到对应的扣款记录。" + req_sn);
                throw new RuntimeException("未查找到对应的扣款记录。" + req_sn);
            }
            FipCutpaydetl record = cutpaydetlList.get(0);

            if ("0000".equals(retcode_head)) { //报文头“0000”：处理完成
                //已查找到数据库中对应的记录，可以进行日志记录
                T100004Toa.Body.BodyDetail bodyDetail = toa.BODY.RET_DETAILS.get(0);
                String retcode_detl = bodyDetail.RET_CODE;
                if ("0000".equals(retcode_detl)) { //交易成功的唯一标志
                    if (bodyDetail.ACCOUNT_NO.equals(record.getBiBankactno())) {
                        long recordAmt = record.getPaybackamt().multiply(new BigDecimal(100)).longValue();
                        long returnAmt = Integer.parseInt(bodyDetail.AMOUNT);
                        if (recordAmt == returnAmt) {
                            record.setBillstatus(BillStatus.CUTPAY_SUCCESS.getCode());
                            record.setDateBankCutpay(new Date());
                        } else {
                            logger.error("返回金额不匹配");
                            appendNewJoblog(record.getPkid(), "fip_cutpaydetl", "银联返回", "返回金额不匹配:" + returnAmt);
                        }
                    } else {
                        logger.error("帐号不匹配");
                        appendNewJoblog(record.getPkid(), "fip_cutpaydetl", "银联返回", "帐号不匹配" + bodyDetail.ACCOUNT_NO);
                    }
                } else {  //交易失败
                    record.setBillstatus(BillStatus.CUTPAY_FAILED.getCode());
                }
                record.setTxRetcode(String.valueOf(retcode_detl));
                record.setTxRetmsg(bodyDetail.ERR_MSG);
            } else if ("1002".equals(retcode_head)) {//无法查询到该交易，可以重发  关键！
                record.setBillstatus(BillStatus.RESEND_PEND.getCode());
                record.setTxRetcode(String.valueOf(retcode_head));
                record.setTxRetmsg(toa.INFO.ERR_MSG);
            } else { //待查询 (TODO: 未处理 0001，0002)
                record.setBillstatus(BillStatus.CUTPAY_QRY_PEND.getCode());
                record.setTxRetcode(String.valueOf(retcode_head));
                record.setTxRetmsg(toa.INFO.ERR_MSG);
            }
            record.setRecversion(record.getRecversion() + 1);
            cutpaydetlMapper.updateByPrimaryKey(record);
        } else { //
            throw new RuntimeException("该笔交易记录为空，可能已被删除。 " + message);
        }*/
        logger.debug(" ................. 处理返回的消息结束........");
    }

}
