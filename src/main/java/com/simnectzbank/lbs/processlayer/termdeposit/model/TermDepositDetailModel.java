package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class TermDepositDetailModel {
	
	private String accountnumber;

	private String depositnumber;

	private BigDecimal depositamount;

	private String termperiod;

	private BigDecimal terminterestrate;

	private String maturitydate;

	private BigDecimal maturityinterest;

	private BigDecimal maturityamount;

	private String maturitystatus;
	
	private String currencycode;
	
	private String createdate;
	
	private String systemdate;
	
	
	
	

	
	public String getSystemdate() {
		return systemdate;
	}

	public void setSystemdate(String systemdate) {
		this.systemdate = systemdate;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getDepositnumber() {
		return depositnumber;
	}

	public void setDepositnumber(String depositnumber) {
		this.depositnumber = depositnumber;
	}

	public BigDecimal getDepositamount() {
		return depositamount;
	}

	public void setDepositamount(BigDecimal depositamount) {
		this.depositamount = depositamount;
	}

	public String getTermperiod() {
		return termperiod;
	}

	public void setTermperiod(String termperiod) {
		this.termperiod = termperiod;
	}

	public BigDecimal getTerminterestrate() {
		return terminterestrate;
	}

	public void setTerminterestrate(BigDecimal terminterestrate) {
		this.terminterestrate = terminterestrate;
	}

	public String getMaturitydate() {
		return maturitydate;
	}

	public void setMaturitydate(String maturitydate) {
		this.maturitydate = maturitydate;
	}

	public BigDecimal getMaturityinterest() {
		return maturityinterest;
	}

	public void setMaturityinterest(BigDecimal maturityinterest) {
		this.maturityinterest = maturityinterest;
	}

	public BigDecimal getMaturityamount() {
		return maturityamount;
	}

	public void setMaturityamount(BigDecimal maturityamount) {
		this.maturityamount = maturityamount;
	}

	public String getMaturitystatus() {
		return maturitystatus;
	}

	public void setMaturitystatus(String maturitystatus) {
		this.maturitystatus = maturitystatus;
	}

}