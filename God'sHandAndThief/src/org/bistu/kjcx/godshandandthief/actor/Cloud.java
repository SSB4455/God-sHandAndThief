package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Cloud extends GameActor {
	private Context context;
	
	private int cloudSpeed, frameW, frameH;
	private long go_elapsed;
	
	
	
	public Cloud(Context context) {
		this.context = context;
		setCloud();
	}
	
	public Cloud(Context context, float actorX) {
		this.context = context;
		setCloud();
		setActorX(actorX);
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(go_elapsed > 50) {
			if(Background.FACE_TO == Background.TO_LEFT)
				actorX -= (cloudSpeed * go_elapsed) / 1000;
			else
				actorX += (cloudSpeed * go_elapsed) / 1000;
			go_elapsed = 0;
		}
		
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
	
	void setCloud() {
		cloudSpeed = 100;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.super_mario_cloud);
		
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();

		shrink = (MainSurfaceView.SCREEN_W / 6) / (float) frameW;
		
		actorX = Background.FACE_TO == Background.TO_LEFT ? MainSurfaceView.SCREEN_W : -frameW;
		actorY = MainSurfaceView.SCREEN_H / 8 + frameH * (shrink - 1) / 2;
		
		Log.i("Cloud", "cloudShrink = " + shrink);
		paint = new Paint();
	}
	
	void setActorX(float actorX) {
		this.actorX = actorX;
	}
	
	void setActorY(float actorY) {
		this.actorY = actorY;
	}
}
