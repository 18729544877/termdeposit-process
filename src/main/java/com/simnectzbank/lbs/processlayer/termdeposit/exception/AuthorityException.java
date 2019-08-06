package com.simnectzbank.lbs.processlayer.termdeposit.exception;

import com.csi.sbs.common.business.exception.GlobalException;

@SuppressWarnings("serial")
public class AuthorityException extends GlobalException {
	
	public AuthorityException(String message, int code) {
		super(message, code);
	}

}
