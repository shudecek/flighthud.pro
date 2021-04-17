package com.apporilla.flighthud.Indicators.AttitudeIndicatorItem;


import java.util.ArrayList;
import java.util.List;

import com.apporilla.flighthud.Utils.PointD;



public class WindowedScrollingItemListD {

	private double stepSize;
	private double windowSize;
	public double oneOverWindowSize;

	
	public WindowedScrollingItemListD(final double stepSize)
	{
		this.stepSize = stepSize;
	}
	
	public void SetWindowSize( double windowSize)
	{
		this.windowSize = windowSize;
		this.oneOverWindowSize = 1.0/windowSize;		
	}
	
/*	
	// returns hashmap of numbers and their position that are within window  <position,value>
	public HashMap<Double,Double> GetItems(double windowCenter)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSize-(frameStart%stepSize);
		else
			firstItem = ((0-frameStart)%stepSize);
		
		HashMap<Double,Double> al = new HashMap<Double,Double>();
		if (firstItem == stepSize)
			al.put(0.0, frameStart);
		for (Double i=firstItem; i<windowSize; i+=stepSize)
		{
			al.put(i, frameStart+i);
		}		
		return al;		
	}
*/
	// returns hashmap of numbers and their position that are within window  <position,value>
	public void GetItems(final double windowCenter, final List<PointD> al)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSize-(frameStart%stepSize);
		else
			firstItem = ((0-frameStart)%stepSize);
		
		al.clear(); 
		if (firstItem == stepSize)
			al.add(new PointD(0.0, frameStart));
		for (double i=firstItem; i<windowSize; i+=stepSize)
		{
			al.add(new PointD(i, frameStart+i));
		}					
	}
	
}