package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Businessman extends GameActor implements OnGestureListener {
	
	public static int SPEED = 300;
	
	private int health, frameW, frameH, currentFrame, bodyMotion;
	private int [] frameTotal;
	private final int UP = 0;
	private final int RIGHT = 1;
	
	private final int IS_RUN = 0;
	private final int IS_UP = 1;
	private final int IS_DOWN = 2;
	private long brushTime, upTime, downTime;
	
	boolean [] fling;
	
	private Bitmap [][] frame;
	private GestureDetector mGestureDetector;
	
	
	
	public Businessman(Context context) {
		
		frameTotal = new int[3];
		frame = new Bitmap[3][];
		
		bodyMotion = IS_RUN;
		frameTotal[bodyMotion] = 5;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run);
		frameW = actorBitmap.getWidth();			//100
		frameH = actorBitmap.getHeight() / 5;		//120
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = Bitmap.createBitmap(actorBitmap, 0, frameH * i, frameW, frameH);
		}
		
		bodyMotion = IS_UP;
		frameTotal[bodyMotion] = 5;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_up);
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = actorBitmap;
		}
		
		bodyMotion = IS_DOWN;
		frameTotal[bodyMotion] = 5;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_down);
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = actorBitmap;
		}
		
		brushTime = 0;
		
		shrink = (MainSurfaceView.SCREEN_H / 4) / (float) frameH;		//高度是屏幕高度1/4 
		
		actorX = MainSurfaceView.SCREEN_W / 5 + frameW * (shrink - 1) / 2;		//定位
		actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
		
		fling = new boolean[2];
		
		health = 3;
		
		//初始化GestureDetector
		mGestureDetector = new GestureDetector(context, this);
		mGestureDetector.setIsLongpressEnabled(true);
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		brushTime += elapsedTime;
		if(fling[UP]) {
			if(bodyMotion == IS_RUN) {
				bodyMotion = IS_UP;
				fling[UP] = false;
			}
		} else if(fling[RIGHT]) {
			if(bodyMotion == IS_RUN) {
				bodyMotion = IS_DOWN;
				fling[RIGHT] = false;
			}
		}
		
		if(bodyMotion == IS_UP) {
			actorY = Background.FLOOR - 100 - frameH - frameH * (shrink - 1) / 2;
			if(upTime < 700)
				upTime += elapsedTime;
			else {
				upTime = 0;
				bodyMotion = IS_RUN;
				actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
			}
		}
		if(bodyMotion == IS_DOWN)
			if(downTime < 700)
				downTime += elapsedTime;
			else {
				downTime = 0;
				bodyMotion = IS_RUN;
			}
		
		if(brushTime > 80) {
			currentFrame = ++currentFrame % frameTotal[bodyMotion];
			brushTime = 0;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		canvas.drawBitmap(frame[bodyMotion][currentFrame], actorX, actorY, paint);
		canvas.restore();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int distanceX = (int)(e2.getX() - e1.getX());
		int distanceY = (int)(e2.getY() - e1.getY());
		Log.i(this.getClass().toString(), "onFling ————> distanceX = " + distanceX);
		Log.i(this.getClass().toString(), "onFling ————> distanceY = " + distanceY);
		
		int minL = 50;
		int minOfHalfW = 40;
		
		if(distanceY < -minL && distanceX < minOfHalfW && distanceX > -minOfHalfW) {
			fling[UP] = true;
			Log.i(this.getClass().toString(), "Fling to up.");
		}
		if(distanceX > minL && distanceY < minOfHalfW && distanceY > -minOfHalfW) {
			fling[RIGHT] = true;
			Log.i(this.getClass().toString(), "Fling to right.");
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
