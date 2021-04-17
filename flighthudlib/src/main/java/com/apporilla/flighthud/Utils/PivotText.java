package com.apporilla.flighthud.Utils;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;

// Changeable text with maintains separate center point for horizontal and vertical rotation
public class PivotText extends ChangeableText {

	private float mXHoriz;
	private float mYHoriz;
	private float mXVert;
	private float mYVert;
	private boolean mIsLandscape;
	private HorizontalAlign mHorizontalAlign;
	private String mText;
	
	public PivotText(final float pXHoriz, final float pYHoriz, final float pXVert, final float pYVert, final Font pFont, final String pText, final int pCharactersMax, final HorizontalAlign pHorizontalAlign)
	{
		super(pXHoriz,pYHoriz,pFont,pText,pCharactersMax);

		mXHoriz=pXHoriz;
		mYHoriz=pYHoriz;
		mXVert=pXVert;
		mYVert=pYVert;
		mHorizontalAlign=pHorizontalAlign;
		
	}
	
	public void Reorientate(final boolean pIsLandscape)
	{
		mIsLandscape=pIsLandscape;

	}
	
	public void setText(String pText)
	{
		mText = pText;
		super.setText(pText);
		setRotationCenter(getWidth()*0.5f,getHeight()*0.5f);
		setScaleCenter(getWidth()*0.5f,getHeight()*0.5f);
		if (mIsLandscape==true)
		{					
			setRotation(90);	
			if (mHorizontalAlign == HorizontalAlign.LEFT)
				setPosition(mXHoriz-getWidth()*0.5f, mYHoriz+getWidth()*0.5f); 
			else if (mHorizontalAlign == HorizontalAlign.RIGHT)
				setPosition(mXHoriz-getWidth()*0.5f, mYHoriz-getWidth()*0.5f); 
			else
				setPosition(0,0); // TODO:  support other alignments
		}
		else
		{
			setRotation(0);
			setPosition(mXVert,mYVert);
		}		
		
	}
	
	public void resetText()
	{
		if ((mText != null) && (!mText.equals("")))
			setText(mText);
	}
}
