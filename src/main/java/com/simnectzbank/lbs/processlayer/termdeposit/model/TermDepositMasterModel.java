package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel
public class TermDepositMasterModel {

	
	@NotNull(message="tdAccountNumber is a required field")
	@NotBlank(message="tdAccountNumber is a required field")
	@ApiModelProperty(notes="The term deposit account, to which your money will be transferred and deposited."
	,example="HK760001001000000005100")
	private String tdAccountNumber;
	
	@NotNull(message="tdCcy is a required field")
	@NotBlank(message="tdCcy is a required field")
	@ApiModelProperty(notes="The currency your deposit is recorded in.</br>Possible values:HKD"
			,example="HKD")
	private String tdCcy;

	@ApiModelProperty(notes="The amount that you’d like to save in the term deposit. tdAmount >= 10000"
			,example="20000.00")
	private BigDecimal tdAmount;

	@NotNull(message="tdContractPeriod is a required field")
	@NotBlank(message="tdContractPeriod is a required field")
	@ApiModelProperty(notes="The duration that your money is deposited.</br> Possible values:1day, 1week, 2weeks, 1month, 2months, 3months, 6months, 9months, 12months"
			,example="1month")
	private String tdContractPeriod;
	
	@NotNull(message="debitAccountNumber is a required field")
	@NotBlank(message="debitAccountNumber is a required field")
	@ApiModelProperty(notes="A unique deposit account number， from which you’d like to transfer money."
	,example="HK720001001000000001001")
	private String debitAccountNumber;
	

	

	public String getTdAccountNumber() {
		return tdAccountNumber;
	}

	public void setTdAccountNumber(String tdAccountNumber) {
		this.tdAccountNumber = tdAccountNumber;
	}

	public String getTdCcy() {
		return tdCcy;
	}

	public void setTdCcy(String tdCcy) {
		this.tdCcy = tdCcy;
	}

	public BigDecimal getTdAmount() {
		return tdAmount;
	}

	public void setTdAmount(BigDecimal tdAmount) {
		this.tdAmount = tdAmount;
	}

	public String getTdContractPeriod() {
		return tdContractPeriod;
	}

	public void setTdContractPeriod(String tdContractPeriod) {
		this.tdContractPeriod = tdContractPeriod;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}
	
}