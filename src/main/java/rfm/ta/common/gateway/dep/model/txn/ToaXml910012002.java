package rfm.ta.common.gateway.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import rfm.ta.common.gateway.dep.model.base.ToaXml;
import rfm.ta.common.gateway.dep.model.base.ToaXmlHttpInfo;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

@XStreamAlias("root")
public class ToaXml910012002 extends ToaXml {
    public Info info = new Info();
    public Body Body = new Body();

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public ToaXml910012002.Body getBody() {
        return Body;
    }

    public void setBody(ToaXml910012002.Body body) {
        Body = body;
    }

    public static class Info extends ToaXmlHttpInfo {
        public String bankbranchid;       //网点编号
        public String bankoperid;         //人员编号

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
    }

    public static class Body implements Serializable {
        public String rtncode;        //业务层面-返回代码
        public String rtnmsg;         //业务层面-返回信息
        public String fdcserial;      //预售资金监管平台流水

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

        public String getFdcserial() {
            return fdcserial;
        }

        public void setFdcserial(String fdcserial) {
            this.fdcserial = fdcserial;
        }
    }

    @Override
    public String toString() {
        this.info.txncode = "2002";
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(ToaXml910012002.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    public static ToaXml910012002 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(ToaXml910012002.class);
        return (ToaXml910012002) xs.fromXML(xml);
    }
}
