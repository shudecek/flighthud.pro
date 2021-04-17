package com.apporilla.flighthud.metar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.apporilla.flighthud.Utils.MathX;


import android.app.Activity;
import android.content.Context;
import android.location.Location;

public class MetarHelper {


	
	private Context mContext;
	private Activity mActivity;
	public ArrayList<GeoData> mNearbyMetarsList;
	public long mMetarTimestamp = 0;
	public final static String dirN="N";
	public final static String dirNNE="NNE";
	public final static String dirNE="NE";
	public final static String dirENE="ENE";
	public final static String dirE="E";
	public final static String dirESE="ESE";
	public final static String dirSE="SE";
	public final static String dirSSE="SSE";
	public final static String dirS="S";
	public final static String dirSSW="SSW";
	public final static String dirSW="SW";
	public final static String dirWSW="WSW";
	public final static String dirW="W";
	public final static String dirWNW="WNW";
	public final static String dirNW="NW";
	public final static String dirNNW="NNW";
		
	public MetarHelper(Context context, Activity activity)
	{
		this.mContext = context;
		this.mActivity = activity;
		mNearbyMetarsList = new ArrayList<GeoData>();
	}
	
	public MetarHelper(Context context)
	{
		this.mContext = context;
		
	}
	

	public String getMetarText(Location location)
	{
		StringBuilder sb = new StringBuilder();
		
		if ((mNearbyMetarsList != null) && (mNearbyMetarsList.size() >0) && (location != null))
		{
			
			// 6 are fetched from db every 2 min and saved, then we sort to get 3 nearest every 3 seconds
			 
			for (int i=0; i<mNearbyMetarsList.size(); i++)
			{				
				mNearbyMetarsList.get(i).distance = MathX.geoDistance(
						location.getLatitude(), 
						location.getLongitude(), 
						mNearbyMetarsList.get(i).latitude, 
						mNearbyMetarsList.get(i).longitude);
				
			}			
			
			Collections.sort(mNearbyMetarsList);
			
			for (int i=0; i<mNearbyMetarsList.size(); i++)
			{
				if (i>2)
					break;
				final String id = mNearbyMetarsList.get(i).id;
				final double distance = mNearbyMetarsList.get(i).distance; 
				final double direction = MathX.getDegrees(						
						location.getLatitude(), 
						location.getLongitude(), 
						mNearbyMetarsList.get(i).latitude, 
						mNearbyMetarsList.get(i).longitude,
						0);
				final String s=String.format("%s %.1f %s\n", id,distance,getDirectionString(direction));
				sb.append(s);
			}
			
		}
		
		return sb.toString(); 
	}

	public String getDirectionString(final double direction)
	{
		if (direction <= 11.25) 
			return dirN;
		else if (direction <= 33.75)
			return dirNNE;
		else if (direction <= 56.25)
			return dirNE;
		else if (direction <= 78.75)
			return dirENE;
		else if (direction <= 101.25)
			return dirE;
		else if (direction <= 123.75)
			return dirESE;
		else if (direction <= 146.25)
			return dirSE;
		else if (direction <= 168.75)
			return dirSSE;
		else if (direction <= 191.25)
			return dirS;
		else if (direction <= 213.75)
			return dirSSW;
		else if (direction <= 236.25)
			return dirSW;
		else if (direction <= 258.75)
			return dirWSW;
		else if (direction <= 281.25)
			return dirW;
		else if (direction <= 303.75)
			return dirWNW;
		else if (direction <= 326.25)
			return dirNW;
		else if (direction <= 348.75)
			return dirNNW;
		else 
			return dirN;
			
				
		
	}
	
	public boolean getNearbyList(final Location location)
	{
        DBAdapterGeo db = new DBAdapterGeo(mContext);       
        


        int count=6;
        
        if (location != null)
        {
	        db.open(); 		
	        mNearbyMetarsList = db.getNearby(location.getLongitude(), location.getLatitude(), count);
			db.close();
			return true;	        	
        }
        else
        	return false;

	}
	

		
/*
	 ArrayList<MetarLocation> getMetarPoints(GeoPoint topLeft, GeoPoint bottomRight)	 
	 {
		 int topE4 = topLeft.getLatitudeE6()/100;
		 int bottomE4 = bottomRight.getLatitudeE6()/100;
		 int leftE4 = topLeft.getLongitudeE6()/100;
		 int rightE4 = bottomRight.getLongitudeE6()/100;
		 
		 ArrayList<MetarLocation> loc;
		 
	     DBAdapterGeo db = new DBAdapterGeo(context);       
	     db.open();		 
		 loc = db.getInBoundary( topE4, bottomE4, leftE4, rightE4);
		 db.close();
		 
		 return loc;
		 
	 }	
	*/
}
