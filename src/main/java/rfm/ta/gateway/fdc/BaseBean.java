package rfm.ta.gateway.fdc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: ����5:23
 * To change this template use File | Settings | File Templates.
 */
public class BaseBean {

    public String toXml() {
        String xmlHead = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n";
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(this.getClass());
        String xmlContent = xStream.toXML(this);
        return xmlHead + xmlContent;
    }

    public static Object toObject(Class clazz, String xml) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(clazz);
        return xStream.fromXML(xml.trim());
    }

    public String toFDCDatagram() {
        return toXml();
    }
}
