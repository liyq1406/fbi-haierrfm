package rfm.ta.gateway.sbs.domain.base;

import java.lang.reflect.Field;

public abstract class AbstractFrameMsg {

    protected int offset = 0;

    //字段类型  1：字符串 2：短整数
    protected int[] fieldTypes;

    //字段长度  字节数
    protected int[] fieldLengths;

    // 实例化域
    public void assembleFields(byte[] buffer) {
        Class clazz = this.getClass();
        try {
            Field[] fields = clazz.getDeclaredFields();
            int pos = this.offset;
            for (int i = 0; i < this.fieldLengths.length; i++) {
                byte[] bytes = new byte[fieldLengths[i]];
                System.arraycopy(buffer, pos, bytes, 0, bytes.length);
                fields[i].setAccessible(true);
                if (this.fieldTypes[i] == 1) {
                    String s = new String(bytes);
                    System.out.println("【域】" + fields[i].getName() + " : " + s);
                    fields[i].set(this, s);
                }else if (this.fieldTypes[i] == 2) {
                    short len = 0;
                    len = (short)((bytes[0] << 8 & 0xFF00 )| (bytes[1]  & 0x00FF ));
                    fields[i].set(this, len);
                }else {
                    throw new RuntimeException("字段类型");
                }
                pos += bytes.length;
            }
        } catch (Exception e) {
            throw new RuntimeException("报文处理有误！", e);
        }
    }


    public int getMessageLength(){
        int len = 0;
        for (int fieldLength : this.fieldLengths) {
            len += fieldLength;
        }
        return len;
    }

    public int getOffset() {
        return offset;
    }

}
