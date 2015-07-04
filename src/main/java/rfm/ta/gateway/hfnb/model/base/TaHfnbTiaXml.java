package rfm.ta.gateway.hfnb.model.base;

import rfm.ta.gateway.hfnb.enums.TxnRtnCode;

import java.io.Serializable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

public abstract class TaHfnbTiaXml implements Serializable {
     public TaHfnbTiaXml transform(String xml) {
         try {
         return getTia(xml);
         }catch (Exception e) {
             throw new RuntimeException(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.toRtnMsg());
         }
     }

    public abstract TaHfnbTiaXml getTia(String xml);
}
