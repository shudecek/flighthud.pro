package com.apporilla.flighthud;

import com.apporilla.flighthud.Utils.DateFormat;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper implements LocationListener {
	private Context mContext;
	private LocationManager locationManager;	
	private String locationBestProvider;
	public Location location;		
	
	public double mSpeed;		
	public double mAltitude;
	public double mLatitude;
	public double mLongitude;
	public String mTime=""; 
	public long mLocationLastUpdated=Long.MAX_VALUE;
	
	final static double MetersPerSecond2Knots = 1.9438444924406; 
	final static double Meters2Feet = 3.2808399; 
	
	public LocationHelper(Context context) {
		mContext = context;
	}

	@Override
	public void onLocationChanged(Location loc) {
		location = loc;
					
		mSpeed = (loc.getSpeed()*MetersPerSecond2Knots);
		mAltitude = (loc.getAltitude()*Meters2Feet);
		mLatitude = loc.getLatitude();
		mLongitude = loc.getLongitude();
		mTime = (String)DateFormat.GetUtcTimeOnly( loc.getTime());
		mLocationLastUpdated = System.currentTimeMillis();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		locationBestProvider = locationManager.getBestProvider(new Criteria(), false);
				
	}

	@Override
	public void onProviderEnabled(String provider) {
		locationBestProvider = locationManager.getBestProvider(new Criteria(), false);	
			
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	public void onCreate() {
		
		locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);    
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		locationBestProvider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(locationBestProvider);  
		
	}

	public void onResume() {
		locationManager.requestLocationUpdates( locationBestProvider, 0, 0, this); 		
	}

	public void onPause() {
		locationManager.removeUpdates(this);		
	}

}
