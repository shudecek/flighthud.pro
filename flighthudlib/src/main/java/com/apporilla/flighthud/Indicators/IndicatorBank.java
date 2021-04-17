package com.apporilla.flighthud.Indicators;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;

import com.apporilla.flighthud.MainActivity;

import android.graphics.PointF;

public class IndicatorBank {
	
	Scene mScene;
	Line lineZero;
	Line linePlus10;
	Line linePlus20;
	Line linePlus30;
	Line linePlus45;
	Line lineMinus10;
	Line lineMinus20;
	Line lineMinus30;
	Line lineMinus45;
	Line pointerLeft;
	Line pointerRight;	
	private boolean mIsLandscape;
	
	public IndicatorBank(Scene scene) {
		this.mScene = scene;
	}

	public PointF GetPointFromAngle( double distance, double angle)
	{
		final double x = Math.sin(Math.toRadians(angle));
		final double y = Math.cos(Math.toRadians(angle));
		return new PointF((float)(x*distance),(float)(y*distance));
	}
	
	public Line RadialLineBuilder(double startX, double startY, double distanceFromCenter, double angle, double length, double lineWidth)
	{
		//final PointF p1 = GetPointFromAngle(distanceFromCenter,angle);
		final double x1 = distanceFromCenter*Math.sin(Math.toRadians(angle));
		final double y1 = distanceFromCenter*Math.cos(Math.toRadians(angle));
		
		//final PointF p2 = GetPointFromAngle(distanceFromCenter+length,angle);
		final double x2 = (distanceFromCenter+length)*Math.sin(Math.toRadians(angle));
		final double y2 = (distanceFromCenter+length)*Math.cos(Math.toRadians(angle));
		
		
		final Line l = new Line((float)(startX+x1),(float)(startY+y1),(float)(startX+x2),(float)(startY+y2));
		l.setLineWidth((float)lineWidth);
		l.setColor(0.0f,1.0f,0.0f);
		return l;
	}
	
	public void RadialLineMover(final Line l, final double startX, final double startY, final double distanceFromCenter, final double angle, final double length)
	{
		final double x1 = distanceFromCenter*Math.sin(Math.toRadians(angle));
		final double y1 = distanceFromCenter*Math.cos(Math.toRadians(angle));
		
		final double x2 = (distanceFromCenter+length)*Math.sin(Math.toRadians(angle));
		final double y2 = (distanceFromCenter+length)*Math.cos(Math.toRadians(angle));
				
		l.setPosition((float)(startX+x1),(float)(startY+y1),(float)(startX+x2),(float)(startY+y2));
	}
	
	public Line SlantedRadialLineBuilder(double startX, double startY, double distanceFromCenter, double angle, double length, double angleOffset, double lineWidth)
	{
		final double x1 = distanceFromCenter*Math.sin(Math.toRadians(angle));
		final double y1 = distanceFromCenter*Math.cos(Math.toRadians(angle));		
		
		final double x2 = (distanceFromCenter+length)*Math.sin(Math.toRadians(angle+angleOffset));
		final double y2 = (distanceFromCenter+length)*Math.cos(Math.toRadians(angle+angleOffset));
		
		final Line l = new Line((float)(startX+x1),(float)(startY+y1),(float)(startX+x2),(float)(startY+y2));
		l.setLineWidth((float)lineWidth);
		l.setColor(0.0f,1.0f,0.0f);
		return l;				
	}
	
	public void SlantedRadialLineMover(final Line l, final double startX, final double startY, final double distanceFromCenter, final double angle, final double length, final double angle2)
	{
		final double x1 = distanceFromCenter*Math.sin(Math.toRadians(angle));
		final double y1 = distanceFromCenter*Math.cos(Math.toRadians(angle));		
		
		final double x2 = (distanceFromCenter+length)*Math.sin(Math.toRadians(angle2));
		final double y2 = (distanceFromCenter+length)*Math.cos(Math.toRadians(angle2));
		
		l.setPosition((float)(startX+x1),(float)(startY+y1),(float)(startX+x2),(float)(startY+y2));

		return;				
	}	
	
	
	public void Create() {
		final int x = MainActivity.CAMERA_WIDTH/2;
		final int y = MainActivity.CAMERA_HEIGHT-152;
		
		lineZero = RadialLineBuilder(x, y, 0, 0, 1, 1);
		linePlus10 = RadialLineBuilder(x,y, 0, 0,1, 1);
		linePlus20 = RadialLineBuilder(x,y, 0, 0,1, 1);
		linePlus30 = RadialLineBuilder(x,y, 0, 0,1, 1);
		linePlus45 = RadialLineBuilder(x,y, 0, 0,1, 1);
		lineMinus10 = RadialLineBuilder(x,y, 0,0,1, 1);
		lineMinus20 = RadialLineBuilder(x,y, 0,0,1, 1);
		lineMinus30 = RadialLineBuilder(x,y, 0,0,1, 1);
		lineMinus45 = RadialLineBuilder(x,y, 0,0,1, 1);
		
		pointerLeft  = SlantedRadialLineBuilder(x,y, 137,0,16, 4, 2);
		pointerRight  = SlantedRadialLineBuilder(x,y, 137,0,16, -4, 2);
		
						
		mScene.attachChild(lineZero);	
		mScene.attachChild(linePlus10);	
		mScene.attachChild(linePlus20);	
		mScene.attachChild(linePlus30);	
		mScene.attachChild(linePlus45);	
		mScene.attachChild(lineMinus10);	
		mScene.attachChild(lineMinus20);	
		mScene.attachChild(lineMinus30);	
		mScene.attachChild(lineMinus45);	
		mScene.attachChild(pointerLeft);	
		mScene.attachChild(pointerRight);
		
	}
	
	public void Reorientate(final boolean pIsLandscape)
	{
		mIsLandscape = pIsLandscape;
		
		if (pIsLandscape)
		{
			final int x = 152;
			final int y = MainActivity.CAMERA_HEIGHT/2;
			
			RadialLineMover(lineZero, x, y, 125, 270, 10);
			RadialLineMover(linePlus10, x,y, 130, 280,5);
			RadialLineMover(linePlus20, x,y, 130, 290,5);
			RadialLineMover(linePlus30, x,y, 125, 300,10);
			RadialLineMover(linePlus45, x,y, 125, 315,10);
			RadialLineMover(lineMinus10, x,y, 130,260,5);
			RadialLineMover(lineMinus20, x,y, 130,250,5);
			RadialLineMover(lineMinus30, x,y, 125,240,10);
			RadialLineMover(lineMinus45, x,y, 125,225,10);		
				
		}
		else
		{
			final int x = MainActivity.CAMERA_WIDTH/2;
			final int y = MainActivity.CAMERA_HEIGHT-152;
			
			RadialLineMover(lineZero, x, y, 125, 0, 10);
			RadialLineMover(linePlus10, x,y, 130, 10,5);
			RadialLineMover(linePlus20, x,y, 130, 20,5);
			RadialLineMover(linePlus30, x,y, 125, 30,10);
			RadialLineMover(linePlus45, x,y, 125, 45,10);
			RadialLineMover(lineMinus10, x,y, 130,-10,5);
			RadialLineMover(lineMinus20, x,y, 130,-20,5);
			RadialLineMover(lineMinus30, x,y, 125,-30,10);
			RadialLineMover(lineMinus45, x,y, 125,-45,10);		
			
		}
	}
	
	
	public void Draw(double angle)
	{
		final int x;
		final int y;	
		final int maxOffset;
		final int minOffset;
		
		if (mIsLandscape)
		{
			x = 152;
			y = MainActivity.CAMERA_HEIGHT/2;
			maxOffset=-40;
			minOffset=-140;
		}
		else
		{
			x = MainActivity.CAMERA_WIDTH/2;
			y = MainActivity.CAMERA_HEIGHT-152;	
			maxOffset = 50;
			minOffset = -50;
			
		}
		
		double rightOffset = angle+4;
		double leftOffset = angle-4;
		if (rightOffset > maxOffset)
			rightOffset = maxOffset;
		else if (rightOffset < minOffset)
			rightOffset = minOffset;
		if (leftOffset < minOffset)
			leftOffset = minOffset;
		else if (leftOffset > maxOffset)
			leftOffset = maxOffset;		
		if (angle>maxOffset)
			angle=maxOffset;
		else if (angle<minOffset)
			angle=minOffset;
		
		SlantedRadialLineMover(pointerLeft, x,y, 137,angle,16, rightOffset);
		SlantedRadialLineMover(pointerRight, x,y, 137,angle,16, leftOffset);
		
	
	}

}
