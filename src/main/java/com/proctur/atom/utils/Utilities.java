package com.proctur.atom.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utilities {

	public static int objectToInteger(Object obj) {
		try {
			return obj != null ? Integer.parseInt(obj.toString()) : 0;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public static String objectToString(Object obj) {

		return obj != null ? obj.toString() : null;

	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static BigDecimal objectToBigDecimal(Object obj) {
		try {
			return obj != null ? new BigDecimal(obj.toString()) : BigDecimal.ZERO;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw e;
		}

	}
	  public static int generateRandomNumber(int numberOfDigits) {
	        int min = (int)Math.pow(10, numberOfDigits - 1);
	        int max = (int)Math.pow(10, numberOfDigits) - 1;
	        Random random = new Random();
	        return random.nextInt(max - min + 1) + min;
	    }
	  public static String generateMerchantTxnIdForAtom(String tenantId) {
	        return tenantId + "#" + System.currentTimeMillis() + "#" + generateRandomNumber(4);
	    }

}
