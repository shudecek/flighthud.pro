package com.apporilla.flighthud;

public class HudItem {

	public boolean fixed; // fixed objects don't rotate/move around
	public Object o;
	public float degreesYOffset;
	
	public HudItem(boolean fixed, Object o, float degreesYOffset)
	{
		this.fixed = fixed;
		this.o = o;	
		this.degreesYOffset = degreesYOffset;
	}
}
