package com.simnectzbank.lbs.processlayer.termdeposit.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.ChangeToResultUtil;
import com.csi.sbs.common.business.util.PostUtil;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.model.HolidayModel;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SysConfigModel;

public class SendUtil {
    @SuppressWarnings("rawtypes")
	public static ResultUtil sendPostRequest(RestTemplate restTemplate, String path, String requestStr) throws Exception{    	
    	ResponseEntity<String> rest = restTemplate.postForEntity(path, PostUtil.getRequestEntity(requestStr), String.class);
    	ResultUtil changeResult = ChangeToResultUtil.change(rest);
    	
    	return changeResult;
    }
    
    @SuppressWarnings("rawtypes")
   	public static HolidayModel sendPostRequestStr(RestTemplate restTemplate, String path, String requestStr, Class<HolidayModel> class1) throws Exception{    	
       	ResponseEntity<String> rest = restTemplate.postForEntity(path, PostUtil.getRequestEntity(requestStr), String.class);
       	ResultUtil changeResult = ChangeToResultUtil.change(rest);
       	
       	HolidayModel account = JSONObject.parseObject(JsonProcess.changeEntityTOJSON(changeResult.getData()), HolidayModel.class);
       	return account;
       }
    
    @SuppressWarnings("rawtypes")
	public static String sendPostForSysParameters(RestTemplate restTemplate, String path, String requestStr) throws Exception{
    	SysConfigModel sysConfigModel = new SysConfigModel();
    	sysConfigModel.setItem(requestStr);
    	
    	ResponseEntity<String> rest = restTemplate.postForEntity(path, PostUtil.getRequestEntity(JSON.toJSONString(sysConfigModel)), String.class);
    	ResultUtil changeResult = ChangeToResultUtil.change(rest);
    	
    	return (String) changeResult.getData();
    }

	
}
