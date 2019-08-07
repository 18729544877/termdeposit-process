package com.simnectzbank.lbs.processlayer.termdeposit.util;

import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.exception.OtherException;

public class IsCurrentTypeUtil {
	
	public static void isCurrentType(String accountNumber) throws Exception{
		String accountType = accountNumber.substring(accountNumber.length()-3);
		if(!accountType.equals(SysConstant.ACCOUNT_TYPE2)){
			throw new OtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE201003),ExceptionConstant.ERROR_CODE201003);
		}
	}

}
