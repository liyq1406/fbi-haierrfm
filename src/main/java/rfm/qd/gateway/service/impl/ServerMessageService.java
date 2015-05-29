package rfm.qd.gateway.service.impl;

import rfm.qd.common.constant.AccountStatus;
import rfm.qd.common.constant.ContractStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.gateway.cbus.domain.txn.QDJG01Res;
import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.CommonRes;
import rfm.qd.gateway.domain.T000.T0001Req;
import rfm.qd.gateway.domain.T000.T0001Res;
import rfm.qd.gateway.domain.T000.T0002Req;
import rfm.qd.gateway.domain.T000.T0002Res;
import rfm.qd.gateway.domain.T200.*;
import rfm.qd.gateway.service.BiDbService;
import rfm.qd.gateway.service.CbusTxnService;
import rfm.qd.gateway.service.IMessageService;
import rfm.qd.gateway.utils.BiRtnCode;
import rfm.qd.gateway.utils.StringUtil;
import rfm.qd.repository.model.*;
import rfm.qd.service.account.CbusFdcActtxnService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.advance.utils.PropertyManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-18
 * Time: ����2:27
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ServerMessageService implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ServerMessageService.class);
    @Autowired
    private BiDbService biDbService;
    @Autowired
    private CbusTxnService cbusTxnService;
    @Autowired
    private CbusFdcActtxnService cbusFdcActtxnService;

    @Override
    public synchronized String handleMessage(String message) {

        String responseMsg = null;
        // �õ������룬���ݽ����뽫xmlת������Ӧ�Ľӿڶ���
        String opCode = StringUtil.getSubstrBetweenStrs(message, "<OpCode>", "</OpCode>");
        int nOpCode = Integer.parseInt(opCode);

        /*   0001-0002-2001-2002-2003-2006-2007-2008
        *    TODO �ֶδ�ȷ�� �ݶ�������ϸ��ע�ֶΣ�����;��
        */
        switch (nOpCode) {
            case 1:
                T0001Req t0001Req = (T0001Req) BaseBean.toObject(T0001Req.class, message);
                logger.info(t0001Req.head.OpDate + t0001Req.head.OpTime + "==���ս��ף�" + t0001Req.head.OpCode);

                List<RsAccount> accountList = biDbService.selectAccountByCodeName(t0001Req.param.Acct, t0001Req.param.AcctName);

                T0001Res t0001Res = new T0001Res();

                if (accountList.isEmpty()) {
                    t0001Res.head.RetCode = BiRtnCode.BI_RTN_CODE_NO_ACCOUNT.getCode();
                    t0001Res.head.RetMsg = "û�в鵽�˻��������˻���Ϣ��";
                } else {
                    //RsAccount account = accountList.get(0);
                    // CBUS �˻����  2012-04-26
                    QDJG01Res qdjg01Res = null;
                    try {
                        if ("cbus".equals(PropertyManager.getProperty("bank.act.flag"))) {
                            qdjg01Res = cbusTxnService.qdjg01QryActbal(t0001Req.param.Acct);
                            t0001Res.param.Balance = StringUtil.toBiformatAmt(new BigDecimal(qdjg01Res.actbal));
                            t0001Res.param.UsableBalance = StringUtil.toBiformatAmt(new BigDecimal(qdjg01Res.avabal));
                        } else {
                            RsAccount account = accountList.get(0);
                            t0001Res.param.Balance = StringUtil.toBiformatAmt(account.getBalance());
                            t0001Res.param.UsableBalance = StringUtil.toBiformatAmt(account.getBalanceUsable());
                        }
                    } catch (Exception e) {
                        t0001Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t0001Res.head.RetMsg = e.getMessage();
                    }

                }

                responseMsg = t0001Res.toFDCDatagram();
                break;
            case 2:
                T0002Req t0002Req = (T0002Req) BaseBean.toObject(T0002Req.class, message);
                logger.info(t0002Req.head.OpDate + t0002Req.head.OpTime + "==���ս��ף�" + t0002Req.head.OpCode);

                T0002Res t0002Res = new T0002Res();

                if (!biDbService.isAccountExistByCodeName(t0002Req.param.Acct, t0002Req.param.AcctName)) {
                    t0002Res.head.RetCode = BiRtnCode.BI_RTN_CODE_NO_ACCOUNT.getCode();
                    t0002Res.head.RetMsg = "û�в鵽�˻��������˻���Ϣ��";
                    responseMsg = t0002Res.toFDCDatagram();
                    break;
                }
                // ����
                if ("cbus".equals(PropertyManager.getProperty("bank.act.txn.flag"))) {
                    List<CbsAccTxn> accTxnList = cbusFdcActtxnService.qryAccTxns(t0002Req.param.Acct, t0002Req.param.BeginDate,
                            t0002Req.param.EndDate);
                    if (!accTxnList.isEmpty()) {
                        t0002Res.param.DetailNum = String.valueOf(accTxnList.size());
                        for (CbsAccTxn accTxn : accTxnList) {
                            T0002Res.Param.Record record = T0002Res.getRecord();
                            record.Date = accTxn.getTxnDate();
                            record.Time = accTxn.getTxnTime();

                            /*record.Flag = accDetail.getInoutFlag();
                            record.Type = accDetail.getTradeType();
                            if()*/
                            String creditAmt = accTxn.getCreditAmt();
                            String debitAmt = accTxn.getDebitAmt();
                            double camt = StringUtils.isEmpty(creditAmt) ? 0.00 : Double.parseDouble(creditAmt);
                            double damt = StringUtils.isEmpty(debitAmt) ? 0.00 : Double.parseDouble(debitAmt);
                            if(camt > 0) {
                                // ����
                                record.Flag = "1";
                                record.Amt = StringUtil.toBiformatAmt(new BigDecimal(creditAmt));
                            } else if(damt > 0) {
                                // ֧��
                                record.Flag = "0";
                                record.Amt = StringUtil.toBiformatAmt(new BigDecimal(debitAmt));
                            } else {
                                record.Amt = "000";
                                record.Flag = "0";
                            }
                            record.Type = "09";
                            // 0-֧�� 1-����
                            record.ContractNum = "";
                            /*record.ToAcct = accDetail.getToAccountCode();
                            record.ToAcctName = accDetail.getToAccountName();
                            record.ToBankName = accDetail.getToHsBankName();*/
                            record.Purpose = accTxn.getRemark();
                            t0002Res.param.recordList.add(record);
                        }
                    } else {
                        t0002Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t0002Res.head.RetMsg = "������ϸ��¼Ϊ�գ�";
                    }
                }
                // �Ǻ���
                else {
                    List<RsAccDetail> accDetailList = biDbService.selectAccDetailsByCodeNameDate(t0002Req.param.Acct,
                            t0002Req.param.AcctName, StringUtil.transDate8ToDate10(t0002Req.param.BeginDate),
                            StringUtil.transDate8ToDate10(t0002Req.param.EndDate));
                    if (!accDetailList.isEmpty()) {
                        t0002Res.param.DetailNum = String.valueOf(accDetailList.size());
                        for (RsAccDetail accDetail : accDetailList) {
                            T0002Res.Param.Record record = T0002Res.getRecord();
                            record.Date = StringUtil.transDate10ToDate8(accDetail.getTradeDate());
                            record.Time = "121212";

                            record.Flag = accDetail.getInoutFlag();
                            record.Type = accDetail.getTradeType();
                            record.Amt = StringUtil.toBiformatAmt(accDetail.getTradeAmt());
                            record.ContractNum = accDetail.getContractNo();
                            record.ToAcct = accDetail.getToAccountCode();
                            record.ToAcctName = accDetail.getToAccountName();
                            record.ToBankName = accDetail.getToHsBankName();
                            record.Purpose = TradeType.HOUSE_INCOME.valueOfAlias(accDetail.getTradeType()).getTitle();
                            t0002Res.param.recordList.add(record);
                        }
                    } else {
                        t0002Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t0002Res.head.RetMsg = "������ϸ��¼Ϊ�գ�";
                    }
                }

                responseMsg = t0002Res.toFDCDatagram();
                break;

            case 2001:
                T2001Req t2001Req = (T2001Req) BaseBean.toObject(T2001Req.class, message);
                logger.info(t2001Req.head.OpDate + t2001Req.head.OpTime + "==���ս��ף�" + t2001Req.head.OpCode);

                List<RsAccount> initAccountList = biDbService.selectAccountByCodeName(t2001Req.param.Acct, t2001Req.param.AcctName);

                T2001Res t2001Res = new T2001Res();

                if (!initAccountList.isEmpty()) {
                    RsAccount account = initAccountList.get(0);
                    account.setStatusFlag(AccountStatus.WATCH.getCode());
                    account.setAgrnum(t2001Req.param.AgrNum);
                    if (biDbService.updateAccount(account) != 1) {
                        t2001Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t2001Res.head.RetMsg = "���� ʧ�ܣ������ԡ�";
                    }
                } else {
                    t2001Res.head.RetCode = BiRtnCode.BI_RTN_CODE_NO_ACCOUNT.getCode();
                    t2001Res.head.RetMsg = "û�в鵽�˻��������˻���Ϣ��";
                }
                responseMsg = t2001Res.toFDCDatagram();
                break;
            case 2002:
                T2002Req t2002Req = (T2002Req) BaseBean.toObject(T2002Req.class, message);
                logger.info(t2002Req.head.OpDate + t2002Req.head.OpTime + "==���ս��ף�" + t2002Req.head.OpCode);

                List<RsAccount> limitAccountList = biDbService.selectAccountByCodeName(t2002Req.param.Acct, t2002Req.param.AcctName);

                T2002Res t2002Res = new T2002Res();

                if (!limitAccountList.isEmpty()) {
                    RsAccount account = limitAccountList.get(0);
                    account.setLimitFlag(t2002Req.param.LockFlag);
                    if (biDbService.updateAccount(account) != 1) {
                        t2002Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t2002Res.head.RetMsg = "����ʧ�ܣ������ԡ�";
                    }
                } else {
                    t2002Res.head.RetCode = BiRtnCode.BI_RTN_CODE_NO_ACCOUNT.getCode();
                    t2002Res.head.RetMsg = "û�в鵽�˻��������˻���Ϣ��";
                }
                responseMsg = t2002Res.toFDCDatagram();
                break;
            case 2003:
                //  ��ͬ�ӿڱ�
                T2003Req t2003Req = (T2003Req) BaseBean.toObject(T2003Req.class, message);
                logger.info(t2003Req.head.OpDate + t2003Req.head.OpTime + "==���ս��ף�" + t2003Req.head.OpCode);

                BiContract contract = new BiContract();
                contract.setContractNo(t2003Req.param.ContractNum);                                     // ��ͬ��
                contract.setAccountCode(t2003Req.param.Acct);                                               // ����˺�
                contract.setAccountName(t2003Req.param.AcctName);                                     // ����˻�����
                contract.setBuyerName(t2003Req.param.BuyerName);                                     // ����������
                contract.setBuyerAccCode(t2003Req.param.BuyerAcct);                                   // �����˸��˽����˺�
                contract.setBuyerBankName(t2003Req.param.BuyerBankName);                      // �������˻�������
                contract.setBuyerCertType(t2003Req.param.BuyerIDType);                              //������֤������
                contract.setBuyerCertNo(t2003Req.param.BuyerIDCode);                                // ������֤������
                contract.setTotalAmt(new BigDecimal(t2003Req.param.TotalAmt).divide(new BigDecimal(100)));                   //�����ܼ�
                contract.setEarnestAmt(new BigDecimal(t2003Req.param.Deposit).divide(new BigDecimal(100)));                  // ����
                contract.setFirstAmt(new BigDecimal(t2003Req.param.DownPay).divide(new BigDecimal(100)));                    // �׸���
                contract.setLoanAmt(new BigDecimal(t2003Req.param.Mortgage).divide(new BigDecimal(100)));                  // ����
                contract.setHouseAddress(t2003Req.param.HouseAddress);                            // ���ݵ�ַ
                contract.setHouseType(t2003Req.param.HouseType);                                      // ��������
                contract.setHouseNo(t2003Req.param.HouseNO);                                            // ����¥����Ϣ
                contract.setOverAmt(new BigDecimal(t2003Req.param.OverAmt).divide(new BigDecimal(100)));                   // ��������ۿ�
                contract.setTreAccName(t2003Req.param.TreasuryName);                             // �Ͻɲ���ר���˻���
                contract.setTreAccCode(t2003Req.param.TreasuryAcct);                                // �Ͻɲ���ר���˺�
                contract.setTreBankName(t2003Req.param.TreasuryBankName);                  // ����ר��������

                T2003Res t2003Res = new T2003Res();

                if (biDbService.updateDBContractByBiContract(contract) != 1) {
                    t2003Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                    t2003Res.head.RetMsg = "����ʧ�ܣ������ԡ�";
                }
                responseMsg = t2003Res.toFDCDatagram();
                break;
            case 2006:     // �����˻��������֪ͨ
                T2006Req t2006Req = (T2006Req) BaseBean.toObject(T2006Req.class, message);
                logger.info(t2006Req.head.OpDate + t2006Req.head.OpTime + "==���ս��ף�" + t2006Req.head.OpCode);

                List<RsAccount> overAccountList = biDbService.selectAccountByCodeName(t2006Req.param.Acct, t2006Req.param.AcctName);

                T2006Res t2006Res = new T2006Res();

                if (!overAccountList.isEmpty()) {
                    RsAccount account = overAccountList.get(0);
                    account.setStatusFlag(AccountStatus.CLOSE.getCode());
                    if (biDbService.updateAccount(account) == 1) {
                        t2006Res.param.CancelDate = SystemService.getSdfdate8();
                        t2006Res.param.CancelTime = SystemService.getSdftime6();
                        t2006Res.param.FinalBalance = StringUtil.toBiformatAmt(account.getBalance());
                    } else {
                        t2006Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t2006Res.head.RetMsg = "����ʧ�ܣ������ԡ�";
                    }
                } else {
                    t2006Res.head.RetCode = BiRtnCode.BI_RTN_CODE_NO_ACCOUNT.getCode();
                    t2006Res.head.RetMsg = "û�в鵽�˻��������˻���Ϣ��";
                }

                responseMsg = t2006Res.toFDCDatagram();
                break;
            case 2007: //Ԥ�۷���ͬ������ֹ֪ͨ
                T2007Req t2007Req = (T2007Req) BaseBean.toObject(T2007Req.class, message);
                logger.info(t2007Req.head.OpDate + t2007Req.head.OpTime + "==���ս��ף�" + t2007Req.head.OpCode);

                BiContractClose contractClose = new BiContractClose();
                contractClose.setAccountCode(t2007Req.param.Acct);
                contractClose.setAccountName(t2007Req.param.AcctName);
                contractClose.setBuyerName(t2007Req.param.BuyerName);
                contractClose.setBuyerAccCode(t2007Req.param.BuyerAcct);
                contractClose.setBuyerBankName(t2007Req.param.BuyerBankName);
                contractClose.setBuyerCertType(t2007Req.param.BuyerIDType);
                contractClose.setBuyerCertNo(t2007Req.param.BuyerIDCode);
                contractClose.setContractNo(t2007Req.param.ContractNum);
                contractClose.setTotalAmt(new BigDecimal(t2007Req.param.TotalAmt).divide(new BigDecimal(100)));
                contractClose.setHouseAddress(t2007Req.param.HouseAddress);
                contractClose.setPurpose(t2007Req.param.EndReason);
                contractClose.setTransAmt(new BigDecimal(t2007Req.param.TransBuyerAmt).divide(new BigDecimal(100)));


                T2007Res t2007Res = new T2007Res();
                RsContract originContract = biDbService.selectContractByCloseInfo(contractClose);
                if (ContractStatus.TRANS.getCode().equalsIgnoreCase(originContract.getStatusFlag())
                        || ContractStatus.CANCEL.getCode().equalsIgnoreCase(originContract.getStatusFlag())
                        || ContractStatus.CANCELING.getCode().equalsIgnoreCase(originContract.getStatusFlag())) {
                    t2007Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                    t2007Res.head.RetMsg = "�ú�ͬ�Ѿ������������ظ�������";
                } else {
                    if (biDbService.recvCloseContractInfo(contractClose) != 1) {
                        t2007Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                        t2007Res.head.RetMsg = "����ʧ�ܣ������ԡ�";
                    }
                }
                responseMsg = t2007Res.toFDCDatagram();
                break;
            case 2008:
                T2008Req t2008Req = (T2008Req) BaseBean.toObject(T2008Req.class, message);
                logger.info(t2008Req.head.OpDate + t2008Req.head.OpTime + "==���ս��ף�" + t2008Req.head.OpCode);

                T2008Res t2008Res = new T2008Res();

                BiPlan biPlan = new BiPlan();
                biPlan.setAccountCode(t2008Req.param.Acct);
                biPlan.setAccountName(t2008Req.param.AcctName);
                biPlan.setPlanNo(t2008Req.param.PlanNO);
                biPlan.setPlanAmount(new BigDecimal(t2008Req.param.PlanAmt).divide(new BigDecimal(100)));
                biPlan.setPlanNum(Integer.parseInt(t2008Req.param.PlanNum));
                if (!org.apache.commons.lang.StringUtils.isEmpty(t2008Req.param.SubmitDate)
                        && t2008Req.param.SubmitDate.length() >= 8) {
                    biPlan.setSubmitDate(StringUtil.transDate8ToDate10(t2008Req.param.SubmitDate));
                } else {
                    biPlan.setSubmitDate(t2008Req.param.SubmitDate);
                }
                int planDetailCnt = t2008Req.param.recordList.size();

                if (!t2008Req.param.PlanNum.equalsIgnoreCase(String.valueOf(planDetailCnt))) {
                    t2008Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FORMAT_ERROR.getCode();
                    t2008Res.head.RetMsg = "�ÿ�ƻ���������ϸ�ƻ�������һ�¡�";
                    logger.error("�ÿ�ƻ���������ϸ�ƻ�������һ�£�");
                    responseMsg = t2008Res.toFDCDatagram();
                    break;
                }
                String wrngRecordNo = null;
                List<BiPlanDetail> biPlanDetailList = null;
                try {
                    if (planDetailCnt >= 1) {
                        biPlanDetailList = new ArrayList<BiPlanDetail>();
                        for (T2008Req.Param.Record record : t2008Req.param.recordList) {
                            BiPlanDetail planDetail = new BiPlanDetail();
                            wrngRecordNo = record.PlanDetailNO;
                            planDetail.setPlanId(t2008Req.param.PlanNO);
                            planDetail.setPlanCtrlNo(record.PlanDetailNO);
                            planDetail.setToAccountName(record.ToAcctName);
                            planDetail.setToAccountCode(record.ToAcct);
                            planDetail.setToHsBankName(record.ToBankName);
                            planDetail.setPlAmount(new BigDecimal(record.Amt).divide(new BigDecimal(100)));
                            if (!org.apache.commons.lang.StringUtils.isEmpty(record.PlanDate)
                                    && record.PlanDate.length() >= 8) {
                                planDetail.setPlanDate(StringUtil.transDate8ToDate10(record.PlanDate));
                            } else {
                                planDetail.setPlanDate(record.PlanDate);
                            }
                            planDetail.setPlanDate(record.PlanDate);
                            planDetail.setPlanDesc(record.Purpose);
                            planDetail.setRemark(record.Remark);
                            biPlanDetailList.add(planDetail);
                        }
                        if (biDbService.storeFdcAllPlanInfos(biPlan, biPlanDetailList) == -1) {
                            throw new RuntimeException("���ձ������ݲ���ʧ�ܣ�");
                        }
                    } else throw new RuntimeException("�ƻ���ϸΪ�գ�");

                } catch (Exception e) {
                    t2008Res.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                    t2008Res.head.RetMsg = e.getMessage() + "����ʧ�ܣ������ԡ�";
                    logger.error("2008������������ʧ�ܣ�", e);
                    responseMsg = t2008Res.toFDCDatagram();
                    break;
                }

                responseMsg = t2008Res.toFDCDatagram();
                break;
            default:
                CommonRes otherData = new CommonRes();
                otherData.head.RetCode = BiRtnCode.BI_RTN_CODE_FAILED.getCode();
                otherData.head.RetMsg = "������[" + opCode + "]�����ڣ�";
                responseMsg = otherData.toFDCDatagram();
                logger.error("====���յ��޷������ף��������룺" + opCode + "��");

        }
        return responseMsg;
    }
}
