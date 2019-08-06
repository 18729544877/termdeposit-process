package com.simnectzbank.lbs.processlayer.termdeposit.exception;

import com.csi.sbs.common.business.exception.GlobalException;

@SuppressWarnings("serial")
public class AcceptException extends GlobalException {
	
	public AcceptException(String message, int code) {
		super(message, code);
	}
}
