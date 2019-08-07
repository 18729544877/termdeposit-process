package com.simnectzbank.lbs.processlayer.termdeposit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalculateDateUtil {
	@SuppressWarnings("unused")
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 指定日期减上天数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 */
	public static Long reduceDay(int num, String newDate) throws ParseException {
		Calendar ca = Calendar.getInstance();
	   	ca.setTimeInMillis(Long.valueOf(newDate));
		ca.add(Calendar.DATE, -num);
		return ca.getTimeInMillis();
	}

	/**
	 * 指定日期减上月数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 */
	public static Long reduceMonth(int num, String newDate) throws ParseException {
		Calendar ca = Calendar.getInstance();
	   	ca.add(Calendar.MONTH, -num);
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
			return reduceDay(1, sysdate);
		case "1week":
			return reduceDay(7, sysdate);
		case "2weeks":
			return reduceDay(14, sysdate);
		case "1month":
			// 加上月数之后的日期
			return reduceMonth(1, sysdate);
		case "2months":
			// 加上月数之后的日期
			return reduceMonth(2, sysdate);
		case "3months":
			// 加上月数之后的日期
			return reduceMonth(3, sysdate);
		case "6months":
			// 加上月数之后的日期
			return reduceMonth(6, sysdate);
		case "9months":
			// 加上月数之后的日期
			return reduceMonth(9, sysdate);
		case "12months":
			// 加上月数之后的日期
			return reduceMonth(12, sysdate);
		}
		return 0l;
	}

}
