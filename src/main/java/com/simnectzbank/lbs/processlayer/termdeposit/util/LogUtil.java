package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.math.BigDecimal;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SysTransactionLogEntity;


public class LogUtil {
	
	public static boolean saveLog(
			RestTemplate restTemplate,
			String operationType,
			String sourceservices,
			String operationStatus,
			String operationDetail, 
			PathConfig pathConfig
			) throws Exception{
		
			SysTransactionLogEntity log = new SysTransactionLogEntity();
			log.setId(UUIDUtil.generateUUID());
			log.setOperationtype(operationType);
			log.setSourceservices(sourceservices);
			log.setOperationstate(operationStatus);
			log.setOperationdate(new BigDecimal(UTCUtil.getUTCTime()));
			log.setOperationdetail(operationDetail);

			SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_insert(), JSON.toJSONString(log));
		
		    return true;
	}

}
