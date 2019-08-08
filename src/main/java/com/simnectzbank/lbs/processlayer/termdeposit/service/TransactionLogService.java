package com.simnectzbank.lbs.processlayer.termdeposit.service;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.simnectzbank.lbs.processlayer.termdeposit.model.InsertTransactionLogModel;

public interface TransactionLogService {

	public Map<String, String> insertTransacitonLog(RestTemplate restTemplate, InsertTransactionLogModel ase)
			throws Exception;

}