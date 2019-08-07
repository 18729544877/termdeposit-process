package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class TermDepositDetailPreModel {

	private String id;

	private String accountnumber;

	private String sandboxid;

	private String dockerid;

	private String depositnumber;

	private BigDecimal depositamount;

	private String termperiod;

	private BigDecimal terminterestrate;

	private BigDecimal maturitydate;

	private BigDecimal maturityinterest;

	private BigDecimal maturityamount;

	private String maturitystatus;

	private BigDecimal createdate;

	private BigDecimal lastupdateddate;

	private BigDecimal systemdate;

	public BigDecimal getSystemdate() {
		return systemdate;
	}

	public void setSystemdate(BigDecimal systemdate) {
		this.systemdate = systemdate;
	}

	public BigDecimal getLastupdateddate() {
		return lastupdateddate;
	}

	public void setLastupdateddate(BigDecimal lastupdateddate) {
		this.lastupdateddate = lastupdateddate;
	}

	public BigDecimal getCreatedate() {
		return createdate;
	}

	public void setCreatedate(BigDecimal createdate) {
		this.createdate = createdate;
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

	public String getDepositnumber() {
		return depositnumber;
	}

	public void setDepositnumber(String depositnumber) {
		this.depositnumber = depositnumber == null ? null : depositnumber.trim();
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
		this.termperiod = termperiod == null ? null : termperiod.trim();
	}

	public BigDecimal getTerminterestrate() {
		return terminterestrate;
	}

	public void setTerminterestrate(BigDecimal terminterestrate) {
		this.terminterestrate = terminterestrate;
	}

	public BigDecimal getMaturitydate() {
		return maturitydate;
	}

	public void setMaturitydate(BigDecimal maturitydate) {
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
		this.maturitystatus = maturitystatus == null ? null : maturitystatus.trim();
	}
}