package com.simnectzbank.lbs.processlayer.termdeposit.service;


import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDrawDownModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRenewalModel;



public interface TermDepositMasterService {
	
	
	 public Map<String,Object> termDepositApplication(HeaderModel header,TermDepositMasterModel tdm) throws Exception;

		 
	 public Map<String,Object> termDepositDrawDown(HeaderModel header,TermDepositDrawDownModel tddm,RestTemplate restTemplate) throws Exception;
	 	 	
	 
	 public Map<String,Object> termDepositRenewal(HeaderModel header,TermDepositRenewalModel tdrm) throws Exception;
}
