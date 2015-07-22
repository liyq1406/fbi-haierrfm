package rfm.ta.common.gateway.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import rfm.ta.common.gateway.dep.model.base.TiaXml;
import rfm.ta.common.gateway.dep.model.base.TiaXmlHttpInfo;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

@XStreamAlias("root")
public class TiaXml910012002 extends TiaXml {
    public Info info;
    public Body body;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Info extends TiaXmlHttpInfo {
        public String bankbranchid;       //Õ¯µ„±‡∫≈
        public String bankoperid;         //»À‘±±‡∫≈

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
        public String originid;          //Ωª¥Ê…Í«Î±‡∫≈
        public String tradeamt;          //Ωª¥ÊΩ∂Ó
        public String accountcode;       //º‡π‹’À∫≈
        public String accountoutcode;    //◊™≥ˆ’À∫≈

        public String getOriginid() {
            return originid;
        }

        public void setOriginid(String originid) {
            this.originid = originid;
        }

        public String getTradeamt() {
            return tradeamt;
        }

        public void setTradeamt(String tradeamt) {
            this.tradeamt = tradeamt;
        }

        public String getAccountcode() {
            return accountcode;
        }

        public void setAccountcode(String accountcode) {
            this.accountcode = accountcode;
        }

        public String getAccountoutcode() {
            return accountoutcode;
        }

        public void setAccountoutcode(String accountoutcode) {
            this.accountoutcode = accountoutcode;
        }
    }

    @Override
    public TiaXml getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TiaXml910012002.class);
        return (TiaXml910012002) xs.fromXML(xml);
    }
}
