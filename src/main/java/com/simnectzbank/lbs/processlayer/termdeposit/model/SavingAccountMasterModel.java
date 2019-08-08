package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class SavingAccountMasterModel {

private String id;
    
    private String countrycode;

    private String clearingcode;

    private String branchcode;
    
    private String customernumber;

    private String accountnumber;

    private String accountstatus;

    private String currencycode;

    private BigDecimal ledgebalance;
    
    private BigDecimal availablebalance;
    
    private BigDecimal holdingbalance;

    private BigDecimal lastupdateddate;
    
    private String sandboxid;
    
    private String dockerid;
    
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
        this.id = id == null ? null : id.trim();
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber == null ? null : accountnumber.trim();
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus == null ? null : accountstatus.trim();
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode == null ? null : currencycode.trim();
    }

	public String getCustomernumber() {
		return customernumber;
	}

	public void setCustomernumber(String customernumber) {
		this.customernumber = customernumber;
	}

	public BigDecimal getLedgebalance() {
		return ledgebalance;
	}

	public void setLedgebalance(BigDecimal ledgebalance) {
		this.ledgebalance = ledgebalance;
	}

	public BigDecimal getAvailablebalance() {
		return availablebalance;
	}

	public void setAvailablebalance(BigDecimal availablebalance) {
		this.availablebalance = availablebalance;
	}

	public BigDecimal getHoldingbalance() {
		return holdingbalance;
	}

	public void setHoldingbalance(BigDecimal holdingbalance) {
		this.holdingbalance = holdingbalance;
	}
}