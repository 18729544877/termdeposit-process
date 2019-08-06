package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class TransactionLogModel {
    //private String id;

    //private String reference;

    @ApiModelProperty(notes="The transaction ID.",example="20190228091829DAPI000003HK0001001")
    private String transeq;

    private String accountnumber;

    @ApiModelProperty(notes="The date the transaction happens.",example="")
    private BigDecimal trandate;

    @ApiModelProperty(notes="The transaction channel.",example="API")
    private String channel;

    @ApiModelProperty(notes="The unique id that identifies a specific transaction terminal."
    		,example="123")
    private String channelid;

    
    //private String countrycode;

    //@ApiModelProperty(notes="The unique identification for a bank.")
    //private String clearingcode;

    //@ApiModelProperty(notes="The unique identification for a bank branch.")
    //private String branchcode;

    @ApiModelProperty(notes="The transaction type.Possible Values:0001 - TD Application 0002 - TD Drawdown 0003 - TD Renewal 0004 - Deposit 0005 – Transfer 0006 - Withdraw 0007 - Foreign Buy 0008 - Foreign Sell",example="0001")
    private String trantype;

    @ApiModelProperty(notes="Transaction amount. ",example="300")
    private BigDecimal tranamt;

    @ApiModelProperty(notes="The initial account balance before the transaction happened. ",example="30000")
    private BigDecimal previousbalamt;

    @ApiModelProperty(notes="The final account balance after the transaction happened.",example="25000")
    private BigDecimal actualbalamt;

    @ApiModelProperty(notes="The relevant account number with whom the transaction happened.",example="HK530001001000003100")
    private String refaccountnumber;

    @ApiModelProperty(notes="The transfer trading flow number.",example="")
    private String tfrseqno;

    @ApiModelProperty(notes="A unique sign to show whether the transaction is deposit or withdraw. Sample: D (means Deposit) Sample: C (means Withdraw)",example="D")
    private String crdrmaintind;

    @ApiModelProperty(notes="Transaction description.",example="")
    private String trandesc;

    private String ccy;

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

	public BigDecimal getTrandate() {
		return trandate;
	}

	public void setTrandate(BigDecimal trandate) {
		this.trandate = trandate;
	}

	
}