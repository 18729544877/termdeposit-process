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

	/**
	 * 利息计算(根据存款周期)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public static BigDecimal calDayInterest(RestTemplate restTemplate,String maturitydate, String termperiod,BigDecimal amount,BigDecimal interestrate) throws Exception {
		// 根据到期日和存款周期,计算之前存款的日期
		Long beforeTDDate = CalculateDateUtil.CalculateTermDepositDays(maturitydate, termperiod);
		// 之前的定存日期和到期日之间相差的天数
		Long days = Long.parseLong(maturitydate)-beforeTDDate;
		String days_ = String.valueOf(days);
		// 计算利息:金额*利率
		BigDecimal interest = amount.multiply(interestrate);
		// 本金+利息
		BigDecimal tot = amount.add(interest);
		// 获取当前服务器时间
		Long currentDate = UTCUtil.getUTCTime();
		// 计算当前服务器时间和到期日之间相差的天数
		Long cmdays = currentDate-Long.parseLong(maturitydate);
		// 如果cmdays 大于0,计算cmdays天的活期利息
		if(cmdays>0){
			//查询系统配置表的活期利率(年利率)
			String temp = "{\"item\": \"CurrentDepositRate\"}";
			ResponseEntity result = restTemplate.postForEntity(sysConfigPath, PostUtil.getRequestEntity(temp), String.class);
		    if(result.getStatusCodeValue()==200 && !StringUtils.isEmpty(result.getBody())){
		    	JSONArray c1 = JSON.parseArray(result.getBody().toString());
		    	JSONObject c2 = JSON.parseObject(c1.get(0).toString());
		    	String c3 = JsonProcess.returnValue(c2, "value");
		    	//根据年利率计算日利率
		    	BigDecimal d1 = new BigDecimal(c3);
		    	BigDecimal d2 = d1.divide(new BigDecimal(360),10,BigDecimal.ROUND_HALF_DOWN);
		    	//计算cmdays天的利息
		    	BigDecimal d3 = d2.multiply(new BigDecimal(cmdays).divide(new BigDecimal(86400000),10,BigDecimal.ROUND_HALF_DOWN));
		    	BigDecimal d4 = d3.multiply(tot);
		    	return interest.add(d4);
		    }
		}
		//返回利息
		return interest;
	}

}
