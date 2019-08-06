package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.csi.sbs.common.business.constant.CommonConstant;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.DataIsolationUtil;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.csi.sbs.common.business.util.XmlToJsonUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TransactionLogService;



@Service("TransactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService {
	

	
	@Resource
	private AccountMasterService accountMasterService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

	

	@SuppressWarnings({ "unchecked", "unused"})
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public Map<String, String> insertTransacitonLog(RestTemplate restTemplate, InsertTransactionLogModel ase) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(ase.getRefaccountnumber()!=null && ase.getAccountnumber().equals(ase.getRefaccountnumber())){
			throw new CallOtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500007),ExceptionConstant.ERROR_CODE500007);
		}
		String accountNumber = ase.getAccountnumber();
		String accountType =  accountNumber.substring(accountNumber.length()-3);
		HeaderModel header =  new HeaderModel();
		header.setCountryCode(ase.getCountrycode());
		header.setClearingCode(ase.getClearingcode());
		header.setSandBoxId(ase.getSandboxid());
		header.setBranchCode(ase.getBranchcode());
		header.setDockerId(ase.getDockerid());
		//插入日志不需要权限校验,先注释掉
//		ResultUtil result = checkAccountNumber(header, accountType, accountNumber, restTemplate);
//		if (result.getCode().equals("0")) {
//			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404001),ExceptionConstant.ERROR_CODE404001);
//		}
		//@SuppressWarnings("unused")
		String accountNumber1 = ase.getRefaccountnumber();
//		if(accountNumber1 != null && accountNumber1.length() >0){
//			String accountType1 =  accountNumber1.substring(accountNumber1.length()-3);
//			ResultUtil result1 = checkAccountNumber(header, accountType1, accountNumber1, restTemplate);
//			if (result1.getCode().equals("0")) {
//				throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404009),ExceptionConstant.ERROR_CODE404009);
//			}
//		}
		TransactionLogEntity  transactionLogEntity = new TransactionLogEntity();
		// 调用服务接口地址
		String param1 = "{\"apiname\":\"getSystemParameter\"}";
		ResponseEntity<String> result1 = restTemplate.postForEntity(
				"http://" + CommonConstant.getSYSADMIN() + SysConstant.SERVICE_INTERNAL_URL + "",
				PostUtil.getRequestEntity(param1), String.class);
		if (result1.getStatusCodeValue()!=200) {
			throw new CallOtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500003),ExceptionConstant.ERROR_CODE500003);
		}
		
		String path = JsonProcess.returnValue(JsonProcess.changeToJSONObject(result1.getBody()), "internaURL");
		// 调用系统参数服务接口
		String param2 = "{\"item\":\"SEQ\"}";
		ResponseEntity<String> result2 = restTemplate.postForEntity(path, PostUtil.getRequestEntity(param2),String.class);
		if (result2.getStatusCodeValue()!=200) {
			throw new CallOtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500003),ExceptionConstant.ERROR_CODE500003);
		}
		String SEQRes = result2.getBody();
		JSONObject jsonObject1 = JsonProcess.changeToJSONObject(JsonProcess.changeToJSONArray(SEQRes).get(0).toString());
		String SEQ = JsonProcess.returnValue(jsonObject1, "value");
		String reference = format.format(new Date()) + SEQ;
		String transeq = format.format(new Date()) + ase.getTrantype() + ase.getChannel() + SEQ + ase.getCountrycode() + ase.getClearingcode() + ase.getBranchcode();
		transactionLogEntity.setId(UUIDUtil.generateUUID());
		transactionLogEntity.setReference(reference);
		transactionLogEntity.setTranseq(transeq);
		transactionLogEntity.setAccountnumber(ase.getAccountnumber());
		transactionLogEntity.setTrandate(new BigDecimal(UTCUtil.getUTCTime()));
		transactionLogEntity.setChannel(ase.getChannel());
		transactionLogEntity.setChannelid(ase.getChannelid());
		transactionLogEntity.setCountrycode(ase.getCountrycode());
		transactionLogEntity.setClearingcode(ase.getClearingcode());
		transactionLogEntity.setBranchcode(ase.getBranchcode());
		transactionLogEntity.setSandboxid(ase.getSandboxid());
		transactionLogEntity.setDockerid(ase.getDockerid());
		transactionLogEntity.setTrantype(ase.getTrantype());
		transactionLogEntity.setTranamt(ase.getTranamt());
		transactionLogEntity.setPreviousbalamt(ase.getPreviousbalamt());
		transactionLogEntity.setActualbalamt(ase.getActualbalamt());
		transactionLogEntity.setRefaccountnumber(ase.getRefaccountnumber());
		transactionLogEntity.setTfrseqno(ase.getTfrseqno());
		transactionLogEntity.setCrdrmaintind(ase.getCrdrmaintind());
		transactionLogEntity.setTrandesc(ase.getTrandesc());
		transactionLogEntity.setCcy(ase.getCcy());
		
		try{
			transactionLogDao.insert(transactionLogEntity);
		}catch(Exception e){
			throw new InsertException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500006),ExceptionConstant.ERROR_CODE500006);
		}
		
		
		AvailableNumberUtil.availableSEQIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_SEQ);
		map.put("msg", SysConstant.CREATE_SUCCESS_TIP);
		map.put("transeq", transactionLogEntity.getTranseq());
		map.put("code", "1");
		return map;
	}

	
	@SuppressWarnings("unused")
	private boolean CheckDate(String date){
		String rexp1 = "((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if(date.matches(rexp1)){
			return true;
		}else{
			return false;
		}
	}
}
