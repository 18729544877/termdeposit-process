package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalculateMaturityDateUtil {

	@SuppressWarnings("unused")
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 指定日期加上天数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 */
	public static Long plusDay(int num, String newDate) throws ParseException {
		Calendar ca = Calendar.getInstance();
	   	ca.setTimeInMillis(Long.valueOf(newDate));
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		return ca.getTimeInMillis();
	}

	/**
	 * 指定日期加上月数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 */
	public static Long plusMonth(int num, String newDate) throws ParseException {
		Calendar ca = Calendar.getInstance();
	   	ca.setTimeInMillis(Long.valueOf(newDate));
	   	ca.add(Calendar.MONTH, num);
		return ca.getTimeInMillis();
	}

	/**
	 * 根据存款周期计算存款天数
	 * 
	 * @throws Exception
	 */
	public static Long CalculateTermDepositDays(String sysdate, String perid) throws Exception {
		switch (perid) {
		case "1day":
			return plusDay(1, sysdate);
		case "1week":
			return plusDay(7, sysdate);
		case "2weeks":
			return plusDay(14, sysdate);
		case "1month":
			// 加上月数之后的日期
			return plusMonth(1, sysdate);
		case "2months":
			// 加上月数之后的日期
			return plusMonth(2, sysdate);
		case "3months":
			// 加上月数之后的日期
			return plusMonth(3, sysdate);
		case "6months":
			// 加上月数之后的日期
			return plusMonth(6, sysdate);
		case "9months":
			// 加上月数之后的日期
			return plusMonth(9, sysdate);
		case "12months":
			// 加上月数之后的日期
			return plusMonth(12, sysdate);
		}
		return 0l;
	}

}
