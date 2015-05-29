package platform.service;

import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class SystemService {

    static private SimpleDateFormat sdfdate8 = new SimpleDateFormat("yyyyMMdd");
    static private SimpleDateFormat sdfdate10 = new SimpleDateFormat("yyyy-MM-dd");
    static private SimpleDateFormat sdftime6 = new SimpleDateFormat("HHmmss");
    static private SimpleDateFormat sdftime8 = new SimpleDateFormat("HH:mm:ss");
    static private SimpleDateFormat sdfdatetime14 = new SimpleDateFormat("yyyyMMddHHmmss");
    static private SimpleDateFormat sdfdatetime18 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTodayAddDays(int days) throws ParseException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DATE, days);
        Date thatDate = cal.getTime();
        return sdfdate10.format(thatDate);
    }

    public static OperatorManager getOperatorManager(){
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("ÓÃ»§Î´µÇÂ¼£¡");
        }
        return om;
    }

    public static String getDatetime14() {
    	return sdfdatetime14.format(new Date());
    }

    public static String getDatetime18() {
    	return sdfdatetime18.format(new Date());
    }

    public static String getSdfdate8() {
        return sdfdate8.format(new Date());
    }

    public static String getSdfdate10() {
        return sdfdate10.format(new Date());
    }

    public static String getSdftime6() {
        return sdftime6.format(new Date());
    }

    public static String getSdftime8() {
        return sdftime8.format(new Date());
    }
}
