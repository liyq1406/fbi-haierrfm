package rfm.ta.view.deposit;

import org.springframework.transaction.annotation.Transactional;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.ToaXml;

import java.lang.reflect.Field;

/**
 * Created by XIANGYANG on 2015-7-1.
 * 交易处理
 */

public abstract class DepAbstractTxnProcessor {

    public abstract ToaXml process(TiaXml tia) throws Exception;

    @Transactional
    public ToaXml run(TiaXml tia) throws Exception {
        return process(tia);
    }

    //TODO--该函数主要功能为将请求报文头中的info拷贝到响应报文头--ADD BY YXY 20150704
    public void copyTiaInfoToToa(TiaXml tia, ToaXml toa) {
        try {
            Field[] fields = tia.getClass().getFields();
            Class toaCLass = toa.getClass();
            Object obj = null;
            for (Field field : fields) {
                if ("info".equals(field.getName())){
                    obj = field.get(tia);
                    if (obj != null) {
                        Field toaField = toaCLass.getField(field.getName());
                        if (toaField != null) {
                            Field[] tiaInfoFields = Class.forName(field.getType().getName()).newInstance().getClass().getFields();
                            Class toaInfoCLass = Class.forName(toaCLass.getField(field.getName()).getType().getName()).newInstance().getClass();
                            Object tiaInfoObj = null;
                            for (Field tiaInfofield : tiaInfoFields) {
                                tiaInfoObj = tiaInfofield.get(obj);
                                if (tiaInfoObj != null) {
                                    Field toaInfoField = toaInfoCLass.getField(tiaInfofield.getName());
                                    if (toaInfoField != null) {
                                        toaInfoField.set(toaField.get(toa), tiaInfoObj);
                                    }
                                }
                            }
                        }
                    }
                }else{
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DepAbstractTxnProcessor copyTiaInfoToToa 解析异常");
        }
    }
}
