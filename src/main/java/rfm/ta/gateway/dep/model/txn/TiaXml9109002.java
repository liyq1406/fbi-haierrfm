package rfm.ta.gateway.dep.model.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.TiaXmlInfo;

import java.io.Serializable;

/**
 * 巨商汇查询入账结果
 */

@XStreamAlias("ROOT")
public class TiaXml9109002 extends TiaXml {
    public TiaXmlInfo INFO;
    public Body BODY;

    public static class Body implements Serializable {
        public String TXNDATE;
        public String RESERVE;
    }

    @Override
    public TiaXml getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(TiaXml9109002.class);
        return (TiaXml9109002) xs.fromXML(xml);
    }
}
