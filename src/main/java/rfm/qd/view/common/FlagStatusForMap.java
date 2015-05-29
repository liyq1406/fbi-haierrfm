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
 * Time: ����2:23
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class FlagStatusForMap {
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;
    //��֧��־
    private Map<String,String> inoutFlagMap;
    //��������
    private Map<String,String> tradeTypeMap;
    //����˻�������ϸ״̬��־
    private Map<String,String> actDetlStatusFlagMap;
    //���ն��˱�־
    private Map<String,String> actDetlDcheckFlagMap;
    //�����־
    private Map<String,String> actDetlChangeFlagMap;
    //������˾ Map<id,name>
    private Map<String,String> companyMap;
    //Rs_account��
    //�˻�״̬
    private Map<String,String> accountStatus;
    //���Ƹ���
    private Map<String,String> accountLimit;

    public Map<String, String> getInoutFlagMap() {
        inoutFlagMap = new HashMap();
        inoutFlagMap.put("0","֧��");
        inoutFlagMap.put("1","����");
        return inoutFlagMap;
    }

    public void setInoutFlagMap(Map<String, String> inoutFlagMap) {
        this.inoutFlagMap = inoutFlagMap;
    }

    public Map<String, String> getAccountLimit() {
        accountLimit = new HashMap();
        accountLimit.put("0", "δ����");
        accountLimit.put("1", "����");
        return accountLimit;
    }

    public void setAccountLimit(Map<String, String> accountLimit) {
        this.accountLimit = accountLimit;
    }

    public Map<String, String> getAccountStatus() {
        accountStatus = new HashMap();
        accountStatus.put("N","��ʼ");
        accountStatus.put("0", "�����");
        accountStatus.put("1", "�������");
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
        // �����־;[N]-����;[R]-����;[D]-��Ʊ;[A]-�������;[B]-������Ʊ
        actDetlChangeFlagMap.put("N","����");
        actDetlChangeFlagMap.put("R","����");
        actDetlChangeFlagMap.put("D","��Ʊ");
        actDetlChangeFlagMap.put("A","�������");
        actDetlChangeFlagMap.put("B","������Ʊ");
        return actDetlChangeFlagMap;
    }

    public void setActDetlChangeFlagMap(Map<String, String> actDetlChangeFlagMap) {
        this.actDetlChangeFlagMap = actDetlChangeFlagMap;
    }

    public Map<String, String> getActDetlDcheckFlagMap() {
        actDetlDcheckFlagMap = new HashMap();
        actDetlDcheckFlagMap.put("0","δ����");
        actDetlDcheckFlagMap.put("1","�Ѷ���");
        return actDetlDcheckFlagMap;
    }

    public void setActDetlDcheckFlagMap(Map<String, String> actDetlDcheckFlagMap) {
        this.actDetlDcheckFlagMap = actDetlDcheckFlagMap;
    }

    public Map<String, String> getTradeTypeMap() {
        tradeTypeMap = new HashMap();
        tradeTypeMap.put("01","��������");
        tradeTypeMap.put("02","�ƻ�����");
        tradeTypeMap.put("03","�˿�");
        tradeTypeMap.put("04","��Ϣ");
        tradeTypeMap.put("05","���ת");
        tradeTypeMap.put("09","����");
        return tradeTypeMap;
    }

    public void setTradeTypeMap(Map<String, String> tradeTypeMap) {
        this.tradeTypeMap = tradeTypeMap;
    }

    public Map<String, String> getActDetlStatusFlagMap() {
        actDetlStatusFlagMap = new HashMap();
        actDetlStatusFlagMap.put("0","��ʼ");
        actDetlStatusFlagMap.put("1","���׳ɹ�");
        actDetlStatusFlagMap.put("2","����ͨ��");
        actDetlStatusFlagMap.put("3","�˻�");
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
