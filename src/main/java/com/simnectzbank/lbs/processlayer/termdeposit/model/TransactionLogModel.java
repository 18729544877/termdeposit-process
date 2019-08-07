package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class TransactionLogModel {
	private String id;

	private String reference;

	private String transeq;

	private String accountnumber;

	private BigDecimal trandate;

	private String channel;

	private String channelid;

	private String countrycode;

	private String clearingcode;

	private String branchcode;

	private String sandboxid;

	private String dockerid;

	private String trantype;

	private BigDecimal tranamt;

	private BigDecimal previousbalamt;

	private BigDecimal actualbalamt;

	private String refaccountnumber;

	private String tfrseqno;

	private String crdrmaintind;

	private String trandesc;

	private String ccy;

	/*
	 * 表外字段
	 * 
	 * @return
	 * 
	 */

	private String stringtrandate;

	private String transFromDate;

	private String transToDate;

	public String getDockerid() {
		return dockerid;
	}

	public void setDockerid(String dockerid) {
		this.dockerid = dockerid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference == null ? null : reference.trim();
	}

	public String getTranseq() {
		return transeq;
	}

	public void setTranseq(String transeq) {
		this.transeq = transeq == null ? null : transeq.trim();
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber == null ? null : accountnumber.trim();
	}

	public BigDecimal getTrandate() {
		return trandate;
	}

	public void setTrandate(BigDecimal trandate) {
		this.trandate = trandate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel == null ? null : channel.trim();
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid == null ? null : channelid.trim();
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode == null ? null : countrycode.trim();
	}

	public String getClearingcode() {
		return clearingcode;
	}

	public void setClearingcode(String clearingcode) {
		this.clearingcode = clearingcode == null ? null : clearingcode.trim();
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode == null ? null : branchcode.trim();
	}

	public String getTrantype() {
		return trantype;
	}

	public void setTrantype(String trantype) {
		this.trantype = trantype == null ? null : trantype.trim();
	}

	public BigDecimal getTranamt() {
		return tranamt;
	}

	public void setTranamt(BigDecimal tranamt) {
		this.tranamt = tranamt;
	}

	public BigDecimal getPreviousbalamt() {
		return previousbalamt;
	}

	public void setPreviousbalamt(BigDecimal previousbalamt) {
		this.previousbalamt = previousbalamt;
	}

	public BigDecimal getActualbalamt() {
		return actualbalamt;
	}

	public void setActualbalamt(BigDecimal actualbalamt) {
		this.actualbalamt = actualbalamt;
	}

	public String getRefaccountnumber() {
		return refaccountnumber;
	}

	public void setRefaccountnumber(String refaccountnumber) {
		this.refaccountnumber = refaccountnumber == null ? null : refaccountnumber.trim();
	}

	public String getTfrseqno() {
		return tfrseqno;
	}

	public void setTfrseqno(String tfrseqno) {
		this.tfrseqno = tfrseqno == null ? null : tfrseqno.trim();
	}

	public String getCrdrmaintind() {
		return crdrmaintind;
	}

	public void setCrdrmaintind(String crdrmaintind) {
		this.crdrmaintind = crdrmaintind == null ? null : crdrmaintind.trim();
	}

	public String getTrandesc() {
		return trandesc;
	}

	public void setTrandesc(String trandesc) {
		this.trandesc = trandesc == null ? null : trandesc.trim();
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy == null ? null : ccy.trim();
	}

	public String getStringtrandate() {
		return stringtrandate;
	}

	public void setStringtrandate(String stringtrandate) {
		this.stringtrandate = stringtrandate;
	}

	public String getTransFromDate() {
		return transFromDate;
	}

	public void setTransFromDate(String transFromDate) {
		this.transFromDate = transFromDate;
	}

	public String getTransToDate() {
		return transToDate;
	}

	public void setTransToDate(String transToDate) {
		this.transToDate = transToDate;
	}

	public String getSandboxid() {
		return sandboxid;
	}

	public void setSandboxid(String sandboxid) {
		this.sandboxid = sandboxid;
	}

}