package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailPreModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositEnquiryModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositForMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.LogUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.util.SendUtil;


@Service("TermDepositEnquiryService")
public  class TermDepositEnquiryServiceImpl implements TermDepositEnquiryService{
	
	@Resource
	PathConfig pathConfig;
	
	@Resource
	LocaleMessage localeMessage;

	private String classname = TermDepositEnquiryServiceImpl.class.getName();
	@SuppressWarnings({ "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositEnquiry(HeaderModel header,TermDepositEnquiryModel tdem,RestTemplate restTemplate) throws Exception {
		ResultUtil result = null;
		String method = "termDepositEnquiry";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		TermDepositForMasterModel tdrequest = new TermDepositForMasterModel();
		tdrequest.setAccountnumber(tdem.getAccountnumber());
		tdrequest.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdrequest = (TermDepositForMasterModel) DataIsolationUtil.condition(header, tdrequest);
		ResultUtil termDepositResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findone(), JSON.toJSONString(tdrequest));
				
		TermDepositForMasterModel tdInfo = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(termDepositResult.getData()), TermDepositForMasterModel.class);
		
		if (tdInfo == null) {
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404006, localeMessage.getMessage(ExceptionConstant.TD_ACCOUNT_NUMBER_NOT_FOUND));
		}else{
			TermDepositDetailModel tde = new TermDepositDetailModel();
			tde.setAccountnumber(tdem.getAccountnumber());
			tde.setDepositnumber(tdem.getTdnumber());
			
			ResultUtil tdDetailResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findOne(), JSON.toJSONString(tde));
			
			TermDepositDetailPreModel reterm = JSONObject.parseObject(
					JsonProcess.changeEntityTOJSON(tdDetailResult.getData()), TermDepositDetailPreModel.class);
			
			if(reterm==null){
				return ResponseUtil.fail(ExceptionConstant.ERROR_CODE404001, localeMessage.getMessage(ExceptionConstant.ACCOUNT_NUMBER_NOT_FOUND));
			}else{
				//根据定存账号查询currencyCode
				TermDepositForMasterModel searchCurrencyCode = new TermDepositForMasterModel();
				searchCurrencyCode.setAccountnumber(reterm.getAccountnumber());
				ResultUtil tdResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findone(), JSON.toJSONString(searchCurrencyCode));
				TermDepositForMasterModel res_searchCurrencyCode = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(tdResult.getData()), TermDepositForMasterModel.class);
				
				//model change
				TermDepositDetailModel tddetail = new TermDepositDetailModel();
				tddetail.setAccountnumber(reterm.getAccountnumber());
				tddetail.setDepositamount(reterm.getDepositamount());
				tddetail.setDepositnumber(reterm.getDepositnumber());
				tddetail.setMaturityamount(reterm.getMaturityamount());
				tddetail.setMaturitydate(String.valueOf(reterm.getMaturitydate()));
				tddetail.setMaturityinterest(reterm.getMaturityinterest());
				tddetail.setMaturitystatus(reterm.getMaturitystatus());
				tddetail.setTerminterestrate(reterm.getTerminterestrate());
				tddetail.setTermperiod(reterm.getTermperiod());
				if(reterm.getCreatedate()!=null && !StringUtils.isEmpty(reterm.getCreatedate())){
					tddetail.setCreatedate(String.valueOf(reterm.getCreatedate()));
				}
				tddetail.setCurrencycode(res_searchCurrencyCode!=null?res_searchCurrencyCode.getCurrencycode():"");
				tddetail.setSystemdate(String.valueOf(reterm.getSystemdate()));
				//save log to DB
				String logstr = "Query success based on acount Number and tdnumber:accountNumber:"+tdem.getAccountnumber()+"tdNumber:"+tdem.getTdnumber();
				LogUtil.saveLog(
						restTemplate, 
						SysConstant.OPERATION_QUERY, 
						SysConstant.LOCAL_SERVICE_NAME, 
						SysConstant.OPERATION_SUCCESS, 
						logstr,
						pathConfig);
				result = ResponseUtil.success(ReturnConstant.RETURN_CODE_FAIL, tddetail, localeMessage.getMessage(ExceptionConstant.SEARCH_SUCCESS));
			}
		}	
		
		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositAllEnquiry(HeaderModel header,String customerNumber, RestTemplate restTemplate) throws Exception {
		List allTermDepositDetail = new ArrayList();
		ResultUtil result = null;
		String method = "termDepositAllEnquiry";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		TermDepositForMasterModel searchTDM = new TermDepositForMasterModel();
		searchTDM.setCustomernumber(customerNumber);
		ResultUtil termDepositResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_master_findMany(), JSON.toJSONString(searchTDM));
		List<TermDepositForMasterModel> res_searchTDM = JSONObject.parseArray(
				JsonProcess.changeEntityTOJSON(termDepositResult.getData()), TermDepositForMasterModel.class);
		
		TermDepositDetailModel searchTDD = new TermDepositDetailModel();
		if(res_searchTDM!=null && res_searchTDM.size()>0){
			for(int i=0;i<res_searchTDM.size();i++){
				searchTDD.setAccountnumber(res_searchTDM.get(i).getAccountnumber());
				//List<TermDepositDetailModel> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
				ResultUtil termDepositDetailResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findMany(), JSON.toJSONString(searchTDD));

				List<TermDepositDetailModel> res_searchTDD = JSONObject.parseArray(
						JsonProcess.changeEntityTOJSON(termDepositDetailResult.getData()), TermDepositDetailModel.class);
				
				List<TermDepositDetailModel> temp = new ArrayList();
				for(int j=0;j<res_searchTDD.size();j++){
					//model change
					TermDepositDetailModel tddetail = new TermDepositDetailModel();
					tddetail.setAccountnumber(res_searchTDD.get(j).getAccountnumber());
					tddetail.setDepositamount(res_searchTDD.get(j).getDepositamount());
					tddetail.setDepositnumber(res_searchTDD.get(j).getDepositnumber());
					tddetail.setMaturityamount(res_searchTDD.get(j).getMaturityamount());
					tddetail.setMaturitydate(String.valueOf(res_searchTDD.get(j).getMaturitydate()));
					tddetail.setMaturityinterest(res_searchTDD.get(j).getMaturityinterest());
					tddetail.setMaturitystatus(res_searchTDD.get(j).getMaturitystatus());
					tddetail.setTerminterestrate(res_searchTDD.get(j).getTerminterestrate());
					tddetail.setTermperiod(res_searchTDD.get(j).getTermperiod());
					if(res_searchTDD.get(j).getCreatedate()!=null && !StringUtils.isEmpty(res_searchTDD.get(j).getCreatedate())){
						tddetail.setCreatedate(String.valueOf(res_searchTDD.get(j).getCreatedate()));
					}
					tddetail.setCurrencycode(res_searchTDM.get(i).getCurrencycode());
					tddetail.setSystemdate(String.valueOf(res_searchTDD.get(j).getSystemdate()));
					temp.add(tddetail);
				}
				if(temp.size()>0){
					allTermDepositDetail.add(temp);
				}
			}
		}
		if(allTermDepositDetail!=null && allTermDepositDetail.size()>0){
			result = ResponseUtil.success(ExceptionConstant.SUCCESS_CODE200, allTermDepositDetail, localeMessage.getMessage(ExceptionConstant.SEARCH_SUCCESS));
		}else{
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404015, localeMessage.getMessage(ExceptionConstant.TD_NUMBER_NOT_EXIST));
		}
		
		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil getTermDepositByAccount(HeaderModel header, String accountNumber, RestTemplate restTemplate)
			throws Exception {
		ResultUtil result = null;
		String method = "getTermDepositByAccount";
		long threadId = Thread.currentThread().getId();
		SendLogUtil.sendDebug(new Date().getTime() + "|" + threadId + "|" + classname + "|" + method + "|" + "customernumber:" +header.getCustomerNumber() + "| method start");
		//根据accountNumber查询定存主表
		TermDepositForMasterModel searchTDM = new TermDepositForMasterModel();
		searchTDM.setAccountnumber(accountNumber);
		//TermDepositForMasterModel res_searchTDM = (TermDepositForMasterModel) termDepositMasterDao.findOne(searchTDM);
		ResultUtil termDEpositDetaiResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findMany(), JSON.toJSONString(searchTDM));
		TermDepositForMasterModel res_searchTDM = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(termDEpositDetaiResult.getData()), TermDepositForMasterModel.class);
		
		//根据accountNumber查询定存明细表
		TermDepositDetailModel searchTDD = new TermDepositDetailModel();
		searchTDD.setAccountnumber(accountNumber);
		//List<TermDepositDetailModel> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
		ResultUtil termDEpositMasterResult = SendUtil.sendPostRequest(restTemplate, pathConfig.getTermdeposit_detail_findMany(), JSON.toJSONString(searchTDD));

		List<TermDepositDetailModel> res_searchTDD = JSONObject.parseArray(
				JsonProcess.changeEntityTOJSON(termDEpositMasterResult.getData()), TermDepositDetailModel.class);
		
		//返回前台集合
		List<TermDepositDetailModel> allTermDepositDetail = new ArrayList();
		//返回前台model 转换
		for(int j=0;j<res_searchTDD.size();j++){
			//model change
			TermDepositDetailModel tddetail = new TermDepositDetailModel();
			tddetail.setAccountnumber(res_searchTDD.get(j).getAccountnumber());
			tddetail.setDepositamount(res_searchTDD.get(j).getDepositamount());
			tddetail.setDepositnumber(res_searchTDD.get(j).getDepositnumber());
			tddetail.setMaturityamount(res_searchTDD.get(j).getMaturityamount());
			tddetail.setMaturitydate(String.valueOf(res_searchTDD.get(j).getMaturitydate()));
			tddetail.setMaturityinterest(res_searchTDD.get(j).getMaturityinterest());
			tddetail.setMaturitystatus(res_searchTDD.get(j).getMaturitystatus());
			tddetail.setTerminterestrate(res_searchTDD.get(j).getTerminterestrate());
			tddetail.setTermperiod(res_searchTDD.get(j).getTermperiod());
			if(res_searchTDD.get(j).getCreatedate()!=null && !StringUtils.isEmpty(res_searchTDD.get(j).getCreatedate())){
				tddetail.setCreatedate(String.valueOf(res_searchTDD.get(j).getCreatedate()));
			}
			tddetail.setCurrencycode(res_searchTDM!=null?res_searchTDM.getCurrencycode():"");
			tddetail.setSystemdate(String.valueOf(res_searchTDD.get(j).getSystemdate()));
			allTermDepositDetail.add(tddetail);
		}
		if(allTermDepositDetail!=null && allTermDepositDetail.size()>0){
			result = ResponseUtil.success(200, allTermDepositDetail, localeMessage.getMessage(ExceptionConstant.SEARCH_SUCCESS));
		}else{
			result = ResponseUtil.fail(ExceptionConstant.ERROR_CODE404015, localeMessage.getMessage(ExceptionConstant.TD_NUMBER_NOT_EXIST));
		}
		
		String resultString = (result != null) ? result.toString() : null;
	    SendLogUtil.sendDebug(new Date().getTime() +"|" + threadId + "|" + classname + "|" + method  +  "| method end result: " + resultString);
		
	    return result;
	}

	
}
