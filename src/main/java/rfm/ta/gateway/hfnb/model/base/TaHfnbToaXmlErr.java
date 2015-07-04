package rfm.ta.gateway.hfnb.model.base;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

@XStreamAlias("ROOT")
public class TaHfnbToaXmlErr {
    public TaHfnbToaXmlInfo info = new TaHfnbToaXmlInfo();
    public Body Body = new Body();

    public TaHfnbToaXmlErr.Body getBody() {
        return Body;
    }

    public void setBody(TaHfnbToaXmlErr.Body body) {
        Body = body;
    }

    public TaHfnbToaXmlInfo getInfo() {
        return info;
    }

    public void setInfo(TaHfnbToaXmlInfo info) {
        this.info = info;
    }

    public static class Body implements Serializable {
        public String rtncode;            //ҵ�����-���ش���
        public String rtnmsg;             //ҵ�����-������Ϣ

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

    @Override
    public String toString() {
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(TaHfnbToaXmlErr.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
