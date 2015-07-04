package rfm.ta.view.deposit;

import java.io.IOException;

/**
 * Created by XIANGYANG on 2015-7-1.
 * 交易处理
 */

public interface TxnProcessor {
    String process(String userid, String msgData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException;
}
