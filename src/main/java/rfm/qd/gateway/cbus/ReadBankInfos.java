package rfm.qd.gateway.cbus;

import rfm.qd.repository.dao.CbsBankInfoMapper;
import rfm.qd.repository.model.CbsBankInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-5-8
 * Time: ÏÂÎç3:46
 * To change this template use File | Settings | File Templates.
 */
public class ReadBankInfos {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});

            File file = new File("F:\\fbi-brzfdc\\doc\\bankno.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            CbsBankInfoMapper bankInfoMapper = (CbsBankInfoMapper) context.getBean("cbsBankInfoMapper");
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("!");
                CbsBankInfo cbsBankInfo = new CbsBankInfo();
                cbsBankInfo.setCode(fields[0]);
                cbsBankInfo.setUpCode(fields[1]);
                cbsBankInfo.setAreaCode(fields[2]);
                cbsBankInfo.setFullName(fields[3]);
                cbsBankInfo.setShortName(fields[4]);
                cbsBankInfo.setAddress(fields[5]);
                cbsBankInfo.setStatus(fields[6]);
                bankInfoMapper.insert(cbsBankInfo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
