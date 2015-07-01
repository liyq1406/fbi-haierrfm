package rfm.ta.gateway.sbs.domain.core.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sbs响应报文form
 */
public class SOFForm {
    private static Logger logger = LoggerFactory.getLogger(SOFForm.class);

    public int length;                                      // Form总长度
    public final static int formHeaderLength = 27;          // Form头长度
    public final static int formBodyFieldLength = 2;        // Form体长度域字节数
    public short formBodyLength;                            // Form体长度
    private SOFFormHeader formHeader;                       // Form头
    private SOFFormBody formBody;                           // Form体

    public void assembleFields(int offset, byte[] buffer) {
        // 包头解析
        formHeader = new SOFFormHeader();
        formHeader.assembleFields(offset, buffer);
        // 包体长度
        byte[] dataLengthBytes = new byte[formBodyFieldLength];
        System.arraycopy(buffer, offset + formHeaderLength, dataLengthBytes, 0, formBodyFieldLength);
        short s1 = (short) (dataLengthBytes[1] & 0xff);
        short s2 = (short) ((dataLengthBytes[0] << 8) & 0xff00);
        formBodyLength = (short) (s2 | s1);
        // 包总长度
        length = formHeaderLength + formBodyFieldLength + formBodyLength;
        // 包体
        logger.info("FormCode:" + formHeader.getFormCode() + " 包体长度：" + formBodyLength);
        if (formBodyLength != 0) {
            try {
                // 实例化Form体
                // 新增系统别判断
                logger.info("Form class:" + "rfm.ta.gateway.sbs.domain.txn.model.form." + formHeader.getFormSys().toLowerCase() + "." + formHeader.getFormCode());
                Class clazz = Class.forName("rfm.ta.gateway.sbs.domain.txn.model.form." + formHeader.getFormSys().toLowerCase() + "." + formHeader.getFormCode());
//                Class clazz = Class.forName("gateway.sbs.txn.model.form." + formHeader.getFormCode());
                formBody = (SOFFormBody) clazz.newInstance();
                // 截取Form体字节数据
                byte[] bodyBytes = new byte[formBodyLength];
                System.arraycopy(buffer, offset + formHeaderLength + formBodyFieldLength, bodyBytes, 0, formBodyLength);
                // 装配Form体
                formBody.assembleFields(0, bodyBytes);
            } catch (Exception e) {
                logger.error("Form解析错误", e);
                throw new RuntimeException("Form解析错误：" + formHeader.getFormCode());
            }
        }
    }

    public SOFFormHeader getFormHeader() {
        return formHeader;
    }

    public SOFFormBody getFormBody() {
        return formBody;
    }
}
