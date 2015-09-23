package org.fbi.dep.model.base;

import common.Enums.TxnRtnCode;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-4-3
 * Time: ионГ12:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class TiaXml implements Serializable{
     public TiaXml transform(String xml) {
         try {
         return getTia(xml);
         }catch (Exception e) {
             throw new RuntimeException(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.toRtnMsg());
         }
     }

    public abstract TiaXml getTia(String xml);
}
