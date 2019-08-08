package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.csi.sbs.common.business.util.PostUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.csi.sbs.common.business.util.XmlToJsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.exception.AcceptException;
import com.simnectzbank.lbs.processlayer.termdeposit.model.ChequeBookModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.CurrentAccountMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TransactionLogService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.IsCurrentTypeUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.LogUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.UTCUtil;

@Service("AccountMasterService")
public class AccountMasterServiceImpl implements AccountMasterService {


	@Resource
	PathConfig pathConfig;
	
	@Resource
	private TermDepositEnquiryService termDepositEnquiryService;

	@SuppressWarnings("unused")
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("unused")
	private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public Map<String, Object> chequeBookRequest(HeaderModel header, ChequeBookModel cbm, RestTemplate restTemplate)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验是否是current账号
		IsCurrentTypeUtil.isCurrentType(cbm.getAccountNumber());
		// 校验是否存在
		CurrentAccountMasterModel account = new CurrentAccountMasterModel();
		account.setAccountnumber(cbm.getAccountNumber());
		account.setCustomernumber(header.getCustomerNumber());
		// 调用数据隔离工具类
		account = (CurrentAccountMasterModel) DataIsolationUtil.condition(header, account);
		@SuppressWarnings("unchecked")
		//CurrentAccountMasterModel reaccount = (CurrentAccountMasterModel) currentAccountMasterDao.findOne(account);
		ResponseEntity<ResultUtil> postForEntity = restTemplate.postForEntity(pathConfig.getAccount_current_findOne(),
				PostUtil.getRequestEntity(JSON.toJSONString(account)), ResultUtil.class);

		CurrentAccountMasterModel reaccount =  JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity.getBody().getData()), CurrentAccountMasterModel.class);
		
		if (reaccount == null) {
			map.put("msg", "Record Not Found");
			map.put("code", "0");
			return map;
		}
		// check account status
		if (!reaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202001),
					ExceptionConstant.ERROR_CODE202001);
		}
		account.setChequebooksize(Long.parseLong(cbm.getChequeBookSize()));
		account.setChequebooktype(cbm.getChequeBookType());
		account.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
		
		//currentAccountMasterDao.chequeBookRequest(account);
		
		// 写入日志
		/*String logstr = account.getAccountnumber() + "chequeBookRequest success";
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);

		map.put("msg", "Transaction Accepted");
		map.put("code", "1");*/

		return map;
	}

	
}
