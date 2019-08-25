package com.simnectzbank.lbs.processlayer.termdeposit.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.ResultUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.config.PathConfig;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.AccountConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.NumberConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.ReturnConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.model.SysConfigModel;


public class AvailableNumberUtil {
	
	public static void availableNumberIncrease(RestTemplate restTemplate, String item, PathConfig pathConfig) throws Exception {
		String appendSave = AccountConstant.STRING_SPACE;
		int nextAvailableNumber = 0;
		
		nextAvailableNumber = getNextAvailableNumber(SysConstant.NEXT_AVAILABLE_SEQ, restTemplate, pathConfig);
		appendSave = prepareAppendSaveData(nextAvailableNumber);

		updateAvailableNumber(appendSave, item, restTemplate, pathConfig);
	}

	@SuppressWarnings("rawtypes")
	static int getNextAvailableNumber(String item, RestTemplate restTemplate, PathConfig pathConfig) throws Exception {
		SysConfigModel sce = new SysConfigModel();
		sce.setItem(item);
		
		ResultUtil change1 = SendUtil.sendPostRequest(restTemplate, pathConfig.getSysadmin_sysconfig_findOne(), JSON.toJSONString(sce));
		SysConfigModel sysConfigModel = JSONObject.parseObject(JsonProcess.changeEntityTOJSON(change1.getData()),
				SysConfigModel.class);

		//get value form sys config table
		String currentAvailableNumber = sysConfigModel.getValue();
		
		int nextAvailableNumber = 0;
		nextAvailableNumber = Integer.parseInt(currentAvailableNumber);
		return nextAvailableNumber;
	}


	public static void availableSEQIncrease(RestTemplate restTemplate, String item, PathConfig pathConfig) throws Exception {
		int nextAvailableNumber = 0;
		String appendSave = AccountConstant.STRING_SPACE;
		
		nextAvailableNumber = getNextAvailableNumber(item, restTemplate, pathConfig);
		appendSave = prepareAppendSaveData(nextAvailableNumber);
		
		updateAvailableNumber(appendSave, item, restTemplate, pathConfig);
	}

	private static String prepareAppendSaveData(int nextAvailableNumber) {
		String appendSave = AccountConstant.STRING_SPACE;
		//number add 1
		nextAvailableNumber = nextAvailableNumber + NumberConstant.NUMBER_INT_1;
		String availableNumber = String.valueOf(nextAvailableNumber);
		int availableNumberLength = availableNumber.length();
		// 
		switch (availableNumberLength) {
		case 1:
			appendSave = NumberConstant.PREFIX_FILL_EIGHT_ZERO + nextAvailableNumber;
			break;
		case 2:
			appendSave = NumberConstant.PREFIX_FILL_SEVEN_ZERO + nextAvailableNumber;
			break;
		case 3:
			appendSave = NumberConstant.PREFIX_FILL_SIX_ZERO + nextAvailableNumber;
			break;
		case 4:
			appendSave = NumberConstant.PREFIX_FILL_FIVE_ZERO + nextAvailableNumber;
			break;
		case 5:
			appendSave = NumberConstant.PREFIX_FILL_FOUR_ZERO + nextAvailableNumber;
			break;
		case 6:
			appendSave = NumberConstant.PREFIX_FILL_THREE_ZERO + nextAvailableNumber;
			break;
		case 7:
			appendSave = NumberConstant.PREFIX_FILL_TWO_ZERO + nextAvailableNumber;
			break;
		case 8:
			appendSave = NumberConstant.PREFIX_FILL_ONE_ZERO + nextAvailableNumber;
			break;
		case 9:
			appendSave = nextAvailableNumber + AccountConstant.STRING_SPACE;
			break;
		}
		return appendSave;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> updateAvailableNumber(String appendSave, String item, RestTemplate restTemplate, PathConfig pathConfig) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SysConfigModel sysConfigModel = new SysConfigModel();
		
		sysConfigModel.setValue(appendSave);
		sysConfigModel.setItem(item);
		
		ResultUtil result = SendUtil.sendPostRequest(restTemplate, pathConfig.getSysadmin_sysconfig_update(), JSON.toJSONString(sysConfigModel));
		String code = result.getCode();
		if (!ReturnConstant.RETURN_CODE_200.equals(code)) {
			map.put(ReturnConstant.MSG, ReturnConstant.FIAL_TO_GENERATE_NEXT_AVAILABLE_NUMBER);
			map.put(ReturnConstant.RETURN_KEY_CODE, ReturnConstant.RETURN_CODE_0);
		}
		
		return map;
	}


	public static void availableTDNumberIncrease(RestTemplate restTemplate, String item, PathConfig pathConfig) throws Exception {
		int nextAvailableNumber = 0;
		String appendSave = AccountConstant.STRING_SPACE;
		
		nextAvailableNumber = getNextAvailableNumber(SysConstant.NEXT_AVAILABLE_TDNUMBER, restTemplate, pathConfig);
		appendSave = prepareAppendSaveData(nextAvailableNumber);

		updateAvailableNumber(appendSave, SysConstant.NEXT_AVAILABLE_TDNUMBER, restTemplate, pathConfig);
	}


	public static void availableDealIncrease(RestTemplate restTemplate, String item, PathConfig pathConfig) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat(SysConstant.TIME_FORMAT_NORMAL);
		String timeStr = sf.format(new Date());
		String appendSave = AccountConstant.STRING_SPACE;
		int nextAvailableNumber = 0;
		
		nextAvailableNumber = getNextAvailableNumber(SysConstant.NEXT_AVAILABLE_DEALNUMBER, restTemplate, pathConfig);
		appendSave = prepareAppendSaveData(nextAvailableNumber);
		appendSave = timeStr + appendSave;

		updateAvailableNumber(appendSave, SysConstant.NEXT_AVAILABLE_DEALNUMBER, restTemplate, pathConfig);
	}

}
