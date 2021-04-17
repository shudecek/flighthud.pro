package com.apporilla.flighthud.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormat {
	public static CharSequence GetUtcTime(long now)
	{
		
		SimpleDateFormat datePattern = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		datePattern.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date_str = datePattern.format(new Date(now));
		return (CharSequence)date_str;
 		
	}		
	
	public static CharSequence GetUtcTimeOnly(long now)
	{
		
		SimpleDateFormat datePattern = new SimpleDateFormat("HH:mm:ss");
		datePattern.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date_str = datePattern.format(new Date(now));
		return (CharSequence)date_str;
 		
	}		
}
