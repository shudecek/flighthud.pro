package com.apporilla.flighthud.Indicators;

import java.util.Iterator;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;

import com.apporilla.flighthud.MainActivity;

public class IndicatorAltitude extends ScrollingItemBase  {

	Line lineBase;
	Font font2;
	
	Line pointerTop;
	Line pointerBottom;

	private boolean mIsLandscape=false;
	
	private double yStart;
	private double yWidth;
	

	public IndicatorAltitude(Scene scene) {
		super(scene);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Create() {		
		mScrollingItemList = new ScrollingItemListD(100,20,100,550,0);	

		mMinorTicks = new Line[28];
		mMajorTicks = new Line[6];
		mLabels = new ChangeableText[6];
		mSubLabels = new ChangeableText[6];
		
		super.Create();
		lineBase = new Line(MainActivity.CAMERA_WIDTH-60,160,MainActivity.CAMERA_WIDTH-60,MainActivity.CAMERA_HEIGHT-160);
		lineBase.setLineWidth(1.0f);
		lineBase.setColor(0.0f,1.0f,0.0f);					
		mScene.attachChild(lineBase);	
		
		pointerTop = new Line(
				MainActivity.CAMERA_WIDTH-60,
				MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH-76,
				MainActivity.CAMERA_HEIGHT/2-8
		);
		pointerTop.setLineWidth(2.0f);
		pointerTop.setColor(0.0f,1.0f,0.0f);
		pointerBottom = new Line(
				MainActivity.CAMERA_WIDTH-60,
				MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH-76,
				MainActivity.CAMERA_HEIGHT/2+8
		);
		pointerBottom.setLineWidth(2.0f);
		pointerBottom.setColor(0.0f,1.0f,0.0f);		
		
		
		mScene.attachChild(pointerTop);	
		mScene.attachChild(pointerBottom);	
		
		
		
		
	}	
	
	public void Reorientate(final boolean pIsLandscape)
	{
		mIsLandscape = pIsLandscape;
		
		if (mIsLandscape)
		{
			yStart = 60;
			yWidth = (int)(MainActivity.CAMERA_WIDTH-80); // 2x yStart
		}
		else
		{
			yStart = 160;
			yWidth = (int)(MainActivity.CAMERA_HEIGHT-320); // 2x yStart
						
		}
		
		
		if (pIsLandscape)
		{
			lineBase.setPosition(
					(float)yStart,
					MainActivity.CAMERA_HEIGHT-60,
					(float)(yStart+yWidth),
					MainActivity.CAMERA_HEIGHT-60);
			pointerTop.setPosition(										
					(float)(yStart+yWidth/2),
					MainActivity.CAMERA_HEIGHT-60,					
					(float)(yStart+yWidth/2)-8,
					MainActivity.CAMERA_HEIGHT-76
			);
			pointerBottom.setPosition(					
					(float)(yStart+yWidth/2),
					MainActivity.CAMERA_HEIGHT-60,					
					(float)(yStart+yWidth/2)+8,
					MainActivity.CAMERA_HEIGHT-76
			);		
						
			
		}
		else
		{
			lineBase.setPosition(MainActivity.CAMERA_WIDTH-60,160,MainActivity.CAMERA_WIDTH-60,MainActivity.CAMERA_HEIGHT-160);
			pointerTop.setPosition(
					MainActivity.CAMERA_WIDTH-60,
					MainActivity.CAMERA_HEIGHT/2,
					MainActivity.CAMERA_WIDTH-76,
					MainActivity.CAMERA_HEIGHT/2-8
			);
			pointerBottom.setPosition(
					MainActivity.CAMERA_WIDTH-60,
					MainActivity.CAMERA_HEIGHT/2,
					MainActivity.CAMERA_WIDTH-76,
					MainActivity.CAMERA_HEIGHT/2+8
			);	
						
			
		}	
	}
	private String BigText(Integer altitude)
	{
		String s="";
		if (altitude<0)
			s = "-";
		altitude=altitude/1000;
		s += altitude.toString();
		return s;
	}	
	private String SmallText(Integer altitude)
	{
		if (altitude<0)
			altitude = 0-altitude;
		altitude=altitude%1000;
		altitude=altitude/100;
		return altitude.toString();
	}		
	
	@Override
	public void Draw(double windowCenter) {
		if (windowCenter < 0)
			windowCenter=0;

		mWindowCenter = windowCenter;
		UpdateLists(windowCenter);
		


		
		// major ticks

		{
			int i=0;
			Iterator<Double> itr = mMajorHashList.iterator();
			
			while (itr.hasNext())
			{
				final Double value = itr.next();
				if (i<mMajorTicks.length)
				{
					if (mIsLandscape)
					{
						mMajorTicks[i].setPosition(
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),
								(float)MainActivity.CAMERA_HEIGHT-60,
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),															
								(float)MainActivity.CAMERA_HEIGHT-45
								);							
					}
					else
					{
						mMajorTicks[i].setPosition(
								(float)MainActivity.CAMERA_WIDTH-60,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),							
								(float)MainActivity.CAMERA_WIDTH-45, 
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize))							
								);								
					}
			
				}
				else
					break;
				
				i++;
				
			}
			while (i<mMajorTicks.length)
			{
				mMajorTicks[i].setPosition(0, 0, 0, 0);
				i++;
			}				
		}
		
		// minor ticks
		{
			int i=0;
			Iterator<Double> itr = mMinorHashList.iterator();
			
			while (itr.hasNext())
			{
				final Double value = itr.next();
				if (i<mMinorTicks.length)
				{
					if (mIsLandscape)
					{
						mMinorTicks[i].setPosition(
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),
								(float)MainActivity.CAMERA_HEIGHT-60,
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),															
								(float)MainActivity.CAMERA_HEIGHT-56
								);								
					}
					else
					{
						mMinorTicks[i].setPosition(
								(float)MainActivity.CAMERA_WIDTH-60,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),
								(float)MainActivity.CAMERA_WIDTH-56, 
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize))							
								);							
					}
			
				}
				else
					break;
				
				i++;
				
			}
			while (i<mMinorTicks.length)
			{
				mMinorTicks[i].setPosition(0, 0, 0, 0);
				i++;
			}			
				
		}		
	
		// update labels
		{
			int iAltitudeLabel=0;
			for (int i=0;i<mLabelsList.size(); i++)
			{
				final double key=mLabelsList.get(i).x;
				final double value=mLabelsList.get(i).y;	

				if (iAltitudeLabel< mLabels.length)
				{
					mLabels[iAltitudeLabel].setText(BigText((int)Math.round( value)));					
					final float height = mLabels[iAltitudeLabel].getHeight();
					final float width = mLabels[iAltitudeLabel].getWidth();
					mSubLabels[iAltitudeLabel].setText(SmallText((int)Math.round(value)));
					mLabels[iAltitudeLabel].setRotationCenter(width*0.5f, height*0.5f);
					mLabels[iAltitudeLabel].setScaleCenter(width*0.5f, height*0.5f);
					mSubLabels[iAltitudeLabel].setRotationCenter(width*0.5f, height*0.5f);
					mSubLabels[iAltitudeLabel].setScaleCenter(width*0.5f, height*0.5f);
					
					
					if (mIsLandscape)
					{
						
						mLabels[iAltitudeLabel].setRotation(90); 						
						
						mLabels[iAltitudeLabel].setPosition(
								(float)((yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(width*0.5)),
								(float)(MainActivity.CAMERA_HEIGHT-(57)+(width*0.5))
								);		

						mSubLabels[iAltitudeLabel].setRotation(90);
						
						mSubLabels[iAltitudeLabel].setPosition(
								(float)((yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(width*0.5)-3),
								(float)MainActivity.CAMERA_HEIGHT-(57)+(mLabels[i].getWidth())*1.5f+3
								);							
					}
					else
					{
						mLabels[iAltitudeLabel].setRotation(0); 						
						
						mLabels[iAltitudeLabel].setPosition(
								(float)MainActivity.CAMERA_WIDTH-(43),
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(height*0.5))
								);
						
						mSubLabels[iAltitudeLabel].setRotation(0); 						

						mSubLabels[iAltitudeLabel].setPosition(
								(float)MainActivity.CAMERA_WIDTH-(43)+mLabels[i].getWidth()+3,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(height*0.5)+2)
								);								
					}
	
					
				}
				else
					break;
				iAltitudeLabel++;
			}
			while (iAltitudeLabel<mLabels.length)
			{
				mLabels[iAltitudeLabel].setText("");
				mSubLabels[iAltitudeLabel].setText("");
				iAltitudeLabel++;
			}
			
		}
	

		
	}
}
