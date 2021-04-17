package com.apporilla.flighthud.Indicators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;

import com.apporilla.flighthud.Utils.PointD;

public abstract class ScrollingItemBase {
	ScrollingItemListD mScrollingItemList;
	
	Scene mScene;
	Line[] mMinorTicks;
	Line[] mMajorTicks;
	public ChangeableText[] mLabels;	
	public ChangeableText[] mSubLabels;
	double mWindowCenter;
	protected int[] mLabelValues;
	
	ArrayList<Double> mMinorHashList;
	ArrayList<Double> mMajorHashList;
	ArrayList<PointD> mLabelsList;
	
	public ScrollingItemBase(Scene scene)
	{
		this.mScene = scene;
		mMinorHashList = new ArrayList<Double>();
		mMajorHashList = new ArrayList<Double>();
		mLabelsList = new ArrayList<PointD>();
	}

	public void UpdateLists(final double windowCenter)
	{
		mScrollingItemList.GetNumbers( windowCenter, mLabelsList);
		mScrollingItemList.GetMajorHashes( windowCenter, mMajorHashList);
		mScrollingItemList.GetMinorHashes( windowCenter, mMinorHashList);		
	}
	
	public void Create()
	{
		for (int i=0;i<mMinorTicks.length; i++)
		{
			mMinorTicks[i] = new Line(0, 0, 0, 0);					
			mMinorTicks[i].setLineWidth(1.0f);
			mMinorTicks[i].setColor(0.0f,1.0f,0.0f);					
			mScene.attachChild(mMinorTicks[i]);			
		}
		
		for (int i=0;i<mMajorTicks.length; i++)
		{
			mMajorTicks[i] = new Line(0, 0, 0, 0);					
			mMajorTicks[i].setLineWidth(1.0f);
			mMajorTicks[i].setColor(0.0f,1.0f,0.0f);					
			mScene.attachChild(mMajorTicks[i]);			
		}
			
	}
	
	public void CreateLabels(Font font, ChangeableText[] labels )
	{
		for (int i=0;i<labels.length; i++)
		{
			labels[i] = new ChangeableText(0,0, font,"", 10);
			mScene.attachChild(labels[i]);
		}			
	}
	
	public abstract void Draw(double windowCenter);
	
}
