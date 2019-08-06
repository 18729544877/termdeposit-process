package com.simnectzbank.lbs.processlayer.termdeposit.service;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.transactionservice.InsertTransactionLogModel;


public interface TransactionLogService {

	
	public Map<String,String> insertTransacitonLog(RestTemplate restTemplate,InsertTransactionLogModel ase) throws Exception;
	
	
}