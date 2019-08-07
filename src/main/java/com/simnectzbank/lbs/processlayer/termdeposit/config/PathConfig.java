package com.simnectzbank.lbs.processlayer.termdeposit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pathConfig")
public class PathConfig {

	@Value("${gateway.url}")
	private String gateWayUrl;

	@Value("${transaction.log.insert}")
	private String transaction_log_insert;
	
	@Value("${account.current.find}")
	private String account_current_find;
	
	@Value("${termdeposit.master.findone}")
	private String termdeposit_master_findone;
	
	@Value("${termdeposit.master.findMany}")
	private String termdeposit_master_findMany;
	
	@Value("${termdeposit.detail.findMany}")
	private String termdeposit_detail_findMany;
	
	@Value("${termdeposit.detail.findOne}")
	private String termdeposit_detail_findOne;
	
	public String getTermdeposit_detail_findOne() {
		return termdeposit_detail_findOne;
	}

	public void setTermdeposit_detail_findOne(String termdeposit_detail_findOne) {
		this.termdeposit_detail_findOne = termdeposit_detail_findOne;
	}

	public String getTermdeposit_master_findone() {
		return termdeposit_master_findone;
	}

	public void setTermdeposit_master_findone(String termdeposit_master_findone) {
		this.termdeposit_master_findone = termdeposit_master_findone;
	}

	public String getTermdeposit_master_findMany() {
		return termdeposit_master_findMany;
	}

	public void setTermdeposit_master_findMany(String termdeposit_master_findMany) {
		this.termdeposit_master_findMany = termdeposit_master_findMany;
	}

	public String getTermdeposit_detail_findMany() {
		return termdeposit_detail_findMany;
	}

	public void setTermdeposit_detail_findMany(String termdeposit_detail_findMany) {
		this.termdeposit_detail_findMany = termdeposit_detail_findMany;
	}

	public String getAccount_current_find() {
		return account_current_find;
	}

	public void setAccount_current_find(String account_current_find) {
		this.account_current_find = account_current_find;
	}

	public String getGateWayUrl() {
		return gateWayUrl;
	}

	public String getTransaction_log_insert() {
		return transaction_log_insert;
	}

	public void setTransaction_log_insert(String transaction_log_insert) {
		this.transaction_log_insert = transaction_log_insert;
	}

	public void setGateWayUrl(String gateWayUrl) {
		this.gateWayUrl = gateWayUrl;
	}

}
