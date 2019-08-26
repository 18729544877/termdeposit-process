package com.simnectzbank.lbs.processlayer.termdeposit.util;

import com.alibaba.fastjson.JSONObject;


public class JsonStyleUtil {
	public static boolean getJSONArrayFlag(Object json) {
		if (json instanceof JSONObject) {
			return false;
		} 
		return true;
	}
}
