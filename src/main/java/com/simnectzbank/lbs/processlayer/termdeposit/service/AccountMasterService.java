package com.simnectzbank.lbs.processlayer.termdeposit.service;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.ChequeBookModel;

public interface AccountMasterService {

	@SuppressWarnings("rawtypes")
	public ResultUtil chequeBookRequest(HeaderModel header, ChequeBookModel cbm, RestTemplate restTemplate)
			throws Exception;

}
