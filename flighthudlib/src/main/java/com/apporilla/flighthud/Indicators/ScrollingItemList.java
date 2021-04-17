package com.apporilla.flighthud.Indicators;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrollingItemList {

	private int stepSizeMajor;
	private int stepSizeMinor;
	private int stepSizeLabel;
	private int windowSize;
	private int stepOffsetMinor;
	
	public ScrollingItemList( int stepSizeMajor, int stepSizeMinor, int stepSizeLabel, int windowSize, int stepOffsetMinor)
	{
		this.stepSizeMajor = stepSizeMajor;
		this.stepSizeMinor = stepSizeMinor;
		this.stepSizeLabel = stepSizeLabel;
		this.windowSize = windowSize;
		this.stepOffsetMinor = stepOffsetMinor;
	}
	
	public int GetWindowSize()
	{
		return windowSize;
	}
	
	// returns hashmap of numbers and their position that are within window  <position,value>
	public HashMap<Integer,Integer> GetNumbers(int windowCenter)
	{
		final int frameStart = windowCenter-(windowSize/2);
		final int firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeLabel-(frameStart%stepSizeLabel);
		else
			firstItem = ((0-frameStart)%stepSizeLabel);
		
		HashMap<Integer,Integer> al = new HashMap<Integer,Integer>();
		if (firstItem == stepSizeLabel)
			al.put(0, frameStart);
		for (Integer i=firstItem; i<windowSize; i+=stepSizeLabel)
		{
			al.put(i, frameStart+i);
		}		
		return al;		
	}
	
	// return major items within window
	public ArrayList<Integer> GetMajorHashes(int windowCenter)
	{
		final int frameStart = windowCenter-(windowSize/2);
		final int firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeMajor-(frameStart%stepSizeMajor);
		else
			firstItem = ((0-frameStart)%stepSizeMajor);
		
		ArrayList<Integer> al = new ArrayList<Integer>();
		if (firstItem == stepSizeMajor)
			al.add(0);
		for (Integer i=firstItem; i<windowSize; i+=stepSizeMajor)
		{
			al.add(i);
		}		
		return al;
	}	
	
	// return minor items within window
	public ArrayList<Integer> GetMinorHashes(int windowCenter)
	{
		final int frameStart = windowCenter-(windowSize/2);
		final int firstItem;
		if (frameStart >= 0)
			firstItem = stepSizeMinor-((frameStart+stepOffsetMinor)%stepSizeMinor);
		else
			firstItem = ((0-(frameStart+stepOffsetMinor))%stepSizeMinor);
	
		ArrayList<Integer> al = new ArrayList<Integer>();
		if (firstItem == stepSizeMinor)
			al.add(0);			
		for (Integer i=firstItem; i<windowSize; i+=stepSizeMinor)
		{
			al.add(i);
		}
		
		return al;
	}
	
	
}
