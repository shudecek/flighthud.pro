package com.apporilla.flighthud.Indicators.AttitudeIndicatorItem;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.font.Font;

enum AttitudeIndicatorItemType {Horizon, Above, Below};

public abstract class AttitudeIndicatorItemBase {
	Scene mScene;
	Font mFont;
	AttitudeIndicatorItemType mType;
	
	public AttitudeIndicatorItemBase() {}
	public abstract void Create();

}
