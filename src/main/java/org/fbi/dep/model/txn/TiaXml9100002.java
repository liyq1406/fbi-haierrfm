package org.fbi.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.dep.model.base.TiaXml;

/**
 * Created by hanjianlong on 2015-9-7.
 * FEB发起的对账结果查询
 */

@XStreamAlias("ROOT")
public class TiaXml9100002 extends TiaXml {
    public Info INFO;
    public Body BODY;

    public Info getINFO() {
        return INFO;
    }

    public void setINFO(Info INFO) {
        this.INFO = INFO;
    }

    public Body getBODY() {
        return BODY;
    }

    public void setBODY(Body BODY) {
        this.BODY = BODY;
    }

    public static class Info {
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

    public static class Body{
        private String CHANNEL;

        public String getCHANNEL() {
            return CHANNEL;
        }

        public void setCHANNEL(String CHANNEL) {
            this.CHANNEL = CHANNEL;
        }
    }

    @Override
    public TiaXml getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TiaXml9100002.class);
        return (TiaXml9100002) xs.fromXML(xml);
    }
}
