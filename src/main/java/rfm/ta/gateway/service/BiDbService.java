package rfm.ta.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.qd.common.constant.ChangeFlag;
import rfm.qd.common.constant.ContractStatus;
import rfm.qd.repository.dao.*;
import rfm.qd.repository.model.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-26
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BiDbService {

    @Autowired
    private QdRsAccountMapper accountMapper;
    @Autowired
    private QdBiContractMapper qdBiContractMapper;
    @Autowired
    private QdBiContractCloseMapper qdBiContractCloseMapper;
    @Autowired
    private QdRsAccDetailMapper accDetailMapper;
    @Autowired
    private QdBiPlanMapper qdBiPlanMapper;
    @Autowired
    private QdBiPlanDetailMapper qdBiPlanDetailMapper;
    @Autowired
    private QdRsContractMapper qdRsContractMapper;
    @Autowired
    private QdRsPlanCtrlMapper qdRsPlanCtrlMapper;

    @Transactional
    public int storeFdcAllPlanInfos(QdBiPlan qdBiPlan, List<QdBiPlanDetail> qdBiPlanDetailList) {

        if (insertBiPlan(qdBiPlan) == 1) {
            QdRsPlanCtrl qdRsPlanCtrl = null;
            boolean isUpdate = false;
            for (QdBiPlanDetail qdBiPlanDetail : qdBiPlanDetailList) {
                if (isExistPlanCtlNo(qdBiPlanDetail)) {
                    QdRsPlanCtrlExample example = new QdRsPlanCtrlExample();
                    example.createCriteria().andDeletedFlagEqualTo("0").andPlanCtrlNoEqualTo(qdBiPlanDetail.getPlanCtrlNo());
                    qdRsPlanCtrl = qdRsPlanCtrlMapper.selectByExample(example).get(0);
                    isUpdate = true;
                } else {
                    qdRsPlanCtrl = new QdRsPlanCtrl();
                    isUpdate = false;
                }
                qdRsPlanCtrl.setAccountCode(qdBiPlan.getAccountCode());
                qdRsPlanCtrl.setCompanyName(qdBiPlan.getAccountName());
                qdRsPlanCtrl.setAcceptDate(qdBiPlan.getSubmitDate());
                qdRsPlanCtrl.setPlanCtrlNo(qdBiPlanDetail.getPlanCtrlNo());
                qdRsPlanCtrl.setToAccountName(qdBiPlanDetail.getToAccountName());
                qdRsPlanCtrl.setToAccountCode(qdBiPlanDetail.getToAccountCode());
                qdRsPlanCtrl.setToHsBankName(qdBiPlanDetail.getToHsBankName());
                qdRsPlanCtrl.setPlAmount(qdBiPlanDetail.getPlAmount());
                qdRsPlanCtrl.setAvAmount(qdRsPlanCtrl.getPlAmount());
                qdRsPlanCtrl.setPlanDate(qdBiPlanDetail.getPlanDate());
                qdRsPlanCtrl.setPlanDesc(qdBiPlanDetail.getPlanDesc());
                qdRsPlanCtrl.setRemark(qdBiPlanDetail.getRemark());

                if (isUpdate) {
                    if (insertBiPlanDetail(qdBiPlanDetail) != 1 || qdRsPlanCtrlMapper.updateByPrimaryKeySelective(qdRsPlanCtrl) != 1) {
                        return -1;
                    }
                } else {
                    if (insertBiPlanDetail(qdBiPlanDetail) != 1 || qdRsPlanCtrlMapper.insertSelective(qdRsPlanCtrl) != 1) {
                        return -1;
                    }
                }
            }
            return 1;
        }
        return -1;
    }

    private boolean isExistPlanCtlNo(QdBiPlanDetail planDetail) {
        QdRsPlanCtrlExample example = new QdRsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andPlanCtrlNoEqualTo(planDetail.getPlanCtrlNo());
        if (qdRsPlanCtrlMapper.countByExample(example) >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 新增计划明细
     *
     * @param qdBiPlanDetail
     * @return
     */
    public int insertBiPlanDetail(QdBiPlanDetail qdBiPlanDetail) {
        return qdBiPlanDetailMapper.insertSelective(qdBiPlanDetail);
    }

    /**
     * 新增计划（主表）
     *
     * @param qdBiPlan
     * @return
     */
    public int insertBiPlan(QdBiPlan qdBiPlan) {
        return qdBiPlanMapper.insertSelective(qdBiPlan);
    }

    /**
     * 接收处理预售房合同撤销信息到DB
     *
     * @param contractClose
     * @return
     */
    @Transactional
    public int recvCloseContractInfo(QdBiContractClose contractClose) {
        if (insertBiContactClose(contractClose) == 1) {
            return updateContractToClose(contractClose);
        } else return -1;
    }

    /**
     * 新增预售合同撤销
     *
     * @param contractClose
     * @return
     */
    public int insertBiContactClose(QdBiContractClose contractClose) {
        return qdBiContractCloseMapper.insertSelective(contractClose);
    }

    /**
     * 撤销预售房合同
     *
     * @param contractClose
     * @return
     */
    public int updateContractToClose(QdBiContractClose contractClose) {

        QdRsContract qdRsContract = selectContractByCloseInfo(contractClose);
        if (qdRsContract != null) {
            qdRsContract.setStatusFlag(ContractStatus.TRANS.getCode());
            qdRsContract.setModificationNum(qdRsContract.getModificationNum() + 1);
            qdRsContract.setTransbuyeramt(contractClose.getTransAmt());
            return qdRsContractMapper.updateByPrimaryKey(qdRsContract);
        }
        return -1;
    }

    public QdRsContract selectContractByCloseInfo(QdBiContractClose contractClose) {
        QdRsContractExample example = new QdRsContractExample();
        example.createCriteria().andContractNoEqualTo(contractClose.getContractNo()).andAccountCodeEqualTo(contractClose.getAccountCode())
                .andDeletedFlagEqualTo("0");
        List<QdRsContract> qdRsContractList = qdRsContractMapper.selectByExample(example);
        if (qdRsContractList.isEmpty() || qdRsContractList.size() > 1) {
            return null;
        } else return qdRsContractList.get(0);
    }

    @Transactional
    public int updateDBContractByBiContract(QdBiContract contract) {
        if (insertBiContract(contract) == 1) {
            return insertOrUpdateRsContract(contract);
        }
        return -1;
    }

    /**
     * 预售合同接口 新增
     *
     * @param contract
     * @return
     */
    @Transactional
    public int insertBiContract(QdBiContract contract) {
        return qdBiContractMapper.insertSelective(contract);
    }

    @Transactional
    public int insertOrUpdateRsContract(QdBiContract contract) {
        QdRsContractExample example = new QdRsContractExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andContractNoEqualTo(contract.getContractNo());
        List<QdRsContract> qdRsContracts = qdRsContractMapper.selectByExample(example);
        QdRsContract qdRsContract = null;
        if (qdRsContracts.isEmpty()) {
            qdRsContract = new QdRsContract();
        } else {
            qdRsContract = qdRsContracts.get(0);
            if(qdRsContract.getReceiveAmt().compareTo(new BigDecimal(0)) > 0) {
                return 1;
            }
            qdRsContract.setModificationNum(qdRsContract.getModificationNum() + 1);
        }
        qdRsContract.setContractNo(contract.getContractNo());                                     // 合同号
        qdRsContract.setAccountCode(contract.getAccountCode());                               // 监管账号
        qdRsContract.setAccountName(contract.getAccountName());                              // 监管账户户名
        qdRsContract.setBuyerName(contract.getBuyerName());                                   // 购房人姓名
        qdRsContract.setBuyerAccName(contract.getBuyerName());
        qdRsContract.setBuyerAccCode(contract.getBuyerAccCode());                          // 购房人个人结算账号
        qdRsContract.setBuyerBankName(contract.getBuyerBankName());                    // 购房人账户开户行
        qdRsContract.setBuyerCertType(contract.getBuyerCertType());                         //购房人证件类型
        qdRsContract.setBuyerCertNo(contract.getBuyerCertNo());                                // 购房人证件号码
        qdRsContract.setTotalAmt(contract.getTotalAmt());                                           //房屋总价
        qdRsContract.setEarnestAmt(contract.getEarnestAmt());                                    // 定金
        qdRsContract.setFirstAmt(contract.getFirstAmt());                                             // 首付款
        qdRsContract.setLoanAmt(contract.getLoanAmt());                                            // 贷款
        qdRsContract.setHouseAddress(contract.getHouseAddress());                            // 房屋地址
        qdRsContract.setHouseType(contract.getHouseType());                                      // 房屋类型
        qdRsContract.setHouseNo(contract.getHouseNo());                                            // 房屋楼栋信息
        qdRsContract.setOverAmt(contract.getOverAmt());                                            // 超标面积价款
        qdRsContract.setTreAccName(contract.getTreAccName());                                 // 上缴财政专用账户名
        qdRsContract.setTreAccCode(contract.getTreAccCode());                                   // 上缴财政专用账号
        qdRsContract.setTreBankName(contract.getTreBankName());                            // 财政专户开户行

        if (qdRsContracts.isEmpty()) {
            return qdRsContractMapper.insertSelective(qdRsContract);
        } else {
            qdRsContract = qdRsContracts.get(0);
            qdRsContract.setModificationNum(qdRsContract.getModificationNum() + 1);
            return qdRsContractMapper.updateByPrimaryKey(qdRsContract);
        }
    }

    /**
     * 查询时间段内账户交易记录 去除冲正
     *
     * @param accountCode
     * @param accountName
     * @param fromDate
     * @param toDate
     * @return
     * @throws java.text.ParseException
     */
    public List<QdRsAccDetail> selectAccDetailsByCodeNameDate(String accountCode, String accountName, String fromDate, String toDate) {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagNotEqualTo(ChangeFlag.CANCEL.getCode())
                .andAccountCodeEqualTo(accountCode)
                .andAccountNameEqualTo(accountName).
                andTradeDateBetween(fromDate, toDate);
        return accDetailMapper.selectByExample(example);
    }

    /**
     * 更新账户状态
     *
     * @param account
     * @return
     */
    @Transactional
    public int updateAccount(QdRsAccount account) {
        account.setModificationNum(account.getModificationNum() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 更新账户限制/解除限制状态
     *
     * @param account
     * @param limitStatus
     * @return
     */
    public int updateAccountToLimitStatus(QdRsAccount account, String limitStatus) {
        account.setLimitFlag(limitStatus);
        account.setModificationNum(account.getModificationNum() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 根据户名和账户号查找账户
     *
     * @param accountCode
     * @param accountName
     * @return
     */
    public List<QdRsAccount> selectAccountByCodeName(String accountCode, String accountName) {
        QdRsAccountExample example = new QdRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccountCodeEqualTo(accountCode).andAccountNameEqualTo(accountName);
        return accountMapper.selectByExample(example);
    }

    public boolean isAccountExistByCodeName(String accountCode, String accountName) {
        QdRsAccountExample example = new QdRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccountCodeEqualTo(accountCode).andAccountNameEqualTo(accountName);
        return accountMapper.countByExample(example) == 1;
    }

}
