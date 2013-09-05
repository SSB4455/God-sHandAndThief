package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GodLayout;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle;
import org.bistu.kjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ThiefPlayerState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private boolean isLose, isWin;
	private PlayerType playerType;
	private GodLayout godLayout;
	private Background background;
	private Businessman businessman;
	
	private Bitmap isLoseBitmap, isWinBitmap;
	private Paint paint;
	
	
	
	public ThiefPlayerState(Context context, StateSystem stateSystem, PlayerType playerType) {
		this.context = context;
		this.stateSystem = stateSystem;
		this.playerType = playerType;
		
		if(playerType == PlayerType.Player) {
			businessman = new Businessman(context);
		}
		if(playerType == PlayerType.Auto) {
			businessman = new Businessman(context, playerType);
		}
		
		background = new Background(context);
		businessman = new Businessman(context);
		
		isLoseBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lose);
		isWinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.win);
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void update(long elapsedTime) {
		
		if(godLayout.getProgressBar().isPlay()) {
			background.update(elapsedTime);
			godLayout.update(elapsedTime);
			
			for(int i = 0; i < godLayout.getObstacleSize(); i++)
				if(businessman.isCollisionWith((Obstacle) godLayout.getObstacle(i))) {
					businessman.beInjured();
				}
			
			if(businessman.getHreat() < 1) {
				isLose = true;
				godLayout.getProgressBar().stop();
			}
			businessman.update(elapsedTime);
		} else if(!isLose && godLayout.getProgressBar().isOver()) {
			isWin = true;
			godLayout.getProgressBar().stop();
		}
	}
	
	public void render(Canvas canvas) {
		background.render(canvas);
		godLayout.render(canvas);
		businessman.render(canvas);
		if(isWin)
			canvas.drawBitmap(isWinBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isWinBitmap.getHeight(), paint);
		if(isLose)
			canvas.drawBitmap(isLoseBitmap, MainSurfaceView.SCREEN_W / 5, MainSurfaceView.SCREEN_H - isLoseBitmap.getHeight(), paint);
	}
	
	void setGodLayout(GodLayout godLayout) {
		
		this.godLayout = godLayout;
		if(playerType == PlayerType.Auto) {
			businessman.setGodLayout(godLayout);
		}
	}
	
	public void reset() {
		isLose = false;
		isWin = false;
		businessman.setHreat(businessman.getHreat() + 1);
		
		if(playerType == PlayerType.Player) {
			GodHandPlayerState godHandPlayerState = new GodHandPlayerState(context, stateSystem, PlayerType.Auto);
			godLayout = godHandPlayerState.createAutoGodLayout(9);
		}else {
			godLayout.clear();
			godLayout.zeroProgressBar();
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if((isWin || isLose) && playerType == PlayerType.Player) {
			Log.i(this.getClass().toString(), "touch to reset");
			reset();
		}
		return businessman.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ����> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//���ñ�������
	}

	public void render() {
		// TODO Auto-generated method stub
	}
	
}
