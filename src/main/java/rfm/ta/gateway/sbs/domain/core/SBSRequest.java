package rfm.ta.gateway.sbs.domain.core;

import java.util.List;

/**
 * SBS «Î«Û
 */
public class SBSRequest {
    private String termid;
    private String tellerid;
    private String auttlr;
    private String autpwd;
    private String txncode;
    private List<String> paramList;

    public SBSRequest(String termid, String tellerid, String txncode, List<String> paramList){
        this.termid = termid;
        this.tellerid = tellerid;
        this.txncode = txncode;
        this.paramList = paramList;
    }

    public SBSRequest(String termid, String tellerid, String auttlr, String autpwd, String txncode, List<String> paramList) {
        this.termid = termid;
        this.tellerid = tellerid;
        this.auttlr = auttlr;
        this.autpwd = autpwd;
        this.txncode = txncode;
        this.paramList = paramList;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public String getTxncode() {
        return txncode;
    }

    public String getTermid() {
        return termid;
    }

    public String getTellerid() {
        return tellerid;
    }

    public String getAuttlr() {
        return auttlr;
    }

    public void setAuttlr(String auttlr) {
        this.auttlr = auttlr;
    }

    public String getAutpwd() {
        return autpwd;
    }

    public void setAutpwd(String autpwd) {
        this.autpwd = autpwd;
    }
}
