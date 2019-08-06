package com.simnectzbank.lbs.processlayer.termdeposit.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TermDepositEnquiryModel {
	
	@NotNull(message="tdnumber is a required field")
	@NotBlank(message="tdnumber is a required field")
	@ApiModelProperty(notes="A unique number that identifies your term deposit. "
	,example="000000001")
	private String tdnumber;
	
	@NotNull(message="accountnumber is a required field")
	@NotBlank(message="accountnumber is a required field")
	@ApiModelProperty(notes="The TD account number associated with the td number."
	,example="HK760001001000000005100")
	private String accountnumber;

	

	public String getTdnumber() {
		return tdnumber;
	}

	public void setTdnumber(String tdnumber) {
		this.tdnumber = tdnumber;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}
	
	
	
	
}
