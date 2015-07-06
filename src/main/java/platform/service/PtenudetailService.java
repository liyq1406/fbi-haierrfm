package platform.service;

import platform.repository.dao.PtenudetailMapper;
import platform.repository.model.Ptenudetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.repository.model.PtenudetailExample;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-4-22
 * Time: ����2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PtenudetailService {
    @Autowired
    private PtenudetailMapper enudetailMapper;

    /**
     * ����ö�ٱ��������������˵�
     *
     * @param enuName     ö������
     * @param isSelectAll �Ƿ�����ȫ����ѡ��
     * @param isExpandID  true:�����б���������ID�� false���б��а���ID
     * @return �����˵�
     */
    public List<SelectItem> getEnuSelectItemList(String enuName, boolean isSelectAll, boolean isExpandID) {
        List<Ptenudetail> records = selectEnuDetail(enuName);
        List<SelectItem> items = new ArrayList<>();
        SelectItem item;
        if (isSelectAll) {
            item = new SelectItem("", "ȫ��");
            items.add(item);
        }
        for (Ptenudetail record : records) {
            if (isExpandID) {
                item = new SelectItem(record.getEnuitemvalue(), record.getEnuitemvalue() + " " + record.getEnuitemlabel());
            } else {
                item = new SelectItem(record.getEnuitemvalue(), record.getEnuitemlabel());
            }
            items.add(item);
        }
        return items;
    }
    public SelectItem getEnuSelectItem(String enuName, int index) {
        List<SelectItem> items = getEnuSelectItemList(enuName,false,false);
        return items.get(index);
    }

    /**
     * ����ָ��ö���嵥
     * @param enuid
     * @return
     */
    public List<Ptenudetail> selectEnuDetail(String enuid) {
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuid);
        example.setOrderByClause(" dispno ");
        return enudetailMapper.selectByExample(example);
    }

    /**
     * ��ȡö�ٱ���ĳһ��� ��չ����ֵ
     * @param enuType
     * @param enuItemValue
     * @return
     */
    public String  selectEnuExpandValue(String enuType, String enuItemValue){
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuType).andEnuitemvalueEqualTo(enuItemValue);
        //TODO �����жϣ�
        return enudetailMapper.selectByExample(example).get(0).getEnuitemexpand();
    }

    /**
     * ���� ö��ֵ����չֵ�Ķ�Ӧ��ϵ MAP
     * @param enuType
     * @return
     */
    public Map<String,String> selectEnuItemValueToExpandValueMap(String enuType){
        PtenudetailExample example = new PtenudetailExample();
        example.createCriteria().andEnutypeEqualTo(enuType);
        List <Ptenudetail> records = enudetailMapper.selectByExample(example);
        Map<String,String> enuMap = new HashMap<String, String>();
        for (Ptenudetail record : records) {
            enuMap.put(record.getEnuitemvalue(), record.getEnuitemlabel());
        }
        return enuMap;
    }
}