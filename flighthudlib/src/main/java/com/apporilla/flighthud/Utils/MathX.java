package com.apporilla.flighthud.Utils;

public class MathX {
	public static final double toRadians = 0.017453292519943295769236907684886;
	public static final double toDegrees = 57.295779513082320876798154814105;
	public static final float toDegreesF = 57.2957795f;
	
	public static double geoDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
		  final double theta = lon1 - lon2;
		  double dist = Math.sin(toRadians*lat1) * Math.sin(toRadians*lat2) + Math.cos(toRadians*lat1) * Math.cos(toRadians*lat2) * Math.cos(toRadians*theta);
		  dist = Math.acos(dist);
		  dist = toDegrees*dist;
		  dist = dist * 60.0;

		  return (dist);
		}
	
	/**
	 * Params: lat1, long1 => Latitude and Longitude of current point
	 *         lat2, long2 => Latitude and Longitude of target  point
	 *         
	 *         headX       => x-Value of built-in phone-compass
	 * 
	 * Returns the degree of a direction from current point to target point
	 *
	 */
	public static double getDegrees( double lat1,  double lon1, double lat2, double lon2, final double headX) {

	    final double dLat = toRadians*(lat2-lat1);
	    final double dLon = toRadians*(lon2-lon1);

	    lat1 = toRadians*(lat1);
	    lat2 = toRadians*(lat2);

	    double y = Math.sin(dLon) * Math.cos(lat2);
	    double x = Math.cos(lat1)*Math.sin(lat2) -
	            Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
	    double brng = toDegrees*(Math.atan2(y, x));

	    // fix negative degrees
	    if(brng<0) {
	        brng=360-Math.abs(brng);
	    }

	    return brng - headX;
	}
	
}
