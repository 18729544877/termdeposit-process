package com.simnectzbank.lbs.processlayer.termdeposit.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.model.HeaderModel;
import com.csi.sbs.common.business.util.HeaderModelUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simnectzbank.lbs.processlayer.termdeposit.model.ChequeBookModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositDrawDownModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositEnquiryModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositMasterModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.TermDepositRenewalModel;
import com.simnectzbank.lbs.processlayer.termdeposit.service.AccountMasterService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositEnquiryService;
import com.simnectzbank.lbs.processlayer.termdeposit.service.TermDepositMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/deposit/term")
@Api(value = "Then controller is term deposit")
public class TermDeposit {

	@Resource
	AccountMasterService accountMasterService;
	
	@Resource
	private TermDepositMasterService termDepositMasterService;

	@Resource
	private TermDepositEnquiryService termDepositEnquiryService;

	@Resource
	private RestTemplate restTemplate;

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 创建定存单
	 * 
	 * @param termDepositMasterModel
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/termDepositApplication", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This API is designed to apply for a term deposit.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public ResultUtil termDepositApplication(@RequestBody @Validated TermDepositMasterModel termDepositMasterModel,
			HttpServletRequest request) throws Exception {
		ResultUtil result = new ResultUtil();
		try{
            HeaderModel header = HeaderModelUtil.getHeader(request);
			Map<String, Object> map = termDepositMasterService.termDepositApplication(header, termDepositMasterModel);
			result.setCode(map.get("code").toString());
			result.setMsg(map.get("msg").toString());
			result.setData(map.get("data").toString());
			return result;
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 定存到期取款
	 * 
	 * @param tddm
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/termDepositDrawDown", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This API is designed to make a term deposit drawdown. ", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public String termDepositDrawDown(@RequestBody TermDepositDrawDownModel termDepositDrawDownModel, HttpServletRequest request)
			throws Exception {
		try{
			HeaderModel header = HeaderModelUtil.getHeader(request);
			Map<String, Object> map = termDepositMasterService.termDepositDrawDown(header, termDepositDrawDownModel,restTemplate);
			return objectMapper.writeValueAsString(map);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 定存到期续存
	 * 
	 * @param tdrm
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/termDepositRenewal", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This API is designed to renew a term deposit.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public String termDepositRenewal(@RequestBody TermDepositRenewalModel termDepositRenewalModel, HttpServletRequest request)
			throws Exception {
		try{
			HeaderModel header = HeaderModelUtil.getHeader(request);
			Map<String, Object> map = termDepositMasterService.termDepositRenewal(header,termDepositRenewalModel);
			return objectMapper.writeValueAsString(map);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 定存查询
	 * 
	 * @param termDepositEnquiryModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/termDepositEnquiry", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This API is designed to get a term deposit details.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public ResultUtil termDepositEnquiry(@RequestBody @Validated TermDepositEnquiryModel termDepositEnquiryModel,HttpServletRequest request)
			throws Exception {
		try {
			HeaderModel header = HeaderModelUtil.getHeader(request);
			return termDepositEnquiryService.termDepositEnquiry(header,termDepositEnquiryModel, restTemplate);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 定存查询(根据customernumber 查询客户所有定存)
	 * 
	 * @param termDepositEnquiryModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/allTermDeposit/{customerNumber}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "This API is designed to retrieve all the term deposits of this customer.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public ResultUtil allTermDeposit(@ApiParam(name = "customerNumber", value = "customer Number eg: 001000000001", required = true) @PathVariable("customerNumber") String customerNumber,HttpServletRequest request)
			throws Exception {
		try {
			HeaderModel header = HeaderModelUtil.getHeader(request);
			return termDepositEnquiryService.termDepositAllEnquiry(header, customerNumber, restTemplate);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 定存查询(根据accountnumber 查询此账号下所有定存)
	 * 
	 * @param termDepositEnquiryModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/termDeposit/{accountNumber}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "This API is designed to retrieve all the term deposits in this account number.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
		@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
		@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
		@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
		@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."),
    })
	public ResultUtil termDeposit(@ApiParam(name = "accountNumber", value = "accountNumber eg: HK760001001000000005100", required = true) @PathVariable("accountNumber") String accountNumber,HttpServletRequest request)
			throws Exception {
		try {
			HeaderModel header = HeaderModelUtil.getHeader(request);
			return termDepositEnquiryService.getTermDepositByAccount(header, accountNumber, restTemplate);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/chequeBookCreation", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "This API is designed to create a cheque book for a current account.", notes = "version 0.0.1")
	@ApiResponses({ @ApiResponse(code = 200, message = "Query completed successfully.(Returned By Get)"),
			@ApiResponse(code = 404, message = "The requested deposit account does not exist.Action: Please make sure the account number and account type you’re inputting are correct."),
			@ApiResponse(code = 201, message = "Normal execution. The request has succeeded. (Returned By Post)"),
			@ApiResponse(code = 403, message = "Token has incorrect scope or a security policy was violated. Action: Please check whether you’re using the right token with the legal authorized user account."),
			@ApiResponse(code = 500, message = "Something went wrong on the API gateway or micro-service. Action: check your network and try again later."), })
	public ResultUtil chequeBookCreation(@RequestBody @Validated ChequeBookModel chequeBookModel,
			HttpServletRequest request) throws Exception {
		Map<String, Object> normalmap = null;
		ResultUtil result = new ResultUtil();
		try {
            HeaderModel header = HeaderModelUtil.getHeader(request);
			normalmap = accountMasterService.chequeBookRequest(header, chequeBookModel, restTemplate);
			result.setCode(normalmap.get("code").toString());
			result.setMsg(normalmap.get("msg").toString());
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

}
