package com.apporilla.flighthud.metar;

public class MetarLocation {
	public String id;
	public int latitudeE4;
	public int longitudeE4;
	public String rawText;
	
	public MetarLocation(String id, int lat, int lng)
	{
		this.id=id;
		this.latitudeE4 = lat;
		this.longitudeE4 = lng;
	}
}
