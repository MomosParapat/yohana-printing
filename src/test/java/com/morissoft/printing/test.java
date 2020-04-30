package com.morissoft.printing;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.morissoft.printing.utils.LocalizedWeek;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Double dbl = Double.valueOf("2.34");
		Double dbl2 = Double.valueOf("3");
		Double dblResult = dbl * dbl2;
		// System.out.println(Math.round(f/dblResult);
		Locale loc = new Locale("id", "ID");
//		log.info("Locale {},{},{}", new LocalizedWeek(loc).getFirstDay(), new LocalizedWeek(loc).getLastDay(),
//				new LocalizedWeek(loc));
		LocalDate ld = LocalDate.of(2020, 3, 30);
		log.info("LOCAL DATE {}", ld);
		Integer completedYear = 2020;
		Integer completedMonth = 3;
		Integer completedDate = 31;
		Date dt = Date.valueOf(String.format("%s-%s-%s", completedYear, completedMonth, completedDate));
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
		cal.setTime(dt);;
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		log.info("DATE {},{},{},{}", year,month,day,dt);
	}

}
