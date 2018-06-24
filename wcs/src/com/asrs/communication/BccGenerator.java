package com.asrs.communication;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-3
 * Time: 13:22:51
 * Copy right Worgsoft.
 */
public class BccGenerator
{
    public static boolean IsBccRight(String contentData, String bccData)
    {
        return GetBcc(contentData).equals(bccData);
    }

    public static String GetBcc(String contentData)
    {
        if (contentData.isEmpty())
        {
            return "";
        }
        else
        {
            int bcc = 0;
            for (int i = 0; i < contentData.length(); i++)
            {
                bcc = bcc ^ contentData.charAt(i);
            }
            String bccData = Integer.toHexString(bcc);
            if (bccData.length() == 1)
            {
                bccData = "0" + bccData;
            }
            return bccData.toUpperCase();
        }
    }

    public static void main(String[] args) {
        System.out.println(GetBcc("1120414114000000000000000000000000"));

    }
}
