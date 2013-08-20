package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Cloud extends GameActor {
	
	private int cloudSpeed, frameW, frameH;
	
	
	
	public Cloud(Context context) {
		
		cloudSpeed = 100;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.super_mario_cloud);
		
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();

		shrink = (MainSurfaceView.SCREEN_W / 6) / (float) frameW;
		
		actorX = Background.FACE_TO == Background.TO_LEFT ? MainSurfaceView.SCREEN_W : -frameW;
		actorY = MainSurfaceView.SCREEN_H / 9 + frameH * (shrink - 1) / 2;
		
		Log.i(this.getClass().toString(), "cloudShrink = " + shrink);
		paint = new Paint();
	}
	
	public Cloud(Context context, float actorX) {
		this(context);
		setActorX(actorX);
	}
	
	@Override
	public void update(long elapsedTime) {
		
		if(Background.FACE_TO == Background.TO_LEFT)
			actorX -= (cloudSpeed * elapsedTime) / 1000;
		else
			actorX += (cloudSpeed * elapsedTime) / 1000;
		
		//³¬³öÆÁÄ»×ª»Ø
		if(actorX < -frameW * shrink) {
			actorX = MainSurfaceView.SCREEN_W + (frameW * (shrink - 1) / 2);
		}
		if(actorX > MainSurfaceView.SCREEN_W + (frameW * (shrink - 1) / 2)) {
			actorX = -frameW * shrink;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		else
			canvas.scale(shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
	
	void setActorX(float actorX) {
		this.actorX = actorX;
	}
	
	void setActorY(float actorY) {
		this.actorY = actorY;
	}
}
