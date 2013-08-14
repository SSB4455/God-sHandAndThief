package org.bistu.kjcx.godandthief.statesystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MenuState implements IGameObject {
	
	private Context context;
	private StateSystem stateSystem;
	
	private int screenW, screenH;
	private long exitTime = 0;
	String hackerFight, onePlayer, morePlayer, beta;
	float onePlayerX, onePlayerY, onePlayerW;
	float morePlayerX, morePlayerY, morePlayerW;
	float betaX, betaY, betaW;
	Paint paint;
	
	
	
	public MenuState(Context context, StateSystem stateSystem, SurfaceHolder sfh) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		screenW = sfh.getSurfaceFrame().right;
		screenH = sfh.getSurfaceFrame().bottom;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		
		hackerFight = "HackerFight";
		onePlayer = "ONE Player";
		morePlayer = "MORE Player";
		beta = "Beta";
		
		onePlayerW = paint.measureText(onePlayer);
		onePlayerX = (screenW - onePlayerW) / 2;
		onePlayerY = screenH / 2;
		morePlayerW = paint.measureText(morePlayer);
		morePlayerX = (screenW - morePlayerW) / 2;
		morePlayerY = onePlayerY + 35;
		betaW = paint.measureText(beta);
		betaX = screenW - betaW - 7;
		betaY = screenH - 7;
		
	}
	
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setTextSize(50);
		canvas.drawText(hackerFight, (screenW - paint.measureText(hackerFight)) / 2, screenH * 5 / 20, paint);
		paint.setTextSize(25);
		canvas.drawText(onePlayer, onePlayerX, onePlayerY, paint);
		canvas.drawText(morePlayer, morePlayerX, morePlayerY, paint);
		paint.setTextSize(16);
		canvas.drawText(beta, betaX, betaY, paint);
		
		//paint.setTextSize(10);	//看看文字所占高度
		//canvas.drawLine(0, onePlayerY + 5, screenW, onePlayerY + 5, paint);
		//canvas.drawLine(0, onePlayerY - 20, screenW, onePlayerY - 20, paint);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {		//防止到另一个State中产生Fling的崩溃
			
			/*
			if(onePlayerX < event.getX() && event.getX() < onePlayerX + onePlayerW)
				if(onePlayerY - 22 < event.getY() && event.getY() < onePlayerY + 5)
					stateSystem.changeState("FightingState");
			
			if(morePlayerX < event.getX() && event.getX() < morePlayerX + morePlayerW)
				if(morePlayerY - 22 < event.getY() && event.getY() < morePlayerY + 5)
					stateSystem.changeState("SplashScreenState");
			
			
			if(betaX - 5 < event.getX() && event.getX() < betaX + betaW + 5)
				if(betaY - 12 < event.getY() && event.getY() < betaY + 3) {
					Intent intent = new Intent();
	        		intent.setClass(context, BetaMenuActivity.class);
	        		intent.putExtra("extraName01", "extra_value");		//传递一个额外信息
	        		context.startActivity(intent);
				}
			*/
			
		}
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {  
			if((System.currentTimeMillis() - exitTime) > 2000) {
		    	Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		        exitTime = System.currentTimeMillis();
		    }
		    else
		        System.exit(0);
			return true; //返回true表示执行结束不需继续执行父类按键响应  
		}
		return false;
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
