package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class UTCUtil {
	
	/**
	 * 获取世界协调时(标准时间)
	 * @return
	 * @throws Exception
	 */
	public static Long getUTCTime() throws Exception {
		Calendar cal = Calendar.getInstance();
	   	return cal.getTimeInMillis();
    }
	
	
	/**
	 * 将世界协调时转换为中国东八区时间(带时分秒)
	 * @throws ParseException 
	 */
	public static String convertToOne(String utc) throws ParseException{
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ca.setTimeInMillis(Long.parseLong(utc));
		ca.add(Calendar.HOUR, 8);
		return format.format(ca.getTime());
	}
	
	/**
	 * 将世界协调时转换为中国东八区时间(不带时分秒)
	 * @throws ParseException 
	 */
	public static String convertToTwo(String utc) throws ParseException{
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ca.setTimeInMillis(Long.parseLong(utc));
		ca.add(Calendar.HOUR, 8);
		return format.format(ca.getTime());
	}
	
	/**
	 * 将中国东八区时间转换为世界协调时(不带时分秒)
	 * @throws ParseException 
	 */
	public static String convertToThree(String utc) throws ParseException{
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ca.setTimeInMillis(Long.parseLong(utc));
		ca.add(Calendar.HOUR, -8);
		return format.format(ca.getTime());
	}
	
//	public static void main(String args[]) throws Exception{
//		System.out.println(getUTCTime());
//	}

}
