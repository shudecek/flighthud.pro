package com.apporilla.flighthud;


import java.io.IOException;
import java.util.List;

import org.anddev.andengine.util.Debug;

import com.apporilla.flighthud.Utils.MathX;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Nicolas Gramlich
 * @since 21:38:21 - 24.05.2010
 */
class CameraPreviewSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SurfaceHolder mSurfaceHolder;
	public Camera mCamera;
	public int mHeight;
	public int mWidth;
	public List<Integer> mZoomRatios;
	public Context mContext;


	// ===========================================================
	// Constructors
	// ===========================================================

	public CameraPreviewSurfaceView(final Context pContext) {
		super(pContext);
		mContext = pContext;
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void surfaceCreated(final SurfaceHolder pSurfaceHolder) {
		try {
			this.mCamera = Camera.open();
		}
		catch (Exception e)
		{
			Log.d("Camera","Camera.open() failed");
			
			((Activity)mContext).finish();
			return;
		}
		try {
			this.mCamera.setPreviewDisplay(pSurfaceHolder);
		} catch (IOException e) {
			Debug.e("Error in Camera.setPreviewDisplay", e);
		}
	}

	public void surfaceDestroyed(final SurfaceHolder pSurfaceHolder) {
		if (mCamera != null)
		{
			this.mCamera.stopPreview();
			this.mCamera.release();
			this.mCamera = null;
		}
	}

	public void setZoom(int pZoom)
	{
		final Camera.Parameters p = this.mCamera.getParameters();
		if (p.isZoomSupported())
		{
			p.setZoom(pZoom);
			this.mCamera.setParameters(p);

		}
		
		
	}
	
	// returns zoom*100
	/*
	public int getZoom()
	{
		int zoom = 100;
		
		final Camera.Parameters p = this.mCamera.getParameters();
		if (p.isZoomSupported() && mZoomRatios != null)
		{
			final int zoomIndex = p.getZoom();
			if (zoomIndex < mZoomRatios.size())
			{
				zoom = mZoomRatios.get(zoomIndex);
			}
			//Log.d("zoom","ix:" + zoomIndex + " z:"+zoom);
		}				
		
		return zoom;
	}
	*/
	
	public void surfaceChanged(final SurfaceHolder pSurfaceHolder, final int pPixelFormat, final int pWidth, final int pHeight) {		
		if (mCamera != null)
		{
			final Camera.Parameters parameters = this.mCamera.getParameters();
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes(); 
			Camera.Size cs = previewSizes.get(0);
			mHeight = cs.height; 
			mWidth = cs.width;
	
			mCamera.setDisplayOrientation(90);
			parameters.setPreviewSize(cs.width,cs.height);
			parameters.setPreviewFormat(ImageFormat.NV21);
			this.mCamera.setParameters(parameters);
			if (parameters.isZoomSupported())
				mZoomRatios = parameters.getZoomRatios();	
			
			this.mCamera.startPreview();
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}