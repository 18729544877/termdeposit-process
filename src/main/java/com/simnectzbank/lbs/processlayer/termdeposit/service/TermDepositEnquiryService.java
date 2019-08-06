package com.simnectzbank.lbs.processlayer.termdeposit.service;


import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositEnquiryModel;


public interface TermDepositEnquiryService {
	
	
	@SuppressWarnings("rawtypes")
	public ResultUtil termDepositEnquiry(HeaderModel header,TermDepositEnquiryModel tdem,RestTemplate restTemplate) throws Exception;
    
    @SuppressWarnings("rawtypes")
	public ResultUtil termDepositAllEnquiry(HeaderModel header,String customerNumber,RestTemplate restTemplate) throws Exception;
    
    @SuppressWarnings("rawtypes")
	public ResultUtil getTermDepositByAccount(HeaderModel header,String accountNumber,RestTemplate restTemplate) throws Exception;

}
