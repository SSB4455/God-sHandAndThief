package org.bistu.kjcx.godandthief.actor;

import org.bistu.kjcx.godandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background extends GameActor {
	
	public static int FACE_TO = 0;
	public static final int TO_RIGHT = 1;
	public static final int TO_LEFT = 0;
	public static final int FLOOR = MainSurfaceView.SCREEN_H * 3 / 4;
	
	private Ground ground;
	private Cloud cloud;
	
	
	
	public Background(Context context) {
		cloud = new Cloud(context);
		ground = new Ground(context);
		
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
}
