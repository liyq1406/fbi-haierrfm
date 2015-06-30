package rfm.ta.view;

import org.apache.log4j.Logger;
import platform.common.utils.MessageUtil;
import rfm.ta.service.sbsservice.DataExchangeService;
import rfm.ta.tagateway.tasbs.core.domain.SOFForm;
import rfm.ta.tagateway.tasbs.txn.model.form.re.T531;
import rfm.ta.tagateway.tasbs.txn.model.form.re.T999;
import rfm.ta.tagateway.tasbs.txn.model.msg.Maa41;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lichao.W At 2015/6/29 16:53
 * wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class TASBSAccountAction implements Serializable{
    private static Logger logger = Logger.getLogger(TASBSAccountAction.class);

    @ManagedProperty(value = "#{dataExchangeService}")
    private DataExchangeService dataExchangeService;

    private Maa41 maa41;
    private T531 t531 = new T531();

    public String onConfirm(){
        try {
            String biadate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            maa41 = new Maa41("801000028201042001", "801090106005022001","100.00" );
            maa41.setTXNDAT("20150701");
            maa41.setREMARK("TAFC");
            maa41.setPASSNO("TAFC" + biadate);//票据编号
//            SOFForm form = dataExchangeService.callSbsTxn("aa41", maa41).get(0);
//            String formcode = form.getFormHeader().getFormCode();
            List<SOFForm> formList = dataExchangeService.callSbsTxn("aa41", maa41);
            String formcode = formList.get(0).getFormHeader().getFormCode();
            if ("T531".equals(formcode)){
                t531 = (T531) formList.get(0).getFormBody();
                System.out.println("房地产入账成功！"+t531.getACTNM1());
            }else if ("T999".equals(formcode)){
                T999 t999 = (T999) formList.get(0).getFormBody();
                System.out.println(t999.getT999MSG());
            }
        }catch (Exception e){
            logger.error("房地产入账失败", e);
            MessageUtil.addError("房地产入账异常.");
        }
        return null;
    }


    // = = = = = = = = = = = get set = = = = = = = = = = = = = =


    public DataExchangeService getDataExchangeService() {
        return dataExchangeService;
    }

    public void setDataExchangeService(DataExchangeService dataExchangeService) {
        this.dataExchangeService = dataExchangeService;
    }

    public Maa41 getMaa41() {
        return maa41;
    }

    public void setMaa41(Maa41 maa41) {
        this.maa41 = maa41;
    }

    public T531 getT531() {
        return t531;
    }

    public void setT531(T531 t531) {
        this.t531 = t531;
    }
}
