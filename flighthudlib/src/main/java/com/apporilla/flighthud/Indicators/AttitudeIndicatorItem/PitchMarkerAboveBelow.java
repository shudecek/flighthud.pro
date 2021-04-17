package com.apporilla.flighthud.Indicators.AttitudeIndicatorItem;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;

import android.util.Log;

import com.apporilla.flighthud.MainActivity;
import com.apporilla.flighthud.Utils.MathX;
import com.apporilla.flighthud.Utils.PointD;
import com.apporilla.flighthud.Utils.PointRotator;

public class PitchMarkerAboveBelow extends AttitudeIndicatorItemBase  {
	public ChangeableText textLeft;
	public ChangeableText textRight;	
	public Line baseLeft;
	public Line baseRight;
	public Line vertLeft;
	public Line vertRight;
	public Line baseLeft2;
	public Line baseRight2;	
	public final float AirplaneWingClearance = 29;
	public final float BigHashExtent = 80;
	public final float SmallHashExtent = 45;
	public final float SmallHash2Start = 64;
	
	final double ox = MainActivity.CAMERA_WIDTH/2;
	final double oy = MainActivity.CAMERA_HEIGHT/2;		
	
	private PointRotator pr;
	private PointD[] p;
	private int mLastValue=Integer.MIN_VALUE;
	private double textWidthHalf=Double.MAX_VALUE;
	private double textHeightHalf=Double.MAX_VALUE;

	public PitchMarkerAboveBelow(Scene scene, Font font)
	{
		mType = AttitudeIndicatorItemType.Above;
		mScene = scene;		
		mFont = font;
	}	
	
	
	@Override
	public void Create()
	{
		
		textLeft = new ChangeableText(0,0, mFont,"", 10);
		mScene.attachChild(textLeft);		
		textRight = new ChangeableText(0,0, mFont,"", 10);
		mScene.attachChild(textRight);	
		
		baseLeft = new Line(0,0,0,0);
		baseRight = new Line(0,0,0,0);
		vertLeft = new Line(0,0,0,0);
		vertRight = new Line(0,0,0,0);
		baseLeft2 = new Line(0,0,0,0);
		baseRight2 = new Line(0,0,0,0);		
		
		baseLeft.setLineWidth(1);
		baseLeft.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(baseLeft);	
		
		baseRight.setLineWidth(1);
		baseRight.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(baseRight);	
		
		vertLeft.setLineWidth(1);
		vertLeft.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(vertLeft);	
		
		vertRight.setLineWidth(1);
		vertRight.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(vertRight);		
		
		baseLeft2.setLineWidth(1);
		baseLeft2.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(baseLeft2);	
		
		baseRight2.setLineWidth(1);
		baseRight2.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(baseRight2);			
		
		pr = new PointRotator(ox,oy);
		p = new PointD[14];
		p[0] = new PointD();
		p[1] = new PointD();
		p[2] = new PointD();
		p[3] = new PointD();
		p[4] = new PointD();
		p[5] = new PointD();
		p[6] = new PointD();
		p[7] = new PointD();		
		p[8] = new PointD();
		p[9] = new PointD();
		p[10] = new PointD();
		p[11] = new PointD();
		p[12] = new PointD();
		p[13] = new PointD();			
		
	}
	
	public void Draw(final double x, final double y, final int value, final double rotation)
	{
		
		if (value != mLastValue)
		{		
			final String text = String.format("%d", value);
			textLeft.setText(text);
			textRight.setText(text);	
			textWidthHalf = textLeft.getWidth()*0.5;
			textHeightHalf = textLeft.getHeight()*0.5;			
		}

		mLastValue = value;
		
		
		if (value > 0)
		{

			p[0].set(x-AirplaneWingClearance,y);
			p[1].set(x-BigHashExtent,y);
			p[2].set(x+AirplaneWingClearance,y);
			p[3].set(x+BigHashExtent,y);		
			p[4].set(x-AirplaneWingClearance,y);
			p[5].set(x-AirplaneWingClearance,y+7);
			p[6].set(x+AirplaneWingClearance,y);
			p[7].set(x+AirplaneWingClearance,y+7);	
			p[8].set(x-BigHashExtent-textWidthHalf-3,y);
			p[9].set(x+BigHashExtent+textWidthHalf,y);	
			
			pr.rotateRadians(p, (0-rotation)*MathX.toRadians,10);

			
		} else
		{

			p[0].set(x-AirplaneWingClearance,y);
			p[1].set(x-SmallHashExtent,y);
			p[2].set(x+AirplaneWingClearance,y);
			p[3].set(x+SmallHashExtent,y);		
			p[4].set(x-AirplaneWingClearance,y);
			p[5].set(x-AirplaneWingClearance,y-7);
			p[6].set(x+AirplaneWingClearance,y);
			p[7].set(x+AirplaneWingClearance,y-7);	
			p[8].set(x-BigHashExtent-textWidthHalf-3,y);
			p[9].set(x+BigHashExtent+textWidthHalf,y);		
			
			p[10].set(x-BigHashExtent,y);
			p[11].set(x-SmallHash2Start,y);			
			p[12].set(x+BigHashExtent,y);
			p[13].set(x+SmallHash2Start,y);
			
			pr.rotateRadians(p, (0-rotation)*MathX.toRadians);

		}

		baseLeft.setPosition((float)p[0].x, (float)p[0].y, (float)p[1].x, (float)p[1].y);
		baseRight.setPosition((float)p[2].x, (float)p[2].y, (float)p[3].x, (float)p[3].y);	
		baseLeft.setVisible(true);
		baseRight.setVisible(true);
		
		vertLeft.setPosition((float)p[4].x, (float)p[4].y, (float)p[5].x, (float)p[5].y);
		vertRight.setPosition((float)p[6].x, (float)p[6].y, (float)p[7].x, (float)p[7].y);	
		vertLeft.setVisible(true);
		vertRight.setVisible(true);		
		
		p[8].x -= textWidthHalf;
		p[8].y -= textHeightHalf;
		p[9].x -= textWidthHalf;
		p[9].y -= textHeightHalf;
		
		textLeft.setPosition((float)p[8].x, (float)p[8].y);
		textRight.setPosition((float)p[9].x, (float)p[9].y);
		textLeft.setRotationCenter((float)textWidthHalf, (float)textHeightHalf);
		textRight.setRotationCenter((float)textWidthHalf, (float)textHeightHalf);	
		textLeft.setRotation(0.0f-(float)rotation);
		textRight.setRotation(0.0f-(float)rotation);
		textLeft.setVisible(true);
		textRight.setVisible(true);			
		
		if (value > 0)
		{
			baseLeft2.setVisible(false);
			baseRight2.setVisible(false);
			
		} else
		{
			baseLeft2.setPosition((float)p[10].x, (float)p[10].y, (float)p[11].x, (float)p[11].y);
			baseRight2.setPosition((float)p[12].x, (float)p[12].y, (float)p[13].x, (float)p[13].y);			
			baseLeft2.setVisible(true);
			baseRight2.setVisible(true);			
		}

		
	}
	
	public void Hide()
	{
		baseLeft.setVisible(false);
		baseRight.setVisible(false);	
		vertLeft.setVisible(false);
		vertRight.setVisible(false);	
		textLeft.setVisible(false);
		textRight.setVisible(false);
		baseLeft2.setVisible(false);
		baseRight2.setVisible(false);		
		
	}	
}
