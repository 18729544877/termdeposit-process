package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.DataIsolationUtil;
import com.csi.sbs.common.business.util.ResponseUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.csi.sbs.common.business.util.SendLogUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.component.LocaleMessage;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ReturnConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.model.ChequeBookModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.CurrentAccountMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.IsCurrentTypeUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.LogUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.SendUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.UTCUtil;

@Service("AccountMasterService")
public class AccountMasterServiceImpl implements AccountMasterService {


	@Resource
	PathConfig pathConfig;
	
	@Resource
	LocaleMessage localeMessage;
	
	@Resource
	private TermDepositEnquiryService termDepositEnquiryService;
	
	private String classname = AccountMasterServiceImpl.class.getName();

	@SuppressWarnings("rawtypes")
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil chequeBookRequest(HeaderModel header, ChequeBookModel cbm, RestTemplate restTemplate)
			throws Exception {
		String method = "chequeBookRequest";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		ResultUtil result = null;
		
		//check whether is current account
		result = IsCurrentTypeUtil.isCurrentType(cbm.getAccountNumber(), localeMessage);
		if(result != null){
			//check whether exist the account
			CurrentAccountMasterModel account = new CurrentAccountMasterModel();
			account.setAccountnumber(cbm.getAccountNumber());
			account.setCustomernumber(header.getCustomerNumber());
			//DataIsolationUtil
			account = (CurrentAccountMasterModel) DataIsolationUtil.condition(header, account);
			ResultUtil currentResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_current_findOne(), JSON.toJSONString(account));
			CurrentAccountMasterModel reaccount =  JSONObject.parseObject(
					JsonProcess.changeEntityTOJSON(currentResult.getData()), CurrentAccountMasterModel.class);
			
			if (reaccount == null) {
				result = ResponseUtil.fail(ReturnConstant.RETURN_CODE_FAIL, localeMessage.getMessage(ReturnConstant.RECORD_NOT_FOUND));
			}else{			
				// check account status
				if (!reaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
					result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NOT_ACTIVE));
				}
				account.setChequebooksize(Long.parseLong(cbm.getChequeBookSize()));
				account.setChequebooktype(cbm.getChequeBookType());
				account.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
				
				//currentAccountMasterDao.chequeBookRequest(account);
				
				//save log to DB
				String logstr = account.getAccountnumber() + "chequeBookRequest success";
				LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
						SysConstant.OPERATION_SUCCESS, logstr, pathConfig);
				
				result = ResponseUtil.success(ReturnConstant.RETURN_CODE_1, localeMessage.getMessage(ReturnConstant.TRANSACTION_ACCEPTED));
			}
		}

		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}

	
}
