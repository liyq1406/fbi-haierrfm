package rfm.qd.gateway.cbus;

import rfm.qd.gateway.cbus.domain.txn.QDJG02Res;
import rfm.qd.gateway.cbus.domain.txn.QDJG03Res;
import rfm.qd.gateway.service.CbusTxnService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-24
 * Time: ����12:48
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
        CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
        // ��ѯ������ϸ
//        List<QDJG02Res> res02List = cbusTxnService.qdjg02qryActtxnsByParams("374100201020000640", "20120427", "20120427");
        List<QDJG02Res> res02List = cbusTxnService.qdjg02qryActtxnsByParams("810200101421001610", "20120507", "20120511");
//        List<QDJG02Res> res02List = cbusTxnService.qdjg02qryActtxnsByParams("374100201020000640", "20111001", "20120507");
        System.out.println("[���İ���]: " + res02List.size());
        int i = 1;
        for (QDJG02Res res : res02List) {
            System.out.println("[���İ�" + (i++) + "�ڼ�¼��]:" + res.recordList.size());
        }
    }

    public static void test01(String account) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
        cbusTxnService.qdjg01QryActbal(account);
    }

   /* public static void test05(String account) throws Exception {
            ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
            CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
            cbusTxnService.qdjg05QryActbal(account);
        }*/

    public static void test03() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
        QDJG03Res res = cbusTxnService.qdjg03payAmtInBank("810200101421001610", "810200101421001548", "2.0", "����ת��5-14");
        System.out.println(res.getHeader().getRtnCode() + res.rtnMsg);
    }

    public static void test04() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        CbusTxnService cbusTxnService = (CbusTxnService) context.getBean("cbusTxnService");
        String bankNo = "104829016059";
        String payOutAcctName = "ת���˻�";
        String payOutAcctNo = "810200101421001610";

        String payInAcctName = "ת���˻�";
        String payInAcctNo = "710200101421001610";
        String amt = "1.0";
        String purpose = "���";



        cbusTxnService.qdjg04payAmtBtwnBank(bankNo, payOutAcctName, payOutAcctNo, payInAcctName, payInAcctNo, amt, purpose,
                "0703", "11", "���Ե��");

    }
}
