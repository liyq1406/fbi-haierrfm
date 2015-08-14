package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TIABody;
import org.fbi.dep.model.base.TIAHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanjianlong on 2015-8-10.
 * 查询多账户余额请求报文(面向SBS  交易号8119）
 */

public class Tia900012701 extends TIA implements Serializable {
    public Header header = new Header();
    public Body body = new Body();

    @Override
    public TIAHeader getHeader() {
        return  header;
    }

    @Override
    public TIABody getBody() {
        return  body;
    }

    //====================================================================
    public  static class Header extends TIAHeader {
    }

    public static class Body extends TIABody {
        public String TOTCNT;               //总笔数
        public List<BodyDetail> DETAILS; //帐户帐号列表
    }

    public static class BodyDetail implements Serializable {
        public String ACTNM;               // 帐户帐号

        public String getACTNM() {
            return ACTNM;
        }

        public void setACTNM(String ACTNM) {
            this.ACTNM = ACTNM;
        }
    }
}
