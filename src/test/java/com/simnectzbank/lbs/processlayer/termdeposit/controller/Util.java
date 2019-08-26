package com.simnectzbank.lbs.processlayer.termdeposit.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



public class Util {

	public static JSONObject getObject(String filePath) throws IOException {
		JSONObject obj = null;
		URL fileUrl = Util.class.getResource(filePath);
		File file = new File(fileUrl.getFile());
		//return JSON.parseObject(getStringValue(filePath),clazz);
		String value = getStringValues(file);
		if(value instanceof String){
			obj = new JSONObject();
			obj.put("value", value);
		}else{			
			obj = JSON.parseObject(getStringValues(file));
		}
		return obj;
	}

	public static String getStringValue(String filePath) throws IOException {
		URL fileUrl = Util.class.getResource(filePath);
		File file = new File(fileUrl.getFile());
		return file.isFile() ? getStringValues(file) : null;
	}
	
	@SuppressWarnings("resource")
	public static String getStringValues(File filePath) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");

        BufferedReader br = new BufferedReader(in);
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

	public static JSONObject setHeader(DataVo dataVO) {
		JSONObject params = new JSONObject();
		params.put("content-type", "application/json");
		if (dataVO.getToken() != null) {
			params.put("token", dataVO.getToken());
		}

		return params;
	}
}
