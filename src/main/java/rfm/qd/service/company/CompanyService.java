package rfm.qd.service.company;

import rfm.qd.repository.dao.RsFdccompanyMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsFdccompany;
import rfm.qd.repository.model.RsFdccompanyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CompanyService {
    @Autowired
    private RsFdccompanyMapper fdccompanyMapper;
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 判断房产商ID是否存在
     *
     * @param fdccompany
     * @return
     */
    public boolean isExistInDb(RsFdccompany fdccompany) {
        RsFdccompanyExample example = new RsFdccompanyExample();
        example.createCriteria().andCompanyIdEqualTo(fdccompany.getCompanyId());
        int cnt = fdccompanyMapper.countByExample(example);
        return (cnt >= 1);
    }

    /**
     * 是否并发更新冲突
     *
     * @param fdccompany
     * @return
     */
    public boolean isModifiable(RsFdccompany fdccompany) {
        RsFdccompany originRecord = fdccompanyMapper.selectByPrimaryKey(fdccompany.getPkId());
        if (!fdccompany.getModificationNum().equals(originRecord.getModificationNum())) {
            return false;
        }
        return true;
    }

    /**
     * 新增房产商信息
     *
     * @param fdccompany
     */
    @Transactional
    public void insertRsFdccompany(RsFdccompany fdccompany) {
        String newId = commonMapper.selectNewCompanyId();
        fdccompany.setCompanyId(newId);
        OperatorManager om = SystemService.getOperatorManager();
        fdccompany.setCreatedBy(om.getOperatorId());
        fdccompany.setCreatedDate(new Date());
        fdccompany.setLastUpdBy(om.getOperatorId());
        fdccompany.setLastUpdDate(new Date());
        fdccompanyMapper.insertSelective(fdccompany);
    }

    /**
     * 并发更新
     *
     * @param fdccompany
     */
    @Transactional
    public void updateRsFdccompany(RsFdccompany fdccompany) {
        if (isModifiable(fdccompany)) {
            fdccompany.setModificationNum(fdccompany.getModificationNum() + 1);
            OperatorManager om = SystemService.getOperatorManager();
            fdccompany.setLastUpdBy(om.getOperatorId());
            fdccompany.setLastUpdDate(new Date());
            fdccompanyMapper.updateByPrimaryKeySelective(fdccompany);
        } else {
            throw new RuntimeException("并发更新冲突！UUID=" + fdccompany.getPkId());
        }
    }

    /**
     * 查询所有未删除记录
     *
     * @return
     */
    public List<RsFdccompany> qryAllRecords() {
        RsFdccompanyExample example = new RsFdccompanyExample();
        example.createCriteria().andDeleteFlagEqualTo("0");
        return fdccompanyMapper.selectByExample(example);
    }

    /**
     * 按名称模糊查询房产公司
     *
     * @param companyName
     * @return
     */
    public List<RsFdccompany> qryRsFdccompanyByName(String companyName) {
        RsFdccompanyExample example = new RsFdccompanyExample();
        example.createCriteria().andCompanyNameLike("%" + companyName + "%").andDeleteFlagEqualTo("0");
        return fdccompanyMapper.selectByExample(example);
    }
    /**
     * 单笔查询
     *
     * @param pkid
     * @return RsFdccompany
     */
    public RsFdccompany selectedReocrdByPK(String pkid) {
        return fdccompanyMapper.selectByPrimaryKey(pkid);
    }

    public List<SelectItem> selectItemsCompany(String newAdd) {
        List<SelectItem> enumOptions = new ArrayList<SelectItem>();
        if (newAdd != null) {
            SelectItem siNew = new SelectItem("",newAdd);
            enumOptions.add(siNew);
        }
        List<RsFdccompany> rsFdccompanies = qryAllRecords();
        if (rsFdccompanies.size() > 0) {
            for (RsFdccompany rf:rsFdccompanies) {
                SelectItem si = new SelectItem(rf.getCompanyId(),rf.getCompanyName());
                enumOptions.add(si);
            }
        }
        return enumOptions;
    }
}
