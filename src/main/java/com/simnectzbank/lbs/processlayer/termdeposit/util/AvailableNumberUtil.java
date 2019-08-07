package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.json.JsonProcess;
import com.csi.sbs.common.business.util.PostUtil;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.PathConstant;
import com.simnectzbank.lbs.processlayer.termdeposit.constant.SysConstant;


public class AvailableNumberUtil {

	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 可用Number加一
	 */
	public static void availableNumberIncrease(RestTemplate restTemplate, String item) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentNumber = restTemplate.getForEntity(PathConstant.NEXT_AVAILABLE + item, String.class).getBody();
		if (currentNumber == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}
		int nextAvailableNumber = 0;
		nextAvailableNumber = Integer.parseInt(
				JsonProcess.returnValue(JsonProcess.changeToJSONObject(currentNumber), "nextAvailableNumber"));
		// 可用number加1
		nextAvailableNumber = nextAvailableNumber + 1;
		String availableNumber = String.valueOf(nextAvailableNumber);
		int availableNumberLength = availableNumber.length();
		String appendSave = "";
		// 可用number长度判断
		switch (availableNumberLength) {
		case 1:
			appendSave = "00000000" + nextAvailableNumber;
			break;
		case 2:
			appendSave = "0000000" + nextAvailableNumber;
			break;
		case 3:
			appendSave = "000000" + nextAvailableNumber;
			break;
		case 4:
			appendSave = "00000" + nextAvailableNumber;
			break;
		case 5:
			appendSave = "0000" + nextAvailableNumber;
			break;
		case 6:
			appendSave = "000" + nextAvailableNumber;
			break;
		case 7:
			appendSave = "00" + nextAvailableNumber;
			break;
		case 8:
			appendSave = "0" + nextAvailableNumber;
			break;
		case 9:
			appendSave = nextAvailableNumber + "";
			break;
		}

		String param = "{\"value\":\"" + appendSave + "\",\"item\":\"" + item + "\"}";
		ResponseEntity<String> result = restTemplate.postForEntity(PathConstant.SAVE_NEXT_AVAILABLE,
				PostUtil.getRequestEntity(param), String.class);
		if (!result.getStatusCode().equals("200")) {
			map.put("msg", "生成下一个可用number失败");
			map.put("code", "0");
		}
	}

	/**
	 * 可用Number加一
	 */
	public static void availableSEQIncrease(RestTemplate restTemplate, String item) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentNumber = restTemplate
				.getForEntity(PathConstant.NEXT_AVAILABLE + SysConstant.NEXT_AVAILABLE_SEQ, String.class).getBody();
		if (currentNumber == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}
		int nextAvailableNumber = 0;
		nextAvailableNumber = Integer.parseInt(
				JsonProcess.returnValue(JsonProcess.changeToJSONObject(currentNumber), "nextAvailableNumber"));
		// 可用number加1
		nextAvailableNumber = nextAvailableNumber + 1;
		String availableNumber = String.valueOf(nextAvailableNumber);
		int availableNumberLength = availableNumber.length();
		String appendSave = "";
		// 可用number长度判断
		switch (availableNumberLength) {
		case 1:
			appendSave = "00000000" + nextAvailableNumber;
			break;
		case 2:
			appendSave = "0000000" + nextAvailableNumber;
			break;
		case 3:
			appendSave = "000000" + nextAvailableNumber;
			break;
		case 4:
			appendSave = "00000" + nextAvailableNumber;
			break;
		case 5:
			appendSave = "0000" + nextAvailableNumber;
			break;
		case 6:
			appendSave = "000" + nextAvailableNumber;
			break;
		case 7:
			appendSave = "00" + nextAvailableNumber;
			break;
		case 8:
			appendSave = "0" + nextAvailableNumber;
			break;
		case 9:
			appendSave = nextAvailableNumber + "";
			break;
		}

		String param = "{\"value\":\"" + appendSave + "\",\"item\":\"" + item + "\"}";
		ResponseEntity<String> result = restTemplate.postForEntity(PathConstant.SAVE_NEXT_AVAILABLE,
				PostUtil.getRequestEntity(param), String.class);
		if (!result.getStatusCode().equals("200")) {
			map.put("msg", "生成下一个可用number失败");
			map.put("code", "0");
		}
	}

	/**
	 * 可用Number加一
	 */
	public static void availableTDNumberIncrease(RestTemplate restTemplate, String item) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentNumber = restTemplate
				.getForEntity(PathConstant.NEXT_AVAILABLE + SysConstant.NEXT_AVAILABLE_TDNUMBER, String.class)
				.getBody();
		if (currentNumber == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}
		int nextAvailableNumber = 0;
		nextAvailableNumber = Integer.parseInt(
				JsonProcess.returnValue(JsonProcess.changeToJSONObject(currentNumber), "nextAvailableNumber"));
		// 可用number加1
		nextAvailableNumber = nextAvailableNumber + 1;
		String availableNumber = String.valueOf(nextAvailableNumber);
		int availableNumberLength = availableNumber.length();
		String appendSave = "";
		// 可用number长度判断
		switch (availableNumberLength) {
		case 1:
			appendSave = "00000000" + nextAvailableNumber;
			break;
		case 2:
			appendSave = "0000000" + nextAvailableNumber;
			break;
		case 3:
			appendSave = "000000" + nextAvailableNumber;
			break;
		case 4:
			appendSave = "00000" + nextAvailableNumber;
			break;
		case 5:
			appendSave = "0000" + nextAvailableNumber;
			break;
		case 6:
			appendSave = "000" + nextAvailableNumber;
			break;
		case 7:
			appendSave = "00" + nextAvailableNumber;
			break;
		case 8:
			appendSave = "0" + nextAvailableNumber;
			break;
		case 9:
			appendSave = nextAvailableNumber + "";
			break;
		}

		String param = "{\"value\":\"" + appendSave + "\",\"item\":\"" + item + "\"}";
		ResponseEntity<String> result = restTemplate.postForEntity(PathConstant.SAVE_NEXT_AVAILABLE,
				PostUtil.getRequestEntity(param), String.class);
		if (!result.getStatusCode().equals("200")) {
			map.put("msg", "生成下一个可用number失败");
			map.put("code", "0");
		}
	}

	/**
	 * 可用DealNumber加一
	 */
	public static void availableDealIncrease(RestTemplate restTemplate, String item) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentNumber = restTemplate
				.getForEntity(PathConstant.NEXT_AVAILABLE + SysConstant.NEXT_AVAILABLE_DEALNUMBER, String.class)
				.getBody();
		if (currentNumber == null) {
			map.put("msg", "调用系统参数失败");
			map.put("code", "0");
		}
		int nextAvailableNumber = 0;
		nextAvailableNumber = Integer.parseInt(
				JsonProcess.returnValue(JsonProcess.changeToJSONObject(currentNumber), "nextAvailableNumber"));
		// 可用number加1
		nextAvailableNumber = nextAvailableNumber + 1;
		String availableNumber = String.valueOf(nextAvailableNumber);
		int availableNumberLength = availableNumber.length();
		String appendSave = "";
		// 可用number长度判断
		switch (availableNumberLength) {
		case 1:
			appendSave = "00000000" + nextAvailableNumber;
			break;
		case 2:
			appendSave = "0000000" + nextAvailableNumber;
			break;
		case 3:
			appendSave = "000000" + nextAvailableNumber;
			break;
		case 4:
			appendSave = "00000" + nextAvailableNumber;
			break;
		case 5:
			appendSave = "0000" + nextAvailableNumber;
			break;
		case 6:
			appendSave = "000" + nextAvailableNumber;
			break;
		case 7:
			appendSave = "00" + nextAvailableNumber;
			break;
		case 8:
			appendSave = "0" + nextAvailableNumber;
			break;
		case 9:
			appendSave = nextAvailableNumber + "";
			break;
		}

		appendSave = sf.format(new Date()) + appendSave;
		String param = "{\"value\":\"" + appendSave + "\",\"item\":\"" + item + "\"}";
		ResponseEntity<String> result = restTemplate.postForEntity(PathConstant.SAVE_NEXT_AVAILABLE,
				PostUtil.getRequestEntity(param), String.class);
		if (!result.getStatusCode().equals("200")) {
			map.put("msg", "生成下一个可用number失败");
			map.put("code", "0");
		}
	}

}
