package rfm.ta.gateway.sbs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rfm.qd.repository.dao.QdCbsBankInfoMapper;
import rfm.qd.repository.model.QdCbsBankInfo;

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
            QdCbsBankInfoMapper bankInfoMapper = (QdCbsBankInfoMapper) context.getBean("qdCbsBankInfoMapper");
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("!");
                QdCbsBankInfo qdCbsBankInfo = new QdCbsBankInfo();
                qdCbsBankInfo.setCode(fields[0]);
                qdCbsBankInfo.setUpCode(fields[1]);
                qdCbsBankInfo.setAreaCode(fields[2]);
                qdCbsBankInfo.setFullName(fields[3]);
                qdCbsBankInfo.setShortName(fields[4]);
                qdCbsBankInfo.setAddress(fields[5]);
                qdCbsBankInfo.setStatus(fields[6]);
                bankInfoMapper.insert(qdCbsBankInfo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
