package org.fbi.dep.model.base;

import java.io.Serializable;

/**
 * RFM往DEP发送的Java对象（抽象类）解析
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
             //    MSG_PARSE_FAILED("1100", "报文解析错误"),
             throw new RuntimeException("1100|报文解析错误");
         }
     }

    public abstract TiaXml getTia(String xml);
}
