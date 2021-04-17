package com.apporilla.flighthud;

import com.apporilla.flighthud.Utils.MathX;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHelper implements SensorEventListener {
	private Context mContext;
	
	// onSensorChanged
	private float R[] = new float[16];
	private float I[] = new float[16];
	private float orientation[] = new float[3];
	private float[] outR = new float[16]; 	

    private SensorManager mSensorManager;     
    private Sensor mAccelerometer;
    private Sensor mMagnetometer ;	
	
    private static float[] mAccelerometerValues = null;
    private static float[] mGeomageneticValues = null;	
    
	public double mPitch;
	public double mRoll;
	public double mYaw;    
	
	public SensorHelper(Context context) {
		mContext = context;
		this.mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
		this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {			
	}

	@Override
	public void onSensorChanged(SensorEvent event) {	
		
		synchronized(this)
		{
		    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		    	mAccelerometerValues = event.values;     
		    } 			
		    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
		    	mGeomageneticValues = event.values;     
		    } 		
		    
		    if ((mAccelerometerValues != null) && (mGeomageneticValues != null))
		    {

		    	boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometerValues, mGeomageneticValues);
		    	if (success)
		    	{
		    		SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR); 
		    		SensorManager.getOrientation(outR,orientation);
		    		mYaw = orientation[0] * MathX.toDegreesF;               
		    		mPitch = orientation[1] * MathX.toDegreesF;
					mRoll = orientation[2] * MathX.toDegreesF;
					
					//String sText = String.format("a:%1.4f\np:%1.4f\nr:%1.4f", yaw,pitch,roll); 
					if (mYaw<0)
						mYaw+=360;

						
		    	}
		    	
		    	
		    }
		}
	    
	    
	    
		
	}

	public void onResume() {
    	mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    	mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void onPause() {
	 	mSensorManager.unregisterListener(this);
		
	}

}
