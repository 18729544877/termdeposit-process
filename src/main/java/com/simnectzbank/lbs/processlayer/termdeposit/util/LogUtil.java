package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.PostUtil;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SysTransactionLogEntity;


public class LogUtil {
	
	public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static boolean saveLog(
			RestTemplate restTemplate,
			String operationType,
			String sourceservices,
			String operationStatus,
			String operationDetail
			) throws Exception{
		
			SysTransactionLogEntity log = new SysTransactionLogEntity();
			log.setId(UUIDUtil.generateUUID());
			log.setOperationtype(operationType);
			log.setSourceservices(sourceservices);
			log.setOperationstate(operationStatus);
			log.setOperationdate(new BigDecimal(UTCUtil.getUTCTime()));
			log.setOperationdetail(operationDetail);

			@SuppressWarnings("unused")
			ResponseEntity<String> result2 = restTemplate.postForEntity(SysConstant.WRITE_LOG_SERVICEPATH,
					PostUtil.getRequestEntity(JsonProcess.changeEntityTOJSON(log)), String.class);
		
		    return true;
	}

}
