package rfm.ta.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaRsAccDetailMapper;
import rfm.ta.repository.dao.TaTxnSbsMapper;
import rfm.ta.repository.model.TaRsAccDetail;
import rfm.ta.repository.model.TaRsAccDetailExample;
import rfm.ta.repository.model.TaTxnSbs;

import java.util.List;

/**
 * Created by Lichao.W At 2015/7/7 9:22
 * wanglichao@163.com
 */
@Service
public class TaAccDetailService {
    @Autowired
    private TaRsAccDetailMapper taRsAccDetailMapper;

    @Autowired
    private TaTxnSbsMapper taTxnSbsMapper;

    public List<TaRsAccDetail> detailAllList (){
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        return taRsAccDetailMapper.selectByExample(example);
    }

    public int sbsdatcopy(TaTxnSbs taTxnSbs){
        return taTxnSbsMapper.insert(taTxnSbs);
    }

    public TaRsAccDetailMapper getTaRsAccDetailMapper() {
        return taRsAccDetailMapper;
    }

    public void setTaRsAccDetailMapper(TaRsAccDetailMapper taRsAccDetailMapper) {
        this.taRsAccDetailMapper = taRsAccDetailMapper;
    }

    public TaTxnSbsMapper getTaTxnSbsMapper() {
        return taTxnSbsMapper;
    }

    public void setTaTxnSbsMapper(TaTxnSbsMapper taTxnSbsMapper) {
        this.taTxnSbsMapper = taTxnSbsMapper;
    }
}
