package rfm.ta.gateway.sbs.helper;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbTiaXml2002;
import rfm.ta.test.Bean20002Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * wanglichao@163.com
 * Created by Lichao.W At 2015/7/6 11:10
 * 对象操作辅助类
 */
public class XmlToBean {

    public static void CopyXmlToBean(Object xml, Object des) {
        try {
            Field[] fields = xml.getClass().getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if ("body".equals(field.getName())) {
                    Object objVal = field.get(xml);
                    Field[] bodyFilds = Class.forName(field.getType().getName()).newInstance().getClass().getFields();
                    Field bodyField = null;
                    for (Field field2 : bodyFilds) {
                        Object bodyVal = field2.get(objVal);
                        try {
                            bodyField = des.getClass().getDeclaredField(field2.getName());
                        } catch (Exception e) {
                            try {
                                bodyField = des.getClass().getDeclaredField(field.getName().toUpperCase());
                            } catch (NoSuchFieldException e1) {
                                try {
                                    bodyField = des.getClass().getDeclaredField(field.getName().toLowerCase());
                                } catch (NoSuchFieldException e2) {
                                    continue;
                                }
                            }
                        }
                        bodyField.setAccessible(true);
                        if (bodyField.getType().getSimpleName().equals("String")) {
                            if (bodyVal != null) {
                                bodyField.set(des, StringUtils.isEmpty((String) bodyVal) ? "" : bodyVal.toString());
                            } else {
                                bodyField.set(des, "");
                            }
                        } else if (bodyField.getType().getSimpleName().equals("BigDecimal")) {
                            if (bodyVal != null) bodyField.set(des, new BigDecimal(bodyVal.toString()));
                        } else {
                            throw new RuntimeException("desFieldInstance : unsupported field type[BigDecimal and String only].");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("AbstractTxnProcessor copyXmlToBean 解析异常");
        }
    }

    public static void main(String[] args) {
        String msgData = "<?xml version=\"1.0\" encoding=\"GBK\"?>" +
                "<root>" +
                "<info>" +
                "<txncode>9009001</txncode>" +
                "<userid>9009001</userid>" +
                "<reqsn>9009001</reqsn>" +
                "<version>9009001</version>" +
                "<txndate>9009001</txndate>" +
                "<txntime>QDJS000092321</txntime>" +
                "</info>" +
                "<body>" +
                "<originid>123456789</originid>" +
                "<originid2>435234</originid2>" +
                "</body>" +
                "</root>";
        TaHfnbTiaXml2002 tia = (TaHfnbTiaXml2002) (new TaHfnbTiaXml2002().getTia(msgData));
        Bean20002Test bean = new Bean20002Test();
        CopyXmlToBean(tia, bean);
        System.out.println(bean.getOriginid() + "\n" + bean.getOriginid2());
    }
}
