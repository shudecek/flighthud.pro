package com.apporilla.flighthud.Utils;

import java.util.ArrayList;

public class PointRotator {
	// point to rotate around
	public double ox;
	public double oy;
	
	public PointRotator(double ox, double oy)
	{
		this.ox=ox;
		this.oy=oy;
	}
	public PointRotator(PointD p)
	{
		this.ox = p.x;
		this.oy = p.y;
	}
	
	// copy to new point
	public void rotateDegrees(final double px, final double py, final double angle, PointD output)
	{
		double rotx = Math.cos(angle*MathX.toRadians) * (px-ox) - Math.sin(angle*MathX.toRadians) * (py-oy) + ox;
		double roty = Math.sin(angle*MathX.toRadians) * (px-ox) + Math.cos(angle*MathX.toRadians) * (py-oy) + oy;
		output.x = rotx;
		output.y = roty;
	}	
	
	// modify in place
	public void rotateDegrees(PointD p,final double angle)
	{
		final double x = Math.cos(angle*MathX.toRadians) * (p.x-ox) - Math.sin(angle*MathX.toRadians) * (p.y-oy) + ox;
		final double y = Math.sin(angle*MathX.toRadians) * (p.x-ox) + Math.cos(angle*MathX.toRadians) * (p.y-oy) + oy;
		p.x = x;
		p.y = y;
	}	
	
	public void rotateDegrees(PointD[] points, final double angle)	
	{
		final double sinAngle = Math.sin(angle*MathX.toRadians);
		final double cosAngle = Math.cos(angle*MathX.toRadians);
		
		for (int i=0; i<points.length; i++)
		{
			final double x = cosAngle * (points[i].x-ox) - sinAngle * (points[i].y-oy) + ox;
			final double y = sinAngle * (points[i].x-ox) + cosAngle * (points[i].y-oy) + oy;
			points[i].x = x;
			points[i].y = y;
		}
	}
	
	public void rotateDegrees(PointD[] points, final double angle, final int count)	
	{
		final double sinAngle = Math.sin(angle*MathX.toRadians);
		final double cosAngle = Math.cos(angle*MathX.toRadians);
		
		for (int i=0; i<count; i++)
		{
			final double x = cosAngle * (points[i].x-ox) - sinAngle * (points[i].y-oy) + ox;
			final double y = sinAngle * (points[i].x-ox) + cosAngle * (points[i].y-oy) + oy;
			points[i].x = x;
			points[i].y = y;
		}
	}	
	
	public void rotateRadians(PointD[] points, final double angle)
	{
		final double sinAngle = Math.sin(angle);
		final double cosAngle = Math.cos(angle);
		
		for (int i=0; i<points.length; i++)
		{
			final double x = cosAngle * (points[i].x-ox) - sinAngle * (points[i].y-oy) + ox;
			final double y = sinAngle * (points[i].x-ox) + cosAngle * (points[i].y-oy) + oy;
			points[i].x = x;
			points[i].y = y;
		}		
	}

	public void rotateRadians(PointD[] points, final double angle, final int count)	
	{
		final double sinAngle = Math.sin(angle);
		final double cosAngle = Math.cos(angle);	
		
		for (int i=0; i<count; i++)
		{
			final double x = cosAngle * (points[i].x-ox) - sinAngle * (points[i].y-oy) + ox;
			final double y = sinAngle * (points[i].x-ox) + cosAngle * (points[i].y-oy) + oy;
			points[i].x = x;
			points[i].y = y;
		}
	}		
	
}
