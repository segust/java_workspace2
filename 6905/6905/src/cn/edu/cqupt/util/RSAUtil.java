package cn.edu.cqupt.util;

import java.security.MessageDigest;

public class RSAUtil {
	public static String string2RSA(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	public static String convertRSA(String inStr){
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 'm');
		}
		String s = new String(a);
		return s;
	}
	
	public static void main(String args[]) {
		String s = new String("1991lhs");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + string2RSA(s));
		System.out.println("加密的：" + convertRSA(s));
		System.out.println("解密的：" + convertRSA(convertRSA(s)));
	}
}
