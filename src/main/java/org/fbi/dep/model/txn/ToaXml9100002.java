package org.fbi.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.fbi.dep.model.base.ToaXml;

/**
 * Created by hanjianlong on 2015-9-8.
 * FEB发起的对账结果查询
 */

@XStreamAlias("ROOT")
public class ToaXml9100002 extends ToaXml {
    private Info INFO = new Info();
    private Body BODY = new Body();

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

    public static class Info{
        private String TXNCODE;
        private String VERSION;
        private String REQSN;
        private String RTNCODE;
        private String RTNMSG;

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

        public String getRTNCODE() {
            return RTNCODE;
        }

        public void setRTNCODE(String RTNCODE) {
            this.RTNCODE = RTNCODE;
        }

        public String getRTNMSG() {
            return RTNMSG;
        }

        public void setRTNMSG(String RTNMSG) {
            this.RTNMSG = RTNMSG;
        }
    }

    public static class Body{

    }

    @Override
    public String toString() {
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(ToaXml9100002.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    public static ToaXml9100002 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(ToaXml9100002.class);
        return (ToaXml9100002) xs.fromXML(xml);
    }
}
