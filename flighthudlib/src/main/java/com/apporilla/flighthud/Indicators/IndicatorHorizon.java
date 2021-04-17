package com.apporilla.flighthud.Indicators;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.font.Font;

import com.apporilla.flighthud.MainActivity;
import com.apporilla.flighthud.Indicators.AttitudeIndicatorItem.AttitudeHorizon;
import com.apporilla.flighthud.Indicators.AttitudeIndicatorItem.PitchMarkerAboveBelow;
import com.apporilla.flighthud.Indicators.AttitudeIndicatorItem.WindowedScrollingItemListD;
import com.apporilla.flighthud.Shape.Ellipse;
import com.apporilla.flighthud.Utils.MathX;
import com.apporilla.flighthud.Utils.PointD;

public class IndicatorHorizon  {
 // attitude indicator called "horizon" to lessen confusion
	Scene mScene; 
	Font mFont;
	double mViewAngle;
	double mViewAngleHalf;
	double mTanOfViewAngle;
	
	// "airplane" in center
	Ellipse body;
	Line leftWing;
	Line rightWing;
	Line rudder;	
	WindowedScrollingItemListD mScrollingItemList;
	//HashMap<Double,Double> mValuesInView;
	List<PointD> mValuesInView;
	public final float AirplaneRadius = 8; 
	
	// dynamically displayed items
	AttitudeHorizon attitudeHorizon;
	PitchMarkerAboveBelow[] pitchMarkerAboveBelow;
	
	
	public IndicatorHorizon(Scene scene, Font font) {
		mScene = scene;
		mFont = font;		
	}

	public void Create() {
		
		mValuesInView = new ArrayList<PointD>();
		final float x = (float)MainActivity.CAMERA_WIDTH_HALF;
		final float y = (float)MainActivity.CAMERA_HEIGHT_HALF;
		body = new Ellipse(x,y,AirplaneRadius);
		body.setLineWidth(2);
		body.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(body);	
		
		leftWing = new Line(x-AirplaneRadius-1, y, x-AirplaneRadius-20, y);
		leftWing.setLineWidth(2);
		leftWing.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(leftWing);

		rightWing = new Line(x+AirplaneRadius+1, y, x+AirplaneRadius+20, y);
		rightWing.setLineWidth(2);
		rightWing.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(rightWing);		
		
		rudder = new Line(x, y-AirplaneRadius-1, x, y-AirplaneRadius-12);
		rudder.setLineWidth(2);
		rudder.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(rudder);		
		
		attitudeHorizon = new AttitudeHorizon(mScene);
		attitudeHorizon.Create();
		

				
	}	
	
	public void Reorientate(final boolean pIsLandscape)
	{
		final float x = (float)MainActivity.CAMERA_WIDTH_HALF;
		final float y = (float)MainActivity.CAMERA_HEIGHT_HALF;		
		
		if (pIsLandscape)
		{
			leftWing.setPosition(x, y-AirplaneRadius-1, x, y-AirplaneRadius-20);
			rightWing.setPosition(x, y+AirplaneRadius+1, x, y+AirplaneRadius+20);
			rudder.setPosition(x+AirplaneRadius+1, y, x+AirplaneRadius+12, y );
			
		}
		else
		{					
			leftWing.setPosition(x-AirplaneRadius-1, y, x-AirplaneRadius-20, y);
			rightWing.setPosition(x+AirplaneRadius+1, y, x+AirplaneRadius+20, y);
			rudder.setPosition(x, y-AirplaneRadius-1, x, y-AirplaneRadius-12);
		}			
	}
	

	public void SetViewAngle(double viewAngle)
	{
		mViewAngle = viewAngle;
		mViewAngleHalf = mViewAngle*0.5;
		mTanOfViewAngle = Math.tan(MathX.toRadians*(mViewAngleHalf));
		mScrollingItemList.SetWindowSize(viewAngle);
	}
	
	
	public void CreateLabels(double viewAngle)
	{

		final double stepSize = 5.0;
		mScrollingItemList = new WindowedScrollingItemListD(stepSize);			
		SetViewAngle( viewAngle);
		
		
		final int count = (int) Math.ceil( viewAngle/stepSize);
		
		pitchMarkerAboveBelow = new PitchMarkerAboveBelow[count];
		for (int i=0; i<pitchMarkerAboveBelow.length; i++)
		{
			pitchMarkerAboveBelow[i] = new PitchMarkerAboveBelow(mScene, mFont);
			pitchMarkerAboveBelow[i].Create();
		}		
		
	}
	
	public void UpdateLists(final double windowCenter)
	{
		mScrollingItemList.GetItems( windowCenter, mValuesInView);	
	
	}	

	
	public void Draw(double roll, double pitch) {
		
		UpdateLists(pitch);		
		
		
		{
			int iPitchMarker=0;
			int iHorizon=0;

			for (int i=0;i<mValuesInView.size(); i++)
			{
				
				final double key=mValuesInView.get(i).x;
				double value=0-mValuesInView.get(i).y;

				final double tanOfCurAngle = Math.tan(MathX.toRadians*(key-(mViewAngleHalf)));
				final double ratio = tanOfCurAngle/mTanOfViewAngle;
				final double horzPos = MainActivity.CAMERA_HEIGHT_HALF*(1.0+ratio);
				
				if (value>90)
					value=value-2*(value-90);
				else if (value <-90)
					value=value-2*(value+90);
				
			
				if (value == 0)
				{
					// horizon					
					attitudeHorizon.Draw(
							MainActivity.CAMERA_WIDTH_HALF, 
							horzPos,
							roll
					);			
					iHorizon++;
					
				} else 
				{

					// above or below
					if (iPitchMarker < pitchMarkerAboveBelow.length )
					{
						pitchMarkerAboveBelow[iPitchMarker].Draw(
								MainActivity.CAMERA_WIDTH_HALF, 
								horzPos,
								(int)Math.round(value),
								roll
						);					
						iPitchMarker++;
					}
				} 
				

			
				
			}
			if (iHorizon==0)
			{
				attitudeHorizon.Hide();
			}
			while (iPitchMarker < pitchMarkerAboveBelow.length)
			{
				pitchMarkerAboveBelow[iPitchMarker].Hide();
				iPitchMarker++;
			}
		

		}	
		
		
	}

}
