package com.simnectzbank.lbs.processlayer.termdeposit.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class TermDepositDrawDownModel {

	@ApiModelProperty(notes="The term deposit account where your money is deposited."
			,example="HK760001001000000005100")
	private String tdAccountNumber;

	@ApiModelProperty(notes="A unique number that identifies your term deposit. "
			,example="000000001")
	private String tdNumber;

	//@ApiModelProperty(notes="The amount that you’d like to save in the term deposit. tdAmount >= 10000"
	//		,example="30000.00")
	//private BigDecimal tdamount;

	//@ApiModelProperty(notes="The currency your deposit is recorded in."
	//		,example="HKD")
	//private String tdccy;

	@ApiModelProperty(notes=" A unique deposit account number, to which you’d like to transfer money."
			,example="HK720001001000000001001")
	private String debitAccountNumber;

	

	public String getTdAccountNumber() {
		return tdAccountNumber;
	}

	public void setTdAccountNumber(String tdAccountNumber) {
		this.tdAccountNumber = tdAccountNumber;
	}

	public String getTdNumber() {
		return tdNumber;
	}

	public void setTdNumber(String tdNumber) {
		this.tdNumber = tdNumber;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}

}