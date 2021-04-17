package com.apporilla.flighthud;

import java.util.ArrayList;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;

import android.graphics.Point;

public class HudHelper {

	public enum HudType {None,F16};
	
	public ArrayList<HudItem> aObjects = null;
	
	public static Point GetHorizonCenter(int cameraWidth, int cameraHeight, double pitch)
	{
		
		final double screenAngle = 70.0; // how many degrees the screen height takes up
		
		double x = (cameraHeight*pitch)/(screenAngle);
		
		
		return new Point( cameraWidth/2,(cameraHeight/2)-(int)x);
	}
	
	public void Init()
	{
		aObjects = new ArrayList<HudItem>();
	}
	
	public void Load(HudType hudType)
	{
		switch(hudType)
		{
			case F16:
			{
				// center "plane"
				
				aObjects.add(new HudItem(true, new Rectangle(-0.1f, -0.1f, 0.2f, 0.2f),0));
				aObjects.add(new HudItem(true, new Line(-0.1f, 0, -0.2f, 0),0));
				aObjects.add(new HudItem(true, new Line(0.1f, 0, 0.2f, 0),0));
				aObjects.add(new HudItem(true, new Line(0.0f, -0.1f, 0.0f, -0.15f),0));
				
				
				// horizon
				aObjects.add(new HudItem(false, new Line(-0.2f,0f,-0.90f,0.0f),0));
				aObjects.add(new HudItem(false, new Line(0.2f,0f,0.90f,0.0f),0));
				
			
				break;
			}
		}
	}
	
	
}
