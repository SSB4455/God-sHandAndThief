package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Businessman extends GameActor implements OnGestureListener {
	
	public static int SPEED = 300;
	
	private int health, frameW, frameH, currentFrame;
	private final int TO_RIGHT = 1;
	private final int TO_LEFT = 0;
	private final int IS_LEFT = 0;
	private final int IS_RIGHT = 1;
	private final int IS_UP = 2;
	private final int IS_DOWN = 3;
	private long go_elapsed;
	
	boolean [] fling;
	
	private Bitmap [] frame;
	private GestureDetector mGestureDetector;
	
	
	
	public Businessman(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run);
		frame = new Bitmap[5];
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight() / frame.length;
		for(int i = 0; i < frame.length; i++) {
			frame[i] = Bitmap.createBitmap(actorBitmap, 0, frameH * i, frameW, frameH);
		}
		currentFrame = 0;
		go_elapsed = 0;
		
		shrink = (MainSurfaceView.SCREEN_H / 4) / (float) frameH;
		
		actorX = MainSurfaceView.SCREEN_W / 5 + frameW * (shrink - 1) / 2;
		actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
		
		fling = new boolean[6];
		
		health = 3;
		
		//³õÊ¼»¯GestureDetector
		mGestureDetector = new GestureDetector(context, this);
		mGestureDetector.setIsLongpressEnabled(true);
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(go_elapsed > 80) {
			//Log.i(this.getClass().toString(), "go_elapsed = " + go_elapsed);
			currentFrame = ++currentFrame % frame.length;
			go_elapsed = 0;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		canvas.drawBitmap(frame[currentFrame], actorX, actorY, paint);
		canvas.restore();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
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
