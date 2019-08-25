package com.simnectzbank.lbs.processlayer.termdeposit.service;


import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDrawDownModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRenewalModel;



public interface TermDepositMasterService {
	
	
	 @SuppressWarnings("rawtypes")
	public ResultUtil termDepositApplication(HeaderModel header,TermDepositMasterModel tdm) throws Exception;

		 
	 @SuppressWarnings("rawtypes")
	public ResultUtil termDepositDrawDown(HeaderModel header,TermDepositDrawDownModel tddm,RestTemplate restTemplate) throws Exception;
	 	 	
	 
	 @SuppressWarnings("rawtypes")
	public ResultUtil termDepositRenewal(HeaderModel header,TermDepositRenewalModel tdrm) throws Exception;
}
