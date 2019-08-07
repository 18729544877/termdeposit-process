package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class TermDepositForMasterModel {
private String id;
	
	private String countrycode;

    private String clearingcode;

    private String branchcode;
    
    private String sandboxid;
    
    private String dockerid;
	
	private String customernumber;

    private String relaccountnumber;

    private String currencycode;

    private String accountnumber;

    private String accountstatus;

    private BigDecimal lastupdateddate;
    
    private BigDecimal createdate;
    
    
    
    

	public BigDecimal getLastupdateddate() {
		return lastupdateddate;
	}

	public BigDecimal getCreatedate() {
		return createdate;
	}

	public void setCreatedate(BigDecimal createdate) {
		this.createdate = createdate;
	}

	public void setLastupdateddate(BigDecimal lastupdateddate) {
		this.lastupdateddate = lastupdateddate;
	}

	public String getDockerid() {
		return dockerid;
	}

	public void setDockerid(String dockerid) {
		this.dockerid = dockerid;
	}

	public String getSandboxid() {
		return sandboxid;
	}

	public void setSandboxid(String sandboxid) {
		this.sandboxid = sandboxid;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getClearingcode() {
		return clearingcode;
	}

	public void setClearingcode(String clearingcode) {
		this.clearingcode = clearingcode;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelaccountnumber() {
		return relaccountnumber;
	}

	public void setRelaccountnumber(String relaccountnumber) {
		this.relaccountnumber = relaccountnumber;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getCustomernumber() {
		return customernumber;
	}

	public void setCustomernumber(String customernumber) {
		this.customernumber = customernumber;
	}
    
    
}
