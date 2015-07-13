package platform.view;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User Think
 * Date 13-3-26
 * Time 下午6:12
 */
@ManagedBean
@ViewScoped
public class PtenudetailAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(PtenudetailAction.class);

    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService ptenudetailService;

    private Map<String,String> taAccTypeMap;
    private List<SelectItem> taAccTypeList;

    private Map<String,String> taAccStatusMap;
    private List<SelectItem> taAccStatusList;

    @PostConstruct
    public void init() {
        try {
            this.taAccTypeMap = ptenudetailService.getTaAccTypeMap();
            this.taAccTypeList=ptenudetailService.getTaAccTypeList();
            this.taAccStatusMap=ptenudetailService.getTaAccStatusMap();
            this.taAccStatusList=ptenudetailService.getTaAccStatusList();

        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }

    public Map<String, String> getTaAccTypeMap() {
        return taAccTypeMap;
    }

    public void setTaAccTypeMap(Map<String, String> taAccTypeMap) {
        this.taAccTypeMap = taAccTypeMap;
    }

    public List<SelectItem> getTaAccTypeList() {
        return taAccTypeList;
    }

    public void setTaAccTypeList(List<SelectItem> taAccTypeList) {
        this.taAccTypeList = taAccTypeList;
    }

    public Map<String, String> getTaAccStatusMap() {
        return taAccStatusMap;
    }

    public void setTaAccStatusMap(Map<String, String> taAccStatusMap) {
        this.taAccStatusMap = taAccStatusMap;
    }

    public List<SelectItem> getTaAccStatusList() {
        return taAccStatusList;
    }

    public void setTaAccStatusList(List<SelectItem> taAccStatusList) {
        this.taAccStatusList = taAccStatusList;
    }
    //职能字段 End
}
