package com.apporilla.flighthud.Indicators.AttitudeIndicatorItem;

import java.util.HashMap;



public class WindowedScrollingItemList {

	private int stepSize;
	private int windowSize;

	
	public WindowedScrollingItemList( int stepSize, int windowSize)
	{
		this.stepSize = stepSize;
		this.windowSize = windowSize;
	}
	
	public int GetWindowSize()
	{
		return windowSize;
	}
	
	// returns hashmap of numbers and their position that are within window  <position,value>
	public HashMap<Integer,Integer> GetItems(int windowCenter)
	{
		final int frameStart = windowCenter-(windowSize/2);
		final int firstItem;
		if (frameStart >= 0)
			firstItem = stepSize-(frameStart%stepSize);
		else
			firstItem = ((0-frameStart)%stepSize);
		
		HashMap<Integer,Integer> al = new HashMap<Integer,Integer>();
		if (firstItem == stepSize)
			al.put(0, frameStart);
		for (Integer i=firstItem; i<windowSize; i+=stepSize)
		{
			al.put(i, frameStart+i);
		}		
		return al;		
	}

	
	
}