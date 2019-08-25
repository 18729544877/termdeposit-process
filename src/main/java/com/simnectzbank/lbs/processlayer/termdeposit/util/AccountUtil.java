package com.simnectzbank.lbs.processlayer.termdeposit.util;

import org.springframework.web.client.RestTemplate;

import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.NumberConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ReturnConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;



public class AccountUtil {
	public static String getAccountType(String accountNumber) {
		String accountType = accountNumber.substring(accountNumber.length() - NumberConstant.ACCOUNT_TYPE_LENGTH, accountNumber.length());
		return accountType;
	}
	
	public static void saveLog(String accountNumber, RestTemplate restTemplate, PathConfig pathConfig) throws Exception {
		String logstr = ReturnConstant.CREATE_ACCOUNT + accountNumber + ReturnConstant.SUCCESS;
		
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_CREATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr, pathConfig);
		AvailableNumberUtil.availableNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_ACCOUNTNUMBER, pathConfig);
	}
}
