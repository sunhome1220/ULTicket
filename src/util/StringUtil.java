package util;

import java.text.Normalizer;
import java.util.regex.Pattern;
//import org.apache.catalina.tribes.util.Arrays;

public class StringUtil {

    public static String nvl(Object o) {
        String tmp = "";
        if (o != null && !o.equals("null")) {
            tmp = new StringUtil().getAuthString(getString(o));
        }
        return tmp;
    }

    public static String getString(Object o) {
        String tmp = "";
        if (o != null && !o.equals("null")) {
            tmp = o.toString().trim();
        }
        return tmp;
    }

    /**
     * 將物件轉成int，若無法轉成數字則回傳0。
     *
     * @param obj
     * @return
     */
    public static int num(Object obj) {
        int retVal = 0;
        try {
            retVal = Integer.parseInt(nvl(obj));
        } catch (NumberFormatException nfe) {
            retVal = 0; // or null if that is your preference
        }
        return retVal;
    }

    /**
     * 長度不足部份補零回傳
     *
     * @param str 字串
     * @param lenSize 字串所需長度
     * @return 回傳補零後字串
     */
    public String MakesUpZero(String str, int lenSize) {
        String zero = "0000000000";
        String returnValue = zero;

        returnValue = zero + str;

        return returnValue.substring(returnValue.length() - lenSize);

    }

    public static Integer tryParse(Object obj) {
        Integer retVal;
        try {
            retVal = Integer.parseInt(nvl(obj));
        } catch (NumberFormatException nfe) {
            retVal = 0; // or null if that is your preference
        }
        return retVal;
    }

    public static long tryLongParse(Object obj) {
        try {
            return Long.parseLong(nvl(obj));
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static Double tryParseDouble(Object obj) {
        Double retVal;
        try {
            retVal = Double.parseDouble(nvl(obj));
        } catch (NumberFormatException nfe) {
            retVal = 0.0;
        }
        return retVal;
    }

    public static float tryParseFloat(Object obj) {
        float retVal;
        try {
            retVal = Float.parseFloat(nvl(obj));
        } catch (NumberFormatException nfe) {
            retVal = 0;
        }
        return retVal;
    }

    /**
     * 將字串中的半全形空白去掉
     */
    public static String replaceWhiteChar(String inString) {
        String strTmp = inString;
        String[] arrWhiteChar = {" ", "@"};

        if (null == strTmp) {
            return "";
        }

        for (int idx = 0; idx < arrWhiteChar.length; idx++) {
            strTmp = strTmp.replaceAll(arrWhiteChar[idx], "");
        }

        return strTmp;
    }

    /**
     * 過濾最右邊(後面)為charsToTrim之字元
     *
     * @param input
     * @param charsToTrim
     * @return
     */
    public static String trimEnd(String input, String charsToTrim) {
        return input.replaceAll("[" + charsToTrim + "]+$", "");
    }

    /**
     * 過濾最左邊(前面)為charsToTrim之字元
     *
     * @param input
     * @param charsToTrim
     * @return
     */
    public static String trimStart(String input, String charsToTrim) {
        return input.replaceAll("^[" + charsToTrim + "]+", "");
    }

    /**
     * 左補足charsToPad之字元
     *
     * @param input
     * @param charsToPad 補足的字元
     * @param length 長度
     * @return
     */
    public static String padLeft(String input, char charsToPad, int length) {
        return String.format("%" + length + "s", input).replace(' ', charsToPad);
    }

    /**
     * 右補足charsToPad字元
     *
     * @param input
     * @param charsToPad 補足的字元
     * @param length 長度
     * @return
     */
    public static String padRight(String input, char charsToPad, int length) {
        return String.format("%-" + length + "s", input).replace(' ', charsToPad);
    }

    /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null) {
            return null;
        }
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    public static void main(String[] args) {
        String s = "a<br   >b<br>c<a>d<br />e<BR  />f&nbsp;g&amp; <p>h</p>i&lt;j&gt;";
        System.out.println(s);
        System.out.println(removeHtmlTag(s));
    }

    /**
     * String join
     *
     * @param list
     * @param conjunction
     * @return
     */
    public static String join(String[] list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list) {
            if (first) {
                first = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 修正弱掃Input Not Normalized
     */
    public String getAuthString(String inString) {

        String str = "";
        if (inString == null) {
            return str;
        } else {
            str = Normalizer.normalize(inString.toString().trim(), Normalizer.Form.NFKC);

            // Normalizes
            str = Normalizer.normalize(str, Normalizer.Form.NFKC);
            if (str.matches("^[A-Za-z0-9,.]+$")) {
                str = str.trim();
            }
            Pattern checkPattern = Pattern.compile("^[A-Za-z0-9,.]+$");
            if (checkPattern.matcher(str).matches()) {
                return str;
            } else {
                // throw new IllegalStateException();
                return str;
            }
        }
    }

    /**
     * 將數字轉成中文
     *
     * @param Num 阿拉伯數字
     * @return
     */
//    public static String getCnNumber(String Num) {
//        char cnNum[] = "○一二三四五六七八九".toCharArray();
//        StringBuilder result = new StringBuilder();
//        if (org.apache.commons.lang.StringUtils.isNumeric(Num)) {
//            char[] enNums = Num.toCharArray();
//            for (char enNum : enNums) {
//                result.append(String.valueOf(cnNum[Character.getNumericValue(enNum)]));
//            }
//        }
//        return result.toString();
//    }
    //金額與講習時數轉中文

    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個  
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十  
        {
            if (si.substring(0, 1).equals("1")) {
                sd += "十";
            } else {
                sd += (GetCH(intInput / 10) + "十");
            }
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百  
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2) {
                sd += "零";
            }
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千  
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3) {
                sd += "零";
            }
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬  
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4) {
                sd += "零";
            }
            sd += ToCH(intInput % 10000);
        }
        if (sd.length() > 1 && sd.substring(sd.length() - 1).equals("零")) {
            sd = sd.substring(0, sd.length() - 1);
        }
        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    /**
     * 將含10進位NCR的中文字串轉換成全中文(尚非十全十美)
     * @param ncrStr 含10進位NCR(Numeric Character Reference)的中文
     * ex:&#23791;一般中文&#20931;&#24658;是否正常&#21234;呢？
     * @see https://my.oschina.net/u/1766462/blog/666709
     * @return 
     */
    
    public static String ConvertDecimalNCRToString(String ncrStr) {
        if(!ncrStr.contains("&#")){
            return ncrStr;//若不含&#就return原值
        }
        String[] split = ncrStr.split("[&;]");
        StringBuilder sb = new StringBuilder();
        for (String word : split) {
            if(word.matches("\\#\\d{5}")){//# + 5位數字
                sb.append((char) Integer.parseInt(word.substring(1)));
            }else {
                sb.append(word);
            }
        }
        return sb.toString();
    }
}
