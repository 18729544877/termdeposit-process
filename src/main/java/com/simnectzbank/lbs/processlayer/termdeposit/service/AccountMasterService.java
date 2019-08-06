package com.simnectzbank.lbs.processlayer.termdeposit.service;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.ChequeBookModel;

public interface AccountMasterService {

	public Map<String, Object> chequeBookRequest(HeaderModel header, ChequeBookModel cbm, RestTemplate restTemplate)
			throws Exception;

}
