package com.apporilla.flighthud;



import org.anddev.andengine.opengl.view.ComponentSizeChooser;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

	/**
	 * @author Nicolas Gramlich
	 * @since 21:38:32 - 24.05.2010
	 */
	public abstract class BaseAugmentedRealityGameActivity2 extends BaseGameActivity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		public CameraPreviewSurfaceView mCameraPreviewSurfaceView;


		// ===========================================================
		// Constructors
		// ===========================================================
		
		@Override
		protected void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);
			this.addContentView(this.mCameraPreviewSurfaceView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			this.mRenderSurfaceView.setZOrderMediaOverlay(true);

			this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
			//this.mRenderSurfaceView.setZOrderOnTop(true);
			//this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
			this.mRenderSurfaceView.bringToFront();

			onCreateCompleted();
		}
		
		protected void onCreateCompleted()
		{
			
		}
		
	    @Override
	    protected void onResume()
	    {
	    	super.onResume();
	    	
   	
	    }
		
		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		
		@Override
		protected void onPause() {
			super.onPause();
			finish();
		}

		protected int getLayoutID() {
			// TODO Auto-generated method stub
			return 0;
		}

		protected int getRenderSurfaceViewID() {
			// TODO Auto-generated method stub
			return 0;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}