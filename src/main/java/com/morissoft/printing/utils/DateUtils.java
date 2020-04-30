package com.morissoft.printing.utils;

import java.sql.Date;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
	final static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));

	public static Integer getDate(Date date) {
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);

	}
	public static Integer getMonth(Date date) {
		cal.setTime(date);
		return (cal.get(Calendar.MONTH)+1);

	}
	public static Integer getYear(Date date) {
		cal.setTime(date);
		return cal.get(Calendar.YEAR);

	}
}
