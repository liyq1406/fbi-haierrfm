package rfm.qd.service;

import rfm.qd.repository.dao.CbsBankInfoMapper;
import rfm.qd.repository.model.CbsBankInfo;
import rfm.qd.repository.model.CbsBankInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-5-9
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BankInfoService {

    @Autowired
    private CbsBankInfoMapper cbsBankInfoMapper;

    public List<SelectItem> qryBankInfoListByNoAndName(String code, String name) {
        CbsBankInfoExample example = new CbsBankInfoExample();
        List<SelectItem> items = new ArrayList<SelectItem>();
        if (!name.contains(" ")) {
            example.createCriteria().andCodeLike("%" + code + "%").andFullNameLike("%" + name + "%");
        } else {
            CbsBankInfoExample.Criteria conditions = example.createCriteria().andCodeLike("%" + code + "%");
            for (String s : name.split(" ")) {
                conditions.andFullNameLike("%" + s + "%");
            }
        }
        int cnt = cbsBankInfoMapper.countByExample(example);
        if (cnt > 30) {
            throw new RuntimeException("查询到银行记录数太多，请细化查询条件！");
        }
        List<CbsBankInfo> cbsBankInfos = cbsBankInfoMapper.selectByExample(example);
        for (CbsBankInfo record : cbsBankInfos) {
            SelectItem item = new SelectItem(record.getCode(), record.getFullName());
            items.add(item);
        }
        return items;
    }
}
