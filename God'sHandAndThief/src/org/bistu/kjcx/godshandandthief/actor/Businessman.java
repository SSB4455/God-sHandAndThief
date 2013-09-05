package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.kjcx.godshandandthief.statesystem.StateSystem.PlayerType;

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
	
	public static int SPEED = MainSurfaceView.SCREEN_W / 2;
	
	private int health, frameW, frameH, incrementWHalf, incrementHHalf, currentFrame, bodyMotion;
	private int heartStartX, heartY, heartInterval;
	private int upHight;
	private int [] frameTotal;
	private float scrollX, scrollY;
	PlayerType playerType;
	private final int UP = 0;
	private final int DOWN = 1;
	
	private final int IS_RUN = 0;
	private final int IS_UP = 1;
	private final int IS_DOWN = 2;
	private final int IS_INJURED = 3;
	private long brushTime, upTime, downTime, injuredTime;
	
	private boolean [] fling;
	
	GodLayout godLayout;
	
	private Bitmap [][] frame;
	private Bitmap heart;
	private GestureDetector mGestureDetector;
	
	
	
	public Businessman(Context context) {
		
		frameTotal = new int[4];
		frame = new Bitmap[4][];
		
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
		frameTotal[bodyMotion] = 1;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_up);
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = actorBitmap;
		}
		
		bodyMotion = IS_DOWN;
		frameTotal[bodyMotion] = 1;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_down);
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = actorBitmap;
		}
		
		bodyMotion = IS_INJURED;
		frameTotal[bodyMotion] = 4;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_injured);
		frame[bodyMotion] = new Bitmap[frameTotal[bodyMotion]];
		for(int i = 0; i < frameTotal[bodyMotion]; i++) {
			frame[bodyMotion][i] = Bitmap.createBitmap(actorBitmap, 0, frameH * i, frameW, frameH);
		}
		
		heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		heartStartX = MainSurfaceView.SCREEN_W / 17;
		heartY = MainSurfaceView.SCREEN_H / 17;
		heartInterval = heart.getWidth();
		
		brushTime = 0;
		
		hight = MainSurfaceView.SCREEN_H / 4;		//高度是屏幕高度1/4
		shrink = hight / (float) frameH;
		width = (int) (frameW * shrink);
		incrementWHalf = (int) (frameW * (shrink - 1) / 2);
		incrementHHalf = (int) (frameH * (shrink - 1) / 2);
		
		actorX = MainSurfaceView.SCREEN_W / 5 + incrementWHalf;		//定位
		actorY = Background.FLOOR - frameH - incrementHHalf;
		Log.d(this.getClass().toString(), "actorX = " + actorX + ", frameW = " + frameW + ", frameH = " + frameH);
		Log.d(this.getClass().toString(), "incrementWHalf = " + incrementWHalf + ", incrementHHalf = " + incrementHHalf);
		
		fling = new boolean[2];
		
		health = 3;
		
		//初始化GestureDetector
		mGestureDetector = new GestureDetector(context, this);
		mGestureDetector.setIsLongpressEnabled(true);
		
		paint = new Paint();
	}
	
	public Businessman(Context context, PlayerType playerType) {
		this(context);
		
		this.playerType = playerType;
		if(playerType == PlayerType.Auto) {
			
		}
		
		
	}
	
	@Override
	public void update(long elapsedTime) {
		brushTime += elapsedTime;
		
		if(godLayout != null) {
			autoMotion();
		}
		
		//处理输入操作
		if(fling[DOWN]) {
			if(bodyMotion == IS_RUN)
				bodyMotion = IS_DOWN;
			if(godLayout != null)
				fling[DOWN] = false;
		}
		if(fling[UP]) {
			if(bodyMotion == IS_RUN || bodyMotion == IS_DOWN)
				bodyMotion = IS_UP;
			fling[DOWN] = false;
			fling[UP] = false;
		}
		
		//处理身体姿势
		if(bodyMotion == IS_UP) {
			actorY = Background.FLOOR - frameH - incrementHHalf - hight;
			if(upTime < 800 || fling[UP])
				upTime += elapsedTime;
			else {
				upTime = 0;
				bodyMotion = IS_RUN;
				actorY = Background.FLOOR - frameH - incrementHHalf;
			}
		}
		if(bodyMotion == IS_DOWN)
			if(downTime < 800 || fling[DOWN])
				downTime += elapsedTime;
			else {
				downTime = 0;
				bodyMotion = IS_RUN;
				actorY = Background.FLOOR - frameH - incrementHHalf;
			}
		if(bodyMotion == IS_INJURED)
			if(injuredTime < 700)
				injuredTime += elapsedTime;
			else {
				injuredTime = 0;
				bodyMotion = IS_RUN;
				actorY = Background.FLOOR - frameH - incrementHHalf;		//防止不在地面跑
			}
		
		if(brushTime > 80) {
			currentFrame = ++currentFrame % frameTotal[bodyMotion];
			brushTime = 0;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		for(int i = 0; i < health; i++)
			canvas.drawBitmap(heart, heartStartX + i * heartInterval, heartY, paint);
		
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		currentFrame = currentFrame % frameTotal[bodyMotion];		//防止不同动作bodyMotion的总数不一样 没有经过update导致数组越界
		canvas.drawBitmap(frame[bodyMotion][currentFrame], actorX, actorY, paint);
		canvas.restore();
	}
	
	public boolean isCollisionWith(Obstacle obstacle) {
		int allowMistake = obstacle.getWidth() / 4;
		if(obstacle.getLeft() < getRight() - allowMistake && getLeft() + allowMistake < obstacle.getRight()) {
			
			//Log.d(this.getClass().toString(), "businessman left = " + getLeft() + ", right = " + getRight());
			//Log.d(this.getClass().toString(), "obstacle left = " + obstacle.getLeft() + ", right = " + obstacle.getRight());
			switch(obstacle.getType()) {
			case Hole :
				if(bodyMotion == IS_DOWN || bodyMotion == IS_INJURED)
					return false;
				else
					return true;
			case Stone :
			case Pit :
				if(bodyMotion == IS_UP || bodyMotion == IS_INJURED)
					return false;
				else
					return true;
			default:
				return false;
			}
		}
		return false;
	}
	
	void autoMotion() {
		for(int i = 0; i < godLayout.getObstacleSize(); i++) {
			Obstacle obstacle = (Obstacle) godLayout.getObstacle(i);
			if(obstacle.getLeft() - 10 <= getRight() && obstacle.getRight() > getRight()) {
				switch(obstacle.getType()) {
				case Hole :
					fling[DOWN] = true;
					break;
				case Stone :
				case Pit :
					fling[UP] = true;
				}
			}
		}
	}
	
	public void beInjured() {
		health--;
		bodyMotion = IS_INJURED;
		Log.i(this.getClass().toString(), "businessman is injured.");
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		//Log.v(this.getClass().toString(), "onTouchEvent");
		if(event.getAction() ==  MotionEvent.ACTION_UP) {
	    	Log.d(this.getClass().toString(), "scrollX = " + scrollX + ", scrollY = " + scrollY);
	    	fling[DOWN] = false;
	    	scrollX = 0;
			scrollY = 0;
	    }
		
		return mGestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		//一次点击只唤醒一次
		//Log.v(this.getClass().toString(), "onDonw");
		return true;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		//一旦点击稍有移动就进入scroll再进入filing 不走长按这条线了 只有按住不动才能唤醒这个方法（只唤醒一次）
		//Log.v(this.getClass().toString(), "onLongPress");
		
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		scrollX -= distanceX;
		scrollY -= distanceY;
		Log.d(this.getClass().toString(), "onScroll ——> distanceX = " + distanceX + ", distanceY = " + distanceY);
		
		int scrollLength = MainSurfaceView.SCREEN_W / 10;
		
		if(scrollLength < -scrollY) {
			fling[UP] = true;
			Log.d(this.getClass().toString(), "onScroll to up.");
		}
		if((scrollX > scrollLength && scrollY < scrollLength / 2) || scrollY > scrollLength) {
			fling[DOWN] = true;
			Log.d(this.getClass().toString(), "onScroll to right.");
		}
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		//Log.v(this.getClass().toString(), "onShowPress");
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		//Log.v(this.getClass().toString(), "onSingleTapUp");
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean setGodLayout(GodLayout godLayout) {
		this.godLayout = godLayout;
		if(this.godLayout == godLayout)
			return true;
		return false;
	}
	
	public int getHreat() {
		return health;
	}
	
	public int setHreat(int health) {
		this.health = health;
		return health;
	}
	
	@Override
	public int getLeft() {
		return (int) (actorX - incrementWHalf);
	}
	
	@Override
	public int getRight() {
		return (int) (actorX + frameW + incrementWHalf);
	}
	
}
