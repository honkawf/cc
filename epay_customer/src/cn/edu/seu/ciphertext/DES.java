package cn.edu.seu.ciphertext;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES implements IEncrypt , IDecrypt{

	/**
	 * @param args
	 */
	String part1 = "486135738435";
	String part2 = "337846125153";
	public String encrypt(String str){
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		byte rawKeyData[] = (part1.substring(3, 8) + part2.substring(4,10)).getBytes();
		// 从原始密匙数据创建一个DESKeySpec对象
		byte[] encryptedData = new byte[0];
		String result = "";
		try {
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 现在，获取数据并加密
			byte data[] = str.getBytes("ISO-8859-1");
			// 正式执行加密操作
			encryptedData = cipher.doFinal(data);
			result = new String(encryptedData , "ISO-8859-1");
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}	
	public String decrypt( String encryptedData){
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		byte rawKeyData[] = (part1.substring(3, 8) + part2.substring(4,10)).getBytes();
		// 从原始密匙数据创建一个DESKeySpec对象
		byte decryptedData[] = new byte[0];
		String result = "";
		try {
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// 正式执行解密操作
			decryptedData = cipher.doFinal(encryptedData.getBytes("ISO-8859-1"));
			result = new String(decryptedData , "ISO-8859-1");
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeySpecException {
		DES ed = new DES();
		String str = "hi.baidu.com/badpeas"; // 待加密数据
		// 2.1 >>> 调用加密方法
		String encryptedData = ed.encrypt(str);
		// 3.1 >>> 调用解密方法
		System.out.println("加密后===>" + encryptedData);
		System.out.println("解密后===>" + ed.decrypt(encryptedData));
	}
}
