package rfm.ta.gateway.sbs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rfm.ta.gateway.sbs.domain.txn.QDJG02Res;
import rfm.ta.gateway.sbs.domain.txn.QDJG03Res;
import rfm.ta.gateway.service.TaSbsTxnService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-24
 * Time: 下午12:48
 * To change this template use File | Settings | File Templates.
 */
public class CbusTest {

    public static void main(String[] args) {
        try {
//            test04();
            // 374100201020000640  4-27
            test02();
//            test03();
//            test01("810200101421001610");
//            test05("810200101421001610");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void test02() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        TaSbsTxnService taSbsTxnService = (TaSbsTxnService) context.getBean("qdSbsTxnService");
        // 查询交易明细
//        List<QDJG02Res> res02List = cbusTxnService.qdjg02qryActtxnsByParams("374100201020000640", "20120427", "20120427");
        List<QDJG02Res> res02List = taSbsTxnService.qdjg02qryActtxnsByParams("810200101421001610", "20120507", "20120511");
//        List<QDJG02Res> res02List = cbusTxnService.qdjg02qryActtxnsByParams("374100201020000640", "20111001", "20120507");
        System.out.println("[报文包数]: " + res02List.size());
        int i = 1;
        for (QDJG02Res res : res02List) {
            System.out.println("[报文包" + (i++) + "内记录数]:" + res.recordList.size());
        }
    }

    public static void test01(String account) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        TaSbsTxnService taSbsTxnService = (TaSbsTxnService) context.getBean("qdSbsTxnService");
        taSbsTxnService.qdjg01QryActbal(account);
    }

   /* public static void test05(String account) throws Exception {
            ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
            CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
            cbusTxnService.qdjg05QryActbal(account);
        }*/

    public static void test03() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        TaSbsTxnService taSbsTxnService = (TaSbsTxnService) context.getBean("qdSbsTxnService");
        QDJG03Res res = taSbsTxnService.qdjg03payAmtInBank("810200101421001610", "810200101421001548", "2.0", "行内转账5-14");
        System.out.println(res.getHeader().getRtnCode() + res.rtnMsg);
    }

    public static void test04() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        TaSbsTxnService taSbsTxnService = (TaSbsTxnService) context.getBean("qdSbsTxnService");
        String bankNo = "104829016059";
        String payOutAcctName = "转出账户";
        String payOutAcctNo = "810200101421001610";

        String payInAcctName = "转入账户";
        String payInAcctNo = "710200101421001610";
        String amt = "1.0";
        String purpose = "电汇";



        taSbsTxnService.qdjg04payAmtBtwnBank(bankNo, payOutAcctName, payOutAcctNo, payInAcctName, payInAcctNo, amt, purpose,
                "0703", "11", "测试电汇");

    }
}
