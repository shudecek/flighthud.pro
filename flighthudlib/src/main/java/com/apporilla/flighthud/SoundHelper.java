package com.apporilla.flighthud;

import java.io.IOException;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.audio.sound.SoundManager;
import org.anddev.andengine.util.Debug;

import android.content.Context;

public class SoundHelper {
	public Sound mSoundCamera; 
	
	public void Load(SoundManager soundManager, Context pContext) {
		
		SoundFactory.setAssetBasePath("mfx/");
		try {
			mSoundCamera = SoundFactory.createSoundFromAsset(soundManager, pContext, "camera_click.ogg");
			
		} catch (final IOException e) {
			Debug.e("Error", e);
		}	

		
	} 
	
	public void PlaySound(final Sound sound)
	{
			sound.play();			
	}
}
