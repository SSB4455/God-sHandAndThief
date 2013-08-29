package org.bistu.kjcx.godshandandthief.statesystem;

import java.util.Random;

import org.bistu.kjcx.godshandandthief.BitmapStorage;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.GodLayout;
import org.bistu.kjcx.godshandandthief.actor.ProgressBar;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle.ObstacleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GodHandPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	GodLayout godLayout;
	private Bitmap waitMoment;
	
	private Paint paint;
	
	
	
	public GodHandPlayerState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		waitMoment = BitmapStorage.getWaitMoment();
		
		paint = new Paint();
	}
	
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		canvas.drawBitmap(waitMoment, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H /5, paint);
	}
	
	boolean setGodLayout(GodLayout godLayout) {
		this.godLayout = godLayout;
		if(this.godLayout == godLayout)
			return true;
		else
			return false;
	}
	
	GodLayout createAutoGodLayout(int level) {
		Random random = new Random();
		godLayout = godLayout == null ? new GodLayout(context) : godLayout;
		level = level % 10;
		long interval = 1000;		//���ټ��1��
		long partLong = (ProgressBar.TOTAL_Long - 4000 - interval * level) / level;		//���4s��Ϊ�˸���һ���ϰ��ļ��Ԥ��2sΪ���һ���ϰ�Ԥ��2s
		for(int i = 0; i < level; i++) {
			long position = 2000 + i * (partLong + interval) + random.nextInt((int) partLong);
			//��һ���ϰ������2s��׼��ʱ��
			godLayout.addObstacle(position, i % 2 == 0 ? ObstacleType.Pit : ObstacleType.Hole);
			Log.v(this.getClass().toString(), "create a obstacle, position = " + position + " type = " + (i % 2 == 0 ? ObstacleType.Pit + "" : ObstacleType.Hole + ""));
		}
		
		return godLayout;
	}
	
	GodLayout getGodLayout() {
		return godLayout;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ����> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//���ñ�������
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
