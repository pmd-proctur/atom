package com.proctur.atom.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proctur.atom.utils.Utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
@Transactional
public class GenericDao {
	
	  @Autowired
	    private EntityManager entityManager;
	  
	public Map<String,String> getAtomConfiguration(String id){
		Map<String, String> dataMap = new HashMap<>();
		
		  String sql = "select p.key_name,p.key_value from payment_gateway_details p where p.name=:payment_gateway_name and p.idTenant=:id";
		  Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("payment_gateway_name", "ATOM");
	        query.setParameter("id", id);
	        List<Object[]> list =query.getResultList();
	        Map<String, String> response = new HashMap<>();
	        for (Object[] obj : list) {
	            response.put(Utilities.objectToString(obj[0]), Utilities.objectToString(obj[1]));
	        }
	        return response;
	}

	public void updatePaymentHistory(Map<String, Object> request, boolean isSuccess) {
		  String sql = "Update payment_history pay set pay.is_transferred=:transfer, pay.merch_txn_id=:merchtxtId,pay.is_processed=:processed "
		  		+ " Where pay.idPayment_gateway=:id";
	        Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("transfer", isSuccess ? 1: 0);
	        query.setParameter("merchtxtId", request.get("merchTxnId"));
	        query.setParameter("processed", isSuccess ? 1: 0);
	        query.setParameter("id", request.get("payment_id"));
	        query.executeUpdate();
		
	}

	public void updatePaymentGateway(Map<String, Object> request, boolean isSuccess) {
		  String sql = "Update payment_gateway pay set pay.payment_status=:status Where pay.id=:id";
		  	  Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("status", isSuccess ? "SUCCESS" : "FAIL");
	        query.setParameter("id", request.get("payment_id"));
	        query.executeUpdate();
	}
	
	
}
