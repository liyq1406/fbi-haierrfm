package org.fbi.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.dep.model.base.TiaXml;

import java.io.Serializable;

/**
 * Created by hanjianlong on 2015-9-7.
 * FEB发起对账通知
 */

@XStreamAlias("ROOT")
public class TiaXml9100001 extends TiaXml {
    public Info INFO;
    public Body BODY;

    public Body getBODY() {
        return BODY;
    }

    public void setBODY(Body BODY) {
        this.BODY = BODY;
    }

    public Info getINFO() {
        return INFO;
    }

    public void setINFO(Info INFO) {
        this.INFO = INFO;
    }

    public static class Info implements Serializable {
        private String TXNCODE;
        private String VERSION;
        private String REQSN;

        public String getTXNCODE() {
            return TXNCODE;
        }

        public void setTXNCODE(String TXNCODE) {
            this.TXNCODE = TXNCODE;
        }

        public String getVERSION() {
            return VERSION;
        }

        public void setVERSION(String VERSION) {
            this.VERSION = VERSION;
        }

        public String getREQSN() {
            return REQSN;
        }

        public void setREQSN(String REQSN) {
            this.REQSN = REQSN;
        }
    }

    public static class Body implements Serializable{
        private String TXNDATE;
        private String TXNTIME;
        private String CHNCODE;
        private String ACTION;
        private String REMARK;
        public String getTXNDATE() {
            return TXNDATE;
        }

        public void setTXNDATE(String TXNDATE) {
            this.TXNDATE = TXNDATE;
        }

        public String getTXNTIME() {
            return TXNTIME;
        }

        public void setTXNTIME(String TXNTIME) {
            this.TXNTIME = TXNTIME;
        }

        public String getCHNCODE() {
            return CHNCODE;
        }

        public void setCHNCODE(String CHNCODE) {
            this.CHNCODE = CHNCODE;
        }

        public String getACTION() {
            return ACTION;
        }

        public void setACTION(String ACTION) {
            this.ACTION = ACTION;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }
    }

    @Override
    public TiaXml getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TiaXml9100001.class);
        return (TiaXml9100001) xs.fromXML(xml);
    }
}
