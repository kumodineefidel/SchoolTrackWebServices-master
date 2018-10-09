package com.gts.webservices.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	public static Date getDateFromLong(Integer timeTime) throws ParseException{
		System.out.println(timeTime);
		
		String time = String.valueOf(timeTime);
		Long longTime = Long.valueOf(time);
       
        Date date = new Date(longTime * 1000L);

		DateFormat gmtFormat = new SimpleDateFormat();
		TimeZone gmtTime = TimeZone.getTimeZone("GMT+5");
		gmtFormat.setTimeZone(gmtTime);
		System.out.println("GMT Time: " + gmtFormat.format(date));
	
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:ss");
		dateFormat.setTimeZone(gmtTime);
		Date date2 = dateFormat.parse(gmtFormat.format(date));
		
		System.out.println(date2);
		return date2;
	}
	
	public static void main(String args[]) throws ParseException{
		DateUtil.getDateFromLong(1447042202);
	}
}
