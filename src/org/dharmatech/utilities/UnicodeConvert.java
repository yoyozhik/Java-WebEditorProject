/* Conversion between Unicode and Char */
/* Author: Wei Zhang
   Latest Version: 2016 Jan 17
*/
/* API
public class UnicodeConvert {
    public UnicodeConvert()
    public static String toUnicodes(String s)
    public static String toChars(String s)
    public static void main(String[] args)
}
*/
/* Note:
Making static methods to avoid initilization overhead.
Not thread safe!
This app has no multi-thread scenario so it is safe.
*/
package org.dharmatech.utilities;

import java.util.regex.*;

public class UnicodeConvert {
    //private String origText;
    //Convert string from chars to unicode
    public static String toUnicodes(String s) {
        if (s == null) {
            throw new NullPointerException("Null string");
        }
        //origText = s;
        //String r = new String(origText);
        String r = new String(s);
        char[] cs = r.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            int ascVal = (int) c;
            if (ascVal > 127) {
                sb.append("&#" + ascVal + ";");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    //Convert string from unicode to chars
    public static String toChars(String s) {
        if (s == null) {
            throw new NullPointerException("Null string");
        }
        //origText = s;
        //String r = new String(origText);
        String r = new String(s);
        String pattern = "&#(\\d+);";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(r);
        String c;
        while (m.find()) {
            String f = m.group(1);
            c = Character.toString((char) Integer.parseInt(f));
            r = r.replace("&#" + f + ";", c);
            m = p.matcher(r);
        }
        return r;
    }
    public static void main(String[] args) {
        String st = "		<title>&#27963;&#21205;&#20844;&#21578; Activities - Midwest-Minnesota Buddhist Association ";
        UnicodeConvert u = new UnicodeConvert();
        String a = u.toChars(st);
        String b = u.toUnicodes(a);
        System.out.println(st);
        System.out.println(a);
        System.out.println(b);
        String ch = "點此前往";
        String chu = u.toUnicodes(ch);
        System.out.println(ch);
        System.out.println(chu);
        System.out.println(u.toChars(chu));
    }        
}