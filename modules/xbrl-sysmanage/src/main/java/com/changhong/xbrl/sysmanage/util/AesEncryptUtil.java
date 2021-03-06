package com.changhong.xbrl.sysmanage.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptUtil {

	public static final String KEY_ALGORITHM = "AES";
	public static final String ENCRYPTION_MODE = "AES/CBC/PKCS5Padding";

	/** 
	 * AES加密为base 64 code 
	 * @param content 待加密的内容 
	 * @param encryptKey 加密密钥 
	 * @return 加密后的base 64 code 
	 * @throws Exception
	 */  
	public static String aesEncrypt(String content, String encryptKey, String ivParameter) throws Exception {
	    return base64Encode(aesEncryptToBytes(content, encryptKey,ivParameter));  
	} 

	  /**
	  * 将base 64 code AES解密
	  * @param encryptStr 待解密的base 64 code
	  * @param decryptKey 解密密钥
	  * @return 解密后的string
	  * @throws Exception
	  */
	  public static String aesDecrypt(String encryptStr, String decryptKey, String ivParameter) throws Exception {
		  return StringUtils.isBlank(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey,ivParameter);
	  }

    /**
	 * base 64 decode
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
    public static byte[] base64Decode(String base64Code) throws Exception {
	   return StringUtils.isBlank(base64Code) ? null : new Base64().decode(base64Code);
	}
		
	/**
	* base 64 encode
	* @param bytes 待编码的byte[]
	* @return 编码后的base 64 code
	*/
	public static String base64Encode(byte[] bytes){
		 return new Base64().encodeToString(bytes);
	 }

	 /** 
	  * AES加密 
	  * @param content 待加密的内容 
	  * @param encryptKey 加密密钥 
      * @return 加密后的byte[] 
	  * @throws Exception
	  */  
	 public static byte[] aesEncryptToBytes(String content, String encryptKey, String ivParameter) throws Exception {
		 if(encryptKey.length() != 16 || ivParameter.length() != 16){
			 return new byte[0];
		 }
	     SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM);
	     IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
	     Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);//"算法/模式/补码方式"
	     cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
	     return cipher.doFinal(content.getBytes("utf-8"));  
	  }

	 /** 
	 * AES解密 
	 * @param encryptBytes 待解密的byte[] 
	 * @param decryptKey 解密密钥 
	 * @return 解密后的String 
	 * @throws Exception
	 */  
	  public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey, String ivParameter) throws Exception {
		  if(decryptKey.length() != 16 || ivParameter.length() != 16){
				 return new Base64().encodeToString(encryptBytes);
			 }
		  byte[] raw = decryptKey.getBytes("ASCII");    
		  SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
	      IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
	      Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);//"算法/模式/补码方式"
	      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	      byte[] decryptBytes = cipher.doFinal(encryptBytes);  
	      return new String(decryptBytes);
	  }

}
