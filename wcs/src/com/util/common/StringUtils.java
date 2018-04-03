package com.util.common;

import org.apache.commons.lang.StringEscapeUtils;
import sun.security.action.GetPropertyAction;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理string的工具类
 * <p/>
 * User: xiongying
 * Date: 14-10-17
 * Time: 下午18:03
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static final int CONVERT_TYPE_EMPTY = 1;
    public static final int CONVERT_TYPE_LOWER_CASE = 2;
    public static final int CONVERT_TYPE_UPPER_CASE = 3;

    /**
     * 比较两个字符串的大小
     *
     * @param value1
     * @param value2
     * @return
     */
    public static final int compare(String value1, String value2) {
        byte[] buf1 = value1.getBytes();
        byte[] buf2 = value2.getBytes();
        int len = Math.min(buf1.length, buf2.length);

        for (int i = 0; i < len; i++) {
            if (buf1[i] != buf2[i])
                return buf1[i] - buf2[i];
        }

        return buf1.length - buf2.length;
    }

    /**
     * 把字符串中的全角字符转换成半角字符
     *
     * @param src
     * @return
     */
    public static final String sbcToDbc(String src) {
        String outStr = "";
        String tempStr = "";
        byte[] b = null;

        for (int i = 0; i < src.length(); i++) {
            try {
                tempStr = src.substring(i, i + 1);
                b = tempStr.getBytes("unicode");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;

                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                outStr = outStr + tempStr;
        }

        return outStr;
    }

    public static String rep(Object s) {
        if (s == null)
            return "";
        if (s instanceof String) {
            String new_name = (String) s;
            return new_name.trim();
        }
        return s + "";
    }

    public static boolean isEmpty(Object s) {
        if (s == null)
            return true;
        if (s instanceof String) {
            String str = (String) s;
            if ("".equals(str.trim()))
                return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object s) {
        return !isEmpty(s);
    }

    /**
     * 把文本编码为Html代码
     *
     * @param str
     * @return 编码后的字符串
     */
    public static String escapeHtmlJson(String str) {
        return StringUtils.escapeHtml(str).replaceAll("\\\\", "\\\\\\\\");
    }

    public static String escapeHtml(String str) {
        if (str == null)
            return "";
        String s = StringEscapeUtils.escapeHtml(str);
        s = s.replaceAll("\r", "");
        s = s.replaceAll("\n", "<br/>");
        s = s.replaceAll("	", "&nbsp;&nbsp;&nbsp;&nbsp;");

        return s;

    }

    public static String formatString(String str) {
        if (isEmpty(str))
            return str;
        String formatStr = str.replaceAll("<", "&lt;");
        formatStr = formatStr.replaceAll(">", "&gt;");
        formatStr = formatStr.replaceAll("\\n", "<br>");
        formatStr = formatStr.replaceAll("\\s", "&nbsp;");
        return formatStr;
    }

    /**
     * 截取文件名
     *
     * @param fileName 文件名
     * @param length   长度
     * @return
     */
    public static String subFileName(String fileName, int length) {
        if (isEmpty(fileName))
            return fileName;
        // 省略号长度
        int ellipsisSize = 2;
        int elBackSize = 2;
        // 文件扩展名长度（包括.）
        int expandSize;
        // 文件名去除扩展名后的长度
        int surplusSiz;

        // 省略号内容
        String ellipsisStr = " ... ";
        // 文件扩展名（包括.）
        String expandStr;
        if (fileName.length() <= length)
            return fileName;
        expandSize = fileName.lastIndexOf(".");
        surplusSiz = fileName.length() - expandSize;
        if ((length - ellipsisSize - surplusSiz - elBackSize) > 0 && surplusSiz > (length - ellipsisSize - expandSize - elBackSize)) {
            expandStr = fileName.substring(expandSize, fileName.length());
            String str = "";
            String backStr = "";
            backStr = fileName.substring(expandSize - 2, expandSize);
            str = fileName.substring(0, (length - surplusSiz - ellipsisSize));
            return str + ellipsisStr + backStr + expandStr;
        }
        return ellipsisStr;
    }

    /**
     * 统计字符串中包含的某个字符的个数
     *
     * @param str        要查找的字符串
     * @param includeStr 包含的字符串
     * @return 个数
     */
    public static int getStringCount(String str, String includeStr) {
        if (str == null)
            return 0;
        Pattern p = Pattern.compile(includeStr, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static String subString(String str, int start, int end) {
        if (str == null)
            return "";
        return org.apache.commons.lang.StringUtils.substring(str, start, end);
    }

    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');
    static String dfltEncName = null;

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' '); /*
                                     * encoding a space to a + is done in the
									 * encode() method
									 */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');

        dfltEncName = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
    }

    public static String encode(String s, String enc) throws UnsupportedEncodingException {

        boolean needToChange = false;
        StringBuffer out = new StringBuffer(s.length());
        Charset charset;
        CharArrayWriter charArrayWriter = new CharArrayWriter();

        if (enc == null)
            throw new NullPointerException("charsetName");

        try {
            charset = Charset.forName(enc);
        } catch (IllegalCharsetNameException e) {
            throw new UnsupportedEncodingException(enc);
        } catch (UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(enc);
        }

        for (int i = 0; i < s.length(); ) {
            int c = (int) s.charAt(i);
            // System.out.println("Examining character: " + c);
            if (dontNeedEncoding.get(c)) {
                if (c == ' ') {
                    needToChange = true;
                }
                // System.out.println("Storing: " + c);
                out.append((char) c);
                i++;
            } else {
                // convert to external encoding before hex conversion
                do {
                    charArrayWriter.write(c);
					/*
					 * If this character represents the start of a Unicode
					 * surrogate pair, then pass in two characters. It's not
					 * clear what should be done if a bytes reserved in the
					 * surrogate pairs range occurs outside of a legal surrogate
					 * pair. For now, just treat it as if it were any other
					 * character.
					 */
                    if (c >= 0xD800 && c <= 0xDBFF) {
						/*
						 * System.out.println(Integer.toHexString(c) +
						 * " is high surrogate");
						 */
                        if ((i + 1) < s.length()) {
                            int d = (int) s.charAt(i + 1);
							/*
							 * System.out.println("\tExamining " +
							 * Integer.toHexString(d));
							 */
                            if (d >= 0xDC00 && d <= 0xDFFF) {
								/*
								 * System.out.println("\t" +
								 * Integer.toHexString(d) +
								 * " is low surrogate");
								 */
                                charArrayWriter.write(d);
                                i++;
                            }
                        }
                    }
                    i++;
                } while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

                charArrayWriter.flush();
                String str = new String(charArrayWriter.toCharArray());
                byte[] ba = str.getBytes(charset);
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }

        return (needToChange ? out.toString() : s);
    }

    /**
     * 将一段带有空格分隔字符串的单词首字符大写
     *
     * @param str 字符串
     * @return
     */
    public static String toFirstCapsByStr(String str) {
        str = str.replaceAll("\\s{1,}", " ");
        String[] array = str.split(" ");
        StringBuffer sb = new StringBuffer();
        for (String s : array)
            sb.append(s.substring(0, 1).toUpperCase() + s.substring(1) + " ");
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 例：toStr("2", "0-abc,2-123") 返回123
     *
     * @param val
     * @param rule
     * @return
     */
    public static String toStr(String val, String rule) {
        if (StringUtils.isEmpty(rule))
            return "";
        String[] obj = rule.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String str : obj) {
            if (str.indexOf("-") == -1)
                continue;
            String[] array = str.split("-");
            map.put(array[0], array[1]);
        }
        return map.get(val);
    }

    /**
     * 初始化值,为空,返回 ""
     *
     * @param value
     * @return
     */
    public static String initValue(String value) {
        if (value == null)
            return "";
        return value;
    }

    public static Object initValue(Object value, String replValue) {
        if (value == null)
            return replValue;
        return value;
    }

    /**
     * 把字符串转成utf8编码，保证中文文件名不会乱码
     *
     * @param
     * @return
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 全角转半角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        String result = null;
        if (input != null) {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            result = new String(c);
        }
        return result;
    }

    public static String doTrim(String str) {
        return StringUtils.doTrim(str, StringUtils.CONVERT_TYPE_EMPTY);
    }

    public static String doTrim(String str, int convertType) {
        if (str != null) {
            String value = StringUtils.ToDBC(str).trim();
            if (!StringUtils.isEmpty(value) && !"null".equals(str.toLowerCase())) {
                switch (convertType) {
                    case StringUtils.CONVERT_TYPE_LOWER_CASE:
                        return value.toLowerCase();
                    case StringUtils.CONVERT_TYPE_UPPER_CASE:
                        return value.toUpperCase();
                    default:
                        return value;
                }
            }
        }
        return null;
    }
}
