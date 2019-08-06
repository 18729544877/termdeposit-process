package com.simnectzbank.lbs.processlayer.termdeposit.exception;

import com.csi.sbs.common.business.exception.GlobalException;

@SuppressWarnings("serial")
public class OtherException extends GlobalException {
	
	public OtherException(String message, int code)
    {
        super(message, code);
    }
}
