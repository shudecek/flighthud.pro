package com.apporilla.flighthud;

public class ExifUtil {
	public static String GetLongitudeLatitudeString(double degrees)
	{
		if (degrees < 0)
			degrees = 0-degrees;
		
		int d = (int)degrees;
		// Truncate the decimals
		double t1 = ((degrees - d) * 60);
		int m = (int)t1;
		int s = (int)((t1 - m) * 60); 

		final String str= String.format("%d/1,%d/1,%d/1", d,m,s);
		return str;
	}

	public static String GetLongitudeRef(double longitude)
	{
		if (longitude <= 0.0)
			return "W";
		else
			return "E";
	}
	public static String GetLatitudeRef(double latitude)
	{
		if (latitude >= 0)
			return "N";
		else
			return "S";
	}
	


	
	
}
