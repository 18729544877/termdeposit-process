package com.simnectzbank.lbs.processlayer.termdeposit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pathConfig")
public class PathConfig {
	
	@Value("${gateway.url}")
	private String gateWayUrl;

	public String getGateWayUrl() {
		return gateWayUrl;
	}

	public void setGateWayUrl(String gateWayUrl) {
		this.gateWayUrl = gateWayUrl;
	}

}
