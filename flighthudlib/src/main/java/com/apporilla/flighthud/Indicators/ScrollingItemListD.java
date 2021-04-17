package com.apporilla.flighthud.Indicators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.apporilla.flighthud.Utils.PointD;

public class ScrollingItemListD {

	private double stepSizeMajor;
	private double stepSizeMinor;
	private double stepSizeLabel;
	public double windowSize;
	public double oneOverWindowSize;
	private double stepOffsetMinor;
	
	public ScrollingItemListD( double stepSizeMajor, double stepSizeMinor, double stepSizeLabel, double windowSize, double stepOffsetMinor)
	{
		this.stepSizeMajor = stepSizeMajor;
		this.stepSizeMinor = stepSizeMinor;
		this.stepSizeLabel = stepSizeLabel;
		this.windowSize = windowSize;
		this.oneOverWindowSize = 1.0/windowSize;
		this.stepOffsetMinor = stepOffsetMinor;
	}	
	
	// returns hashmap of numbers and their position that are within window  <position,value>
	public void GetNumbers(double windowCenter, HashMap<Double,Double> al)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeLabel-(frameStart%stepSizeLabel);
		else
			firstItem = ((0-frameStart)%stepSizeLabel);
		
		al.clear();		
		if (firstItem == stepSizeLabel)
			al.put(0.0, frameStart);
		for (double i=firstItem; i<windowSize; i+=stepSizeLabel)
		{
			al.put(i, frameStart+i);
		}		
	
	}
	
	// returns hashmap of numbers and their position that are within window  <position,value>
	public void GetNumbers(double windowCenter, List<PointD> al)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeLabel-(frameStart%stepSizeLabel);
		else
			firstItem = ((0-frameStart)%stepSizeLabel);
		
		al.clear();		
		if (firstItem == stepSizeLabel)
			al.add(new PointD(0.0, frameStart)); 
		for (double i=firstItem; i<windowSize; i+=stepSizeLabel)
		{
			al.add(new PointD(i,frameStart+i));			
		}		
	
	}	
	
	// return major items within window
	public void GetMajorHashes(double windowCenter, ArrayList<Double> al)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeMajor-(frameStart%stepSizeMajor);
		else
			firstItem = ((0-frameStart)%stepSizeMajor);
		
		al.clear();		
		if (firstItem == stepSizeMajor)
			al.add(0.0);
		for (double i=firstItem; i<windowSize; i+=stepSizeMajor)
		{
			al.add(i);
		}		
	}	
	
	// return minor items within window
	public void GetMinorHashes(double windowCenter, ArrayList<Double> al)
	{
		final double frameStart = windowCenter-(windowSize/2);
		final double firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeMinor-((frameStart+stepOffsetMinor)%stepSizeMinor);
		else
			firstItem = ((0-(frameStart+stepOffsetMinor))%stepSizeMinor);
	
		al.clear();
		if (firstItem == stepSizeMinor)
			al.add(0.0);			
		for (double i=firstItem; i<windowSize; i+=stepSizeMinor)
		{
			al.add(i);
		}
		
	}
	
	
}
