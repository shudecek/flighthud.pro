package com.apporilla.flighthud.Indicators;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;

import com.apporilla.flighthud.MainActivity;

public class IndicatorZoom {
	Scene mScene;
	Line line;
	boolean mVisible;
	
	public IndicatorZoom(Scene pScene)
	{
		mScene = pScene;
	}
	
	public void Create() 
	{
		line = new Line(0, 0, 0, 0);
		line.setLineWidth(2);
		line.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(line);		
	}
	
	public void SetVisibility(boolean pVisbile)
	{
		mVisible = pVisbile;
		line.setVisible(pVisbile);
	}
	
	public void Draw(double value)
	{
		if (mVisible)
		{			
			line.setPosition((float)(MainActivity.CAMERA_WIDTH*value), (float)(MainActivity.CAMERA_HEIGHT*0.8), (float)(MainActivity.CAMERA_WIDTH*value), (float)(MainActivity.CAMERA_HEIGHT*0.8-20));
			
		}
	}
	
}
