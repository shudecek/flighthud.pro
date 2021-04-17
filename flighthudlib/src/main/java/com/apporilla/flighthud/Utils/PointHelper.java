package com.apporilla.flighthud.Utils;

public class PointHelper {
	public static void rotateDegrees(final double px, final double py, final double ox, final double oy, final double angle, PointD output)
	{
		double rotx = Math.cos(angle*MathX.toRadians) * (px-ox) - Math.sin(angle*MathX.toRadians) * (py-oy) + ox;
		double roty = Math.sin(angle*MathX.toRadians) * (px-ox) + Math.cos(angle*MathX.toRadians) * (py-oy) + oy;
		output.x = rotx;
		output.y = roty;
	}	
	



	
}
