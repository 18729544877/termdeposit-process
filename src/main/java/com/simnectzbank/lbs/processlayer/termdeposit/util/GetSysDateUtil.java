package com.simnectzbank.lbs.processlayer.termdeposit.util;

import org.springframework.web.client.RestTemplate;

import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;


public class GetSysDateUtil {

	public static String getSystemDate(RestTemplate restTemplate, PathConfig pathConfig) throws Exception {
		String param = "SystemDate";
		String systimeStr = SendUtil.sendPostForSysParameters(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), param);
		
		return systimeStr;
	}

}
