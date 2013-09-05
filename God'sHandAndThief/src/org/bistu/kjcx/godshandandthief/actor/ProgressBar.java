package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ProgressBar extends GameActor {
	public static long TOTAL_Long = 30000;
	
	private long playTime;
	private boolean isPlay;
	
	
	
	public ProgressBar() {
		playTime = 0;
		isPlay = true;
		
		paint = new Paint();
	}
	
	public ProgressBar(Bitmap actorBitmap) {
		this();
		
	}
	
	@Override
	public void update(long elapsedTime) {
		if(isPlay)
			playTime += elapsedTime;
		if(playTime > TOTAL_Long) {
			isPlay = false;
			//playTime = 0;
		}
		
	}
	
	@Override
	public void render(Canvas canvas) {
		
		canvas.drawLine(MainSurfaceView.SCREEN_W / 4, 14, MainSurfaceView.SCREEN_W * 3 / 4, 14, paint);
		canvas.drawCircle(MainSurfaceView.SCREEN_W / 4 + MainSurfaceView.SCREEN_W / 2 * (playTime / (float) TOTAL_Long), 14, 7, paint);
		
	}
	
	public boolean isPlay() {
		return isPlay;
	}
	
	public boolean isOver() {
		if(playTime >= TOTAL_Long)
			return true;
		return false;
	}
	
	public void stop() {
		isPlay = false;
	}
	
	void zreo() {
		playTime = 0;
		isPlay = true;
	}
	
	public long getProgressL() {
		return playTime;
	}
	
	public float getProgressP() {
		return playTime;
	}

	@Override
	public int getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
