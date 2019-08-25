package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.PostUtil;

public class InterestCalculationUtil {
	
	private static String sysConfigPath = "http://SYSADMIN/sysadmin/sysconfig/getSystemParameter";

	@SuppressWarnings("unused")
	private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");


	@SuppressWarnings({ "unused", "rawtypes" })
	public static BigDecimal calDayInterest(RestTemplate restTemplate,String maturitydate, String termperiod,BigDecimal amount,BigDecimal interestrate) throws Exception {
		//calculate deposit date by maturitydate and termperiod
		Long beforeTDDate = CalculateDateUtil.CalculateTermDepositDays(maturitydate, termperiod);
		//maturitydate - depositdate
		Long days = Long.parseLong(maturitydate)-beforeTDDate;
		String days_ = String.valueOf(days);
		//amount * interestrate
		BigDecimal interest = amount.multiply(interestrate);
		//amount add interestrate
		BigDecimal tot = amount.add(interest);
		//get server time
		Long currentDate = UTCUtil.getUTCTime();
		//currentdate - maturitydate
		Long cmdays = currentDate-Long.parseLong(maturitydate);
		if(cmdays>0){
			//retrieve current deposit rate
			String temp = "{\"item\": \"CurrentDepositRate\"}";
			ResponseEntity result = restTemplate.postForEntity(sysConfigPath, PostUtil.getRequestEntity(temp), String.class);
		    if(result.getStatusCodeValue()==200 && !StringUtils.isEmpty(result.getBody())){
		    	JSONArray c1 = JSON.parseArray(result.getBody().toString());
		    	JSONObject c2 = JSON.parseObject(c1.get(0).toString());
		    	String c3 = JsonProcess.returnValue(c2, "value");
		    	//calculate day rate by year interest.
		    	BigDecimal d1 = new BigDecimal(c3);
		    	BigDecimal d2 = d1.divide(new BigDecimal(360),10,BigDecimal.ROUND_HALF_DOWN);
		    	//calculate interest of cmdays days.
		    	BigDecimal d3 = d2.multiply(new BigDecimal(cmdays).divide(new BigDecimal(86400000),10,BigDecimal.ROUND_HALF_DOWN));
		    	BigDecimal d4 = d3.multiply(tot);
		    	return interest.add(d4);
		    }
		}
		
		return interest;
	}

}
