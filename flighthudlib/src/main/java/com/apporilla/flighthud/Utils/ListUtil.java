package com.apporilla.flighthud.Utils;

import java.util.LinkedList;
import java.util.ListIterator;

import android.util.Log;

public class ListUtil {
	public static Double GetListAverage(LinkedList<Double> linkedList) {
		if (linkedList.size()==0)
			return 0.0;
		
		ListIterator<Double> itr = linkedList.listIterator();
		Double total=0.0;
		int count=0;
		
		while(itr.hasNext())
		{
			count++;
			total += itr.next();
		}
		return total/count;
	}
	
	public static double GetFilteredAverage(final LinkedList<Double> linkedList, final double cuttoff)
	{
		if (linkedList.size()==0)
			return 0.0;
		
		double total=0.0;
		double filteredTotal=0.0;
		
		for (int i=0; i<linkedList.size(); i++)
		{				
			total += linkedList.get(i);
		}
		final double average=total/linkedList.size();
		final double boundhi = average+cuttoff;
		final double boundlo = average-cuttoff;

		for (int i=0; i<linkedList.size(); i++)
		{				
			double val = linkedList.get(i);
			if (val > boundhi)
				val = boundhi;
			else if (val < boundlo)
				val = boundlo;
		
			filteredTotal += val;

		}	

		return filteredTotal/linkedList.size();
			
	}
	
	// filter around 360 degreess, that is filter so that distance between 1 and 359 is 2
	public static double GetFilteredAverageAngles(final LinkedList<Double> linkedList, final double cutoff)
	{
		if (linkedList.size()==0)
			return 0.0;
		
		double totalX=0.0;
		double totalY=0.0;
		double filteredTotalX=0.0;
		double filteredTotalY=0.0;
		
		for (int i=0; i<linkedList.size(); i++)
		{				
			totalX += Math.cos( MathX.toRadians*( linkedList.get(i)));
			totalY += Math.sin( MathX.toRadians*( linkedList.get(i)));
		}
		final double averageX=totalX/linkedList.size();
		final double averageY=totalY/linkedList.size();
		//final double averageAngle=MathX.toDegrees*(Math.atan2(averageY, averageX));
		//String debug = "";
		for (int i=0; i<linkedList.size(); i++)
		{
			final double val = linkedList.get(i);
			final double valX = Math.cos( MathX.toRadians*val);
			final double valY = Math.sin( MathX.toRadians*val);
			final double distance = Math.sqrt(((valX-averageX)*(valX-averageX)) + ((valY-averageY)*(valY-averageY)));
			// 1 degree  is 0.017453071 in distance away
			// 2 degrees is 0.034904813
			// 3 degrees is 0.052353897
			// 5 degrees is 0.087239  
			//10 degrees is 0.174311485
			//15 degrees is 0.261052384
			//20 degrees is 0.347296355
			if (distance <= cutoff)
			{				
				filteredTotalX += valX;
				filteredTotalY += valY;
			}
			else
			{
				filteredTotalX += averageX;
				filteredTotalY += averageY;		
		//		debug+=""+distance+",";
			}
		}
		//if (!debug.equals(""))
		//	Log.d("miss",debug);
		final double filteredAverageX=filteredTotalX/linkedList.size();
		final double filteredAverageY=filteredTotalY/linkedList.size();
		final double filteredAverageAngle=MathX.toDegrees*(Math.atan2(filteredAverageY, filteredAverageX));
		

		return filteredAverageAngle; 
	}
	
}
