package rfm.ta.gateway.hfnb.model.base;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

public class TaHfnbToaXmlInfo implements Serializable {
    public String txncode;            //���״���
    public String userid;             //�û�ID
    public String reqsn;              //������ˮ��
    public String version;            //�汾��
    public String txndate;            //��������
    public String txntime;            //����ʱ��
    public String bankbranchid;       //������
    public String bankoperid;         //��Ա���
    public String rtncode;            //ϵͳ����-���ش���
    public String rtnmsg;             //ϵͳ����-������Ϣ

    public String getTxncode() {
        return txncode;
    }

    public void setTxncode(String txncode) {
        this.txncode = txncode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReqsn() {
        return reqsn;
    }

    public void setReqsn(String reqsn) {
        this.reqsn = reqsn;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxntime() {
        return txntime;
    }

    public void setTxntime(String txntime) {
        this.txntime = txntime;
    }

    public String getBankbranchid() {
        return bankbranchid;
    }

    public void setBankbranchid(String bankbranchid) {
        this.bankbranchid = bankbranchid;
    }

    public String getBankoperid() {
        return bankoperid;
    }

    public void setBankoperid(String bankoperid) {
        this.bankoperid = bankoperid;
    }

    public String getRtncode() {
        return rtncode;
    }

    public void setRtncode(String rtncode) {
        this.rtncode = rtncode;
    }

    public String getRtnmsg() {
        return rtnmsg;
    }

    public void setRtnmsg(String rtnmsg) {
        this.rtnmsg = rtnmsg;
    }
}
