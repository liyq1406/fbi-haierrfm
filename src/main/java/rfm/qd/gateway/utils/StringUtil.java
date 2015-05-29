package rfm.qd.gateway.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 获取两子串之间的字符串内容
     *
     * @param fromStr
     * @param startStr
     * @param endStr
     * @return
     */
    public static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }

    /**
     * 将amt单位元转换为分，最少三位、如：0.01元——》001
     *
     * @param amt
     * @return
     */
   public static String toBiformatAmt(BigDecimal amt) {
        String rtnStrAmt = "";
        if (amt.compareTo(new BigDecimal(1)) < 0) {
            if (amt.compareTo(new BigDecimal(0.1)) > 0) {
                rtnStrAmt = "0" + String.valueOf(amt.multiply(new BigDecimal(100)));
            } else {
                rtnStrAmt = "00" + String.valueOf(amt.multiply(new BigDecimal(100)));
            }
        } else {
            rtnStrAmt = String.valueOf(amt.multiply(new BigDecimal(100)));
        }
        return rtnStrAmt.split("\\.")[0];
    }

    /**
     * 将字符串中旧子串内容替换为新内容
     *
     * @param fromStr
     * @param oldSubstr
     * @param newSubstr
     * @return
     */
    public static String replaceOldstrToNewstr(String fromStr, String oldSubstr, String newSubstr) {
        String toStr = fromStr;
        Pattern pattern = null;
        Matcher matcher = null;
        pattern = Pattern.compile(oldSubstr);
        matcher = pattern.matcher(toStr);
        if (matcher.find()) {
            toStr = matcher.replaceAll(newSubstr);
        }
        return toStr;
    }

    public static String transDate10ToDate8(String strDate10) {
        return strDate10.substring(0, 4) + strDate10.substring(5, 7) + strDate10.substring(8, 10);
    }

    public static String transDate8ToDate10(String strDate8) {
        return strDate8.substring(0, 4) + "-" + strDate8.substring(4, 6) + "-" + strDate8.substring(6, 8);
    }

    public static String rightPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr) {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            rtnStrBuilder.append(srcStr);
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            System.arraycopy(srcStr.getBytes(), 0, rtnBytes, 0, totalByteLength);
            rtnStrBuilder.append(rtnBytes);
        }
        return rtnStrBuilder.toString();
    }


}