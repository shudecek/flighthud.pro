package com.apporilla.flighthud.Indicators;

import java.util.Iterator;
import java.util.Map;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;

import com.apporilla.flighthud.MainActivity;


public class IndicatorCompass extends ScrollingItemBase {

	Line pointerLeft;
	Line pointerRight;
	private boolean mIsLandscape;
	public static final String [] aDirections = {
		"N","010","020","030","040","050","060","070","080",
		"E","100","110","120","130","140","150","160","170",
		"S","190","200","210","220","230","240","250","260",
		"W","280","290","300","310","320","330","340","350"};
	//ScrollingItemListD mScrollingItemList;
	
	public IndicatorCompass(Scene scene) {
		super(scene);
		
	}

	@Override
	public void Create() {		
		mScrollingItemList = new ScrollingItemListD(10,10,10,37,5);

		mMinorTicks = new Line[4];
		mMajorTicks = new Line[4];
		mLabels = new ChangeableText[4];
		mLabelValues = new int[4];
		
		super.Create();
		
		pointerLeft = new Line(
				MainActivity.CAMERA_WIDTH/2,
				80,
				MainActivity.CAMERA_WIDTH/2-8,				
				96
		);
		pointerLeft.setLineWidth(2.0f);
		pointerLeft.setColor(0.0f,1.0f,0.0f);
		pointerRight = new Line(
				MainActivity.CAMERA_WIDTH/2,
				80,
				MainActivity.CAMERA_WIDTH/2+8,				
				96
		);
		pointerRight.setLineWidth(2.0f);
		pointerRight.setColor(0.0f,1.0f,0.0f);
		
		mScene.attachChild(pointerLeft);	
		mScene.attachChild(pointerRight);	
		mLabelValues[0]=Integer.MIN_VALUE;
		mLabelValues[1]=Integer.MIN_VALUE;
		mLabelValues[2]=Integer.MIN_VALUE;
		mLabelValues[3]=Integer.MIN_VALUE;
		
		
	}

	private String GetPaddedDirection(int direction)
	{
		
		if (direction < 0)
			direction += 360;
		else if (direction >= 360)
			direction -= 360;
		
		return aDirections[direction/10];
		
	
	}
	
	public void Reorientate(final boolean pIsLandscape)
	{
		mIsLandscape = pIsLandscape;
		
		if (pIsLandscape)
		{
			pointerLeft.setPosition(
				MainActivity.CAMERA_WIDTH-60,
				MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH-76,
				MainActivity.CAMERA_HEIGHT/2-8				
			);	
			pointerRight.setPosition(
				MainActivity.CAMERA_WIDTH-60,
				MainActivity.CAMERA_HEIGHT/2,
				MainActivity.CAMERA_WIDTH-76,
				MainActivity.CAMERA_HEIGHT/2+8	
			);			
		}
		else
		{
			pointerLeft.setPosition(
				MainActivity.CAMERA_WIDTH/2,
				80,
				MainActivity.CAMERA_WIDTH/2-8,				
				96
			);	
			pointerRight.setPosition(
				MainActivity.CAMERA_WIDTH/2,
				80,
				MainActivity.CAMERA_WIDTH/2+8,				
				96
			);
		}
		
	}
	
	
	@Override
	public void Draw(double windowCenter) {
		if (windowCenter < mScrollingItemList.windowSize*0.5)
			windowCenter += 360.0;

		mWindowCenter = windowCenter;		
		UpdateLists(windowCenter);
		
		final double xStart;
		final double xWidth;

		
		if (mIsLandscape)
		{
			xStart = (MainActivity.CAMERA_HEIGHT*0.25);
			xWidth = (MainActivity.CAMERA_HEIGHT*0.50);
		}
		else
		{
			xStart = (MainActivity.CAMERA_WIDTH*0.0);
			xWidth = (MainActivity.CAMERA_WIDTH*1.00);
			
		}

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
								(float)MainActivity.CAMERA_WIDTH-40.0f,
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),							
								(float)MainActivity.CAMERA_WIDTH-60.0f,
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize)							
								);
							
						
					}
					else
					{
						mMajorTicks[i].setPosition(
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),							
								60.0f, 
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),							
								80.0f);							
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
								(float)MainActivity.CAMERA_WIDTH-50.0f,
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),
								(float)MainActivity.CAMERA_WIDTH-60.0f,
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize)							
								);								
					}
					else
					{
						mMinorTicks[i].setPosition(
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),
								70.0f, 
								(float)(xStart+((value)*xWidth)*mScrollingItemList.oneOverWindowSize),							
								80.0f);								
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

			int iCompassLabel=0;

			for (int i=0;i<mLabelsList.size(); i++)
			{
				final double key=mLabelsList.get(i).x;
				final double value=mLabelsList.get(i).y;				
				
				if (iCompassLabel< mLabels.length)
				{
					final int iValue = (int)Math.round(value);
					if (iValue != mLabelValues[iCompassLabel])
						mLabels[iCompassLabel].setText(GetPaddedDirection(iValue));		
					
					
					final float width = mLabels[iCompassLabel].getWidth();
					final float height = mLabels[iCompassLabel].getHeight();
					
					mLabels[iCompassLabel].setRotationCenter(width*0.5f, height*0.5f);
					mLabels[iCompassLabel].setScaleCenter(width*0.5f, height*0.5f);
	
					
					if (mIsLandscape)
					{
						mLabels[iCompassLabel].setRotation(90);
						mLabels[iCompassLabel].setPosition(
								(float)MainActivity.CAMERA_WIDTH-25.0f-(width*0.5f),
								(float)((xStart+((key)*xWidth)*mScrollingItemList.oneOverWindowSize)-22/*(height*0.5f)-2*/)
								);								
					}
					else
					{
						mLabels[iCompassLabel].setRotation(0);
						mLabels[iCompassLabel].setPosition(
								(float)((xStart+((key)*xWidth)*mScrollingItemList.oneOverWindowSize)-(width*0.5)),
								30.0f);						
					}

					mLabelValues[iCompassLabel] = iValue;
					iCompassLabel++;
				}
				else
					break;
				
			}
			while (iCompassLabel<mLabels.length)
			{
				mLabels[iCompassLabel].setText("");
				mLabelValues[iCompassLabel]=Integer.MIN_VALUE;
				iCompassLabel++;
			}			
		}
		
	
	}

}
