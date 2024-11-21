package com.proctur.atom.utils;

import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AtomEncryption {
	static Logger log = Logger.getLogger(AtomEncryption.class.getName());
	private static int pswdIterations = 65536;
	private static int keySize = 256;
	private static final byte[] ivBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

	public static String encrypt(String plainText, String key) {
		try {
			byte[] saltBytes = key.getBytes("UTF-8");
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, pswdIterations, keySize);
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			IvParameterSpec localIvParameterSpec = new IvParameterSpec(ivBytes);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(1, secret, localIvParameterSpec);
			byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
			return byteToHex(encryptedTextBytes);
		} catch (Exception var10) {
			log.info("Exception while encrypting data:" + var10.toString());
			return null;
		}
	}

	public static String decrypt(String encryptedText, String key) {
		try {
			byte[] saltBytes = key.getBytes("UTF-8");
			byte[] encryptedTextBytes = hex2ByteArray(encryptedText);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, pswdIterations, keySize);
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			IvParameterSpec localIvParameterSpec = new IvParameterSpec(ivBytes);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(2, secret, localIvParameterSpec);
			byte[] decryptedTextBytes = (byte[]) null;
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			return new String(decryptedTextBytes);
		} catch (Exception var11) {
			log.info("Exception while decrypting data:" + var11.toString());
			return null;
		}
	}

	private static String byteToHex(byte[] byData) {
		StringBuffer sb = new StringBuffer(byData.length * 2);

		for (int i = 0; i < byData.length; ++i) {
			int v = byData[i] & 255;
			if (v < 16) {
				sb.append('0');
			}

			sb.append(Integer.toHexString(v));
		}

		return sb.toString().toUpperCase();
	}

	private static byte[] hex2ByteArray(String sHexData) {
		byte[] rawData = new byte[sHexData.length() / 2];

		for (int i = 0; i < rawData.length; ++i) {
			int index = i * 2;
			int v = Integer.parseInt(sHexData.substring(index, index + 2), 16);
			rawData[i] = (byte) v;
		}

		return rawData;
	}
}
