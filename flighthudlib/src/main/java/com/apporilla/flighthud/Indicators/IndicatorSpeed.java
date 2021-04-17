package com.apporilla.flighthud.Indicators;

import java.util.Iterator;
import java.util.Map;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;

import com.apporilla.flighthud.MainActivity;

public class IndicatorSpeed extends ScrollingItemBase  {
	
	Line lineBase;

	Line pointerTop;
	Line pointerBottom;	
	private boolean mIsLandscape;
	
	private double yStart;
	private double yWidth;
		
	
	public IndicatorSpeed(Scene scene) {
		super(scene);
		
	}

	

	@Override
	public void Create() {		
		mScrollingItemList = new ScrollingItemListD(10,10,10,46,5);	

		mMinorTicks = new Line[5];
		mMajorTicks = new Line[5];
		mLabels = new ChangeableText[5];
		
		super.Create();
		lineBase = new Line(60,160,60,MainActivity.CAMERA_HEIGHT-160);
		lineBase.setLineWidth(1.0f);
		lineBase.setColor(0.0f,1.0f,0.0f);					
		mScene.attachChild(lineBase);		
		
		pointerTop = new Line(
				60,
				MainActivity.CAMERA_HEIGHT/2,
				76,
				MainActivity.CAMERA_HEIGHT/2-8
		);
		pointerTop.setLineWidth(2.0f);
		pointerTop.setColor(0.0f,1.0f,0.0f);
		pointerBottom = new Line(
				60,
				MainActivity.CAMERA_HEIGHT/2,
				76,
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
					60,
					(float)(yStart+yWidth),
					60);
			pointerTop.setPosition(
					(float)(yStart+yWidth/2),
					60,
					(float)(yStart+yWidth/2)-8,
					76
			);
			pointerBottom.setPosition(
					(float)(yStart+yWidth/2),
					60,
					(float)(yStart+yWidth/2)+8,
					76	
			);				
		}
		else
		{
			lineBase.setPosition(60,160,60,MainActivity.CAMERA_HEIGHT-160);
			pointerTop.setPosition(
					60,
					MainActivity.CAMERA_HEIGHT/2,
					76,
					MainActivity.CAMERA_HEIGHT/2-8
			);
			pointerBottom.setPosition(
					60,
					MainActivity.CAMERA_HEIGHT/2,
					76,
					MainActivity.CAMERA_HEIGHT/2+8
			);			
		}
	}
	
	private String CleanText(Integer speed)
	{
/*		if (speed<0)
			return "";
		else*/
			return speed.toString();
	}
	
	@Override
	public void Draw(double windowCenter) {
		if (windowCenter < 0)
			windowCenter=0;

		mWindowCenter = windowCenter;
		UpdateLists(windowCenter);
		
		//final int yStart = 160;
		//final int yWidth = (int)(MainActivity.CAMERA_HEIGHT-320);
		


		
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
								60.0f,
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),							
								44.0f
								);							
					}
					else
					{
						mMajorTicks[i].setPosition(
								60.0f,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),							
								44.0f, 
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
								60.0f, 
								(float)((yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),
								50.0f
								);							
					}
					else
					{
						mMinorTicks[i].setPosition(
								60.0f,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((value)*yWidth)*mScrollingItemList.oneOverWindowSize)),
								50.0f, 
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
			int iSpeedLabel=0;
			for (int i=0;i<mLabelsList.size(); i++)
			{
				final double key=mLabelsList.get(i).x;
				final double value=mLabelsList.get(i).y;	
				
				if (iSpeedLabel< mLabels.length)
				{
					mLabels[iSpeedLabel].setText(CleanText((int)Math.round(value)));
								
					final float width = mLabels[iSpeedLabel].getWidth();
					final float height = mLabels[iSpeedLabel].getHeight();
					mLabels[iSpeedLabel].setRotationCenter(width*0.5f, height*0.5f);
					mLabels[iSpeedLabel].setScaleCenter(width*0.5f, height*0.5f);
					
				
					
					if (mIsLandscape)
					{
						mLabels[iSpeedLabel].setRotation(90); 
						mLabels[iSpeedLabel].setPosition(
								(float)((yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(width*0.5)),								
								(float)20-width*0.5f
								);								
					}
					else
					{
						mLabels[iSpeedLabel].setRotation(0); 
						mLabels[iSpeedLabel].setPosition(
								(float)40-width,
								(float)(MainActivity.CAMERA_HEIGHT-(yStart+((key)*yWidth)*mScrollingItemList.oneOverWindowSize)-(height*0.5))
								);							
					}
				
				}
				else
					break;
				iSpeedLabel++;
			}
			while (iSpeedLabel<mLabels.length)
			{
				mLabels[iSpeedLabel].setText("");
				iSpeedLabel++;
			}
			
		}
	

		
		
		
	}

}
