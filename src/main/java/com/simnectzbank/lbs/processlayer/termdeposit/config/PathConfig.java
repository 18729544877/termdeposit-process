package com.simnectzbank.lbs.processlayer.termdeposit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pathConfig")
public class PathConfig {

	@Value("${gateway.url}")
	private String gateWayUrl;

	@Value("${transaction.log.insert}")
	private String transaction_log_insert;
	
	@Value("${account.current.findOne}")
	private String account_current_findOne;
	
	@Value("${termdeposit.master.findone}")
	private String termdeposit_master_findone;
	
	@Value("${termdeposit.master.findMany}")
	private String termdeposit_master_findMany;
	
	@Value("${termdeposit.detail.findMany}")
	private String termdeposit_detail_findMany;
	
	@Value("${termdeposit.detail.findOne}")
	private String termdeposit_detail_findOne;
	
	@Value("${account.saving.findOne}")
	private String account_saving_findOne;
	
	@Value("${deposit.range.findMax}")
	private String deposit_range_findMax;
	
	@Value("${deposit.range.findOne}")
	private String deposit_range_findOne;
	
	@Value("${termdeposit.detail.update}")
	private String termdeposit_detail_update;
	
	@Value("${termdeposit.detail.insert}")
	private String termdeposit_detail_insert;
	
	@Value("${account.saving.update}")
	private String account_saving_update;
	
	@Value("${transaction.log.findOne}")
	private String transaction_log_findOne;
	
	@Value("${transaction.log.update}")
	private String transaction_log_update;
	
	@Value("${account.current.update}")
	private String account_current_update;
	
	@Value("${deposit.rate.findOne}")
	private String deposit_rate_findOne;
	
	@Value("${account.saving.withdrawal}")
	private String account_saving_withdrawal;
	
	@Value("${account.current.withdrawal}")
	private String account_current_withdrawal;
	
	@Value("${sysadmin.sysconfig.findOne}")
	private String sysadmin_sysconfig_findOne;
	
	@Value("${sysadmin.sysconfig.update}")
	private String sysadmin_sysconfig_update;
	
	@Value("${sysadmin.holiday.findOne}")
	private String sysadmin_holiday_findOne;
	
	
	public String getSysadmin_holiday_findOne() {
		return sysadmin_holiday_findOne;
	}

	public void setSysadmin_holiday_findOne(String sysadmin_holiday_findOne) {
		this.sysadmin_holiday_findOne = sysadmin_holiday_findOne;
	}

	public String getSysadmin_sysconfig_findOne() {
		return sysadmin_sysconfig_findOne;
	}

	public void setSysadmin_sysconfig_findOne(String sysadmin_sysconfig_findOne) {
		this.sysadmin_sysconfig_findOne = sysadmin_sysconfig_findOne;
	}

	public String getSysadmin_sysconfig_update() {
		return sysadmin_sysconfig_update;
	}

	public void setSysadmin_sysconfig_update(String sysadmin_sysconfig_update) {
		this.sysadmin_sysconfig_update = sysadmin_sysconfig_update;
	}

	public String getAccount_current_withdrawal() {
		return account_current_withdrawal;
	}

	public void setAccount_current_withdrawal(String account_current_withdrawal) {
		this.account_current_withdrawal = account_current_withdrawal;
	}

	public String getAccount_saving_withdrawal() {
		return account_saving_withdrawal;
	}

	public void setAccount_saving_withdrawal(String account_saving_withdrawal) {
		this.account_saving_withdrawal = account_saving_withdrawal;
	}

	public String getDeposit_rate_findOne() {
		return deposit_rate_findOne;
	}

	public void setDeposit_rate_findOne(String deposit_rate_findOne) {
		this.deposit_rate_findOne = deposit_rate_findOne;
	}

	public String getAccount_current_update() {
		return account_current_update;
	}

	public void setAccount_current_update(String account_current_update) {
		this.account_current_update = account_current_update;
	}

	public String getTransaction_log_update() {
		return transaction_log_update;
	}

	public void setTransaction_log_update(String transaction_log_update) {
		this.transaction_log_update = transaction_log_update;
	}

	public String getTransaction_log_findOne() {
		return transaction_log_findOne;
	}

	public void setTransaction_log_findOne(String transaction_log_findOne) {
		this.transaction_log_findOne = transaction_log_findOne;
	}

	public String getTermdeposit_detail_insert() {
		return termdeposit_detail_insert;
	}

	public void setTermdeposit_detail_insert(String termdeposit_detail_insert) {
		this.termdeposit_detail_insert = termdeposit_detail_insert;
	}

	public String getAccount_saving_findOne() {
		return account_saving_findOne;
	}

	public void setAccount_saving_findOne(String account_saving_findOne) {
		this.account_saving_findOne = account_saving_findOne;
	}

	public String getDeposit_range_findMax() {
		return deposit_range_findMax;
	}

	public void setDeposit_range_findMax(String deposit_range_findMax) {
		this.deposit_range_findMax = deposit_range_findMax;
	}

	public String getDeposit_range_findOne() {
		return deposit_range_findOne;
	}

	public void setDeposit_range_findOne(String deposit_range_findOne) {
		this.deposit_range_findOne = deposit_range_findOne;
	}

	public String getTermdeposit_detail_update() {
		return termdeposit_detail_update;
	}

	public void setTermdeposit_detail_update(String termdeposit_detail_update) {
		this.termdeposit_detail_update = termdeposit_detail_update;
	}

	public String getAccount_saving_update() {
		return account_saving_update;
	}

	public void setAccount_saving_update(String account_saving_update) {
		this.account_saving_update = account_saving_update;
	}

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

	

	public String getAccount_current_findOne() {
		return account_current_findOne;
	}

	public void setAccount_current_findOne(String account_current_findOne) {
		this.account_current_findOne = account_current_findOne;
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
