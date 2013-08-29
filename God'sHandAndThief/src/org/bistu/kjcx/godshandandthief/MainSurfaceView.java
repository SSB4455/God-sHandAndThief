package org.bistu.kjcx.godshandandthief;

import org.bistu.kjcx.godshandandthief.statesystem.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

public class MainSurfaceView extends SurfaceView implements Callback, Runnable {
	
	public static int SCREEN_H, SCREEN_W;
	private static int BRUSH_FRAME_FREQUENCY;
	private static long BRUSH_FRAME_LONG;
	
	private SurfaceHolder sfh;		//����һ��SurfaceHolder ����SurfaceView��
	private StateSystem stateSystem;
	
	private long [] time;
	private long elapsedTime, brushTime;
	private boolean flag;
	
	private Context context;
	Canvas canvas;
	private Paint paint;
	
	
	
	public MainSurfaceView(Context context) {
		super(context);
		
		this.context = context;
		new BitmapStorage(context);
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();
		SCREEN_H = dm.heightPixels;
		SCREEN_W = dm.widthPixels;
		Log.i(this.getClass().toString(), "SCREEN_H = " + SCREEN_H + ", SCREEN_W = " + SCREEN_W);
		
		setBrushFrameFrequency(100);
		time = new long[3];
		brushTime = 0;
		
		stateSystem = new StateSystem();
		paint = new Paint();
		paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/UbuntuMono-R.ttf"));
		paint.setColor(Color.WHITE);
		
		this.setClickable(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setLongClickable(true);
		//Log.i(this.getClass().toString(), "structure ����> isFocused = " + isFocused());
		
		sfh = this.getHolder();		//��ȡ��ǰSurfaceView��SurfaceHolder
		sfh.addCallback(this);		//���õ�ǰSurfaceView�Ļص�
	}	

	public void surfaceCreated(SurfaceHolder holder) {		//Callback
		stateSystem.addState("SplashState", new SplashState(context, stateSystem));
		stateSystem.addState("MenuState", new MenuState(context, stateSystem));
		stateSystem.changeState("SplashState");
		
		flag = true;
		new Thread(this).start();
		time[2] = System.currentTimeMillis();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {		//Callback
		Toast.makeText(context, "surfaceChanged", Toast.LENGTH_SHORT).show();
		Log.i(this.getClass().toString(), "surfaceChanged");
		//flag = false;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {		//Callback
		flag = false;
		//�����ַ�����ֹ�˳�ʱ�쳣. ��surfaceView����ʱ���߳���ͣ300ms . ������ִ��run()����ʱ,isThreadRunning����false��. 
        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
	}
	
	public void run() {		//Runnable
		try {
			while(flag) {
				canvas = sfh.lockCanvas();
				time[0] = System.currentTimeMillis();
				//brushTime += elapsedTime;
				stateSystem.update(elapsedTime);
				time[1] = System.currentTimeMillis();
				stateSystem.render(canvas);
				calculateProcessTime();
				time[2] = System.currentTimeMillis();
				sfh.unlockCanvasAndPost(canvas);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.i(this.getClass().toString(), "onKeyDown");
		//Toast.makeText(context, "MainSurfaceView --> onKeyDown", Toast.LENGTH_SHORT).show();
		
		switch(keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.i(this.getClass().toString(), "onKeyDown ����> up");break;
		
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.i(this.getClass().toString(), "onKeyDown ����> down");break;
		
		case KeyEvent.KEYCODE_DPAD_LEFT:
			Log.i(this.getClass().toString(), "onKeyDown ����> left");break;
		
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Log.i(this.getClass().toString(), "onKeyDown ����> right");break;
			
		case KeyEvent.KEYCODE_BACK:
			Log.i(this.getClass().toString(), "onKeyDown ����> back");
		}
		
		//return false;		//false�Ǽ�����
		//return super.onKeyDown(keyCode, event);
		return stateSystem.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.i(this.getClass().toString(), "onTouchEvent ����> onTouch");
		
		//return super.onTouchEvent(event);
		return stateSystem.onTouchEvent(event);
	}

	double calculateProcessTime() {
		canvas.drawText("updateTime  = " + (time[1] - time[0]), 0, 10, paint);
		canvas.drawText("renderTime  = " + (System.currentTimeMillis() - time[1]), 0, 21, paint);
		elapsedTime = (System.currentTimeMillis() - time[2]);
		canvas.drawText("elapsedTime = " + elapsedTime, 0, 32, paint);
		//canvas.drawText("PostCanvasTime = " + (time[0] - time[2]), 0, 43, paint);
		
		return elapsedTime;
	}
	
	private long setBrushFrameFrequency(int times) {
		BRUSH_FRAME_FREQUENCY = times;
		return BRUSH_FRAME_LONG = 1000 / times;
	}
	
	public static int getBrushFrameFrequency() {
		return BRUSH_FRAME_FREQUENCY;
	}
	
	public static long getBrushFrameLong() {
		return BRUSH_FRAME_LONG;
	}
	
}
