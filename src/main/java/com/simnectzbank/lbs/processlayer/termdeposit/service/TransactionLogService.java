package com.simnectzbank.lbs.processlayer.termdeposit.service;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.InsertTransactionLogModel;

public interface TransactionLogService {

	@SuppressWarnings("rawtypes")
	public ResultUtil insertTransacitonLog(RestTemplate restTemplate, InsertTransactionLogModel ase, HeaderModel header)
			throws Exception;

}