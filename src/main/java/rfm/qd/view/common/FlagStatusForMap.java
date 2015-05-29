package rfm.qd.view.common;

import rfm.qd.repository.model.RsFdccompany;
import rfm.qd.service.company.CompanyService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-9-6
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class FlagStatusForMap {
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;
    //收支标志
    private Map<String,String> inoutFlagMap;
    //交易类型
    private Map<String,String> tradeTypeMap;
    //监管账户交易明细状态标志
    private Map<String,String> actDetlStatusFlagMap;
    //日终对账标志
    private Map<String,String> actDetlDcheckFlagMap;
    //变更标志
    private Map<String,String> actDetlChangeFlagMap;
    //房产公司 Map<id,name>
    private Map<String,String> companyMap;
    //Rs_account表
    //账户状态
    private Map<String,String> accountStatus;
    //限制付款
    private Map<String,String> accountLimit;

    public Map<String, String> getInoutFlagMap() {
        inoutFlagMap = new HashMap();
        inoutFlagMap.put("0","支出");
        inoutFlagMap.put("1","收入");
        return inoutFlagMap;
    }

    public void setInoutFlagMap(Map<String, String> inoutFlagMap) {
        this.inoutFlagMap = inoutFlagMap;
    }

    public Map<String, String> getAccountLimit() {
        accountLimit = new HashMap();
        accountLimit.put("0", "未限制");
        accountLimit.put("1", "限制");
        return accountLimit;
    }

    public void setAccountLimit(Map<String, String> accountLimit) {
        this.accountLimit = accountLimit;
    }

    public Map<String, String> getAccountStatus() {
        accountStatus = new HashMap();
        accountStatus.put("N","初始");
        accountStatus.put("0", "监管中");
        accountStatus.put("1", "撤销监管");
        return accountStatus;
    }

    public void setAccountStatus(Map<String, String> accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Map<String, String> getCompanyMap() {
        List<RsFdccompany> rsFdccompanyList = companyService.qryRsFdccompanyByName("");
        companyMap = new HashMap();
        for (RsFdccompany rf:rsFdccompanyList) {
            companyMap.put(rf.getCompanyId(),rf.getCompanyName());
        }
        return companyMap;
    }

    public void setCompanyMap(Map<String, String> companyMap) {
        this.companyMap = companyMap;
    }

    public Map<String, String> getActDetlChangeFlagMap() {
        actDetlChangeFlagMap = new HashMap();
        // 变更标志;[N]-正常;[R]-冲正;[D]-退票;[A]-申请冲正;[B]-申请退票
        actDetlChangeFlagMap.put("N","正常");
        actDetlChangeFlagMap.put("R","冲正");
        actDetlChangeFlagMap.put("D","退票");
        actDetlChangeFlagMap.put("A","申请冲正");
        actDetlChangeFlagMap.put("B","申请退票");
        return actDetlChangeFlagMap;
    }

    public void setActDetlChangeFlagMap(Map<String, String> actDetlChangeFlagMap) {
        this.actDetlChangeFlagMap = actDetlChangeFlagMap;
    }

    public Map<String, String> getActDetlDcheckFlagMap() {
        actDetlDcheckFlagMap = new HashMap();
        actDetlDcheckFlagMap.put("0","未对账");
        actDetlDcheckFlagMap.put("1","已对账");
        return actDetlDcheckFlagMap;
    }

    public void setActDetlDcheckFlagMap(Map<String, String> actDetlDcheckFlagMap) {
        this.actDetlDcheckFlagMap = actDetlDcheckFlagMap;
    }

    public Map<String, String> getTradeTypeMap() {
        tradeTypeMap = new HashMap();
        tradeTypeMap.put("01","房款收入");
        tradeTypeMap.put("02","计划付款");
        tradeTypeMap.put("03","退款");
        tradeTypeMap.put("04","利息");
        tradeTypeMap.put("05","房款划转");
        tradeTypeMap.put("09","其他");
        return tradeTypeMap;
    }

    public void setTradeTypeMap(Map<String, String> tradeTypeMap) {
        this.tradeTypeMap = tradeTypeMap;
    }

    public Map<String, String> getActDetlStatusFlagMap() {
        actDetlStatusFlagMap = new HashMap();
        actDetlStatusFlagMap.put("0","初始");
        actDetlStatusFlagMap.put("1","交易成功");
        actDetlStatusFlagMap.put("2","复核通过");
        actDetlStatusFlagMap.put("3","退回");
        return actDetlStatusFlagMap;
    }

    public void setActDetlStatusFlagMap(Map<String, String> actDetlStatusFlagMap) {
        this.actDetlStatusFlagMap = actDetlStatusFlagMap;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
