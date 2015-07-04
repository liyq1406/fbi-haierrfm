package rfm.ta.gateway.hfnb.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import rfm.ta.gateway.hfnb.model.base.TaHfnbTiaXml;
import rfm.ta.gateway.hfnb.model.base.TaHfnbTiaXmlInfo;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

@XStreamAlias("root")
public class TaHfnbTiaXml2011 extends TaHfnbTiaXml {
    public TaHfnbTiaXmlInfo info;
    public Body body;

    public TaHfnbTiaXmlInfo getInfo() {
        return info;
    }

    public void setInfo(TaHfnbTiaXmlInfo info) {
        this.info = info;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body implements Serializable {
        public String originid;        //Ωª¥Ê…Í«Î±‡∫≈

        public String getOriginid() {
            return originid;
        }

        public void setOriginid(String originid) {
            this.originid = originid;
        }
    }

    @Override
    public TaHfnbTiaXml getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TaHfnbTiaXml2011.class);
        return (TaHfnbTiaXml2011) xs.fromXML(xml);
    }
}
