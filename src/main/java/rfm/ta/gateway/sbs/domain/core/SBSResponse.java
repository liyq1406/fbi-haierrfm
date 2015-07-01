package rfm.ta.gateway.sbs.domain.core;


import rfm.ta.gateway.sbs.domain.core.domain.SOFForm;

import java.util.ArrayList;
import java.util.List;

/**
 * SBS响应
 */
public class SBSResponse {
    protected List<String> formCodes = new ArrayList<>();
    protected List<SOFForm> sofForms = new ArrayList<>();

    public List<String> getFormCodes() {
        return formCodes;
    }

    public List<SOFForm> getSofForms() {
        return sofForms;
    }

    // 解析报文
    public void init(byte[] buffer) {
        int index = 0;
        do {
            // 包不是以空格开头，则视为空包，不解析
            if (buffer[index] != 0x20) {
                return;
            }
            SOFForm sofForm = new SOFForm();
            sofForm.assembleFields(index, buffer);
            formCodes.add(sofForm.getFormHeader().getFormCode());
            sofForms.add(sofForm);
            index += sofForm.length;
        } while (buffer.length > index);
    }
}
