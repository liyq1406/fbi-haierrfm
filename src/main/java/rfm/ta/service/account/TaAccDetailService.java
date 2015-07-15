package rfm.ta.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaRsAccDtlMapper;
import rfm.ta.repository.dao.TaTxnSbsMapper;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;
import rfm.ta.repository.model.TaTxnSbs;

import java.util.List;

/**
 * Created by Lichao.W At 2015/7/7 9:22
 * wanglichao@163.com
 */
@Service
public class TaAccDetailService {
    @Autowired
    private TaRsAccDtlMapper taRsAccDtlMapper;

    @Autowired
    private TaTxnSbsMapper taTxnSbsMapper;

    public List<TaRsAccDtl> detailAllList (){
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        return taRsAccDtlMapper.selectByExample(example);
    }

    public int sbsdatcopy(TaTxnSbs taTxnSbs){
        return taTxnSbsMapper.insert(taTxnSbs);
    }

    public TaRsAccDtlMapper getTaRsAccDtlMapper() {
        return taRsAccDtlMapper;
    }

    public void setTaRsAccDtlMapper(TaRsAccDtlMapper taRsAccDtlMapper) {
        this.taRsAccDtlMapper = taRsAccDtlMapper;
    }

    public TaTxnSbsMapper getTaTxnSbsMapper() {
        return taTxnSbsMapper;
    }

    public void setTaTxnSbsMapper(TaTxnSbsMapper taTxnSbsMapper) {
        this.taTxnSbsMapper = taTxnSbsMapper;
    }
}
