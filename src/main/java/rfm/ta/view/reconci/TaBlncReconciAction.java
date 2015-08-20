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
 * ��̩��������Action
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
        // ȡ�����м���еļ���˺�����
        taRsAccList = taAccService.qryAllMonitRecords();
    }

    /**
     * ��ȡsbs����
     */
    public void onQryLocaldata() {
        try {
            if(taRsAccList.size() <=0) {
                MessageUtil.addError("û�м���˺ţ��޷���ȡsbs����");
                return;
            }

            // ���ͼ���˺ŵ�SBS��ѯ���
            List<Toa900012701> toaSbs=taSbsService.sendAndRecvRealTimeTxn900012701(taRsAccList);

            if(toaSbs !=null && toaSbs.size() > 0) {
                // �����Ƿ���ڲ�ѯʧ��
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
            logger.error("��ȡsbs���ݣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ������
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
                    MessageUtil.addInfo("ftp���ͷ������ĳɹ�!");
                } else{
                    MessageUtil.addError("ftp���ͷ�������ʧ��!");
                }
            }
        } catch (Exception e) {
            logger.error("�����ˣ�", e);
            MessageUtil.addError(e.getMessage());
        } finally {
            if(file != null && file.exists()){
                file.delete();
            }
        }
    }

    /**
     * ����dat�ļ�
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
