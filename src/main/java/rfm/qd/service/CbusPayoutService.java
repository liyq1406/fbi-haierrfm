package rfm.qd.service;

import rfm.qd.common.constant.*;
import rfm.qd.gateway.cbus.domain.txn.QDJG03Res;
import rfm.qd.gateway.cbus.domain.txn.QDJG04Res;
import rfm.qd.gateway.service.CbusTxnService;
import rfm.qd.repository.dao.QdRsPayoutMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.repository.model.QdRsPayoutExample;
import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.security.OperatorManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: ����3:46
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CbusPayoutService {

    private static Logger logger = LoggerFactory.getLogger(CbusPayoutService.class);

    @Autowired
    private QdRsPayoutMapper qdRsPayoutMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ExpensesPlanService expensesPlanService;
    @Autowired
    private RsAccDetailService rsAccDetailService;
    @Autowired
    private CbusTxnService cbusTxnService;

    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public int updateRsPayout(QdRsPayout qdRsPayout) {
        QdRsPayout originRecord = qdRsPayoutMapper.selectByPrimaryKey(qdRsPayout.getPkId());
        if (!originRecord.getModificationNum().equals(qdRsPayout.getModificationNum())) {
            logger.info("��¼���°汾�ţ�" + qdRsPayout.getModificationNum());
            logger.info("��¼ԭ�汾�ţ�" + originRecord.getModificationNum());
            throw new RuntimeException("��¼�������³�ͻ�������ԣ�");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            qdRsPayout.setLastUpdBy(operId);
            qdRsPayout.setLastUpdDate(new Date());
            qdRsPayout.setModificationNum(qdRsPayout.getModificationNum() + 1);
            return qdRsPayoutMapper.updateByPrimaryKey(qdRsPayout);
        }
    }

    @Transactional
    public int updateRsPayoutToExec(QdRsPayout qdRsPayout) throws Exception {

        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        String operName = om.getOperatorName();
        String operDate = sdf10.format(new Date());
        qdRsPayout.setExecUserId(operId);
        qdRsPayout.setExecUserName(operName);
        qdRsPayout.setExecDate(operDate);
        qdRsPayout.setTradeDate(operDate);
        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_SUCCESS.getCode());
        qdRsPayout.setWorkResult(WorkResult.COMMIT.getCode());
        qdRsPayout.setSerial(commonMapper.selectMaxPayoutSerial());
        qdRsPayout.setBankSerial(qdRsPayout.getSerial());
        // ��Ӧ�ƻ��ۿ�
        QdRsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(qdRsPayout.getBusinessNo());
        planCtrl.setAvAmount(planCtrl.getAvAmount().subtract(qdRsPayout.getApAmount()));

        //======== ��ʼ���� ===========
        //--------------------------------------------------
        // ����������ϸ
        QdRsAccDetail accDetail = new QdRsAccDetail();
        accDetail.setAccountCode(qdRsPayout.getPayAccount());
        accDetail.setAccountName(qdRsPayout.getPayCompanyName());
        accDetail.setToAccountCode(qdRsPayout.getRecAccount());
        accDetail.setToAccountName(qdRsPayout.getRecCompanyName());
        accDetail.setToHsBankName(qdRsPayout.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// ֧��
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setTradeAmt(qdRsPayout.getApAmount());
        accDetail.setBalance(new BigDecimal("0.00"));
        accDetail.setLocalSerial(qdRsPayout.getSerial());
        accDetail.setBankSerial(qdRsPayout.getBankSerial());
        accDetail.setTradeType(TradeType.PLAN_PAYOUT.getCode());
        accDetail.setPlanCtrlNo(qdRsPayout.getBusinessNo());
        accDetail.setRemark(qdRsPayout.getRemark());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        //-----------------------------------------------------
        // �����˻���� �Ϳ������

        // �Ƿ���ļ���
        if ("cbus".equals(PropertyManager.getProperty("bank.act.flag"))) {
            //  ����ת��
            if ("10".equals(qdRsPayout.getTransType())) {
                try {
                    QDJG03Res res03 = cbusTxnService.qdjg03payAmtInBank(qdRsPayout.getPayAccount(), qdRsPayout.getRecAccount(),
                            String.format("%.2f", qdRsPayout.getPlAmount()), qdRsPayout.getPurpose());

                    if ("00".equals(res03.getHeader().getRtnCode())) {
                        expensesPlanService.updatePlanCtrl(planCtrl);
                        rsAccDetailService.insertAccDetail(accDetail);
                        qdRsPayout.setBankSerial(res03.serialNo);
                        updateRsPayout(qdRsPayout);
                        return 1;
                    } else if ("99".equals(res03.getHeader().getRtnCode())) {
                        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        qdRsPayout.setWorkResult(WorkResult.PASS.getCode());
                        updateRsPayout(qdRsPayout);
                        throw new RuntimeException("����ʧ�ܣ�" + res03.rtnMsg);
                    } else {
                        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        qdRsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                        updateRsPayout(qdRsPayout);
                        throw new RuntimeException("���׽��������" + res03.rtnMsg);
                    }
                } catch (Exception e) {
                    qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                    qdRsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                    updateRsPayout(qdRsPayout);
                    throw new RuntimeException("���׽��������" + e.getMessage() == null ? "" : e.getMessage());
                }
            }
            //  ����ת��
            /*
           String sndToBkNo, String rmtrNameFl, String rmtrAcctNo,
           String payeeNameFl, String payeeFlAcctNo, String rmtAmt, String rmtPurp
            */
            if ("20".equals(qdRsPayout.getTransType())) {
                try {
                    QDJG04Res res04 = cbusTxnService.qdjg04payAmtBtwnBank(qdRsPayout.getRecBankCode(),
                            qdRsPayout.getPayCompanyName(), qdRsPayout.getPayAccount(),
                            qdRsPayout.getRecCompanyName(), qdRsPayout.getRecAccount(),
                            String.format("%.2f", qdRsPayout.getPlAmount()), qdRsPayout.getPurpose(),
                            qdRsPayout.getVoucherType(), qdRsPayout.getDocNo(),
                            StringUtils.isEmpty(qdRsPayout.getRemark()) ? qdRsPayout.getPurpose() : qdRsPayout.getRemark());
                    if ("00".equals(res04.getHeader().getRtnCode())) {
                        expensesPlanService.updatePlanCtrl(planCtrl);
                        rsAccDetailService.insertAccDetail(accDetail);
                        qdRsPayout.setBankSerial(res04.txnSerailNo);
                        qdRsPayout.setWfInstanceId(res04.fmTrntAmtSqNo);
                        updateRsPayout(qdRsPayout);
                        return 1;
                    } else if ("99".equals(res04.getHeader().getRtnCode())) {
                        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        qdRsPayout.setWorkResult(WorkResult.PASS.getCode());
                        updateRsPayout(qdRsPayout);
                        throw new RuntimeException("����ʧ�ܣ�" + res04.rtnMsg);
                    } else {
                        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        qdRsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                        updateRsPayout(qdRsPayout);
                        throw new RuntimeException("���׽��������" + res04.rtnMsg);
                    }
                } catch (Exception e) {
                    qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                    qdRsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                    updateRsPayout(qdRsPayout);
                    throw new RuntimeException("���׽��������" + e.getMessage() == null ? "" : e.getMessage());
                }
            }
        } else {
            expensesPlanService.updatePlanCtrl(planCtrl);
            rsAccDetailService.insertAccDetail(accDetail);
            updateRsPayout(qdRsPayout);
            return 1;
        }

        return -1;
    }

    public List<QdRsPayout> selectRecordsByWorkResult(String workResultCode) {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(workResultCode);
        example.setOrderByClause(" CREATED_DATE DESC ");
        return qdRsPayoutMapper.selectByExample(example);
    }

    public List<QdRsPayout> qryPrintVchrPayouts() {
        QdRsPayoutExample example = new QdRsPayoutExample();
        List<String> printWRnums = new ArrayList<String>();
        printWRnums.add(WorkResult.COMMIT.getCode());
        printWRnums.add(WorkResult.SENT.getCode());
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultIn(printWRnums);
        example.setOrderByClause(" CREATED_DATE desc ");
        return qdRsPayoutMapper.selectByExample(example);
    }
}
