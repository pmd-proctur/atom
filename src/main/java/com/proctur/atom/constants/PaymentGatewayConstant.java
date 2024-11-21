package com.proctur.atom.constants;

import java.util.Arrays;
import java.util.List;

public interface PaymentGatewayConstant {
	public String ATOM_PAYMENT_GATEWAY_NAME = "Atom";
	public String PAYPAL_PAYMENT_GATEWAY_NAME = "PayPal";
	public String PAYMENT_STATUS_SUCCESS = "Success";
	public String PAYMENT_STATUS_FAILED = "Failed";
	public String ATOM_MERCHANT_ID = "merchant_id";
	public String ATOM_PASSWORD = "tx_password";
	public String ATOM_PROD_ID = "product_id";
	public String ATOM_REQUEST_ENCRYPTION_KEY = "aes_req_key";
	public String ATOM_REQUEST_DECRYPTION_KEY = "aes_res_key";
	public String ATOM_REQ_HASH = "req_hash_key";
	public List<String> ATOM_SUCCESS_CODE = Arrays.asList("OTS0000", "OTS0002");
}
