package com.proctur.atom.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

//import com.atom.ots.enc.AtomEncryption;

public class AuthEncryption {
	private static String encMsg = "never give up on encryption logic";
	private final Cipher cipher;

	public static String getAuthDecrypted(String encData, String key) {
		String decrypted = null;
		try {
			decrypted = AtomEncryption.decrypt(encData, key);
		} catch (Exception e) {
			System.out.println(": EXCEPTION IN DECRYPTION ::" + e.getMessage());
		}
		return decrypted;
	}

	public static String getAuthEncrypted(String plainData, String key) {
		String encrypted = null;
		try {
			encrypted = AtomEncryption.encrypt(plainData, key);
		} catch (Exception e) {
			System.out.println(": EXCEPTION IN ENCRYPTION ::" + e.getMessage());
		}
		return encrypted;
	}

	public AuthEncryption() throws Exception {
		try {
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception e) {
			throw fail(e);
		}
	}

	public String getDecryptedData(String tempData, String info1, String info2) {
		String decryptedData = null;
		try {
			tempData = tempData.replace(" ", "+");
			decryptedData = decrypt(info1, info2, tempData);
		} catch (Exception e) {
			System.out.println(": EXCEPTION IN DECRYPTION ::" + e.getMessage());
		}

		return decryptedData;
	}

	public String decrypt(String salt, String iv, String ciphertext) {
		try {
			SecretKey key = generateKey(salt, encMsg);

			byte[] decrypted = doFinal(2, key, iv, base64(ciphertext));
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			System.out.println(": EXCEPTION IN DECODING ::" + e.getMessage());
			throw fail(e);
		}
	}

	public String encrypt(String salt, String iv, String plaintext) {
		try {
			SecretKey key = generateKey(salt, encMsg);
			byte[] encrypted = doFinal(1, key, iv, plaintext.getBytes(StandardCharsets.UTF_8));
			return base64(encrypted);
		} catch (Exception e) {
			throw fail(e);
		}
	}

	private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
		try {
			this.cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
			return this.cipher.doFinal(bytes);
		} catch (Exception e) {
			System.out.println(": EXCEPTION IN ENC/DEC ::" + e.getMessage());
			throw fail(e);
		}
	}

	private static SecretKey generateKey(String salt, String passphrase) throws NoSuchAlgorithmException {
		try {
			int iterationCount = 1000;
			int keySize = 128;
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
			return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		} catch (java.security.spec.InvalidKeySpecException e) {
			System.out.println(": EXCEPTION IN GENERATION OF ENC/DEC KEY ::" + e.getMessage());
			throw fail(e);
		}
	}

	private static IllegalStateException fail(Exception e) {
		return new IllegalStateException(e);
	}

	public static String random(int length) {
		byte[] salt = new byte[length];
		new SecureRandom().nextBytes(salt);
		return hex(salt);
	}

	public static String base64(byte[] bytes) {
		return new String(Base64.getEncoder().encode(bytes));
	}

	public static byte[] base64(String str) {
		str = str.replace(" ", "+").trim();

		return Base64.getDecoder().decode(str.getBytes());
	}

	public static String hex(byte[] bytes) {
		return new String(Hex.encodeHex(bytes));
	}

	public static byte[] hex(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	  public static String genAtomReqSignature(String data, String key) {
	        try {
	            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
	            Mac mac = Mac.getInstance("HmacSHA512");
	            mac.init(secretKeySpec);
	            byte[] hmacData = mac.doFinal(data.getBytes());
	            return Base64.getEncoder().encodeToString(hmacData);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	  
	  public static String generateSignature(String hashKey, String sb) {
	        String resp = null;
	        try {
	            System.out.println("String =" + sb.toString());
	            resp = byteToHexString(encodeWithHMACSHA2(sb, hashKey));
	        } catch (Exception e) {
	            System.out.println("Unable to encocd value with key :" + hashKey +
	                               " and input :" +
	                               sb.toString());
	            e.printStackTrace();
	        }
	        return resp;
	    }

	    private static byte[] encodeWithHMACSHA2(String text, String keyString)
	            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
	        Key sk = new SecretKeySpec(keyString.getBytes("UTF-8"), "HMACSHA512");
	        Mac mac = Mac.getInstance(sk.getAlgorithm());
	        mac.init(sk);
	        byte[] hmac = mac.doFinal(text.getBytes("UTF-8"));
	        return hmac;
	    }

	    public static String byteToHexString(byte byData[]) {
	        StringBuilder sb = new StringBuilder(byData.length * 2);
	        for (int i = 0; i < byData.length; i++) {
	            int v = byData[i] & 0xff;
	            if (v < 16)
	                sb.append('0');
	            sb.append(Integer.toHexString(v));
	        }
	        return sb.toString();
	    }

	  
}