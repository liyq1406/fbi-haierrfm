package rfm.ta.service.account;

import org.fbi.dep.model.txn.Tia9901002;
import org.fbi.dep.model.txn.Toa9901001;
import org.fbi.dep.model.txn.Toa9901002;
import org.fbi.dep.model.txn.Tia9901001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.PtenudetailService;
import platform.service.SystemService;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.security.OperatorManager;
import common.utils.ToolUtil;
import rfm.ta.common.enums.*;
import rfm.ta.repository.dao.TaRsAccMapper;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccExample;
import rfm.ta.service.dep.DepService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccService {
    private static final Logger logger = LoggerFactory.getLogger(TaAccService.class);

    @Autowired
    private TaRsAccMapper accountMapper;
    @Autowired
    private PtenudetailService ptenudetailService;
    @Autowired
    private DepService depService;

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
            account.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG1.getCode());
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
    public void sendAndRecvRealTimeTxn9901001(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901001 tia9901001Temp=new Tia9901001();
            tia9901001Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901001Temp.header.TX_CODE=EnuTaTxCode.TRADE_1001.getCode();                     // 01   交易代码       4   1001
            tia9901001Temp.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();                      // 02   监管银行代码   2
            tia9901001Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   城市代码       6
            tia9901001Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   监管申请编号   14
            tia9901001Temp.body.ACC_TYPE=taRsAccPara.getAccType();                                // 05   帐户类别       1   0：预售监管户
            tia9901001Temp.body.ACC_ID=taRsAccPara.getAccId();                                    // 06   监管专户账号    30
            tia9901001Temp.body.ACC_NAME=taRsAccPara.getAccName();                                // 07   监管专户户名   150
            tia9901001Temp.header.REQ_SN=taRsAccPara.getReqSn();                                  // 08   流水号         30
            tia9901001Temp.body.TX_DATE=ToolUtil.getStrLastUpdDate() ;                            // 09   日期           10  送系统日期即可
            tia9901001Temp.body.BRANCH_ID=ToolUtil.getOperatorManager().getOperator().getDeptid();// 10   网点号         30
            tia9901001Temp.header.USER_ID=ToolUtil.getOperatorManager().getOperatorId();          // 11   柜员号         30
            tia9901001Temp.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode() ;                 // 12   发起方         1   1_监管银行
            //通过MQ发送信息到DEP
            String strMsgid=depService.sendDepMessage(tia9901001Temp);
            Toa9901001 toaPara=(Toa9901001)depService.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setPreSalePerName(toaPara.body.PRE_SALE_PER_NAME);
                taRsAccPara.setPreSaleProAddr(toaPara.body.PRE_SALE_PRO_ADDR);
                taRsAccPara.setPreSaleProName(toaPara.body.PRE_SALE_PRO_NAME);
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_SUPV.getCode());
                updateRecord(taRsAccPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("建立监管失败", e);
            throw new RuntimeException("建立监管失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统解除监管交易
     *
     * @param taRsAccPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9901002(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901002 tia9901002Temp=new Tia9901002() ;
            tia9901002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901002Temp.header.TX_CODE=EnuTaTxCode.TRADE_1002.getCode();                     // 01   交易代码       4   1002
            tia9901002Temp.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();                      // 02   监管银行代码   2
            tia9901002Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   城市代码       6
            tia9901002Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   终止证明编号  14
            tia9901002Temp.body.ACC_ID=taRsAccPara.getAccId();                                    // 05   监管专户账号  30
            tia9901002Temp.body.ACC_NAME=taRsAccPara.getAccName();                                // 06   监管专户户名  150
            tia9901002Temp.header.REQ_SN=taRsAccPara.getReqSn();                                  // 07   流水号        30
            tia9901002Temp.body.TX_DATE=ToolUtil.getStrLastUpdDate() ;                            // 08   日期          10  送系统日期即可
            tia9901002Temp.body.BRANCH_ID=ToolUtil.getOperatorManager().getOperator().getDeptid();// 09   网点号        30
            tia9901002Temp.header.USER_ID=ToolUtil.getOperatorManager().getOperatorId();          // 10   柜员号        30
            tia9901002Temp.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode() ;                 // 11   发起方        1   1_监管银行
            //通过MQ发送信息到DEP
            String strMsgid=depService.sendDepMessage(tia9901002Temp);
            Toa9901002 toaPara=(Toa9901002)depService.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_CANCL.getCode());
                updateRecord(taRsAccPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                updateRecord(taRsAccPara);
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("解除监管失败", e);
            throw new RuntimeException("解除监管失败", e);
        }
    }
}
