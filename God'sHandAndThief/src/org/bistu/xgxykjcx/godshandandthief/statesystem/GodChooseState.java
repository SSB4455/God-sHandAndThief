package org.bistu.xgxykjcx.godshandandthief.statesystem;

import org.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import org.bistu.xgxykjcx.godshandandthief.MainActivity;
import org.bistu.xgxykjcx.godshandandthief.MainSurfaceView;
import org.bistu.xgxykjcx.godshandandthief.statesystem.StateSystem.PlayerType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class GodChooseState implements IGameObject {
	private Context context;
	private StateSystem stateSystem;
	
	private final int X = 0, Y = 1;
	private float [][] menuLocation;
	private Bitmap [] menuButton;
	private Paint paint;
	
	
	
	public GodChooseState(StateSystem stateSystem) {
		this.context = MainActivity.CONTEXT;
		this.stateSystem = stateSystem;
		
		menuButton = new Bitmap[4];
		menuButton[0] = BitmapStorage.getImGodhand();
		menuButton[1] = BitmapStorage.getCatchComputer();
		menuButton[2] = BitmapStorage.getCatchThief();
		menuButton[3] = BitmapStorage.getGodHappy();
		
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
		if(event.getAction() == MotionEvent.ACTION_UP) {		//��ֹ����һ��State�в���Fling�ı���
			for(int i = 0; i < menuLocation.length; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					if(i == 1) {		//��������
						GodHandPlayerState godHandPlayerState = new GodHandPlayerState(stateSystem, PlayerType.Player);
						ThiefPlayerState thiefPlayerState = new ThiefPlayerState(stateSystem, PlayerType.Auto);
						thiefPlayerState.setGodLayout(godHandPlayerState.getGodLayout());
						godHandPlayerState.setThiefPlayerState(thiefPlayerState);
						//stateSystem.addState("ThiefPlayerState", thiefPlayerState);
						stateSystem.addState("GodHandPlayerState", godHandPlayerState);
						stateSystem.changeState("GodHandPlayerState");
						Toast.makeText(context, "catch computer...", Toast.LENGTH_SHORT).show();
					}
					if(i == 2) {		//С͵����
						IGameObject bluetoothChooseState = new BluetoothChooseState(stateSystem, BluetoothChooseState.GODSHAND);
						stateSystem.addState("BluetoothChooseState", bluetoothChooseState);
						stateSystem.changeState("BluetoothChooseState");
						Toast.makeText(context, "Bluetooth Choose State", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		return true;
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
