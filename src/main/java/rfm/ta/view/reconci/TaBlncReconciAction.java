package rfm.ta.view.reconci;

import common.utils.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012701;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.EnuTaBankId;
import rfm.ta.common.enums.EnuTaCityId;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.dep.TaSbsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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

    @ManagedProperty(value = "#{taSbsService}")
    private TaSbsService taSbsService;

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
                MessageUtil.addError("没有监管账号，无法获取sbs数据");
                return;
            }

            // 发送监管账号到SBS查询余额
            List<Toa900012701> toaSbs=taSbsService.sendAndRecvRealTimeTxn900012701(taRsAccList);

            if(toaSbs !=null && toaSbs.size() > 0) {
                // 遍历是否存在查询失败
                for(Toa900012701 toa900012701:toaSbs) {
                    if (!"0000".equals(toa900012701.header.RETURN_CODE)) {
                        MessageUtil.addError(toa900012701.header.RETURN_MSG);
                        return;
                    }
                }

                String sysdate = ToolUtil.getStrLastUpdDate();
                taRsAccList = new ArrayList<>();
                TaRsAcc taRsAcc = null;
                for(Toa900012701 toa900012701:toaSbs){
                    for(Toa900012701.BodyDetail bodyDetail:toa900012701.body.DETAILS){
                        taRsAcc = new TaRsAcc();
                        taRsAcc.setAccId(bodyDetail.ACTNUM);
                        taRsAcc.setTxAmt(bodyDetail.BOKBAL);
                        taRsAcc.setTxDate(sysdate);
                        taRsAccList.add(taRsAcc);
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
        File file = null;
        try {
            if(taRsAccList.size() <=0) {
                return;
            }
            String fileName = "BF" + EnuTaBankId.BANK_HAIER.getCode() +
                    EnuTaCityId.CITY_TAIAN.getCode() +
                    ToolUtil.getStrToday() +".dat";
            file = createFile(fileName);
            if(file != null){
                boolean result = ToolUtil.uploadFile("rfmtest", fileName, file);
                if(result){
                    MessageUtil.addInfo("ftp发送房产中心成功!");
                } else{
                    MessageUtil.addError("ftp发送房产中心失败!");
                }
            }
        } catch (Exception e) {
            logger.error("余额对账，", e);
            MessageUtil.addError(e.getMessage());
        } finally {
            if(file != null && file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 作成dat文件
     * @param fileName
     * @return
     */
    private File createFile(String fileName) {
        File file = null;
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/reconci");
        String newLineCh = "\r\n";
        StringBuffer line = new StringBuffer();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            file = ToolUtil.createFile(filePath, fileName);
            if(file != null){
                for(TaRsAcc taRsAcc : taRsAccList){
                    line.append(StringUtils.rightPad(taRsAcc.getAccId(), 30, ' '));
                    line.append("|");
                    line.append(StringUtils.rightPad(taRsAcc.getTxAmt(), 20, ' '));
                    line.append("|");
                    line.append(taRsAcc.getTxDate());
                    line.append("|");
                    line.append(newLineCh);
                }
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(line.toString());
                bw.flush();
            }

            return file;
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
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
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
