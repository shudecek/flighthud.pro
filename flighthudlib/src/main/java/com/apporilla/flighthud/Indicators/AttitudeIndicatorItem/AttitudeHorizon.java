package com.apporilla.flighthud.Indicators.AttitudeIndicatorItem;


import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;


import com.apporilla.flighthud.MainActivity;
import com.apporilla.flighthud.Utils.MathX;
import com.apporilla.flighthud.Utils.PointD;
import com.apporilla.flighthud.Utils.PointRotator;

public class AttitudeHorizon extends AttitudeIndicatorItemBase {
	public Line left;
	public Line right;
	public final float AirplaneWingClearance = 29;
	final double ox = MainActivity.CAMERA_WIDTH/2;
	final double oy = MainActivity.CAMERA_HEIGHT/2;	
	private PointD[] p;
	private PointRotator pr;
	
	public AttitudeHorizon(Scene scene)
	{
		mType = AttitudeIndicatorItemType.Horizon;
		mScene = scene;	
	}
	
	@Override
	public void Create()
	{
		left = new Line(0,0,0,0);
		right = new Line(0,0,0,0);

		left.setLineWidth(2);
		left.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(left);	
		
		right.setLineWidth(2);
		right.setColor(0.0f,1.0f,0.0f);
		mScene.attachChild(right);	
		
		p = new PointD[4];
		p[0] = new PointD();
		p[1] = new PointD();
		p[2] = new PointD();
		p[3] = new PointD();
		pr = new PointRotator(ox,oy);
		
	}
	

	
	public void Draw(final double x, final double y, final double rotation)
	{
		final int width = MainActivity.CAMERA_WIDTH/2;

		p[0].set(x-width,y);
		p[1].set(x-AirplaneWingClearance,y);
		p[2].set(x+width,y);
		p[3].set(x+AirplaneWingClearance,y);
		
		pr.rotateRadians(p, (0-rotation)*MathX.toRadians);
		left.setPosition((float)p[0].x, (float)p[0].y, (float)p[1].x, (float)p[1].y);
		right.setPosition((float)p[2].x, (float)p[2].y, (float)p[3].x, (float)p[3].y);
		
		left.setVisible(true);
		right.setVisible(true);	
	}
	
	public void Hide()
	{
		left.setVisible(false);
		right.setVisible(false);		
	}	
}
