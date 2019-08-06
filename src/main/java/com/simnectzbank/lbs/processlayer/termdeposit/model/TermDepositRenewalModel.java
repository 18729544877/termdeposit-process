package com.simnectzbank.lbs.processlayer.termdeposit.model;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;


public class TermDepositRenewalModel {
	
	@NotNull(message="tdaccountnumber is a required field")
	@NotBlank(message="tdaccountnumber is a required field")
	@ApiModelProperty(notes="The term deposit account where your money is deposited."
	,example="HK760001001000000005100")
	private String tdaccountnumber;

	@NotNull(message="tdnumber is a required field")
	@NotBlank(message="tdnumber is a required field")
	@ApiModelProperty(notes="A unique number that identifies your term deposit. "
	,example="000000001")
    private String tdnumber;

//	@NotNull(message="tdccy is a required field")
//	@NotBlank(message="tdccy is a required field")
//	@ApiModelProperty(notes="The currency your deposit is recorded in."
//	,example="HKD")
//    private String tdccy;
    
    @NotNull(message="tdRenewalPeriod is a required field")
	@NotBlank(message="tdRenewalPeriod is a required field")
    @ApiModelProperty(notes="The duration that your money is deposited.</br> Possible values:1day, 1week, 2weeks, 1month, 2months, 3months, 6months, 9months, 12months"
	,example="1month")
    private String tdRenewalPeriod;

	public String getTdaccountnumber() {
		return tdaccountnumber;
	}

	public void setTdaccountnumber(String tdaccountnumber) {
		this.tdaccountnumber = tdaccountnumber;
	}

	public String getTdnumber() {
		return tdnumber;
	}

	public void setTdnumber(String tdnumber) {
		this.tdnumber = tdnumber;
	}

//	public String getTdccy() {
//		return tdccy;
//	}
//
//	public void setTdccy(String tdccy) {
//		this.tdccy = tdccy;
//	}

	public String getTdRenewalPeriod() {
		return tdRenewalPeriod;
	}

	public void setTdRenewalPeriod(String tdRenewalPeriod) {
		this.tdRenewalPeriod = tdRenewalPeriod;
	}

}
