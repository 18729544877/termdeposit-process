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
import com.csi.sbs.common.business.util.UUIDUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.component.LocaleMessage;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ReturnConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.model.CurrentAccountMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.DepositAmountRangeModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.HolidayModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.InsertTransactionLogModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SavingAccountMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SysConfigModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailPreModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDrawDownModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositForMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRateModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRenewalModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TransactionLogModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TransactionLogService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.AccountUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.AvailableNumberUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.CalculateMaturityDateUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.GetSysDateUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.InterestCalculationUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.LogUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.SendUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.UTCUtil;


@Service("TermDepositMasterService")
public class TermDepositMasterServiceImpl implements TermDepositMasterService {

	@Resource
	PathConfig pathConfig;
	
	@Resource
	private RestTemplate restTemplate;
	
	@Resource
	LocaleMessage localeMessage;

	@Resource
	private AccountMasterService accountMasterService;

	@Resource
	private TransactionLogService transactionLogService;
	
	private String classname = TermDepositMasterServiceImpl.class.getName();

	@SuppressWarnings({ "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositApplication(HeaderModel header, TermDepositMasterModel tdm) throws Exception {
		ResultUtil result = null;
		String method = "termDepositApplication";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		// check TdAccount Number
		TermDepositForMasterModel tdaccount = new TermDepositForMasterModel();
		tdaccount.setAccountnumber(tdm.getTdAccountNumber());
		tdaccount.setCurrencycode(tdm.getTdCcy());
		//调用数据隔离工具类
		tdaccount = (TermDepositForMasterModel) DataIsolationUtil.condition(header, tdaccount);
		ResultUtil termDepositMasterResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findone(), JSON.toJSONString(tdaccount));
		TermDepositForMasterModel retdaccount = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(termDepositMasterResult.getData()), TermDepositForMasterModel.class);
		
		if (retdaccount == null) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}else if(!retdaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NOT_ACTIVE));
		}else{
			
		}
		
		// check Debit Account Number(saving account)
		String accountType = AccountUtil.getAccountType(tdm.getDebitAccountNumber());
		SavingAccountMasterModel savaccount = new SavingAccountMasterModel();
		CurrentAccountMasterModel currentaccount = new CurrentAccountMasterModel();
		SavingAccountMasterModel resavaccount = null;
		if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
			resavaccount = getSaveingAccount(tdm, header, savaccount);
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
			CurrentAccountMasterModel recurrent = getCurrentAccount(tdm, header, currentaccount);
			resavaccount = new SavingAccountMasterModel();
			resavaccount.setAccountnumber(recurrent.getAccountnumber());
			resavaccount.setAvailablebalance(recurrent.getAvailablebalance());
			resavaccount.setLedgebalance(recurrent.getLedgebalance());
			resavaccount.setCustomernumber(header.getCustomerNumber());
		}

		if (resavaccount == null) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404007, localeMessage.getMessage(ExceptionConstant.DEBIT_ACCOUNT_NUMBER_NOT_FOUND));
		}else if(!resavaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NOT_ACTIVE));
		}else if(resavaccount.getAvailablebalance().compareTo(tdm.getTdAmount()) == -1) {
			//insufficent fund
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202002, localeMessage.getMessage(ExceptionConstant.INSUFFICIENT_FUND));
		}else{
			// get next available TD number
			String tdNumber = SendUtil.sendPostForSysParameters(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), "NextAvailableTDNumber");
			// Map Amount Range
			DepositAmountRangeModel depositAmountRange = new DepositAmountRangeModel();
			depositAmountRange.setTdAmount(tdm.getTdAmount());
			// depositAmountRange.setCountrycode(header.getCountryCode());
			// depositAmountRange.setClearingcode(header.getClearingCode());
			// depositAmountRange.setBranchcode(header.getBranchCode());
			depositAmountRange.setCcytype(tdm.getTdCcy());
			
			// Alina 获取amountrangemax为null的amount range
			DepositAmountRangeModel maxInfo = new DepositAmountRangeModel();
			// maxInfo.setCountrycode(header.getCountryCode());
			// maxInfo.setClearingcode(header.getClearingCode());
			// maxInfo.setBranchcode(header.getBranchCode());
			maxInfo.setCcytype(tdm.getTdCcy());
			//DepositAmountRangeModel reMax = depositAmountRangeDao.findMax(maxInfo);
			ResultUtil depositRange = SendUtil.sendPostRequest(restTemplate, pathConfig.getDeposit_range_findMax(), JSON.toJSONString(maxInfo));
			DepositAmountRangeModel reMax = JSONObject.parseObject(
					JsonProcess.changeEntityTOJSON(depositRange.getData()), DepositAmountRangeModel.class);
			// Map Rate
			TermDepositRateModel rate = new TermDepositRateModel();
			if (reMax != null && tdm.getTdAmount().compareTo(reMax.getAmountrangemin()) >= 0) {
				rate.setDepositrange(reMax.getId());
			} else {
				//DepositAmountRangeModel redep = depositAmountRangeDao.findOne(depositAmountRange);
				ResultUtil depositAmountRangeFindOne = SendUtil.sendPostRequest(restTemplate, pathConfig.getDeposit_range_findOne(), JSON.toJSONString(depositRange));
				DepositAmountRangeModel redep = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(depositAmountRangeFindOne.getData()), DepositAmountRangeModel.class);
				
				if (redep == null) {
					result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202009, localeMessage.getMessage(ExceptionConstant.THE_AMOUNT_NOT_SUPPORT_TERM_DEPOSIT));
				}else{					
					rate.setDepositrange(redep.getId());
				}
			}
			rate.setTdperiod(tdm.getTdContractPeriod());
			
			ResultUtil termDepositRateFindOne = SendUtil.sendPostRequest(restTemplate, pathConfig.getDeposit_range_findOne(), JSON.toJSONString(rate));
			TermDepositRateModel rerate = JSONObject.parseObject(
					JsonProcess.changeEntityTOJSON(termDepositRateFindOne.getData()), TermDepositRateModel.class);
			
			if (rerate == null) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202014, localeMessage.getMessage(ExceptionConstant.UNSUPPORTED_COTRACT_PERIOD));
			}else{
				// Calculate maturity date
				SysConfigModel sysConfigModel = new SysConfigModel();
				sysConfigModel.setItem("SystemDate");
				Long maturitydate = null;
				String redate = SendUtil.sendPostForSysParameters(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), sysConfigModel.toString());
				maturitydate = CalculateMaturityDateUtil.CalculateTermDepositDays(redate, tdm.getTdContractPeriod());
				//判断到期日是否是法定节假日(需要将UTC时间转为中国东八区时间)
				HolidayModel model = new HolidayModel();
				model.setDay(UTCUtil.convertToTwo(String.valueOf(maturitydate)).replace("-", ""));
				
				HolidayModel findHoliday = SendUtil.sendPostRequestStr(restTemplate, pathConfig.getSysadmin_holiday_findOne(), JSON.toJSONString(model), HolidayModel.class);
				
				if(findHoliday != null){
					//到期日如果是法定节假日,将到期日往后顺延到下一个工作日
					boolean temp_flag = true;
					int temp_day = 0;
					do{
						temp_day++;
						Long l1 = CalculateMaturityDateUtil.plusDay(temp_day, String.valueOf(maturitydate));
						String temp_date = UTCUtil.convertToTwo(String.valueOf(l1));
						String formatDateStr = temp_date.replace("-", "");
						HolidayModel holidayModel = new HolidayModel();
						holidayModel.setDay(formatDateStr);
						
						HolidayModel holiday = SendUtil.sendPostRequestStr(restTemplate, pathConfig.getSysadmin_holiday_findOne(), JSON.toJSONString(model), HolidayModel.class);
						
						if(holiday != null){
							temp_flag = true;
						}else{
							//temp = temp_date;
							maturitydate = CalculateMaturityDateUtil.plusDay(temp_day, String.valueOf(maturitydate));
							temp_flag = false;
						}
					}while(temp_flag);
				}
				// TermDepositDetail add
				TermDepositDetailPreModel termDepositDetail = new TermDepositDetailPreModel();
				termDepositDetail.setAccountnumber(tdm.getTdAccountNumber());
				termDepositDetail.setDepositamount(tdm.getTdAmount());
				termDepositDetail.setDepositnumber(tdNumber);
				termDepositDetail.setSandboxid(header.getSandBoxId());
				termDepositDetail.setDockerid(header.getDockerId());
				termDepositDetail.setId(UUIDUtil.generateUUID());
				termDepositDetail.setMaturitydate(new BigDecimal(maturitydate));
				termDepositDetail.setMaturitystatus(SysConstant.MATURITY_STATUS_A);
				termDepositDetail.setTerminterestrate(rerate.getTdinterestrate());
				termDepositDetail.setTermperiod(tdm.getTdContractPeriod());
				termDepositDetail.setCreatedate(new BigDecimal(UTCUtil.getUTCTime()));
				termDepositDetail.setSystemdate(new BigDecimal(GetSysDateUtil.getSystemDate(restTemplate, pathConfig)));
				
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
					savaccount.setAccountnumber(resavaccount.getAccountnumber());
					savaccount.setAvailablebalance(resavaccount.getAvailablebalance().subtract(tdm.getTdAmount()));
					savaccount.setLedgebalance(resavaccount.getLedgebalance().subtract(tdm.getTdAmount()));
					
					//int i = savingAccountMasterDao.withdrawal(savaccount);
					SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_saving_withdrawal(), JSON.toJSONString(savaccount));
				}
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
					currentaccount.setAccountnumber(resavaccount.getAccountnumber());
					currentaccount.setAvailablebalance(resavaccount.getAvailablebalance().subtract(tdm.getTdAmount()));
					currentaccount.setLedgebalance(resavaccount.getLedgebalance().subtract(tdm.getTdAmount()));
					//int i = currentAccountMasterDao.withdrawal(currentaccount);
					SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_current_withdrawal(), JSON.toJSONString(currentaccount));
				}
				
				//termDepositDetailDao.insert(termDepositDetail);
				SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_insert(), JSON.toJSONString(termDepositDetail));
				AvailableNumberUtil.availableTDNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_TDNUMBER, pathConfig);
				
				// write Log
				String logstr = "Transaction Accepted TDNumber:" + tdNumber + " Account Number:" + tdm.getTdAccountNumber();
				LogUtil.saveLog(restTemplate, SysConstant.OPERATION_CREATE, SysConstant.LOCAL_SERVICE_NAME,
						SysConstant.OPERATION_SUCCESS, logstr, pathConfig);
				
				// debit account transaction log信息
				InsertTransactionLogModel depositAccount = new InsertTransactionLogModel();
				depositAccount.setAccountnumber(resavaccount.getAccountnumber());
				depositAccount.setBranchcode(header.getBranchCode());
				depositAccount.setCcy(retdaccount.getCurrencycode());
				depositAccount.setChannel(SysConstant.CHANNEL_TYPE);
				depositAccount.setChannelid(header.getUserID());
				depositAccount.setClearingcode(header.getClearingCode());
				depositAccount.setCountrycode(header.getCountryCode());
				depositAccount.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE1);
				depositAccount.setPreviousbalamt(resavaccount.getLedgebalance());
				depositAccount.setRefaccountnumber(tdm.getTdAccountNumber());
				depositAccount.setTranamt(tdm.getTdAmount());
				depositAccount.setSandboxid(header.getSandBoxId());
				depositAccount.setDockerid(header.getDockerId());
				depositAccount.setTrandesc("Term Deposit Application");
				depositAccount.setTrantype(SysConstant.TRANSACTION_TYPE6);
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
					depositAccount.setActualbalamt(savaccount.getLedgebalance());
				}
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
					depositAccount.setActualbalamt(currentaccount.getLedgebalance());
				}
				result = transactionLogService.insertTransacitonLog(restTemplate, depositAccount, header);
				String transeq = result.getCode();
				// td account transaction log 信息
				
				InsertTransactionLogModel tdApplication = getTermDepositApplication(header, tdm, retdaccount, transeq, resavaccount);
				result = transactionLogService.insertTransacitonLog(restTemplate, tdApplication, header);
				
				// 给转出账户更新对方账户流水号
				TransactionLogModel transactionLogEntity = new TransactionLogModel();
				transactionLogEntity.setTranseq(transeq);
				ResultUtil rest = SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_findOne(), JSON.toJSONString(transactionLogEntity));
				TransactionLogModel transferOutUpdate = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(rest.getData()), TransactionLogModel.class);
				
				transactionLogEntity.setId(transferOutUpdate.getId());
				transactionLogEntity.setTfrseqno(transeq);
				SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_update(), JSON.toJSONString(transactionLogEntity));
				
				result = ResponseUtil.success(1, tdNumber, logstr);
			}
		}
		
		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}

	private InsertTransactionLogModel getTermDepositApplication(HeaderModel header, TermDepositMasterModel tdm,
			TermDepositForMasterModel retdaccount, String transeq, SavingAccountMasterModel resavaccount) {
		InsertTransactionLogModel tdApplication = new InsertTransactionLogModel();
		tdApplication.setAccountnumber(tdm.getTdAccountNumber());
		tdApplication.setBranchcode(header.getBranchCode());
		tdApplication.setCcy(retdaccount.getCurrencycode());
		tdApplication.setChannel(SysConstant.CHANNEL_TYPE);
		tdApplication.setChannelid(header.getUserID());
		tdApplication.setClearingcode(header.getClearingCode());
		tdApplication.setCountrycode(header.getCountryCode());
		tdApplication.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE2);
		tdApplication.setRefaccountnumber(resavaccount.getAccountnumber());
		tdApplication.setTfrseqno(transeq);
		tdApplication.setTranamt(tdm.getTdAmount());
		tdApplication.setSandboxid(header.getSandBoxId());
		tdApplication.setDockerid(header.getDockerId());
		tdApplication.setTrandesc("Term Deposit Application");
		tdApplication.setTrantype(SysConstant.TRANSACITON_TYPE1);
		
		return tdApplication;
	}

	@SuppressWarnings("rawtypes")
	private CurrentAccountMasterModel getCurrentAccount(TermDepositMasterModel tdm, HeaderModel header,
			CurrentAccountMasterModel currentaccount) throws Exception {
		currentaccount.setAccountnumber(tdm.getDebitAccountNumber());
		currentaccount.setCustomernumber(header.getCustomerNumber());
		currentaccount = (CurrentAccountMasterModel) DataIsolationUtil.condition(header, currentaccount);
		//CurrentAccountMasterModel recurrent = (CurrentAccountMasterModel) currentAccountMasterDao.findOne(currentaccount);
	    ResultUtil result = SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_current_findOne(), JSON.toJSONString(currentaccount));
		CurrentAccountMasterModel recurrent = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(result.getData()), CurrentAccountMasterModel.class);
		return recurrent;
	}

	@SuppressWarnings("rawtypes")
	private SavingAccountMasterModel getSaveingAccount(TermDepositMasterModel tdm, HeaderModel header,
			SavingAccountMasterModel savaccount) throws Exception {
		SavingAccountMasterModel savingAccountMasterModel = new SavingAccountMasterModel();
		
		savaccount.setAccountnumber(tdm.getDebitAccountNumber());
		savaccount.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		savaccount = (SavingAccountMasterModel) DataIsolationUtil.condition(header, savaccount);
		
		ResultUtil result = SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_saving_findOne(), JSON.toJSONString(savaccount));
		savingAccountMasterModel = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(result.getData()), SavingAccountMasterModel.class);
		return savingAccountMasterModel;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositDrawDown(HeaderModel header, TermDepositDrawDownModel tddm,RestTemplate restTemplate) throws Exception {
		ResultUtil result = null;
		String method = "termDepositDrawDown";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		// check TdAccount Number
		TermDepositForMasterModel tdaccount = new TermDepositForMasterModel();
		tdaccount.setAccountnumber(tddm.getTdAccountNumber());
		tdaccount.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdaccount = (TermDepositForMasterModel) DataIsolationUtil.condition(header, tdaccount);
		ResultUtil termDepositMaster = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findone(), JSON.toJSONString(tdaccount));
		TermDepositForMasterModel retdaccount = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(termDepositMaster.getData()), TermDepositForMasterModel.class);
		
		if (retdaccount == null) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}

		// check Transaction Account and TD number
		TermDepositDetailModel tddetail = new TermDepositDetailModel();
		tddetail.setAccountnumber(tddm.getTdAccountNumber());
		tddetail.setDepositnumber(tddm.getTdNumber());

		ResultUtil rest = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findOne(), JSON.toJSONString(tddetail));
		TermDepositDetailPreModel retddetail = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(rest.getData()), TermDepositDetailPreModel.class);
		
		if (retddetail == null) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}else{
			// check Debit Account Number(saving account)
			String accountType = tddm.getDebitAccountNumber().substring(tddm.getDebitAccountNumber().length() - 3);
			SavingAccountMasterModel savaccount = new SavingAccountMasterModel();
			CurrentAccountMasterModel currentaccount = new CurrentAccountMasterModel();
			SavingAccountMasterModel resavaccount = null;
			if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
				resavaccount = getSavingAccount(tddm, header, savaccount);
			}else if(accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
				currentaccount.setAccountnumber(tddm.getDebitAccountNumber());
				//调用数据隔离工具类
				currentaccount = (CurrentAccountMasterModel) DataIsolationUtil.condition(header, currentaccount);
				ResultUtil reslt = SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_current_findOne(), JSON.toJSONString(currentaccount));
				CurrentAccountMasterModel recurrent = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(reslt.getData()), CurrentAccountMasterModel.class);
				
				resavaccount = new SavingAccountMasterModel();
				resavaccount.setAccountnumber(recurrent.getAccountnumber());
				resavaccount.setAvailablebalance(recurrent.getAvailablebalance());
				resavaccount.setLedgebalance(recurrent.getLedgebalance());
			}
			// check save account
			if (resavaccount == null) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404007, localeMessage.getMessage(ExceptionConstant.DEBIT_ACCOUNT_NUMBER_NOT_FOUND));
			}else if (!resavaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NOT_ACTIVE));
			}else if (retddetail.getMaturitystatus().equals(SysConstant.MATURITY_STATUS_D)) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202010, localeMessage.getMessage(ExceptionConstant.TD_RECORD_HAS_BEEN_DRAWN_DOWN));
			}else if(Long.parseLong(String.valueOf(retddetail.getMaturitydate())) > UTCUtil.getUTCTime()){
				// check Maturity date
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202011, "");
			}else{
				// Calculation of interest
				BigDecimal interest = InterestCalculationUtil.calDayInterest(restTemplate,String.valueOf(retddetail.getMaturitydate()), retddetail.getTermperiod(), retddetail.getDepositamount(), retddetail.getTerminterestrate());
				// Maturity Amount
				BigDecimal maturityAmount = retddetail.getDepositamount().add(interest);
				// update tddetail
				TermDepositDetailPreModel uptddetail = new TermDepositDetailPreModel();
				uptddetail.setMaturityamount(maturityAmount);
				uptddetail.setMaturityinterest(interest);
				uptddetail.setMaturitystatus(SysConstant.MATURITY_STATUS_D);
				uptddetail.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
				uptddetail.setId(retddetail.getId());
				// update accountMaster
				BigDecimal r11 = resavaccount.getLedgebalance();
				BigDecimal r12 = resavaccount.getAvailablebalance();
				
				// Alina according accounttype to update the account balance
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
					savaccount.setLedgebalance(r11.add(maturityAmount));
					savaccount.setAvailablebalance(r12.add(maturityAmount));
					savaccount.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
					SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_saving_update(), JSON.toJSONString(savaccount));
				}
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
					currentaccount.setLedgebalance(r11.add(maturityAmount));
					currentaccount.setAvailablebalance(r12.add(maturityAmount));
					currentaccount.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
					SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_current_update(), JSON.toJSONString(currentaccount));
				}
				//termDepositDetailDao.update(uptddetail);
				SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_update(), JSON.toJSONString(uptddetail));
				
				// write log
				String logstr = "Transaction Accepted";
				LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
						SysConstant.OPERATION_SUCCESS, logstr, pathConfig);
				
				// deposit account transaction log
				InsertTransactionLogModel depositAccount = new InsertTransactionLogModel();
				depositAccount.setAccountnumber(resavaccount.getAccountnumber());
				depositAccount.setBranchcode(header.getBranchCode());
				depositAccount.setCcy(retdaccount.getCurrencycode());
				depositAccount.setChannel(SysConstant.CHANNEL_TYPE);
				depositAccount.setChannelid(header.getUserID());
				depositAccount.setClearingcode(header.getClearingCode());
				depositAccount.setCountrycode(header.getCountryCode());
				depositAccount.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE2);
				depositAccount.setPreviousbalamt(r11);
				depositAccount.setRefaccountnumber(tddm.getTdAccountNumber());
				depositAccount.setTranamt(maturityAmount);
				depositAccount.setSandboxid(header.getSandBoxId());
				depositAccount.setDockerid(header.getDockerId());
				depositAccount.setTrandesc("Term Deposit Draw Down");
				depositAccount.setTrantype(SysConstant.TRANSACTION_TYPE4);
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_SAVING)) {
					depositAccount.setActualbalamt(savaccount.getLedgebalance());
				}
				if (accountType.equals(SysConstant.ACCOUNT_TYPE_CURRENT)) {
					depositAccount.setActualbalamt(currentaccount.getLedgebalance());
				}
				
				result = transactionLogService.insertTransacitonLog(restTemplate, depositAccount, header);
				String transeq = (String) result.getData();
				// td account transaction log
				InsertTransactionLogModel tdApplication = new InsertTransactionLogModel();
				tdApplication.setAccountnumber(tddm.getTdAccountNumber());
				tdApplication.setBranchcode(header.getBranchCode());
				tdApplication.setCcy(retdaccount.getCurrencycode());
				tdApplication.setChannel(SysConstant.CHANNEL_TYPE);
				tdApplication.setChannelid(header.getUserID());
				tdApplication.setClearingcode(header.getClearingCode());
				tdApplication.setCountrycode(header.getCountryCode());
				tdApplication.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE1);
				tdApplication.setRefaccountnumber(resavaccount.getAccountnumber());
				tdApplication.setTfrseqno(transeq);
				tdApplication.setTranamt(maturityAmount);
				tdApplication.setSandboxid(header.getSandBoxId());
				tdApplication.setDockerid(header.getDockerId());
				tdApplication.setTrandesc("Term Deposit Draw Down");
				tdApplication.setTrantype(SysConstant.TRANSACTION_TYPE2);
				result = transactionLogService.insertTransacitonLog(restTemplate, tdApplication, header);
				
				// 给转出账户更新对方账户流水号
				TransactionLogModel transactionLogEntity = new TransactionLogModel();
				transactionLogEntity.setTranseq(transeq);
				ResultUtil saveTransactionLog = SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_findOne(), JSON.toJSONString(transactionLogEntity));
				TransactionLogModel transferOutUpdate = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(saveTransactionLog.getData()), TransactionLogModel.class);
				
				transactionLogEntity.setId(transferOutUpdate.getId());
				transactionLogEntity.setTfrseqno(transeq);
				SendUtil.sendPostRequest(restTemplate, pathConfig.getTransaction_log_update(), JSON.toJSONString(transactionLogEntity));
				
				result = ResponseUtil.success(ReturnConstant.RETURN_CODE_1, ReturnConstant.TRANSACTION_ACCEPTED);
			}
		}

		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}

	@SuppressWarnings({ "rawtypes" })
	private SavingAccountMasterModel getSavingAccount(TermDepositDrawDownModel tddm, HeaderModel header,
			SavingAccountMasterModel savaccount) throws Exception {
		
		SavingAccountMasterModel savingAccountMasterModel = new SavingAccountMasterModel();
		savaccount.setAccountnumber(tddm.getDebitAccountNumber());
		//调用数据隔离工具类
		savaccount = (SavingAccountMasterModel) DataIsolationUtil.condition(header, savaccount);
		ResultUtil savAccResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getAccount_saving_findOne(), JSON.toJSONString(savaccount));
		savingAccountMasterModel = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(savAccResult.getData()), SavingAccountMasterModel.class);
		
		return savingAccountMasterModel;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositRenewal(HeaderModel header, TermDepositRenewalModel tdrm) throws Exception {
		ResultUtil result = null;
		String method = "termDepositRenewal";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		BigDecimal tdamount = null;
		BigDecimal maturityInterest = null;

		TermDepositForMasterModel tdrequest = new TermDepositForMasterModel();
		tdrequest.setAccountnumber(tdrm.getTdaccountnumber());
		tdrequest.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdrequest = (TermDepositForMasterModel) DataIsolationUtil.condition(header, tdrequest);
		ResultUtil rest = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findone(), JSON.toJSONString(tdrequest));
		TermDepositForMasterModel tdInfo = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(rest.getData()), TermDepositForMasterModel.class);
		
		if (tdInfo == null) { 
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}else if(!tdInfo.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
			// check td account status
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NOT_ACTIVE));
		}else{
			// check debitAccountNumber and TD number
			TermDepositDetailModel termDetail = new TermDepositDetailModel();
			termDetail.setAccountnumber(tdrm.getTdaccountnumber());
			termDetail.setDepositnumber(tdrm.getTdnumber());
			ResultUtil  termDepositDetaiResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findOne(), JSON.toJSONString(termDetail));
			TermDepositDetailPreModel retermDetail = JSONObject.parseObject(
					JsonProcess.changeEntityTOJSON(termDepositDetaiResult.getData()), TermDepositDetailPreModel.class);
			
			if (retermDetail == null) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
			}else if (retermDetail.getMaturitystatus().equals(SysConstant.MATURITY_STATUS_D)) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202010, localeMessage.getMessage(ExceptionConstant.TD_RECORD_HAS_BEEN_DRAWN_DOWN));
			}else if (Long.parseLong(String.valueOf(retermDetail.getMaturitydate())) > UTCUtil.getUTCTime()) {
				result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202015, localeMessage.getMessage(ExceptionConstant.TRANSACTION_NOT_MATURED_FOR_RENEWAL));
			}else{
				tdamount = retermDetail.getDepositamount();
				maturityInterest = tdamount.multiply(retermDetail.getTerminterestrate());
				BigDecimal depositAmount = maturityInterest.add(tdamount);
				String redate = GetSysDateUtil.getSystemDate(restTemplate, pathConfig);
				//calculate due date
				Long maturitydate = CalculateMaturityDateUtil.CalculateTermDepositDays(redate, tdrm.getTdRenewalPeriod());
				//calculate rate 
				// update tdmaster
				TermDepositDetailPreModel uptermdeposit = new TermDepositDetailPreModel();
				uptermdeposit.setId(retermDetail.getId());
				uptermdeposit.setMaturityinterest(maturityInterest);
				uptermdeposit.setMaturityamount(depositAmount);
				uptermdeposit.setMaturitystatus(SysConstant.MATURITY_STATUS_D);
				uptermdeposit.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
				SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_update(), JSON.toJSONString(uptermdeposit));
				
				//find deposit amount range
				DepositAmountRangeModel depositAmountRange = new DepositAmountRangeModel();
				depositAmountRange.setTdAmount(tdamount);
				ResultUtil reslt = SendUtil.sendPostRequest(restTemplate, pathConfig.getDeposit_range_findOne(), JSON.toJSONString(depositAmountRange));
				DepositAmountRangeModel redep = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(reslt.getData()), DepositAmountRangeModel.class);
				
				if (redep == null) {
					result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202009, localeMessage.getMessage(ExceptionConstant.THE_AMOUNT_NOT_SUPPORT_TERM_DEPOSIT));
				}else{
					// Map Rate
					TermDepositRateModel rate = new TermDepositRateModel();
					rate.setDepositrange(redep.getId());
					rate.setTdperiod(tdrm.getTdRenewalPeriod());
					ResultUtil rateResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getDeposit_rate_findOne(), JSON.toJSONString(rate));
					TermDepositRateModel rerate = JSONObject.parseObject(
							JsonProcess.changeEntityTOJSON(rateResult.getData()), TermDepositRateModel.class);
					
					if (rerate == null) {
						result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE202014, localeMessage.getMessage(ExceptionConstant.UNSUPPORTED_COTRACT_PERIOD));
					}else{
						// get next available TD number
						String tdNumber = SendUtil.sendPostForSysParameters(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), SysConstant.NEXT_AVAILABLE_TDNUMBER);
						// TermDepositMasterEntity add
						insertTermdepositDetail(tdrm, header, rerate, depositAmount, tdNumber, maturitydate);
						
						// save log to DB
						String tdCurrencyCode = tdInfo.getCurrencycode();
						saveLog(header, tdrm, depositAmount, tdCurrencyCode);
						result = ResponseUtil.success(ReturnConstant.RETURN_CODE_0, ReturnConstant.TRANSACTION_ACCEPTED);
					}
				}
			}
		}

		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}

	private void saveLog(HeaderModel header, TermDepositRenewalModel tdrm, BigDecimal depositAmount,
			String tdCurrencyCode) throws Exception {
		String logstr = "Transaction Accepted :" + tdrm.getTdaccountnumber();
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr, pathConfig);
		AvailableNumberUtil.availableTDNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_TDNUMBER, pathConfig);
		//resume transaction log
		InsertTransactionLogModel tdRenewal = new InsertTransactionLogModel();
		tdRenewal.setAccountnumber(tdrm.getTdaccountnumber());
		tdRenewal.setBranchcode(header.getBranchCode());
		tdRenewal.setCcy(tdCurrencyCode);
		tdRenewal.setChannel(SysConstant.CHANNEL_TYPE);
		tdRenewal.setChannelid(header.getUserID());
		tdRenewal.setClearingcode(header.getClearingCode());
		tdRenewal.setCountrycode(header.getCountryCode());
		tdRenewal.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE1);
		tdRenewal.setTranamt(depositAmount);
		tdRenewal.setTrandesc("Term Deposit Draw Down");
		tdRenewal.setSandboxid(header.getSandBoxId());
		tdRenewal.setDockerid(header.getDockerId());
		tdRenewal.setTrantype(SysConstant.TRANSACTION_TYPE3);
		transactionLogService.insertTransacitonLog(restTemplate, tdRenewal, header);
	}

	private void insertTermdepositDetail(TermDepositRenewalModel tdrm, HeaderModel header,
			TermDepositRateModel rerate, BigDecimal depositAmount, String tdNumber, Long maturitydate) throws Exception {
		TermDepositDetailPreModel tdreniew = new TermDepositDetailPreModel();
		tdreniew.setAccountnumber(tdrm.getTdaccountnumber());
		tdreniew.setDepositamount(depositAmount);
		tdreniew.setDepositnumber(tdNumber);
		tdreniew.setSandboxid(header.getSandBoxId());
		tdreniew.setDockerid(header.getDockerId());
		tdreniew.setId(UUIDUtil.generateUUID());
		tdreniew.setMaturitydate(new BigDecimal(maturitydate));
		tdreniew.setMaturitystatus(SysConstant.MATURITY_STATUS_A);
		tdreniew.setTerminterestrate(rerate.getTdinterestrate());
		tdreniew.setTermperiod(rerate.getTdperiod());
		tdreniew.setCreatedate(new BigDecimal(UTCUtil.getUTCTime()));
		
		//save termDeposit Detail
		SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_insert(), JSON.toJSONString(tdreniew));
	}

}
