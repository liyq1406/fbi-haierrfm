package rfm.ta.view.deposit;

import rfm.ta.gateway.hfnb.model.base.TaHfnbTiaXml;
import rfm.ta.gateway.hfnb.model.base.TaHfnbToaXml;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by XIANGYANG on 2015-7-1.
 * 交易处理
 */

public abstract class AbstractTxnProcessor implements TxnProcessor {
    public abstract String process(String userid, String msgData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException;

    public void copyTiaInfoToToa(TaHfnbTiaXml tia, TaHfnbToaXml toa) {
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
            throw new RuntimeException("AbstractTxnProcessor copyTiaInfoToToa 解析异常");
        }
    }
}
