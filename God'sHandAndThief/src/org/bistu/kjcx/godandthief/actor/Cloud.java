package org.bistu.kjcx.godandthief.actor;

import org.bistu.kjcx.godandthief.MainSurfaceView;
import org.bistu.kjcx.godandthief.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Cloud extends GameActor {
	private Context context;
	
	private int cloudSpeed, frameW, frameH;
	private float cloudShrink;
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
		if(actorX < -frameW * cloudShrink) {
			actorX = MainSurfaceView.SCREEN_W + (frameW * (cloudShrink - 1) / 2);
		}
		if(actorX > MainSurfaceView.SCREEN_W + (frameW * (cloudShrink - 1) / 2)) {
			actorX = -frameW * cloudShrink;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-cloudShrink, cloudShrink, actorX + frameW /2, actorY + frameH /2);
		else
			canvas.scale(cloudShrink, cloudShrink, actorX + frameW /2, actorY + frameH /2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
	
	void setCloud() {
		cloudSpeed = 100;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.super_mario_cloud);
		
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();

		cloudShrink = (MainSurfaceView.SCREEN_W / 6) / (float) frameW;
		
		actorX = Background.FACE_TO == Background.TO_LEFT ? MainSurfaceView.SCREEN_W : -frameW;
		actorY = MainSurfaceView.SCREEN_H / 8;
		
		Log.i("Cloud", "cloudShrink = " + cloudShrink);
		paint = new Paint();
	}
	
	void setActorX(float actorX) {
		this.actorX = actorX;
	}
	
	void setActorY(float actorY) {
		this.actorY = actorY;
	}
}
