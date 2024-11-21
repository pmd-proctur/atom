package com.proctur.atom.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proctur.atom.EduIMSException;
import com.proctur.atom.controller.services.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class PaymentGatewayController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayController.class);
	
	@Autowired
	private PaymentService paymentService;

	@PostMapping(value = "/payment/token")
	public Object generateAtomToken(@RequestBody Map<String, Object> request_info) {
		return paymentService.generateToken(request_info);
	}
	
	  @RequestMapping("/payments/process/{tenant_id}")
	    public Object processSuccessAndFailAtomPayment(String encData, @PathVariable("tenant_id") String tenantId) {
	        try {
	            paymentService.processSuccessAndFailAtomPayment(encData, tenantId);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new EduIMSException("Error while process your request! please connect with institute admin!", HttpStatus.BAD_REQUEST.value(), "Error while process your request! please connect with institute admin!");
	        }
	        return "";
	    }


}
