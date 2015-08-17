package rfm.ta.view;

import common.utils.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012701;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.EnuTaBankId;
import rfm.ta.common.enums.EnuTaCityId;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.account.TaAccService;
import rfm.ta.service.account.TaBlncReconciService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * （泰）余额对账Action
 * Created by Thinkpad on 2015/8/13.
 */
@ManagedBean
@ViewScoped
public class TaBlncReconciAction {
    private static final Logger logger = LoggerFactory.getLogger(TaBlncReconciAction.class);

    @ManagedProperty(value = "#{taBlncReconciService}")
    private TaBlncReconciService taBlncReconciService;

    @ManagedProperty(value = "#{taAccService}")
    private TaAccService taAccService;

    private List<TaRsAcc> taRsAccList;

    @PostConstruct
    public void init() {
        // 取得所有监管中的监管账号数据
        taRsAccList = taAccService.qryAllMonitRecords();
    }

    /**
     * 获取sbs数据
     */
    public void onQryLocaldata() {
        try {
            if(taRsAccList.size() <=0) {
                return;
            }

            // 发送监管账号到SBS查询余额
            //List<Toa900012701> toaSbs=taBlncReconciService.sendAndRecvRealTimeTxn900012701(taRsAccList);
            // test start
            List<Toa900012701> toaSbs = new ArrayList<Toa900012701>();
            Toa900012701 toa = null;
            List<Toa900012701.BodyDetail> details = null;
            Toa900012701.BodyDetail detail = null;

            toa = new Toa900012701();
            details = new ArrayList<Toa900012701.BodyDetail>();
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test1");
            detail.setBOKBAL("10");
            details.add(detail);
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test2");
            detail.setBOKBAL("20");
            details.add(detail);
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test3");
            detail.setBOKBAL("30");
            details.add(detail);
            toa.body.DETAILS = details;
            toaSbs.add(toa);

            toa = new Toa900012701();
            details = new ArrayList<Toa900012701.BodyDetail>();
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test4");
            detail.setBOKBAL("40");
            details.add(detail);
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test5");
            detail.setBOKBAL("50");
            details.add(detail);
            detail = new Toa900012701.BodyDetail();
            detail.setACTNUM("test6");
            detail.setBOKBAL("60");
            details.add(detail);
            toa.body.DETAILS = details;
            toaSbs.add(toa);

            // test end
            if(toaSbs !=null && toaSbs.size() > 0) {
                String sysdate = ToolUtil.getStrLastUpdDate();
                taRsAccList = new ArrayList<TaRsAcc>();
                TaRsAcc taRsAcc = null;
                for(Toa900012701 toa900012701:toaSbs){
                    for(Toa900012701.BodyDetail bodyDetail:toa900012701.body.DETAILS){
                        taRsAcc = new TaRsAcc();
                        taRsAcc.setAccId(bodyDetail.ACTNUM);
                        taRsAcc.setTxDate(sysdate);
                    }
                }
            }
        }catch (Exception e){
            logger.error("获取sbs数据，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 余额对账
     */
    public void onReconciliation(){
        try {
            if(taRsAccList.size() <=0) {
                return;
            }

//            createFile(taRsAccList);
        } catch (Exception e) {
            logger.error("余额对账，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void createFile(List<Toa900012701> toaSbs) {
        String sysdate = ToolUtil.getStrLastUpdDate();
        File file = null;
        String filePath = "d:";
        String fileName = "BF" + EnuTaBankId.BANK_HAIER.getCode() +
                EnuTaCityId.CITY_TAIAN.getCode() +
                ToolUtil.getStrToday() +".dat";
        String newLineCh = "\r\n";
        StringBuffer line = new StringBuffer();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            file = ToolUtil.createFile(filePath, fileName);
            if(file != null){
                for(Toa900012701 toa900012701:toaSbs){
                    for(Toa900012701.BodyDetail bodyDetail:toa900012701.body.DETAILS){
                        line.append(StringUtils.rightPad(bodyDetail.ACTNUM, 30, ' '));
                        line.append("|");
                        line.append(StringUtils.rightPad(bodyDetail.BOKBAL, 20, ' '));
                        line.append("|");
                        line.append(sysdate);
                        line.append("|");
                        line.append(newLineCh);
                    }
                }
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(line.toString());
                bw.flush();
                boolean result = ToolUtil.uploadFile("rfmtest", fileName, file);
                if(!result){
                    logger.error("ftp发送房产中心失败!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(filePath + fileName + ".dat", e);
        } finally {
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
            if(file != null && file.exists()){
                file.delete();
            }
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaBlncReconciService getTaBlncReconciService() {
        return taBlncReconciService;
    }

    public void setTaBlncReconciService(TaBlncReconciService taBlncReconciService) {
        this.taBlncReconciService = taBlncReconciService;
    }

    public TaAccService getTaAccService() {
        return taAccService;
    }

    public void setTaAccService(TaAccService taAccService) {
        this.taAccService = taAccService;
    }

    public List<TaRsAcc> getTaRsAccList() {
        return taRsAccList;
    }

    public void setTaRsAccList(List<TaRsAcc> taRsAccList) {
        this.taRsAccList = taRsAccList;
    }
}
