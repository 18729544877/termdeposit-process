package com.simnectzbank.lbs.processlayer.termdeposit.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.codingapi.tx.annotation.TxTransaction;
import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.DataIsolationUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;


@Service("TermDepositEnquiryService")
public  class TermDepositEnquiryServiceImpl implements TermDepositEnquiryService{
	
	
	
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
		
		TermDepositMasterEntity tdrequest = new TermDepositMasterEntity();
		tdrequest.setAccountnumber(tdem.getAccountnumber());
		tdrequest.setCustomernumber(header.getCustomerNumber());
		//调用数据隔离工具类
		tdrequest = (TermDepositMasterEntity) DataIsolationUtil.condition(header, tdrequest);
		TermDepositMasterEntity tdInfo = (TermDepositMasterEntity) termDepositMasterDao.findOne(tdrequest);
		if (tdInfo == null) {
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404006),ExceptionConstant.ERROR_CODE404006);
		}	
		
		TermDepositDetailEntity tde = new TermDepositDetailEntity();
		tde.setAccountnumber(tdem.getAccountnumber());
		tde.setDepositnumber(tdem.getTdnumber());
		
		TermDepositDetailEntity reterm = (TermDepositDetailEntity) termDepositDetailDao.findOne(tde);
		if(reterm==null){
			throw new NotFoundException(ExceptionConstant.getExceptionMap().get(ExceptionConstant.ERROR_CODE404001),ExceptionConstant.ERROR_CODE404001);
		}
		//根据定存账号查询currencyCode
		TermDepositMasterEntity searchCurrencyCode = new TermDepositMasterEntity();
		searchCurrencyCode.setAccountnumber(reterm.getAccountnumber());
		TermDepositMasterEntity res_searchCurrencyCode = (TermDepositMasterEntity) termDepositMasterDao.findOne(searchCurrencyCode);
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
		TermDepositMasterEntity searchTDM = new TermDepositMasterEntity();
		searchTDM.setCustomernumber(customerNumber);
		List<TermDepositMasterEntity> res_searchTDM = termDepositMasterDao.findMany(searchTDM);
		TermDepositDetailEntity searchTDD = new TermDepositDetailEntity();
		if(res_searchTDM!=null && res_searchTDM.size()>0){
			for(int i=0;i<res_searchTDM.size();i++){
				searchTDD.setAccountnumber(res_searchTDM.get(i).getAccountnumber());
				List<TermDepositDetailEntity> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
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
		TermDepositMasterEntity searchTDM = new TermDepositMasterEntity();
		searchTDM.setAccountnumber(accountNumber);
		TermDepositMasterEntity res_searchTDM = (TermDepositMasterEntity) termDepositMasterDao.findOne(searchTDM);
		//根据accountNumber查询定存明细表
		TermDepositDetailEntity searchTDD = new TermDepositDetailEntity();
		searchTDD.setAccountnumber(accountNumber);
		List<TermDepositDetailEntity> res_searchTDD = termDepositDetailDao.findMany(searchTDD);
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
