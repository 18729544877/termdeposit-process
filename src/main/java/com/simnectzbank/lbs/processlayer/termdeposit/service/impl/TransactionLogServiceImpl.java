package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.annotation.TxTransaction;
import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.ResponseUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.csi.sbs.common.business.util.SendLogUtil;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.component.LocaleMessage;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.model.InsertTransactionLogModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TransactionLogModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TransactionLogService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.AvailableNumberUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.SendUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.UTCUtil;

@Service("TransactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService {

	@Resource
	PathConfig pathConfig;

	@Resource
	private AccountMasterService accountMasterService;
	
	@Resource
	LocaleMessage localeMessage;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

	private String classname = TermDepositMasterServiceImpl.class.getName();
	

	@SuppressWarnings({ "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	public ResultUtil insertTransacitonLog(RestTemplate restTemplate, InsertTransactionLogModel ase, HeaderModel header)
			throws Exception {
		ResultUtil result = null;
		String method = "insertTransacitonLog";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		if (ase.getRefaccountnumber() != null && ase.getAccountnumber().equals(ase.getRefaccountnumber())) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE500007, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NUMBER_CANNOT_SAME_WITH_REF_ACCOUNT_NUMBER));
		}
		TransactionLogModel transactionLogModel = new TransactionLogModel();
		transactionLogModel = getTransactionLogModel(ase, restTemplate);
		try {
            SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_insert(), JSON.toJSONString(transactionLogModel));
		} catch (Exception e) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE500006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}

		AvailableNumberUtil.availableSEQIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_SEQ, pathConfig);
		result = ResponseUtil.success(1, transactionLogModel.getTranseq(), SysConstant.CREATE_SUCCESS_TIP);
		
		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}
	
	private TransactionLogModel getTransactionLogModel(InsertTransactionLogModel ase, RestTemplate restTemplate) throws Exception {
		TransactionLogModel transactionLogModel= new TransactionLogModel();
		String sequence = SendUtil.sendPostForSysParameters(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), SysConstant.NEXT_AVAILABLE_SEQ);
		String reference = format.format(new Date()) + sequence;
		String transeq = format.format(new Date()) + ase.getTrantype() + ase.getChannel() + sequence + ase.getCountrycode()
				+ ase.getClearingcode() + ase.getBranchcode();
		transactionLogModel.setId(UUIDUtil.generateUUID());
		transactionLogModel.setReference(reference);
		transactionLogModel.setTranseq(transeq);
		transactionLogModel.setAccountnumber(ase.getAccountnumber());
		transactionLogModel.setTrandate(new BigDecimal(UTCUtil.getUTCTime()));
		transactionLogModel.setChannel(ase.getChannel());
		transactionLogModel.setChannelid(ase.getChannelid());
		transactionLogModel.setCountrycode(ase.getCountrycode());
		transactionLogModel.setClearingcode(ase.getClearingcode());
		transactionLogModel.setBranchcode(ase.getBranchcode());
		transactionLogModel.setSandboxid(ase.getSandboxid());
		transactionLogModel.setDockerid(ase.getDockerid());
		transactionLogModel.setTrantype(ase.getTrantype());
		transactionLogModel.setTranamt(ase.getTranamt());
		transactionLogModel.setPreviousbalamt(ase.getPreviousbalamt());
		transactionLogModel.setActualbalamt(ase.getActualbalamt());
		transactionLogModel.setRefaccountnumber(ase.getRefaccountnumber());
		transactionLogModel.setTfrseqno(ase.getTfrseqno());
		transactionLogModel.setCrdrmaintind(ase.getCrdrmaintind());
		transactionLogModel.setTrandesc(ase.getTrandesc());
		transactionLogModel.setCcy(ase.getCcy());
		return transactionLogModel;
	}

	@SuppressWarnings("unused")
	private boolean CheckDate(String date) {
		String rexp1 = "((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if (date.matches(rexp1)) {
			return true;
		} else {
			return false;
		}
	}
}
