package com.simnectzbank.lbs.processlayer.termdeposit.util;

import com.csi.sbs.common.business.util.ResponseUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.component.LocaleMessage;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;

public class IsCurrentTypeUtil {

	@SuppressWarnings("rawtypes")
	public static ResultUtil isCurrentType(String accountNumber, LocaleMessage localeMessage) throws Exception {
		ResultUtil result = null;

		String accountType = accountNumber.substring(accountNumber.length() - 3);
		if (!accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE201003,
					localeMessage.getMessage(ExceptionConstant.NOT_A_CURRENT_ACCOUNT));
		}
		return result;
	}

}
