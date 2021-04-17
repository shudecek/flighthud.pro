package com.apporilla.flighthud;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.ScreenGrabber;
import org.anddev.andengine.entity.util.ScreenGrabber.IScreenGrabberCallback;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.util.HorizontalAlign;

import com.apporilla.flighthud.Indicators.IndicatorAltitude;
import com.apporilla.flighthud.Indicators.IndicatorBank;
import com.apporilla.flighthud.Indicators.IndicatorCompass;
import com.apporilla.flighthud.Indicators.IndicatorHorizon;
import com.apporilla.flighthud.Indicators.IndicatorSpeed;
import com.apporilla.flighthud.Indicators.IndicatorZoom;
import com.apporilla.flighthud.Utils.ListUtil;
import com.apporilla.flighthud.Utils.PivotText;
import com.apporilla.flighthud.metar.DBAdapterGeo;
import com.apporilla.flighthud.metar.MetarHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera.Parameters;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.hardware.Camera.PreviewCallback;
/*
 * 
 * TODO:
 * docs:
 * - screen shot which describes on screen items
 * 
 * 
 * backlog:
 * - process capture on new thread?
 * - options menu
 * - option to set camera preview size from available preview sizes
 * - allow background to be camera/color/or picture from gallery?
 * - add,update,delete,show bookmarked locations
 * - allow change of line thickness
 * - allow units changes to feet/meters, mph,kph,kts
 * - show labels for alt,kts,none
 * - opencv tracking (faces?), track anything moving across screen?
 * - option for hash marks every 5 or 10 degrees
 * - EVO 3D version
 * 
 * Known issues:
 * 
 * version 1.1:
 * 	- crash at getHVA(MainActivity.java:542) 
 *  - Galaxy Nexus shows black boxes around text elements
 * 
 * version 1.4:
 *  - zoom ratios returned in parameters.getZoomRatios() are about 10-20% off 
 *  	resulting in cg overlay not matching preview and preview not matching screen grab 
 */
public class MainActivity extends BaseAugmentedRealityGameActivity2 /*, IOnSceneTouchListener*/ {

		// ===========================================================
		// Constants
		// ===========================================================
		private static final String TAG = "PilotHUD";
		public static final int CAMERA_WIDTH = 480;
		public static final int CAMERA_HEIGHT = 720;
		public static final double CAMERA_WIDTH_HALF = (CAMERA_WIDTH*0.5);
		public static final double CAMERA_HEIGHT_HALF = (CAMERA_HEIGHT*0.5);
		
		public static final String VERSION = "1.5.6";
		public static final String COPYRIGHT = "Pilot HUD, Version " + VERSION + ", (c)2014 Apporilla.";

		public static final double CAMERA_SENSOR_DIAMETER = 4.456; // known iphone 3g value

		protected String mStorageFolderName="PilotHUD";
		private Context mContext = null;
		private IndicatorCompass mCompassIndicator;
		private IndicatorSpeed mSpeedIndicator;
		private IndicatorAltitude mAltitudeIndicator;
		private IndicatorBank mBankIndicator;
		private IndicatorHorizon mHorizonIndicator;
		private IndicatorZoom mZoomIndicator;
		private SensorHelper mSensorHelper;
		private LocationHelper mLocationHelper;
		private MetarHelper mMetarHelper;
		private float mCurrentZoomSlider; // 0=min 1=max
		private float mTouchX;
		private float mTouchY;
		private float mSliderTouchX;
		private float mSliderTouchY;
		private ScreenGrabber mScreenGrabber;
		private Bitmap mBitmapCaptureRender;
		private Bitmap mBitmapCaptureVideo;		
		
		private volatile int mGrabStatus; // 0=reset, 1=render, 2=video, 4=error
		private boolean bLandscape=false;
		private SoundHelper mSoundHelper;	
		private long mCaptureTimer;

		// ===========================================================
		// Fields
		// ===========================================================

		private Camera mCamera;


		private BitmapTextureAtlas mFontTexture;
		private BitmapTextureAtlas mFontTexture2;
		private BitmapTextureAtlas mFontTextureCompass;
		private BitmapTextureAtlas mFontTextureMetar;
		private Font mFontBig;
		private Font mFontSmall;
		private Font mFontCompass;
		private Font mFontMetar;
		
		private PivotText mLatitudeText;
		private PivotText mLongitudeText;
		private PivotText mTimeText;
		private PivotText mVersionText;
		private PivotText mMetarText;
		private ChangeableText mDebugText;
		


		
		private double mPitchLast=Double.MIN_VALUE;
		private double mRollLast=Double.MIN_VALUE;
		private double mYawLast=Double.MIN_VALUE;
		private double mSpeedLast=Double.MIN_VALUE;
		private double mAltitudeLast=Double.MIN_VALUE;
		private double mLatitudeLast=Double.MIN_VALUE;
		private double mLongitudeLast=Double.MIN_VALUE;
		private String mTimeLast="";
		private String mMetarStringLast="";
		
		private double mFocalLength;
		
		LinkedList<Double> qYaw;
		LinkedList<Double> qSpeed;
		LinkedList<Double> qAltitude;
		LinkedList<Double> qPitch;
		LinkedList<Double> qRoll;
		

		private int test=0;
		private boolean bKeyStatusCamera=false;
		private boolean bKeyStatusSearch=false;
		private boolean mCaptureInProgress=false;
	
		
		

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		
		@Override
		protected int getLayoutID() {

			return 0;
		}

		@Override
		protected int getRenderSurfaceViewID() {

			return 0;
		}
	
		
		@Override
		public Engine onLoadEngine() {
			//Toast.makeText(this, "If you don't see a sprite moving over the screen, try starting this while already being in Landscape orientation!!", Toast.LENGTH_LONG).show();
			this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
			this.mSensorHelper = new SensorHelper(this);

			return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera).setNeedsSound(true));
		}

		@Override
		public void onLoadResources() {
		//	this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//	this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/face_box.png", 0, 0);

		//	this.mEngine.getTextureManager().loadTexture(this.mTexture);
			this.mFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA); // 512x512 because it must be big enough for all text
			this.mFontTexture2 = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA); // 512x512 because it must be big enough for all text
			this.mFontTextureCompass = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mFontTextureMetar = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			
			this.mFontBig = new Font(this.mFontTexture, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 24, true, Color.GREEN);
			this.mFontSmall = new Font(this.mFontTexture2, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 16, true, Color.GREEN);
			this.mFontCompass = new Font(this.mFontTextureCompass, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 28, true, Color.GREEN);
			this.mFontMetar = new Font(this.mFontTextureMetar, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 15, true, Color.GREEN);
			
			this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
			this.mEngine.getTextureManager().loadTexture(this.mFontTexture2);
			this.mEngine.getTextureManager().loadTexture(this.mFontTextureCompass);
			this.mEngine.getTextureManager().loadTexture(this.mFontTextureMetar);
			
			this.mEngine.getFontManager().loadFont(this.mFontBig);				
			this.mEngine.getFontManager().loadFont(this.mFontSmall);
			this.mEngine.getFontManager().loadFont(this.mFontCompass);	
			this.mEngine.getFontManager().loadFont(this.mFontMetar);
			
	        DBAdapterGeo db = new DBAdapterGeo(this);    
	        
	        mCaptureTimer = System.currentTimeMillis();
	        
	        try {        	 
	        	db.createDataBase();

	        } catch (IOException ioe) { 
	        	throw new Error("Unable to create database");
	        }   				
		}

		@Override
		public Scene onLoadScene() {
			//this.mEngine.registerUpdateHandler(new FPSLogger());
            //final ScreenCapture screenCapture = new ScreenCapture();  
			
			mSoundHelper = new SoundHelper();
			mSoundHelper.Load(this.mEngine.getSoundManager(), this);
            
			final Scene scene = new Scene();
			
			
			
			//		scene.setBackgroundEnabled(false);
			scene.setBackground(new ColorBackground(0.0f, 0.0f, 0.0f, 0.0f));

		
			//final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
			//final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
			/*
			final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion);
			face.addShapeModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
			scene.getTopLayer().addEntity(face);
*/

			
			mCompassIndicator = new IndicatorCompass(scene);
			mCompassIndicator.Create();
			mCompassIndicator.CreateLabels(this.mFontCompass, mCompassIndicator.mLabels);
			mCompassIndicator.Reorientate(bLandscape);		
			
			qYaw = new LinkedList<Double>();
			qSpeed = new LinkedList<Double>();
			qAltitude = new LinkedList<Double>();
			qPitch = new LinkedList<Double>();
			qRoll = new LinkedList<Double>();
			
			mSpeedIndicator = new IndicatorSpeed(scene);
			mSpeedIndicator.Create();
			mSpeedIndicator.CreateLabels(this.mFontBig, mSpeedIndicator.mLabels);
			mSpeedIndicator.Reorientate(bLandscape);			
			
			mAltitudeIndicator = new IndicatorAltitude(scene);
			mAltitudeIndicator.Create();
			mAltitudeIndicator.CreateLabels(this.mFontBig, mAltitudeIndicator.mLabels);
			mAltitudeIndicator.CreateLabels(this.mFontSmall, mAltitudeIndicator.mSubLabels);
			mAltitudeIndicator.Reorientate(bLandscape);
			
			mBankIndicator = new IndicatorBank(scene);
			mBankIndicator.Create();
			mBankIndicator.Reorientate(bLandscape);
			
			mHorizonIndicator = new IndicatorHorizon(scene,this.mFontSmall);		
			Log.d("camera", "hva:" + getHVA() + " vva:" + getVVA());			
			mHorizonIndicator.Create();
			mHorizonIndicator.Reorientate(bLandscape);
			mHorizonIndicator.CreateLabels(getRenderedViewAngle());
			
			mZoomIndicator = new IndicatorZoom(scene);
			mZoomIndicator.Create();
			
			mFocalLength = getFocalLength();
			mLatitudeText = new PivotText(24,-10, 20, CAMERA_HEIGHT-16, this.mFontSmall, "", 16, HorizontalAlign.LEFT);
			mLongitudeText = new PivotText(40,-10, 20, CAMERA_HEIGHT-32, this.mFontSmall, "", 16, HorizontalAlign.LEFT);	
			mTimeText = new PivotText(24, CAMERA_HEIGHT-24, (int)(CAMERA_WIDTH*0.75), CAMERA_HEIGHT-16, this.mFontSmall, "", 32, HorizontalAlign.RIGHT);
			mVersionText = new PivotText(40,CAMERA_HEIGHT-24, (float)CAMERA_WIDTH*0.75f, (float)CAMERA_HEIGHT-32, this.mFontSmall, "v"+ VERSION, 16, HorizontalAlign.RIGHT);
			mMetarText = new PivotText(28,64, 10, CAMERA_HEIGHT-120, this.mFontMetar, "", 100, HorizontalAlign.LEFT);			
			mDebugText = new ChangeableText(5,600, this.mFontBig,"", 32);
			
			Reorientate(bLandscape);		
			
			scene.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() { }

				@Override
				public void onUpdate(final float pSecondsElapsed) {
					
					final double avgYaw =  ListUtil.GetFilteredAverageAngles(qYaw, 0.05);
					final double avgSpeed = ListUtil.GetFilteredAverage(qSpeed, 0.5);
					final double avgAltitude = ListUtil.GetFilteredAverage(qAltitude, 0.5);
					final double avgPitch = ListUtil.GetFilteredAverage(qPitch, 0.5);
					final double avgRoll = ListUtil.GetFilteredAverage(qRoll, 0.5);
					
										
					if (avgYaw != mYawLast)
						mCompassIndicator.Draw(avgYaw);
					if (avgSpeed != mSpeedLast)					
						mSpeedIndicator.Draw(avgSpeed);
					if (avgAltitude != mAltitudeLast)
						mAltitudeIndicator.Draw(avgAltitude);
					if (avgRoll != mRollLast)
						mBankIndicator.Draw(avgRoll);
					if ((avgRoll != mRollLast) || (avgPitch != mPitchLast))
							mHorizonIndicator.Draw(avgRoll, avgPitch);

					if (mLocationHelper.mLatitude != mLatitudeLast)
					{
						final String latText = String.format("%1.4f", mLocationHelper.mLatitude);
						mLatitudeText.setText(latText);
					}					
					if (mLocationHelper.mLongitude != mLongitudeLast)
					{
						final String longText = String.format("%1.4f", mLocationHelper.mLongitude);
						mLongitudeText.setText(longText);
					}		
					
					final String tempTimeText;
					if (bLandscape)
					{
						if (mLocationHelper.mTime.equals(""))
							tempTimeText = "";
						else
							tempTimeText=mLocationHelper.mTime+".";
					}
					else
						tempTimeText=mLocationHelper.mTime;
					
					if (!mTimeLast.equals(tempTimeText))
					{					
						mTimeText.setText(tempTimeText);							
					}
					
					if (mCameraPreviewSurfaceView.mZoomRatios != null)
						mZoomIndicator.Draw(mCurrentZoomSlider);
					
					mYawLast = avgYaw;
					mSpeedLast = avgSpeed;
					mAltitudeLast = avgAltitude;
					mRollLast = avgRoll;
					mPitchLast = avgPitch;
					mLatitudeLast = mLocationHelper.mLatitude;
					mLongitudeLast = mLocationHelper.mLongitude;
					mTimeLast = mLocationHelper.mTime;


		
		//mLatitudeText.setText("-118.3454");
		//mLongitudeText.setText("32.3455");
		//mTimeText.setText("00:00:00");		

		//mTimeText.setVisible(false);
		//mLatitudeText.setVisible(false);
		//mLongitudeText.setVisible(false);
		//mVersionText.setVisible(false);
				

					
					/*
					String sText = String.format("yaw:%1.4f", mYaw); 
					if (mDebug != null)
						mDebug.setText(sText);
						*/
				}

			
			});
			

			
			
			scene.registerUpdateHandler(new TimerHandler(3.0f, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {				
					if ((mLocationHelper.mLocationLastUpdated != Long.MAX_VALUE) && (mLocationHelper.mLocationLastUpdated > (mMetarHelper.mMetarTimestamp+120000)))
					{
						mMetarHelper.mMetarTimestamp = System.currentTimeMillis();
						mMetarHelper.getNearbyList(mLocationHelper.location);
						mMetarHelper.mMetarTimestamp = System.currentTimeMillis();
						
					}
					
					final String mMetarString = mMetarHelper.getMetarText(mLocationHelper.location);
					if (!mMetarString.equals(mMetarStringLast))
					{
						mMetarText.setText(mMetarString);
					}		
					mMetarStringLast = mMetarString;					
					
				}
			}));		
			
			scene.registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {				
				
					qYaw.add(mSensorHelper.mYaw);		
					TrimList(qYaw,7);
					qSpeed.add(mLocationHelper.mSpeed);
					TrimList(qSpeed,4);
					qAltitude.add(mLocationHelper.mAltitude);
					TrimList(qAltitude,4);
					qPitch.add(mSensorHelper.mPitch);
					TrimList(qPitch,7);
					qRoll.add(mSensorHelper.mRoll);
					TrimList(qRoll,7);
					
					/*										
					test+=1;
									qSpeed.add((double) test);
									TrimList(qSpeed,4);					
																		
					if ((test>100) && (test <= 200))
					{
						setZoom((test-100)/100.0);
					}
					
					
					test+=1;
					if (test<0)
						test+=360;
					else if (test>66360)
						test=0;
//					mAltitude.Draw(test);
//					mSpeed.Draw(test);
*/
				
					
					
							
					
				}


				
				private void TrimList(LinkedList<Double> linkedList, int maxLength)
				{
					while (linkedList.size() > maxLength)
						linkedList.poll();	
				}
				
			}));
			
			
			

			scene.attachChild(mLatitudeText);
			scene.attachChild(mLongitudeText);		
			scene.attachChild(mTimeText);
			scene.attachChild(mVersionText);
			scene.attachChild(mMetarText);
			scene.attachChild(mDebugText);
			scene.setTouchAreaBindingEnabled(true);
			scene.setOnSceneTouchListener(new IOnSceneTouchListener(){
				public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent)
				{						
					final int action = pSceneTouchEvent.getAction();
					final float currX = pSceneTouchEvent.getX();
					final float currY = pSceneTouchEvent.getY();
					if (action == TouchEvent.ACTION_DOWN)
					{
						mTouchX = currX;
						mTouchY = currY;
						if ((mTouchY >= CAMERA_HEIGHT*0.8) && (mTouchY <= CAMERA_HEIGHT))
						{
							mSliderTouchX = currX;
							mSliderTouchY = currY;
							mZoomIndicator.SetVisibility(true);
						}
						
					} else if (action == TouchEvent.ACTION_MOVE)
					{
						if ((mSliderTouchY > 0.0) && (mSliderTouchX > 0.0))
						{
							final float distance = currX-mSliderTouchX;
							final float pctDistance = distance/CAMERA_WIDTH;
							zoomSliderChange(pctDistance);
							mSliderTouchX=currX;

							if (distance != 0.0f)
							{
								double curZoom = setZoom(mCurrentZoomSlider);
								//mDebugText.setText("zoom:" + curZoom);

							}
						}

					}
					else if (action == TouchEvent.ACTION_UP)
					{
						mTouchX = -1.0f;
						mTouchY = -1.0f;
						mSliderTouchX = -1.0f;
						mSliderTouchY = -1.0f;
						mZoomIndicator.SetVisibility(false);
					}
													
					return true;
				}

            }); 
			
			mScreenGrabber = new ScreenGrabber();   
            scene.attachChild(mScreenGrabber);  			
            

			
			
			return scene;
		}

		private void Reorientate(final boolean pIsLandscape) {
			
			mLatitudeText.Reorientate(pIsLandscape);
			mLongitudeText.Reorientate(pIsLandscape);
			mTimeText.Reorientate(pIsLandscape);
			mVersionText.Reorientate(pIsLandscape);
			mMetarText.Reorientate(pIsLandscape);
			
			mVersionText.setText( "v"+ VERSION);
			

			
		}

		private void doCapture()
		{
			
	        if ((System.currentTimeMillis() - mCaptureTimer) < 500)
	        	return;
	        
	        if (mCaptureInProgress)
	        	return;	        
	        mCaptureInProgress = true;
	        

			mSoundHelper.PlaySound(mSoundHelper.mSoundCamera);

	        mCaptureTimer = System.currentTimeMillis();
    			
			mBitmapCaptureVideo = null;
			mBitmapCaptureRender = null;   
			mGrabStatus = 0;
			
			
			mScreenGrabber.grab(mRenderSurfaceView.getWidth(),mRenderSurfaceView.getHeight(), new IScreenGrabberCallback()
			{

				@Override
				public void onScreenGrabbed(Bitmap pBitmap) {
					
					if (!bLandscape)
					{
						mBitmapCaptureRender = pBitmap;
					}
					else
					{
						Matrix matrix = new Matrix();
						matrix.postRotate(270); 
						mBitmapCaptureRender = Bitmap.createBitmap(pBitmap, 0, 0,
								pBitmap.getWidth(), pBitmap.getHeight(),
								matrix, true); 
														
					}
					
					
					mGrabStatus |= 1;
					onBitmapAction();
				}

				@Override
				public void onScreenGrabFailed(Exception e) {
					mGrabStatus |= 4;
					e.printStackTrace();
					onBitmapAction();
				}
				
				
			});
			
			android.hardware.Camera.PreviewCallback previewCallback = new PreviewCallback()
			{

				@Override
				public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
					
					Parameters parameters = mCameraPreviewSurfaceView.mCamera.getParameters();
					int imageFormat = parameters.getPreviewFormat();
					if (imageFormat == ImageFormat.NV21)
					{
						final int width = mCameraPreviewSurfaceView.mWidth;
						final int height = mCameraPreviewSurfaceView.mHeight;
						int[] mIntArray = new int[width*height];  // Decode Yuv data to integer array
						decodeYUV420SP(mIntArray, data, width, height);  //Initialize the bitmap, with the replaced color
						Bitmap bmp = Bitmap.createBitmap(mIntArray, width, height, Bitmap.Config.ARGB_8888);    // Draw the bitmap with the replaced color
						mIntArray = null;
						// rotate 90 degrees	
						
						Bitmap b=null;
						final float scale = getZoom(camera)/100.0f;
						Matrix matrixZoom = new Matrix();							
						matrixZoom.postScale(scale, scale);
						
						if (!bLandscape)
						{
							Matrix matrix = new Matrix();
							matrix.postRotate(90); 
							Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0,
									bmp.getWidth(), bmp.getHeight(),
									matrix, true); 
							
							bmp.recycle();						

							final float transX = (0.0f-scale*rotated.getWidth()*0.5f)+(rotated.getWidth()*0.5f);
							final float transY = (0.0f-scale*rotated.getHeight()*0.5f)+(rotated.getHeight()*0.5f);
							//Log.d("zoom","z:"+scale + " w:" + rotated.getWidth() + " t:" + transX);

							matrixZoom.postTranslate(transX, transY);
							b = Bitmap.createBitmap(rotated.getWidth(), rotated.getHeight(), Bitmap.Config.ARGB_8888);
							Canvas c = new Canvas(b); 						
							Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
							c.drawBitmap(rotated,matrixZoom,p);

							rotated.recycle();	
							
						}
						else
						{
							final float transX = (0.0f-scale*bmp.getWidth()*0.5f)+(bmp.getWidth()*0.5f);
							final float transY = (0.0f-scale*bmp.getHeight()*0.5f)+(bmp.getHeight()*0.5f);
							
							matrixZoom.postTranslate(transX, transY);
							b = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
							Canvas c = new Canvas(b);
							
							Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
							c.drawBitmap(bmp,matrixZoom,p);
							
							bmp.recycle();
							
						}
						


					
				
						mBitmapCaptureVideo = b;
						mGrabStatus |= 2;
						onBitmapAction();
						
							
					}
					mCameraPreviewSurfaceView.mCamera.setPreviewCallback(null);
					

					
				}
				
			};
			mCameraPreviewSurfaceView.mCamera.setPreviewCallback(previewCallback);
			

			
                             
		

		
    		return; 
			
		}
		
		private void onBitmapAction()
		{

			if (mGrabStatus == 0)
			{
				// new capture started, no data yet
			}
			else if ((mGrabStatus & 4) == 4)
			{
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override                                                                  
					public void run() {
						Toast.makeText(MainActivity.this, "FAILED to capture image", Toast.LENGTH_SHORT).show();
						}
					});
				
				mGrabStatus = 0; // reset
			}
			else if (mGrabStatus == 3)
			{
				if ((mBitmapCaptureVideo == null) || (mBitmapCaptureRender == null))
					return;

				// grabbed both
				boolean bStatus=false;
    			final String sOurDir = Environment.getExternalStorageDirectory() + "/" + mStorageFolderName + "/";
    			final String sOurFile = System.currentTimeMillis() + ".jpg";
    			final String sOurFullPath = sOurDir+sOurFile;
    			
				File ourDir = new File( sOurDir);
				if (!ourDir.exists())
					ourDir.mkdirs();      
				
				if (!ourDir.exists())
					return;						
				
				// scale rendered bm to match video capture
				final double aspectVideo = 1.0*mBitmapCaptureVideo.getWidth()/mBitmapCaptureVideo.getHeight();
				final double aspectRender = 1.0*mBitmapCaptureRender.getWidth()/mBitmapCaptureRender.getHeight();
				
				//Log.d("aspect","v:"+aspectVideo+ " r:"+aspectRender);
				final double ratioWidth = 1.0*mBitmapCaptureVideo.getWidth()/mBitmapCaptureRender.getWidth();
				final double ratioHeight = 1.0*mBitmapCaptureVideo.getHeight()/mBitmapCaptureRender.getHeight();
				
				final int destHeight;
				final int destWidth;
				
				// keep aspect, match width or height then scale other
				if (aspectVideo <= aspectRender)
				{
					// better fit with width matching and scaling height
					destHeight = (int)(mBitmapCaptureRender.getHeight() * ratioWidth);
					destWidth = (int)mBitmapCaptureVideo.getWidth();					
				}
				else
				{
					// better fit with height matching and scaling width
					destHeight = (int)mBitmapCaptureVideo.getHeight();
					destWidth = (int)(mBitmapCaptureRender.getWidth() * ratioHeight);
				}
				
				final Bitmap bmScaled = Bitmap.createScaledBitmap(mBitmapCaptureRender, destWidth, destHeight, true);												
				final Bitmap combined = overlayBitmaps(mBitmapCaptureVideo, bmScaled);
				
				
				OutputStream outStream = null;
				File file = new File(sOurFullPath);
				try
				{
					outStream = new FileOutputStream(file);		

					combined.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
					//img.compressToJpeg(rect, 100, outStream);

					outStream.flush();
					outStream.close();

					if ((mLatitudeLast != 0.0 ) || (mLongitudeLast != 0.0))
					{
			            ExifInterface exif = new ExifInterface(sOurFullPath);
			            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,ExifUtil.GetLongitudeLatitudeString(mLatitudeLast)); 
			            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,ExifUtil.GetLongitudeLatitudeString( mLongitudeLast));
			            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, ExifUtil.GetLatitudeRef(mLatitudeLast) );  
			            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, ExifUtil.GetLongitudeRef(mLongitudeLast) ); 
			           // exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "1000/1");
			           // exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, "0");
													            
			            
			            exif.saveAttributes(); 		
					}
					MediaScannerConnection.scanFile(
							mContext, 
						    new String[] { sOurFullPath }, null, 
						    new MediaScannerConnection.OnScanCompletedListener() { 

							@Override
							public void onScanCompleted( String path, Uri uri) {
							    Log.i(mStorageFolderName, "Scanned " + path + ":"); 
						        Log.i(mStorageFolderName, "-> uri=" + uri); 													
							} 
						}); 
		            				            							
					bStatus=true;
					
					final String sMessage;
					if (bStatus==true)					
						sMessage = "Image: " + sOurFile + " taken!";
					
					else
						sMessage = "Could not save image!";
					
					MainActivity.this.runOnUiThread(new Runnable() {                        
						@Override                                                                  
						public void run() {   
							if (bLandscape)
							{
								Toast myToast = new Toast(MainActivity.this);
								// Creating our custom text view, and setting text/rotation
								CustomTextView text = new CustomTextView(MainActivity.this);
								text.SetText("   "+sMessage);
								
								text.SetRotation(90, 0, 0);
								text.SetTextSize(32);
								
								
								myToast.setView(text);
								// Setting duration and displaying the toast
								myToast.setDuration(Toast.LENGTH_SHORT);
								myToast.show();								
							}
							else								
								Toast.makeText(MainActivity.this, sMessage, Toast.LENGTH_SHORT).show();
							
							

							}                                             
						});    
				}
				catch (FileNotFoundException e)
				{						
					e.printStackTrace();
				}
				catch (IOException e)
				{						
					e.printStackTrace();
				}	
				finally
				{
					mGrabStatus = 0; //reset
					mCaptureInProgress = false;
					
				}
			}

		}
		
		public int getZoom( android.hardware.Camera camera)
		{
			int zoom = 100;
			
			final Parameters p = camera.getParameters();
			if (p.isZoomSupported())
			{
				final List<Integer> zoomRatios = p.getZoomRatios();
				final int zoomIndex = p.getZoom();
				if (zoomIndex < zoomRatios.size())
				{
					zoom = zoomRatios.get(zoomIndex);
				}	
				//Log.d("zoom","ix:" + zoomIndex + " z:"+zoom);			
				
			}				
			return zoom;
		}		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
			if ((keyCode == KeyEvent.KEYCODE_CAMERA) && (event.getAction() == KeyEvent.ACTION_DOWN))
			{
				if (bKeyStatusCamera==false)
				{
					bKeyStatusCamera=true;
					doCapture();
				}
				return true;				
			} else if ((keyCode == KeyEvent.KEYCODE_SEARCH) && (event.getAction() == KeyEvent.ACTION_DOWN))
			{
				if (bKeyStatusSearch==false)
				{
					bKeyStatusSearch=true;
					doCapture();
				}
				return true;				
			} 		
			
			
			return super.onKeyDown(keyCode, event); 
		}
		
		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event)
		{
			if ((keyCode == KeyEvent.KEYCODE_CAMERA) && (event.getAction() == KeyEvent.ACTION_UP)) {
				bKeyStatusCamera=false;
				return true;
			}  else if ((keyCode == KeyEvent.KEYCODE_SEARCH) && (event.getAction() == KeyEvent.ACTION_UP)) {
				bKeyStatusSearch=false;
				return true;
			}			
			

			
			return super.onKeyUp(keyCode, event); 			
		}
		
		private Bitmap overlayBitmaps(Bitmap mBitmapCaptureVideo,Bitmap mBitmapCaptureRender) {
			Bitmap bmOverlay = Bitmap.createBitmap(mBitmapCaptureVideo.getWidth(), mBitmapCaptureVideo.getHeight(), mBitmapCaptureVideo.getConfig());
			Canvas canvas = new Canvas(bmOverlay);      
			canvas.drawBitmap(mBitmapCaptureVideo, 0, 0, null);
			
			// rendered image may be smaller, so center it
			final int widthDiffHalf = (mBitmapCaptureVideo.getWidth()-mBitmapCaptureRender.getWidth())/2;
			final int heightDiffHalf = (mBitmapCaptureVideo.getHeight()-mBitmapCaptureRender.getHeight())/2;
			//Log.d("bitmap","w:"+widthDiffHalf+" h:"+heightDiffHalf);		
			
			canvas.drawBitmap(mBitmapCaptureRender, widthDiffHalf, heightDiffHalf, null);     
			return bmOverlay;  

		}

		// pZoom is in range from 0.0 to 1.0 where 0 is min zoom and 1 is max zoom
		private double setZoom(double pZoom)
		{
			if (mCameraPreviewSurfaceView.mZoomRatios == null)
				return 1.0;
			final int zoomCount = mCameraPreviewSurfaceView.mZoomRatios.size();
			int zoomFactor = (int)(zoomCount*pZoom);
			if (zoomFactor>zoomCount-1)
				zoomFactor=zoomCount-1;
			mCameraPreviewSurfaceView.setZoom(zoomFactor);
			final double zoomValue = mCameraPreviewSurfaceView.mZoomRatios.get(zoomFactor)/100.0; 
			//final double fieldOfView = 2.0*Math.toDegrees(Math.atan((CAMERA_SENSOR_DIAMETER*0.5)/(mFocalLength*zoomValue)));
			final double fieldOfView = getRenderedViewAngle()/zoomValue;			
			mHorizonIndicator.SetViewAngle(fieldOfView);
			//Log.d("zoom","p=" + pZoom + " id="+zoomFactor+" v="+mCameraPreviewSurfaceView.mZoomRatios.get(zoomFactor) + " zv="+ zoomValue);
			return zoomValue;
		}

		
	    
		private double getRenderedViewAngle() {
	    	final double hva = getHVA();
	    	// get width at our rendering camera resolution (720x480)
	    	final double widthRatio = (CAMERA_WIDTH*1.0)/mCameraPreviewSurfaceView.mHeight;
	    	final double displayedWidthAtCamera = widthRatio*mCameraPreviewSurfaceView.mWidth;
	    	
	    	final double ratioOfCameraToFullscreen = (CAMERA_HEIGHT*1.0)/displayedWidthAtCamera;
	    	final double tanHalf = Math.tan(Math.toRadians(hva*0.5));
	    	final double newAngle = Math.toDegrees(Math.atan(tanHalf*ratioOfCameraToFullscreen))*2.0;
	    	//cameraHeight/renderedHeight
	    	
	    	return newAngle;
		}
		
		
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	    	menu.add(1,10001,Menu.FIRST,"rotate portrait/landscape");
	    	
	    	return true;
	    }
		
	    @Override 
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	switch(item.getItemId())
	    	{
	    	case 10001:
	    		bLandscape=!bLandscape;
	    		mCompassIndicator.Reorientate(bLandscape);
	    		mSpeedIndicator.Reorientate(bLandscape);
	    		mSpeedIndicator.Draw(ListUtil.GetFilteredAverage(qSpeed, 0.5));
	    		mAltitudeIndicator.Reorientate(bLandscape);
	    		mAltitudeIndicator.Draw(ListUtil.GetFilteredAverage(qAltitude, 0.5));
	    		mBankIndicator.Reorientate(bLandscape);
	    		mHorizonIndicator.Reorientate(bLandscape);
	    		Reorientate(bLandscape);
	    		
				mLatitudeText.resetText();
				mLongitudeText.resetText();
				mTimeText.resetText();
				mVersionText.resetText();
				mMetarText.resetText();
	    		
	    		break;
	    	}

	    	return super.onOptionsItemSelected(item);
	    }		

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.mContext = this.getBaseContext();
	        mLocationHelper = new LocationHelper(this);
	        mLocationHelper.onCreate();
	        mMetarHelper = new MetarHelper(this);
	        
	        		        
	    }	
	    
	    @Override
	    protected void onResume()
	    {
	    	super.onResume();
	    	mLocationHelper.onResume();
	    	mSensorHelper.onResume();

	 	    	
	    }
	    
		 @Override
		 protected void onPause()
		 {
		 	super.onPause();   
		 	mLocationHelper.onPause();
		 	mSensorHelper.onPause();
	 	
		 }   	    
		
		@Override
		public void onLoadComplete() {

		}

		private void zoomSliderChange(float offset)
		{
			mCurrentZoomSlider+=offset;
			if (mCurrentZoomSlider > 1.0f)
				mCurrentZoomSlider = 1.0f;
			else if (mCurrentZoomSlider < 0.0f)
				mCurrentZoomSlider=0.0f;
		}

		
		public final double getHVA()
		{	
			double hva=50.0;
			if ((mCameraPreviewSurfaceView != null) && (mCameraPreviewSurfaceView.mCamera != null))
			{
				Parameters cameraParameters = mCameraPreviewSurfaceView.mCamera.getParameters();
				if (cameraParameters != null)
					hva = cameraParameters.getHorizontalViewAngle();
			}

			return hva;
		}
		public final double getVVA()
		{		
			double vva=35.0;
			if ((mCameraPreviewSurfaceView != null) && (mCameraPreviewSurfaceView.mCamera != null))
			{
				Parameters cameraParameters = mCameraPreviewSurfaceView.mCamera.getParameters();
				if (cameraParameters != null)
					vva = cameraParameters.getVerticalViewAngle();
			}

			return vva;			
		}

		public final double getFocalLength()
		{
			double fl=4.3; 
			if ((mCameraPreviewSurfaceView != null) && (mCameraPreviewSurfaceView.mCamera != null))
			{
				Parameters cameraParameters = mCameraPreviewSurfaceView.mCamera.getParameters();
				if (cameraParameters != null)
					fl = cameraParameters.getFocalLength();
			}

			return fl;				
		}
		

	
		static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
			final int frameSize = width * height;
			for (int j = 0, yp = 0; j < height; j++) {
				int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
				for (int i = 0; i < width; i++, yp++) {
					int y = (0xff & ((int) yuv420sp[yp])) - 16;
					if (y < 0) y = 0;
					if ((i & 1) == 0) {
						v = (0xff & yuv420sp[uvp++]) - 128;
						u = (0xff & yuv420sp[uvp++]) - 128;
						}
					int y1192 = 1192 * y;
					int r = (y1192 + 1634 * v);
					int g = (y1192 - 833 * v - 400 * u);
					int b = (y1192 + 2066 * u);
					if (r < 0) r = 0; 
					else if (r > 262143) r = 262143;
					if (g < 0) g = 0;
					else if (g > 262143) g = 262143;
					if (b < 0) b = 0;
					else if (b > 262143) b = 262143;
					rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
					}
				}
			} 	
}