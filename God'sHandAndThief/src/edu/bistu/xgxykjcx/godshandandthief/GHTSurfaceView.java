package edu.bistu.xgxykjcx.godshandandthief;

import edu.bistu.xgxykjcx.godshandandthief.statesystem.*;

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

public class GHTSurfaceView extends SurfaceView implements Callback, Runnable {
	
	public static int SCREEN_H, SCREEN_W, DENSITY_DPI;
	public static float DENSITY;
	private static int BRUSH_FRAME_FREQUENCY;
	private static long BRUSH_FRAME_LONG;
	
	private SurfaceHolder sfh;		// 创建一个SurfaceHolder 控制SurfaceView的
	private StateSystem stateSystem;
	
	private long [] time;
	private long elapsedTime, brushTime;
	private boolean flag;
	
	private Context context;
	private Canvas canvas;
	private Paint paint;
	
	
	
	public GHTSurfaceView(Context context) {
		super(context);
		
		this.context = context;
		DisplayMetrics metric = new DisplayMetrics();
		metric = getResources().getDisplayMetrics();
		DENSITY = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
		DENSITY_DPI = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
		SCREEN_H = metric.heightPixels;
		SCREEN_W = metric.widthPixels;
		Log.d(this.getClass().getSimpleName(), "SCREEN_H = " + SCREEN_H + ", SCREEN_W = " + SCREEN_W);
		
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
		//Log.d(this.getClass().getSimpleName(), "structure ——> isFocused = " + isFocused());
		
		sfh = this.getHolder();		// 获取当前SurfaceView的SurfaceHolder
		sfh.addCallback(this);		// 设置当前SurfaceView的回调
	}
	
	public void surfaceCreated(SurfaceHolder holder) {		//Callback
		stateSystem.addState("SplashState", new SplashState(stateSystem));
		stateSystem.addState("MenuState", new MenuState(stateSystem));
		stateSystem.changeState("SplashState");
		
		flag = true;
		new Thread(this).start();
		time[2] = System.currentTimeMillis();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {		//Callback
		Toast.makeText(context, "surfaceChanged", Toast.LENGTH_SHORT).show();
		Log.d(this.getClass().getSimpleName(), "surfaceChanged");
		//flag = false;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {		// Callback
		flag = false;
		// 第三种方法防止退出时异常. 当surfaceView销毁时让线程暂停300ms . 醒来再执行run()方法时,isThreadRunning就是false了. 
        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
	}
	
	public void run() {		// Runnable
		//try {
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
		//} catch(Exception e) {
			//System.out.println(e);
		//}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.i(this.getClass().getSimpleName(), "onKeyDown");
		//Toast.makeText(context, "MainSurfaceView --> onKeyDown", Toast.LENGTH_SHORT).show();
		
		switch(keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.d(this.getClass().getSimpleName(), "onKeyDown ——> up");break;
		
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.d(this.getClass().getSimpleName(), "onKeyDown ——> down");break;
		
		case KeyEvent.KEYCODE_DPAD_LEFT:
			Log.d(this.getClass().getSimpleName(), "onKeyDown ——> left");break;
		
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Log.d(this.getClass().getSimpleName(), "onKeyDown ——> right");break;
			
		case KeyEvent.KEYCODE_BACK:
			Log.d(this.getClass().getSimpleName(), "onKeyDown ——> back");
		}
		
		//return false;		// false是继续做
		//return super.onKeyDown(keyCode, event);
		return stateSystem.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d(this.getClass().getSimpleName(), "onTouchEvent ——> onTouch");
		
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
