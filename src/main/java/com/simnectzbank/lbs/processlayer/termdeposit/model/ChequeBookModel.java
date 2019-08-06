package com.simnectzbank.lbs.processlayer.termdeposit.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChequeBookModel {

	@NotNull(message="accountNumber is a required field")
	@NotBlank(message="accountNumber is a required field")
	@ApiModelProperty(notes="A unique number used to identify a current bank account."
	,example="HK110001001000000002002")
	private String accountNumber;
	
	@NotNull(message="chequeBookType is a required field")
	@NotBlank(message="chequeBookType is a required field")
	@ApiModelProperty(notes="When it is set S, that means the chequebooktype is Short."
			+ "Possible values:S-Short,L-Long."
			,example="S")
	private String chequeBookType;
	
	@NotNull(message="chequeBookSize is a required field")
	@NotBlank(message="chequeBookSize is a required field")
	@ApiModelProperty(notes="The size of a cheque book.Possible values:50, 100"
			,example="50")
	private String chequeBookSize;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getChequeBookType() {
		return chequeBookType;
	}

	public void setChequeBookType(String chequeBookType) {
		this.chequeBookType = chequeBookType;
	}

	public String getChequeBookSize() {
		return chequeBookSize;
	}

	public void setChequeBookSize(String chequeBookSize) {
		this.chequeBookSize = chequeBookSize;
	}

}
