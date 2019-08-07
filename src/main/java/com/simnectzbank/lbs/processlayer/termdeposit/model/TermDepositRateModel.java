package com.simnectzbank.lbs.processlayer.termdeposit.model;

import java.math.BigDecimal;

public class TermDepositRateModel {
    
	private String id;

    private String depositrange;

    private String tdperiod;

    private BigDecimal tdinterestrate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDepositrange() {
        return depositrange;
    }

    public void setDepositrange(String depositrange) {
        this.depositrange = depositrange == null ? null : depositrange.trim();
    }

    public String getTdperiod() {
        return tdperiod;
    }

    public void setTdperiod(String tdperiod) {
        this.tdperiod = tdperiod == null ? null : tdperiod.trim();
    }

    public BigDecimal getTdinterestrate() {
        return tdinterestrate;
    }

    public void setTdinterestrate(BigDecimal tdinterestrate) {
        this.tdinterestrate = tdinterestrate;
    }
}