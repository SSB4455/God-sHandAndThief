package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ProgressBar extends GameActor {
	public static long TOTAL_TIME = 30000;
	
	private long playTime;
	private boolean isPlay;
	
	
	
	public ProgressBar(Bitmap actorBitmap) {
		playTime = 0;
		isPlay = true;
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		playTime += elapsedTime;
		if(playTime > TOTAL_TIME) {
			isPlay = false;
			playTime = 0;
		}
		
	}
	
	@Override
	public void render(Canvas canvas) {
		
		canvas.drawLine(MainSurfaceView.SCREEN_W / 4, 14, MainSurfaceView.SCREEN_W * 3 / 4, 14, paint);
		canvas.drawCircle(MainSurfaceView.SCREEN_W / 4 + MainSurfaceView.SCREEN_W / 2 * (playTime / (float) TOTAL_TIME), 14, 7, paint);
		
		/*
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		else
			canvas.scale(shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
		*/
	}
	
	public boolean isPlay() {
		return isPlay;
	}
}
