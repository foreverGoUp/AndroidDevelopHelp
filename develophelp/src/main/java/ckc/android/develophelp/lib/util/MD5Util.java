package ckc.android.develophelp.lib.util;

import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author clearInstance
 */
public class MD5Util {

    private static final String TAG = "MD5Util";

    /**
     * 普通md5加密
     *
     * @param s 需要加密的字符串
     * @return
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 自定义MD5加密
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public static String MD5Pwd(String str) {
        char hexDigits[] = {'A', '0', '1', '2', '9', '3', 'X', '5', '6', '7', '8', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str2[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str2[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str2[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str2);
        } catch (Exception e) {
            Log.e(TAG, "md5密码加密失败");
            return str;
        }
    }

//	    public static void main(String[] args) {
//	    	String str = "123456";
//	    	System.out.println(MD5Pwd(str));
//	    	System.out.println(MD5Pwd(MD5Pwd(str)));
//	    	System.out.println(MD5Pwd(str).length());
//	    	System.out.println(MD5Pwd(MD5Pwd(str)).length());
//	    	System.out.println(MD5Pwd(MD5Pwd(str)).equals("E0A8DC2797B8378BBE3XEA35F1AF662E"));
//	    	System.out.println(MD5Pwd(str).equals("E0A8DC2797B8378BBE3XEA35F1AF662E"));
//		}
}
