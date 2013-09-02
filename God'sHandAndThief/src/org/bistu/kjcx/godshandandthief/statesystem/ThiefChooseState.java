package org.bistu.kjcx.godshandandthief.statesystem;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
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
import android.widget.Toast;

public class ThiefChooseState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	private Paint paint;
	
	
	
	public ThiefChooseState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		menuButton = new Bitmap[4];
		menuButton[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.im_thief_come_on);
		menuButton[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.computer_pursue_me);
		menuButton[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.god_pursue_me);
		menuButton[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.thief_happy);
		
		menuLocation = new float[4][];
		menuLocation[0] = new float[2];
		menuLocation[0][X] = MainSurfaceView.SCREEN_W / 8;
		menuLocation[0][Y] = MainSurfaceView.SCREEN_H / 8;
		menuLocation[1] = new float[2];
		menuLocation[1][X] = MainSurfaceView.SCREEN_W / 3;
		menuLocation[1][Y] = MainSurfaceView.SCREEN_H / 2;
		menuLocation[2] = new float[2];
		menuLocation[2][X] = MainSurfaceView.SCREEN_W / 3;
		menuLocation[2][Y] = MainSurfaceView.SCREEN_H * 3 / 4;
		menuLocation[3] = new float[2];
		menuLocation[3][X] = 0;
		menuLocation[3][Y] = MainSurfaceView.SCREEN_H / 4;
		
		paint = new Paint();
		
	}
	
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		for(int i = 0; i < menuButton.length; i++) {
			canvas.drawBitmap(menuButton[i], menuLocation[i][X], menuLocation[i][Y], paint);
		}
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {		//防止到另一个State中产生Fling的崩溃
			for(int i = 0; i < menuLocation.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					if(i == 1) {
						ThiefPlayerState thiefPlayerState = new ThiefPlayerState(context, stateSystem, PlayerType.Player);
						GodHandPlayerState godHandPlayerState = new GodHandPlayerState(context, stateSystem, PlayerType.Auto);
						thiefPlayerState.setGodLayout(godHandPlayerState.createAutoGodLayout(9));
						stateSystem.addState("ThiefPlayerState", thiefPlayerState);
						stateSystem.changeState("ThiefPlayerState");
						Toast.makeText(context, "Comptuer pursue me...", Toast.LENGTH_SHORT).show();
					}
					if(i == 2) {
						IGameObject pleaseWaitState = new PleaseWaitState(context, stateSystem);
						stateSystem.addState("PleaseWaitState", pleaseWaitState);
						stateSystem.changeState("PleaseWaitState");
						Toast.makeText(context, "God pursue me...", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.i(this.getClass().toString(), "onKeyDown ——> back");
			stateSystem.changeState("MenuState");
		}
		return true;		//不让别人做了
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
