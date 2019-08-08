package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
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
import com.csi.sbs.common.business.util.PostUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ExceptionConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.exception.NotFoundException;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDetailPreModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositEnquiryModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositForMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;
import com.simnectzbank.lbs.processlayer.termdeposit.util.LogUtil;


@Service("TermDepositEnquiryService")
public  class TermDepositEnquiryServiceImpl implements TermDepositEnquiryService{
	
	@Resource
	PathConfig pathConfig;
	
	@SuppressWarnings("unused")
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unused")
	private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositEnquiry(HeaderModel header,TermDepositEnquiryModel tdem,RestTemplate restTemplate) throws Exception {
		ResultUtil result = new ResultUtil();
		
		TermDepositForMasterModel tdrequest = new TermDepositForMasterModel();
		tdrequest.setAccountnumber(tdem.getAccountnumber());
		tdrequest.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdrequest = (TermDepositForMasterModel) DataIsolationUtil.condition(header, tdrequest);
		//TermDepositForMasterModel tdInfo = (TermDepositForMasterModel) termDepositMasterDao.findOne(tdrequest);
		ResponseEntity<ResultUtil> postForEntity = restTemplate.postForEntity(pathConfig.getTermdeposit_master_findone(),
				PostUtil.getRequestEntity(JSON.toJSONString(tdrequest)), ResultUtil.class);

		TermDepositForMasterModel tdInfo = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity.getBody().getData()), TermDepositForMasterModel.class);
		
		if (tdInfo == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),ExceptionConstant.ERROR_CODE404006);
		}	
		
		TermDepositDetailModel tde = new TermDepositDetailModel();
		tde.setAccountnumber(tdem.getAccountnumber());
		tde.setDepositnumber(tdem.getTdnumber());
		
		//TermDepositDetailModel reterm = (TermDepositDetailModel) termDepositDetailDao.findOne(tde);
		ResponseEntity<ResultUtil> postForEntity2 = restTemplate.postForEntity(pathConfig.getTermdeposit_detail_findOne(),
				PostUtil.getRequestEntity(JSON.toJSONString(tde)), ResultUtil.class);

		TermDepositDetailPreModel reterm = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity2.getBody().getData()), TermDepositDetailPreModel.class);
		
		if(reterm==null){
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404001),ExceptionConstant.ERROR_CODE404001);
		}
		//根据定存账号查询currencyCode
		TermDepositForMasterModel searchCurrencyCode = new TermDepositForMasterModel();
		searchCurrencyCode.setAccountnumber(reterm.getAccountnumber());
		//TermDepositForMasterModel res_searchCurrencyCode = (TermDepositForMasterModel) termDepositMasterDao.findOne(searchCurrencyCode);
		ResponseEntity<ResultUtil> postForEntity1 = restTemplate.postForEntity(pathConfig.getTermdeposit_master_findone(),
				PostUtil.getRequestEntity(JSON.toJSONString(searchCurrencyCode)), ResultUtil.class);

		TermDepositForMasterModel res_searchCurrencyCode = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity1.getBody().getData()), TermDepositForMasterModel.class);
		
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
		result.setCode("1");
		result.setMsg("Search Success");
		result.setData(tddetail);
		//写入日志
		String logstr = "Query success based on acount Number and tdnumber:accountNumber:"+tdem.getAccountnumber()+"tdNumber:"+tdem.getTdnumber();
		LogUtil.saveLog(
				restTemplate, 
				SysConstant.OPERATION_QUERY, 
				SysConstant.LOCAL_SERVICE_NAME, 
				SysConstant.OPERATION_SUCCESS, 
				logstr);
		return result;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil termDepositAllEnquiry(HeaderModel header,String customerNumber, RestTemplate restTemplate) throws Exception {
		List allTermDepositDetail = new ArrayList();
		ResultUtil result = new ResultUtil();
		TermDepositForMasterModel searchTDM = new TermDepositForMasterModel();
		searchTDM.setCustomernumber(customerNumber);
		//List<TermDepositForMasterModel> res_searchTDM = termDepositMasterDao.findMany(searchTDM);
		ResponseEntity<ResultUtil> postForEntity1 = restTemplate.postForEntity(pathConfig.getTermdeposit_master_findMany(),
				PostUtil.getRequestEntity(JSON.toJSONString(searchTDM)), ResultUtil.class);

		List<TermDepositForMasterModel> res_searchTDM = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity1.getBody().getData()), List.class);
		
		TermDepositDetailModel searchTDD = new TermDepositDetailModel();
		if(res_searchTDM!=null && res_searchTDM.size()>0){
			for(int i=0;i<res_searchTDM.size();i++){
				searchTDD.setAccountnumber(res_searchTDM.get(i).getAccountnumber());
				//List<TermDepositDetailModel> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
				ResponseEntity<ResultUtil> postForEntity3 = restTemplate.postForEntity(pathConfig.getTermdeposit_detail_findMany(),
						PostUtil.getRequestEntity(JSON.toJSONString(searchTDD)), ResultUtil.class);

				List<TermDepositDetailModel> res_searchTDD = JSONObject.parseObject(
						JsonProcess.changeEntityTOJSON(postForEntity3.getBody().getData()), List.class);
				
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
			result.setCode(String.valueOf(ExceptionConstant.SUCCESS_CODE200));
			result.setMsg("Search Success");
			result.setData(allTermDepositDetail);
			return result;
		}
		result.setCode(String.valueOf(ExceptionConstant.ERROR_CODE404015));
		result.setMsg(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404015));
	    return result;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@TxTransaction(isStart = true)
	@Transactional
	public ResultUtil getTermDepositByAccount(HeaderModel header, String accountNumber, RestTemplate restTemplate)
			throws Exception {
		ResultUtil result = new ResultUtil();
		//根据accountNumber查询定存主表
		TermDepositForMasterModel searchTDM = new TermDepositForMasterModel();
		searchTDM.setAccountnumber(accountNumber);
		//TermDepositForMasterModel res_searchTDM = (TermDepositForMasterModel) termDepositMasterDao.findOne(searchTDM);
		ResponseEntity<ResultUtil> postForEntity3 = restTemplate.postForEntity(pathConfig.getTermdeposit_detail_findMany(),
				PostUtil.getRequestEntity(JSON.toJSONString(searchTDM)), ResultUtil.class);

		TermDepositForMasterModel res_searchTDM = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity3.getBody().getData()), TermDepositForMasterModel.class);
		
		//根据accountNumber查询定存明细表
		TermDepositDetailModel searchTDD = new TermDepositDetailModel();
		searchTDD.setAccountnumber(accountNumber);
		//List<TermDepositDetailModel> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
		ResponseEntity<ResultUtil> postForEntity4 = restTemplate.postForEntity(pathConfig.getTermdeposit_detail_findMany(),
				PostUtil.getRequestEntity(JSON.toJSONString(searchTDD)), ResultUtil.class);

		List<TermDepositDetailModel> res_searchTDD = JSONObject.parseObject(
				JsonProcess.changeEntityTOJSON(postForEntity4.getBody().getData()), List.class);
		
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
			result.setCode(String.valueOf(ExceptionConstant.SUCCESS_CODE200));
			result.setMsg("Search Success");
			result.setData(allTermDepositDetail);
			return result;
		}
		result.setCode(String.valueOf(ExceptionConstant.ERROR_CODE404015));
		result.setMsg(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404015));
	    return result;
	}

	
}
