package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class DepositAmountRangeModel {
	
	private String id;
	
	private String ccytype;

    private String countrycode;

    private String clearingcode;

    private String branchcode;

	private BigDecimal amountrangemin;

	private BigDecimal amountrangemax;
	
	
	/**
	 * 表外字段
	 */
	private BigDecimal tdAmount;
	
	private BigDecimal tdAmount2;

	

	public BigDecimal getAmountrangemin() {
		return amountrangemin;
	}

	public void setAmountrangemin(BigDecimal amountrangemin) {
		this.amountrangemin = amountrangemin;
	}

	public BigDecimal getAmountrangemax() {
		return amountrangemax;
	}

	public void setAmountrangemax(BigDecimal amountrangemax) {
		this.amountrangemax = amountrangemax;
	}

	public BigDecimal getTdAmount() {
		return tdAmount;
	}

	public void setTdAmount(BigDecimal tdAmount) {
		this.tdAmount = tdAmount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public BigDecimal getTdAmount2() {
		return tdAmount2;
	}

	public void setTdAmount2(BigDecimal tdAmount2) {
		this.tdAmount2 = tdAmount2;
	}

	public String getCcytype() {
		return ccytype;
	}

	public void setCcytype(String ccytype) {
		this.ccytype = ccytype;
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

	
}