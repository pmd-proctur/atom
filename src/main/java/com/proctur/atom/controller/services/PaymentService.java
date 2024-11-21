package com.proctur.atom.controller.services;

import java.util.Map;

public interface PaymentService {

	Object generateToken(Map<String, Object> request_info);

	void processSuccessAndFailAtomPayment(String encData, String tenantId);

}
