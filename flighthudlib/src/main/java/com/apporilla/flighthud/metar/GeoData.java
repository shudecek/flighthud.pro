package com.apporilla.flighthud.metar;

public class GeoData implements Comparable<GeoData> {
	public String id;
	public double latitude;	
	public double longitude;
	public double distance;
	
	public GeoData(final String id, final double latitude, final double longitude )
	{
		this.id=id;
		this.latitude=latitude;		
		this.longitude=longitude;
		this.distance=0.0;
	}

	@Override
	public int compareTo(GeoData another) {
		return (int)(10000*this.distance - 10000*another.distance);
	
	}
}
