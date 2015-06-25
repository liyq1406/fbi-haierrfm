package rfm.ta.gateway.xsocket.client;

import rfm.ta.gateway.fdc.BaseBean;
import rfm.ta.gateway.fdc.T000.T0002Res;
import rfm.ta.gateway.xsocket.crypt.des.DesCrypter;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-21
 * Time: 下午9:58
 * To change this template use File | Settings | File Templates.
 */
public class ClientTest {
    public static void main(String[] args) {

        //String dataGram = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head><OpCode>0002</OpCode><OpDate>20110525</OpDate><OpTime>140105</OpTime><BankCode>313</BankCode></Head><Param><Acct>123456789</Acct><AcctName>青岛市测试地产有限公司</AcctName><BeginDate>20110915</BeginDate><EndDate>20110925</EndDate></Param></root>";
        //testSendData(dataGram);
        try {
            System.out.println(miStr.getBytes().length);
           // String mingStr = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head><OpCode>2008</OpCode><OpDate>20110922</OpDate><OpTime>101621</OpTime><BankCode>313</BankCode></Head><Param><Acct>123456789</Acct><AcctName>青岛市测试地产有限公司</AcctName><PlanNO>20110900000012</PlanNO><SubmitDate>20110922</SubmitDate><PlanAmt>380000000</PlanAmt><PlanNum>3</PlanNum><RecordSet><PlanDetailNO>809253</PlanDetailNO><ToAcctName>青岛青建控股有限公司</ToAcctName><ToAcct></ToAcct><ToBankName>光大银行香港中路支行</ToBankName><Amt>150000000</Amt><PlanDate>20110922</PlanDate><Purpose>工程款</Purpose><Remark></Remark></RecordSet><RecordSet><PlanDetailNO>809255</PlanDetailNO><ToAcctName>青岛百通伟东房地产开发有限公司</ToAcctName><ToAcct></ToAcct><ToBankName>兴业银行青岛分行</ToBankName><Amt>200000000</Amt><PlanDate></PlanDate><Purpose>材料设备款</Purpose><Remark></Remark></RecordSet><RecordSet><PlanDetailNO>809257</PlanDetailNO><ToAcctName>青建集团股份公司房建二分公司</ToAcctName><ToAcct></ToAcct><ToBankName>建行青岛市北支行</ToBankName><Amt>30000000</Amt><PlanDate></PlanDate><Purpose>配套款</Purpose><Remark></Remark></RecordSet></Param></root>";
            //String mingStr = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head><OpCode>2007</OpCode><OpDate>20110922</OpDate><OpTime>112746</OpTime><BankCode>313</BankCode></Head><Param><Acct>123456789</Acct><AcctName>青岛市测试地产有限公司</AcctName><BuyerName>袁丽</BuyerName><BuyerAcct>637522435262533</BuyerAcct><BuyerBankName>建设银行</BuyerBankName><BuyerIDType>身份证</BuyerIDType><BuyerIDCode>370221197112190000</BuyerIDCode><ContractNum>201000149481</ContractNum><TotalAmt>37418600</TotalAmt><HouseAddress></HouseAddress><EndReason></EndReason><TransBuyerAmt>20000000</TransBuyerAmt></Param></root>";
            String mingStr = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head>" +
                    "<RetCode>0000</RetCode><RetMsg></RetMsg></Head></root>";
            String mi = DesCrypter.getInstance().encrypt(mingStr);
            System.out.println("转密文："+mi);
            System.out.println(mi.getBytes().length);
            System.out.println(miStr);
            testDecrypt(miStr.trim());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    static String miStr = "bd0f42ed9acbce34291ef663b47bd46f49850abf36e3f68265126a7c9d09bcd135b6e7c45d4c0d45f808b7098d3e5c42740996637d565bf54c21bc6392770c9b4a49c88b7ab2d4c0d5d27ddb0bc42b337fdba3fc75c4bb53d16b356cede675df37a9b20fc6cc574a1c65b0e02c84da8cce407b0233f002dc51e6bb2ef4546625ef41674a0f3cfee3e0030c949048debc989fb3cd082d6311e8583358dab2beac397dda02da0e8cfe2e6249fc5b9c78c82644711bdc0c039108fa29678cd5a6e4a411911cc80c1cb1d4a015403ce9ec68505622bbed1557aa72b9b8f34375d378db437eff353986e146f39be3f2c1d18e7fd8bbaca8ea3df17fcf86409fb209ec3a0a9e27ba761af4a0460c3be1aa37054c47c52f128eb7d04de430d5524be1f04c12e3690ff73b8ec6c8873eb22179b55ffae9576361b5a14c12e3690ff73b8e72b9b8f34375d378e1df92b50e3dc41740fc776615209ceafcce38584c3718a807ea9e0c6665ee69ce99d37d9eb1a141a6e751206b80b63700b7a9e03b14c76905498b5e84a4070cce99d37d9eb1a14114bea955baeb65e6e4db6ac602905d18b4e2dfa1ea64ee85380144c5a49fd29ffc83ddf6f495f21f5896f9ec7308c75d9316f8d861f1f087ec76a7001bd37dfc7931af06b10406959e88d59619f841a8fba3a61298c07e2f6e5865ce6ad4a43b97e87612290ce72083a525c7cb59451e0742d4d3c9696630477b1a2b0d1bf33075bec893738e3d10dd7ee84251ad61fa310b142b9826a03875bec893738e3d1009a9fea0b278868be6f517c5d4f0ce4c";

    public static void testSendData(String dataGram) {
        TaSocketComponent taSocketComponent = new TaSocketComponent();
        String recvMsg = null;
        try {
            recvMsg = taSocketComponent.sendAndRecvDataByBlockConn(dataGram);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        T0002Res resBean = (T0002Res) BaseBean.toObject(T0002Res.class, recvMsg);
        for(T0002Res.Param.Record record : resBean.param.recordList) {
            System.out.println("交易金额:"+record.Amt);
        }
    }

    public static void testDecrypt(String miStr) throws Exception, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        String strMing = DesCrypter.getInstance().decrypt(miStr);
        System.out.println("转为明文："+strMing);
    }
}
