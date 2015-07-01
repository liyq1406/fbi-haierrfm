package rfm.ta.gateway.sbs.domain.core.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sbs��Ӧ����form
 */
public class SOFForm {
    private static Logger logger = LoggerFactory.getLogger(SOFForm.class);

    public int length;                                      // Form�ܳ���
    public final static int formHeaderLength = 27;          // Formͷ����
    public final static int formBodyFieldLength = 2;        // Form�峤�����ֽ���
    public short formBodyLength;                            // Form�峤��
    private SOFFormHeader formHeader;                       // Formͷ
    private SOFFormBody formBody;                           // Form��

    public void assembleFields(int offset, byte[] buffer) {
        // ��ͷ����
        formHeader = new SOFFormHeader();
        formHeader.assembleFields(offset, buffer);
        // ���峤��
        byte[] dataLengthBytes = new byte[formBodyFieldLength];
        System.arraycopy(buffer, offset + formHeaderLength, dataLengthBytes, 0, formBodyFieldLength);
        short s1 = (short) (dataLengthBytes[1] & 0xff);
        short s2 = (short) ((dataLengthBytes[0] << 8) & 0xff00);
        formBodyLength = (short) (s2 | s1);
        // ���ܳ���
        length = formHeaderLength + formBodyFieldLength + formBodyLength;
        // ����
        logger.info("FormCode:" + formHeader.getFormCode() + " ���峤�ȣ�" + formBodyLength);
        if (formBodyLength != 0) {
            try {
                // ʵ����Form��
                // ����ϵͳ���ж�
                logger.info("Form class:" + "rfm.ta.gateway.sbs.domain.txn.model.form." + formHeader.getFormSys().toLowerCase() + "." + formHeader.getFormCode());
                Class clazz = Class.forName("rfm.ta.gateway.sbs.domain.txn.model.form." + formHeader.getFormSys().toLowerCase() + "." + formHeader.getFormCode());
//                Class clazz = Class.forName("gateway.sbs.txn.model.form." + formHeader.getFormCode());
                formBody = (SOFFormBody) clazz.newInstance();
                // ��ȡForm���ֽ�����
                byte[] bodyBytes = new byte[formBodyLength];
                System.arraycopy(buffer, offset + formHeaderLength + formBodyFieldLength, bodyBytes, 0, formBodyLength);
                // װ��Form��
                formBody.assembleFields(0, bodyBytes);
            } catch (Exception e) {
                logger.error("Form��������", e);
                throw new RuntimeException("Form��������" + formHeader.getFormCode());
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
