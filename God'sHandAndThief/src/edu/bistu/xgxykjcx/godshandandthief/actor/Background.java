package edu.bistu.xgxykjcx.godshandandthief.actor;

import edu.bistu.xgxykjcx.godshandandthief.GHTMainActivity;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Background extends GameActor {
	
	public static int FACE_TO = 0;
	public static final int TO_RIGHT = 1;
	public static final int TO_LEFT = 0;
	public static final int FLOOR = GHTSurfaceView.SCREEN_H * 3 / 4;
	
	private Ground ground;
	private Cloud cloud;
	
	
	
	public Background() {
		cloud = new Cloud(GHTMainActivity.CONTEXT);
		ground = new Ground(GHTMainActivity.CONTEXT);
		
		Log.i(this.getClass().getSimpleName(), "FLOOR = " + FLOOR);
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		cloud.update(elapsedTime);
		ground.update(elapsedTime);
	}
	
	@Override
	public void render(Canvas canvas) {
		paint.setARGB(255, 0, 0, 150);
		canvas.drawColor(paint.getColor());
		
		cloud.render(canvas);
		ground.render(canvas);
	}

	@Override
	public float getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
