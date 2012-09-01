package cn.edu.seu.ciphertext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements IEncrypt{

	/**
	 * @param args
	 */
	public String encrypt(String str){
		byte[] strByte = str.getBytes();   
		MessageDigest md = null;  
		try {   
			md = MessageDigest.getInstance("MD5"); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); 
		} 
		md.update(strByte); 
		byte[] newByte = md.digest(); 
		StringBuilder sb = new StringBuilder(); 
		for (int i = 0; i < newByte.length; i++) {
			if ((newByte[i] & 0xff) < 0x10)
				sb.append("0");  
			sb.append(Long.toString(newByte[i] & 0xff, 16)); 
		}
		return sb.toString();

	}
	public static void main(String[] args) {
		MD5 md5 = new MD5();
		System.out.println("abc after by MD5 encryption: "+md5.encrypt("abc")); 
	}

}
