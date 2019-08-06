package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.DataIsolationUtil;
import com.csi.sbs.common.business.util.UUIDUtil;
import com.csi.sbs.common.business.util.XmlToJsonUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRenewalModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositMasterService;


@Service("TermDepositMasterService")
public class TermDepositMasterServiceImpl implements TermDepositMasterService {

	@SuppressWarnings("unused")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("unused")
	private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource
	private RestTemplate restTemplate;

	@Resource
	private AccountMasterService accountMasterService;

	@Resource
	private TransactionLogService transactionLogService;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public Map<String, Object> termDepositApplication(HeaderModel header, TermDepositMasterModel tdm) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// check TdAccount Number
		TermDepositMasterEntity tdaccount = new TermDepositMasterEntity();
		tdaccount.setAccountnumber(tdm.getTdAccountNumber());
		tdaccount.setCurrencycode(tdm.getTdCcy());
		//调用数据隔离工具类
		tdaccount = (TermDepositMasterEntity) DataIsolationUtil.condition(header, tdaccount);
		TermDepositMasterEntity retdaccount = (TermDepositMasterEntity) termDepositMasterDao.findOne(tdaccount);
		if (retdaccount == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),
					ExceptionConstant.ERROR_CODE404006);
		}
		// check td account status
		if (!retdaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202001),ExceptionConstant.ERROR_CODE202001);
		}
		// check Debit Account Number(saving account)
		String accountType = tdm.getDebitAccountNumber().substring(tdm.getDebitAccountNumber().length() - 3,
				tdm.getDebitAccountNumber().length());
		SavingAccountMasterEntity savaccount = new SavingAccountMasterEntity();
		CurrentAccountMasterEntity currentaccount = new CurrentAccountMasterEntity();
		SavingAccountMasterEntity resavaccount = null;
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			savaccount.setAccountnumber(tdm.getDebitAccountNumber());
			savaccount.setCustomernumber(header.getCustomerNumber());
			//调用数据隔离工具类
			savaccount = (SavingAccountMasterEntity) DataIsolationUtil.condition(header, savaccount);
			resavaccount = (SavingAccountMasterEntity) savingAccountMasterDao.findOne(savaccount);
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			currentaccount.setAccountnumber(tdm.getDebitAccountNumber());
			currentaccount.setCustomernumber(header.getCustomerNumber());
			//调用数据隔离工具类
			currentaccount = (CurrentAccountMasterEntity) DataIsolationUtil.condition(header, currentaccount);
			CurrentAccountMasterEntity recurrent = (CurrentAccountMasterEntity) currentAccountMasterDao
					.findOne(currentaccount);
			resavaccount = new SavingAccountMasterEntity();
			resavaccount.setAccountnumber(recurrent.getAccountnumber());
			resavaccount.setAvailablebalance(recurrent.getAvailablebalance());
			resavaccount.setLedgebalance(recurrent.getLedgebalance());
			resavaccount.setCustomernumber(header.getCustomerNumber());
		}

		if (resavaccount == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404007),
					ExceptionConstant.ERROR_CODE404007);
		}
		// check account status
		if (!resavaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202001),ExceptionConstant.ERROR_CODE202001);
		}
		// check balance
		if (resavaccount.getAvailablebalance().compareTo(tdm.getTdAmount()) == -1) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202002),
					ExceptionConstant.ERROR_CODE202002);
		}
		// get next available TD number
		String response = restTemplate.getForEntity(PathConstant.NEXT_AVAILABLE + "NextAvailableTDNumber", String.class)
				.getBody();
		String tdNumber = JsonProcess.changeToJSONObject(response).getString("nextAvailableNumber");
		// Map Amount Range
		DepositAmountRangeEntity depositAmountRange = new DepositAmountRangeEntity();
		depositAmountRange.setTdAmount(tdm.getTdAmount());
		// depositAmountRange.setCountrycode(header.getCountryCode());
		// depositAmountRange.setClearingcode(header.getClearingCode());
		// depositAmountRange.setBranchcode(header.getBranchCode());
		depositAmountRange.setCcytype(tdm.getTdCcy());

		// Alina 获取amountrangemax为null的amount range
		DepositAmountRangeEntity maxInfo = new DepositAmountRangeEntity();
		// maxInfo.setCountrycode(header.getCountryCode());
		// maxInfo.setClearingcode(header.getClearingCode());
		// maxInfo.setBranchcode(header.getBranchCode());
		maxInfo.setCcytype(tdm.getTdCcy());
		DepositAmountRangeEntity reMax = depositAmountRangeDao.findMax(maxInfo);
		// Map Rate
		TermDepositRateEntity rate = new TermDepositRateEntity();
		if (reMax != null && tdm.getTdAmount().compareTo(reMax.getAmountrangemin()) >= 0) {
			rate.setDepositrange(reMax.getId());
		} else {
			DepositAmountRangeEntity redep = depositAmountRangeDao.findOne(depositAmountRange);
			if (redep == null) {
				throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202009),
						ExceptionConstant.ERROR_CODE202009);
			}
			rate.setDepositrange(redep.getId());
		}
		rate.setTdperiod(tdm.getTdContractPeriod());
		TermDepositRateEntity rerate = (TermDepositRateEntity) termDepositRateDao.findOne(rate);
		if (rerate == null) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202014),
					ExceptionConstant.ERROR_CODE202014);
		}
		// Calculate maturity date
		// 调用服务接口地址
		String param1 = "{\"apiname\":\"getSystemParameter\"}";
		ResponseEntity<String> result1 = restTemplate.postForEntity(PathConstant.SERVICE_INTERNAL_URL,
				PostUtil.getRequestEntity(param1), String.class);
		if (result1.getStatusCodeValue() != 200) {
			throw new CallOtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500003),
					ExceptionConstant.ERROR_CODE500003);
		}
		String path = JsonProcess.returnValue(JsonProcess.changeToJSONObject(result1.getBody()), "internaURL");
		// 调用系统参数服务接口
		String param2 = "{\"item\":\"SystemDate\"}";
		ResponseEntity<String> result2 = restTemplate.postForEntity(path, PostUtil.getRequestEntity(param2),
				String.class);
		if (result2.getStatusCodeValue() != 200) {
			throw new CallOtherException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE500003),
					ExceptionConstant.ERROR_CODE500003);
		}
		JSONArray restr = JsonProcess.changeToJSONArray(result2.getBody());
		String redate = JsonProcess.returnValue(JsonProcess.changeToJSONObject(restr.get(0).toString()), "value");
		Long maturitydate = null;
		maturitydate = CalculateMaturityDateUtil.CalculateTermDepositDays(redate, tdm.getTdContractPeriod());
        //判断到期日是否是法定节假日(需要将UTC时间转为中国东八区时间)
		String temp = UTCUtil.convertToTwo(String.valueOf(maturitydate)).replace("-", "");
		ResponseEntity res_isHolidsy = restTemplate.getForEntity(PathConstant.IS_HOLIDAY+"/"+temp, String.class);
		if(res_isHolidsy.getStatusCodeValue()==200 && !StringUtils.isEmpty(res_isHolidsy.getBody().toString())){
			JSONObject str = XmlToJsonUtil.xmlToJson(res_isHolidsy.getBody().toString());
		    String str1 = JsonProcess.returnValue(str, "Boolean");
		    if(str1.equals("true")){
		    	//到期日如果是法定节假日,将到期日往后顺延到下一个工作日
		        boolean temp_flag = true;
		        int temp_day = 0;
		    	do{
		    		temp_day++;
		    		Long l1 = CalculateMaturityDateUtil.plusDay(temp_day, String.valueOf(maturitydate));
		    		String temp_date = UTCUtil.convertToTwo(String.valueOf(l1));
		    		String temp1 = temp_date.replace("-", "");
		    		ResponseEntity res_isHolidsy2 = restTemplate.getForEntity(PathConstant.IS_HOLIDAY+"/"+temp1, String.class);
		    		if(res_isHolidsy2.getStatusCodeValue()==200 && !StringUtils.isEmpty(res_isHolidsy2.getBody().toString())){
		    			JSONObject str3 = XmlToJsonUtil.xmlToJson(res_isHolidsy2.getBody().toString());
		    		    String str4 = JsonProcess.returnValue(str3, "Boolean");
		    		    if(str4.equals("true")){
		    		    	temp_flag = true;
		    		    }else{
		    		    	temp = temp_date;
		    		    	maturitydate = CalculateMaturityDateUtil.plusDay(temp_day, String.valueOf(maturitydate));
		    		    	temp_flag = false;
		    		    }
		    		}
		    	}while(temp_flag);
		    }
		}
		// TermDepositDetail add
		TermDepositDetailEntity termDepositDetail = new TermDepositDetailEntity();
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
        termDepositDetail.setSystemdate(new BigDecimal(GetSysDateUtil.getSystemDate(restTemplate)));
		
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			savaccount.setAccountnumber(resavaccount.getAccountnumber());
			savaccount.setAvailablebalance(resavaccount.getAvailablebalance().subtract(tdm.getTdAmount()));
			savaccount.setLedgebalance(resavaccount.getLedgebalance().subtract(tdm.getTdAmount()));
			int i = savingAccountMasterDao.withdrawal(savaccount);
			if (i <= 0) {
				map.put("msg", "Update Debit Account Balance Fail");
				map.put("code", "0");
				return map;
			}
		}

		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			currentaccount.setAccountnumber(resavaccount.getAccountnumber());
			currentaccount.setAvailablebalance(resavaccount.getAvailablebalance().subtract(tdm.getTdAmount()));
			currentaccount.setLedgebalance(resavaccount.getLedgebalance().subtract(tdm.getTdAmount()));
			int i = currentAccountMasterDao.withdrawal(currentaccount);
			if (i <= 0) {
				map.put("msg", "Update Debit Account Balance Fail");
				map.put("code", "0");
				return map;
			}
		}

		termDepositDetailDao.insert(termDepositDetail);
		AvailableNumberUtil.availableTDNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_TDNUMBER);

		// write Log
		String logstr = "Transaction Accepted TDNumber:" + tdNumber + " Account Number:" + tdm.getTdAccountNumber();
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_CREATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);

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
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			depositAccount.setActualbalamt(savaccount.getLedgebalance());
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			depositAccount.setActualbalamt(currentaccount.getLedgebalance());
		}
		Map<String, String> depositRes = transactionLogService.insertTransacitonLog(restTemplate, depositAccount);

		// td account transaction log 信息
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
		tdApplication.setTfrseqno(depositRes.get("transeq"));
		tdApplication.setTranamt(tdm.getTdAmount());
		tdApplication.setSandboxid(header.getSandBoxId());
		tdApplication.setDockerid(header.getDockerId());
		tdApplication.setTrandesc("Term Deposit Application");
		tdApplication.setTrantype(SysConstant.TRANSACITON_TYPE1);
		Map<String, String> tdRes = transactionLogService.insertTransacitonLog(restTemplate, tdApplication);

		// 给转出账户更新对方账户流水号
		TransactionLogEntity transactionLogEntity = new TransactionLogEntity();
		transactionLogEntity.setTranseq(depositRes.get("transeq"));
		TransactionLogEntity transferOutUpdate = (TransactionLogEntity) transactionLogDao.findOne(transactionLogEntity);
		transactionLogEntity.setId(transferOutUpdate.getId());
		transactionLogEntity.setTfrseqno(tdRes.get("transeq"));
		transactionLogDao.update(transactionLogEntity);

		map.put("msg", logstr);
		map.put("code", "1");
		map.put("data", tdNumber);
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public Map<String, Object> termDepositDrawDown(HeaderModel header, TermDepositDrawDownModel tddm,RestTemplate restTemplate) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// check TdAccount Number
		TermDepositMasterEntity tdaccount = new TermDepositMasterEntity();
		tdaccount.setAccountnumber(tddm.getTdAccountNumber());
		tdaccount.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdaccount = (TermDepositMasterEntity) DataIsolationUtil.condition(header, tdaccount);
		TermDepositMasterEntity retdaccount = (TermDepositMasterEntity) termDepositMasterDao.findOne(tdaccount);
		if (retdaccount == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),
					ExceptionConstant.ERROR_CODE404006);
		}

		// check Transaction Account and TD number
		TermDepositDetailEntity tddetail = new TermDepositDetailEntity();
		tddetail.setAccountnumber(tddm.getTdAccountNumber());
		tddetail.setDepositnumber(tddm.getTdNumber());

		TermDepositDetailEntity retddetail = (TermDepositDetailEntity) termDepositDetailDao.findOne(tddetail);
		if (retddetail == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),
					ExceptionConstant.ERROR_CODE404006);
		}

		// check Debit Account Number(saving account)
		String accountType = tddm.getDebitAccountNumber().substring(tddm.getDebitAccountNumber().length() - 3);
		SavingAccountMasterEntity savaccount = new SavingAccountMasterEntity();
		CurrentAccountMasterEntity currentaccount = new CurrentAccountMasterEntity();
		SavingAccountMasterEntity resavaccount = null;
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			savaccount.setAccountnumber(tddm.getDebitAccountNumber());
			//调用数据隔离工具类
			savaccount = (SavingAccountMasterEntity) DataIsolationUtil.condition(header, savaccount);
			resavaccount = (SavingAccountMasterEntity) savingAccountMasterDao.findOne(savaccount);
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			currentaccount.setAccountnumber(tddm.getDebitAccountNumber());
			//调用数据隔离工具类
			currentaccount = (CurrentAccountMasterEntity) DataIsolationUtil.condition(header, currentaccount);
			CurrentAccountMasterEntity recurrent = (CurrentAccountMasterEntity) currentAccountMasterDao
					.findOne(currentaccount);
			resavaccount = new SavingAccountMasterEntity();
			resavaccount.setAccountnumber(recurrent.getAccountnumber());
			resavaccount.setAvailablebalance(recurrent.getAvailablebalance());
			resavaccount.setLedgebalance(recurrent.getLedgebalance());
		}
		if (resavaccount == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404007),
					ExceptionConstant.ERROR_CODE404007);
		}
		// check debitaccount status
		if (!resavaccount.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202001),ExceptionConstant.ERROR_CODE202001);
		}
		if (retddetail.getMaturitystatus().equals(SysConstant.MATURITY_STATUS_D)) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202010),
					ExceptionConstant.ERROR_CODE202010);
		}
		// check Maturity date
		if (Long.parseLong(String.valueOf(retddetail.getMaturitydate())) > UTCUtil.getUTCTime()) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202011),
					ExceptionConstant.ERROR_CODE202011);
		}
		// Calculation of interest
		BigDecimal interest = InterestCalculationUtil.calDayInterest(restTemplate,String.valueOf(retddetail.getMaturitydate()), retddetail.getTermperiod(), retddetail.getDepositamount(), retddetail.getTerminterestrate());
		// Maturity Amount
		BigDecimal maturityAmount = retddetail.getDepositamount().add(interest);
		// update tddetail
		TermDepositDetailEntity uptddetail = new TermDepositDetailEntity();
		uptddetail.setMaturityamount(maturityAmount);
		uptddetail.setMaturityinterest(interest);
		uptddetail.setMaturitystatus(SysConstant.MATURITY_STATUS_D);
		uptddetail.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
		uptddetail.setId(retddetail.getId());
		// update accountMaster
		BigDecimal r11 = resavaccount.getLedgebalance();
		BigDecimal r12 = resavaccount.getAvailablebalance();

		// Alina according accounttype to update the account balance
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			savaccount.setLedgebalance(r11.add(maturityAmount));
			savaccount.setAvailablebalance(r12.add(maturityAmount));
			savaccount.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
			savingAccountMasterDao.deposit(savaccount);
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			currentaccount.setLedgebalance(r11.add(maturityAmount));
			currentaccount.setAvailablebalance(r12.add(maturityAmount));
			currentaccount.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
			currentAccountMasterDao.update(currentaccount);
		}
		termDepositDetailDao.update(uptddetail);

		// write log
		String logstr = "Transaction Accepted";
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);

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
		if (accountType.equals(SysConstant.ACCOUNT_TYPE1)) {
			depositAccount.setActualbalamt(savaccount.getLedgebalance());
		}
		if (accountType.equals(SysConstant.ACCOUNT_TYPE2)) {
			depositAccount.setActualbalamt(currentaccount.getLedgebalance());
		}

		Map<String, String> depositRes = transactionLogService.insertTransacitonLog(restTemplate, depositAccount);

		// td account transaction log 信息
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
		tdApplication.setTfrseqno(depositRes.get("transeq"));
		tdApplication.setTranamt(maturityAmount);
		tdApplication.setSandboxid(header.getSandBoxId());
		tdApplication.setDockerid(header.getDockerId());
		tdApplication.setTrandesc("Term Deposit Draw Down");
		tdApplication.setTrantype(SysConstant.TRANSACTION_TYPE2);
		Map<String, String> tdRes = transactionLogService.insertTransacitonLog(restTemplate, tdApplication);

		// 给转出账户更新对方账户流水号
		TransactionLogEntity transactionLogEntity = new TransactionLogEntity();
		transactionLogEntity.setTranseq(depositRes.get("transeq"));
		TransactionLogEntity transferOutUpdate = (TransactionLogEntity) transactionLogDao.findOne(transactionLogEntity);
		transactionLogEntity.setId(transferOutUpdate.getId());
		transactionLogEntity.setTfrseqno(tdRes.get("transeq"));
		transactionLogDao.update(transactionLogEntity);

		map.put("msg", "transaction accepted");
		map.put("code", "1");
		return map;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public Map<String, Object> termDepositRenewal(HeaderModel header, TermDepositRenewalModel tdrm) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal tdamount = null;
		BigDecimal maturityInterest = null;

		TermDepositMasterEntity tdrequest = new TermDepositMasterEntity();
		tdrequest.setAccountnumber(tdrm.getTdaccountnumber());
		tdrequest.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdrequest = (TermDepositMasterEntity) DataIsolationUtil.condition(header, tdrequest);
		TermDepositMasterEntity tdInfo = (TermDepositMasterEntity) termDepositMasterDao.findOne(tdrequest);
		if (tdInfo == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),
					ExceptionConstant.ERROR_CODE404006);
		}
		// check td account status
		if (!tdInfo.getAccountstatus().equals(SysConstant.ACCOUNT_STATE2)){
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202001),ExceptionConstant.ERROR_CODE202001);
		}
		// check debitAccountNumber and TD number
		TermDepositDetailEntity termDetail = new TermDepositDetailEntity();
		termDetail.setAccountnumber(tdrm.getTdaccountnumber());
		termDetail.setDepositnumber(tdrm.getTdnumber());
		TermDepositDetailEntity retermDetail = (TermDepositDetailEntity) termDepositDetailDao.findOne(termDetail);
		if (retermDetail == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),
					ExceptionConstant.ERROR_CODE404006);
		}
		if (retermDetail.getMaturitystatus().equals(SysConstant.MATURITY_STATUS_D)) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202010),
					ExceptionConstant.ERROR_CODE202010);
		}
		if (Long.parseLong(String.valueOf(retermDetail.getMaturitydate())) > UTCUtil.getUTCTime()) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202015),
					ExceptionConstant.ERROR_CODE202015);
		}
		tdamount = retermDetail.getDepositamount();
		String redate = GetSysDateUtil.getSystemDate(restTemplate);
		//计算到期日
		Long maturitydate = CalculateMaturityDateUtil.CalculateTermDepositDays(redate, tdrm.getTdRenewalPeriod());
		//计算利息
		maturityInterest = tdamount.multiply(retermDetail.getTerminterestrate());
		// update tdmaster
		TermDepositDetailEntity uptermdeposit = new TermDepositDetailEntity();
		uptermdeposit.setId(retermDetail.getId());
		uptermdeposit.setMaturityinterest(maturityInterest);
		uptermdeposit.setMaturityamount(maturityInterest.add(tdamount));
		uptermdeposit.setMaturitystatus(SysConstant.MATURITY_STATUS_D);
		uptermdeposit.setLastupdateddate(new BigDecimal(UTCUtil.getUTCTime()));
		termDepositDetailDao.updateInterest(uptermdeposit);

		// Map Amount Range
		DepositAmountRangeEntity depositAmountRange = new DepositAmountRangeEntity();
		depositAmountRange.setTdAmount(tdamount);
		DepositAmountRangeEntity redep = depositAmountRangeDao.findOne(depositAmountRange);
		if (redep == null) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202009),
					ExceptionConstant.ERROR_CODE202009);
		}
		// Map Rate
		TermDepositRateEntity rate = new TermDepositRateEntity();
		rate.setDepositrange(redep.getId());
		rate.setTdperiod(tdrm.getTdRenewalPeriod());
		TermDepositRateEntity rerate = (TermDepositRateEntity) termDepositRateDao.findOne(rate);
		if (rerate == null) {
			throw new AcceptException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE202014),
					ExceptionConstant.ERROR_CODE202014);
		}
		// get next available TD number
		String response = restTemplate.getForEntity(PathConstant.NEXT_AVAILABLE + "NextAvailableTDNumber", String.class)
				.getBody();
		String tdNumber = JsonProcess.changeToJSONObject(response).getString("nextAvailableNumber");

		// TermDepositMasterEntity add
		TermDepositDetailEntity tdreniew = new TermDepositDetailEntity();
		tdreniew.setAccountnumber(tdrm.getTdaccountnumber());
		tdreniew.setDepositamount(maturityInterest.add(tdamount));
		tdreniew.setDepositnumber(tdNumber);
		tdreniew.setSandboxid(header.getSandBoxId());
		tdreniew.setDockerid(header.getDockerId());
		tdreniew.setId(UUIDUtil.generateUUID());
		tdreniew.setMaturitydate(new BigDecimal(maturitydate));
		tdreniew.setMaturitystatus(SysConstant.MATURITY_STATUS_A);
		tdreniew.setTerminterestrate(rerate.getTdinterestrate());
		tdreniew.setTermperiod(rerate.getTdperiod());
		tdreniew.setCreatedate(new BigDecimal(UTCUtil.getUTCTime()));

		termDepositDetailDao.insert(tdreniew);
		map.put("msg", "transaction accepted");
		map.put("code", 0);
		// 写入日志
		String logstr = "Transaction Accepted :" + tdrm.getTdaccountnumber();
		LogUtil.saveLog(restTemplate, SysConstant.OPERATION_UPDATE, SysConstant.LOCAL_SERVICE_NAME,
				SysConstant.OPERATION_SUCCESS, logstr);
		AvailableNumberUtil.availableTDNumberIncrease(restTemplate, SysConstant.NEXT_AVAILABLE_TDNUMBER);
		// 续存 transaction log 信息
		InsertTransactionLogModel tdRenewal = new InsertTransactionLogModel();
		tdRenewal.setAccountnumber(tdrm.getTdaccountnumber());
		tdRenewal.setBranchcode(header.getBranchCode());
		tdRenewal.setCcy(tdInfo.getCurrencycode());
		tdRenewal.setChannel(SysConstant.CHANNEL_TYPE);
		tdRenewal.setChannelid(header.getUserID());
		tdRenewal.setClearingcode(header.getClearingCode());
		tdRenewal.setCountrycode(header.getCountryCode());
		tdRenewal.setCrdrmaintind(SysConstant.CR_DR_MAINT_IND_TYPE1);
		tdRenewal.setTranamt(maturityInterest.add(tdamount));
		tdRenewal.setTrandesc("Term Deposit Draw Down");
		tdRenewal.setSandboxid(header.getSandBoxId());
		tdRenewal.setDockerid(header.getDockerId());
		tdRenewal.setTrantype(SysConstant.TRANSACTION_TYPE3);
		transactionLogService.insertTransacitonLog(restTemplate, tdRenewal);

		return map;
	}

}
