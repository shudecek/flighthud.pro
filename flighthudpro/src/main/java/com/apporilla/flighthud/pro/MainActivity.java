package com.apporilla.flighthud.pro;


import org.anddev.andengine.opengl.view.ComponentSizeChooser;
import org.anddev.andengine.opengl.view.RenderSurfaceView;

import com.apporilla.sdk.Tools;

import android.graphics.PixelFormat;
import android.os.Bundle;


public class MainActivity extends com.apporilla.flighthud.MainActivity {


	@Override
	protected int getLayoutID()
	{
		return R.layout.main;
	}
	
	@Override
	protected int getRenderSurfaceViewID()
	{
		return R.id.mainGameView;
	}

	@Override
	protected void onCreateCompleted()
	{
		Tools.startSession(this,"HouseApps","3efAsS!6");
		//mStorageFolderName = "PilotHUD";		
	}
	
	protected void onSetContentView() {


		mRenderSurfaceView = new RenderSurfaceView(this);	
		mRenderSurfaceView.setEGLConfigChooser(new ComponentSizeChooser(4, 4, 4, 4, 16, 0)); 
		mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mRenderSurfaceView.setRenderer(this.mEngine);
		setContentView(this.mRenderSurfaceView, createSurfaceViewLayoutParams());

		
	}	
		

}