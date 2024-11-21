package com.proctur.atom.controller.services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.proctur.atom.EduIMSException;
import com.proctur.atom.constants.PaymentGatewayConstant;
import com.proctur.atom.dao.GenericDao;
import com.proctur.atom.model.ResponseParser;
import com.proctur.atom.pojo.Extras;
import com.proctur.atom.pojo.HeadDetails;
import com.proctur.atom.pojo.MerchDetails;
import com.proctur.atom.pojo.OtsTransaction;
import com.proctur.atom.pojo.PayDetails;
import com.proctur.atom.pojo.PayInstrument;
import com.proctur.atom.pojo.ServerResponse;
import com.proctur.atom.utils.AuthEncryption;
import com.proctur.atom.utils.SystemProperties;
import com.proctur.atom.utils.Utilities;

@Service
public class AtomGatewayPaymentService implements PaymentService {

	@Autowired
	private SystemProperties systemProperties;
	
	@Autowired
	RestTemplate resttemplatev2;
	
	@Autowired
	GenericDao genericDao;
	
	 public Map<String,Object> generateToken(Map<String, Object> request_info) {
	        Map<String,Object> response = new HashMap<>(); 
	        String tenantId = Utilities.objectToString(request_info.get("tenant_id"));
	        tenantId = "HRIT";
	    //    int clientcode = processPrePaymentData(json_string, institute_id, request_info, PaymentGatewayConstant.ATOM_PAYMENT_GATEWAY_NAME);
	        Map<String, String> payment_gateway_details = genericDao
	                .getAtomConfiguration(tenantId);
	        request_info.put("merch_txn_id", Utilities.generateMerchantTxnIdForAtom(tenantId));
	        OtsTransaction otsTxn = setRequestDetails(payment_gateway_details, request_info, tenantId);
	        Gson gson = new Gson();
	        String json = gson.toJson(otsTxn);
	        String atomTokenId = "";
	        String encryptedData = AuthEncryption.getAuthEncrypted(json, payment_gateway_details.get(PaymentGatewayConstant.ATOM_REQUEST_ENCRYPTION_KEY));
	        String serverResp = getAtomTokenId(payment_gateway_details.get(PaymentGatewayConstant.ATOM_MERCHANT_ID), encryptedData);
	        if ((serverResp != null) && (serverResp.startsWith("merchId"))) {
	            String decryptResponse = serverResp.split("\\&encData=")[1];
	            String decryptedData = AuthEncryption.getAuthDecrypted(decryptResponse, payment_gateway_details.get(PaymentGatewayConstant.ATOM_REQUEST_DECRYPTION_KEY));
	            ServerResponse serverResponse = new ServerResponse();
	            serverResponse = (ServerResponse)gson.fromJson(decryptedData, ServerResponse.class);
	            atomTokenId = serverResponse.getAtomTokenId();
	            response.put("token_id", atomTokenId);
	        } else {
	          handleAtomTokenFailure(serverResp);
	        }
	        return response;
	    }
	 
	   private void handleAtomTokenFailure(String serverResp) {
	        try {
	            String failureMessage = Utilities.objectToString(new JSONObject(serverResp).get("txnDescription"));
	            throw new EduIMSException(failureMessage, HttpStatus.BAD_REQUEST.value(), failureMessage);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }

	public String getAtomTokenId(String merchantId, String serverResp) {
	       // String apiUrl = systemProperties.getAtomTokenURL() + "merchId=" + merchantId + "&encData=" + serverResp;
	        String apiUrl = "https://payment1.atomtech.in/ots/aipay/auth?" + "merchId=" + merchantId + "&encData=" + serverResp;
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
	        ResponseEntity<String> s = resttemplatev2.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
	        return s.getBody();
	    }
	 
	   private OtsTransaction setRequestDetails(Map<String, String> payment_gateway_details, Map<String, Object> request_info, String tenantId) {
	        MerchDetails merchDetails = new MerchDetails();
	        merchDetails.setMerchId(payment_gateway_details.get(PaymentGatewayConstant.ATOM_MERCHANT_ID));
	        merchDetails.setMerchTxnId(Utilities.objectToString(request_info.get("merch_txn_id")));
	        merchDetails.setPassword(payment_gateway_details.get(PaymentGatewayConstant.ATOM_PASSWORD));
	        merchDetails.setMerchTxnDate(Utilities.getCurrentTimeStamp());

	        PayDetails payDetails = new PayDetails();
	        payDetails.setAmount(Utilities.objectToString(Utilities.objectToBigDecimal(request_info.get("amount"))));
	        payDetails.setTxnCurrency("INR");
	        payDetails.setProduct(payment_gateway_details.get(PaymentGatewayConstant.ATOM_PROD_ID));

	        HeadDetails headDetails = new HeadDetails();
	        headDetails.setApi("AUTH");
	        headDetails.setVersion("OTSv1.1");
	        headDetails.setPlatform("FLASH");

	        Extras extras = new Extras();
	        extras.setUdf1(Utilities.objectToString(request_info.get("payment_for")));
	        extras.setUdf2(Utilities.objectToString(request_info.get("payment_source")));
	        extras.setUdf3(tenantId);
	        extras.setUdf4(Utilities.objectToString(request_info.get("payment_id")));
	        extras.setUdf9(Utilities.objectToString(request_info.get("payment_source")) + "," + tenantId);

	        PayInstrument payInstrument = new PayInstrument();

	        payInstrument.setMerchDetails(merchDetails);
	        payInstrument.setPayDetails(payDetails);
	        payInstrument.setExtras(extras);
	        payInstrument.setHeadDetails(headDetails);

	        OtsTransaction otsTxn = new OtsTransaction();
	        otsTxn.setPayInstrument(payInstrument);
	        return otsTxn;
	    }
	   
	   @Override
	public void processSuccessAndFailAtomPayment(String encData, String tenantId) {
		//   Map<String, Object> request = getStaticData();
		   Map<String, Object> request = getParamtersFromDcryptData( encData, tenantId);
		   if(isValidatePostTransactionV2(request)) {
			   genericDao.updatePaymentHistory(request, true);
			   genericDao.updatePaymentGateway(request, true);
		   }else {
			   genericDao.updatePaymentHistory(request, false);
			   genericDao.updatePaymentGateway(request, false);
		   }
		   System.out.println(request);
	}
	   
//	   private Map<String, Object> getStaticData() {
//		   Map<String, Object> req = new HashMap<>();
//           req.put("payment_id", "43a346ab-ded7-4b3b-a785-742f4641c194");
//           req.put("merchTxnId", "FRTTEDSF");
//           req.put("status_code", "OTS00001");
//           return req;
//	}

	private boolean isValidatePostTransactionV2(Map<String, Object> request) {
	        return PaymentGatewayConstant.ATOM_SUCCESS_CODE.contains(request.get("status_code"));
	    }
	   
	   private Map<String, Object> getParamtersFromDcryptData(String encData, String tenantId) {
	        Map<String, String> payment_gateway_details = genericDao
	                .getAtomConfiguration(tenantId);
	        String decryptedData = AuthEncryption.getAuthDecrypted(encData,
	                payment_gateway_details.get(PaymentGatewayConstant.ATOM_REQUEST_DECRYPTION_KEY));
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String, Object> req = new HashMap<>();
	        try {
	            System.out.println("payments process encData::" + encData);
	            System.out.println("payments process Object Mapper:: " + objectMapper);
	            System.out.println("payments process DecryptDate:: " + decryptedData);
	            ResponseParser resp = objectMapper.readValue(decryptedData, ResponseParser.class);
	            /*
	             * udf1=payment_for
	             * udf2=payment_source
	             * udf3=institute_id
	             * udf4=clientCode
	             * udf9=payment_source+","institute_id
	             */
	            req.put("udf1", resp.getPayInstrument().getExtras().getUdf1());
	            req.put("udf2", resp.getPayInstrument().getExtras().getUdf2());
	            req.put("udf3", resp.getPayInstrument().getExtras().getUdf3());
	            req.put("udf4", resp.getPayInstrument().getExtras().getUdf4());
	            req.put("payment_id", resp.getPayInstrument().getExtras().getUdf9());
	            req.put("mmp_txn", resp.getPayInstrument().getPayDetails().getAtomTxnId());
	            req.put("merchTxnId", resp.getPayInstrument().getMerchDetails().getMerchTxnId());
	            req.put("status_code", resp.getPayInstrument().getResponseDetails().getStatusCode());

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return req;
	    }
	   
}
