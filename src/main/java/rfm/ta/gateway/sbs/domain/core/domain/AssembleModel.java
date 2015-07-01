package rfm.ta.gateway.sbs.domain.core.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 可装配类
 */
public class AssembleModel implements Assemble {

    private static Logger logger = LoggerFactory.getLogger(AssembleModel.class);

    protected int offset = 0;

    //字段类型  1：字符串 2：短整数 3:BigDecimal 4:BigDecimal 自动除以100.0  5:int
    protected int[] fieldTypes;

    //字段长度  字节数
    protected int[] fieldLengths;

    public void assembleFields(int offset, byte[] buffer) {
        this.offset = offset;
        Class clazz = this.getClass();
        try {
            Field[] fields = clazz.getDeclaredFields();
            int pos = this.offset;
            BigDecimal bd100 = new BigDecimal("100.0");
            for (int i = 0; i < this.fieldLengths.length; i++) {
                byte[] bytes = new byte[fieldLengths[i]];
                System.arraycopy(buffer, pos, bytes, 0, bytes.length);
                fields[i].setAccessible(true);
                if (this.fieldTypes[i] == 1) {
                    String s = new String(bytes);
                    fields[i].set(this, s.trim());
                } else if (this.fieldTypes[i] == 2) {
                    short len = 0;
                    len = (short) ((bytes[0] << 8 & 0xFF00) | (bytes[1] & 0x00FF));
                    fields[i].set(this, len);
                } else if (this.fieldTypes[i] == 3) {
                    fields[i].set(this, new BigDecimal(new String(bytes).trim()));
                } else if (this.fieldTypes[i] == 4) {
                    fields[i].set(this, new BigDecimal(new String(bytes).trim()).divide(bd100));
                } else if (this.fieldTypes[i] == 5) {
                    fields[i].set(this, Integer.parseInt(new String(bytes).trim()));
                } else {
                    throw new RuntimeException("不支持的解析数据类型：" + this.fieldTypes[i]);
                }
                pos += bytes.length;
            }
        } catch (Exception e) {
            logger.error("报文解析异常"+offset, e);
            throw new RuntimeException("报文解析异常！", e);
        }
    }
}
