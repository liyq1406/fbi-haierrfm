package org.fbi.dep.model.base;

import java.io.Serializable;

/**
 * RFM��DEP���͵�Java���󣨳����ࣩ����
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public abstract class TiaXml implements Serializable{
     public TiaXml transform(String xml) {
         try {
         return getTia(xml);
         }catch (Exception e) {
             //    MSG_PARSE_FAILED("1100", "���Ľ�������"),
             throw new RuntimeException("1100|���Ľ�������");
         }
     }

    public abstract TiaXml getTia(String xml);
}
