package org.bistu.kjcx.godandthief.actor;

import org.bistu.kjcx.godandthief.MainSurfaceView;
import org.bistu.kjcx.godandthief.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Ground extends GameActor {
	
	private int frameW, frameH;
	private float groundShrink;
	private long go_elapsed;
	
	
	
	public Ground(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.super_mario_ground);
		
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		groundShrink = (MainSurfaceView.SCREEN_H / 4) / (float) frameH;
		
		actorX = 0;
		actorY = MainSurfaceView.SCREEN_H * 3 / 4 + (MainSurfaceView.SCREEN_H / 4 - frameH) / 2;
		
		Log.i("Ground", "groundShrink = " + groundShrink + "\nactorX = " + actorX + "\tactorY = " + actorY);
		paint = new Paint();
		
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(go_elapsed > 50) {
			if(Background.FACE_TO == Background.TO_LEFT)
				actorX -= (Businessman.SPEED * go_elapsed) / 1000;
			else
				actorX += (Businessman.SPEED * go_elapsed) / 1000;
			go_elapsed = 0;
		}
		
		//³¬³öÆÁÄ»×ª»Ø
		if(actorX < MainSurfaceView.SCREEN_W - frameW * (groundShrink + 1) / 2) {
			actorX = frameW * (groundShrink - 1) / 2;
		}
		if(actorX > frameW * (groundShrink - 1) / 2) {
			actorX = MainSurfaceView.SCREEN_W - frameW * (groundShrink + 1) / 2;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-groundShrink, groundShrink, actorX + frameW /2, actorY + frameH /2);
		else
			canvas.scale(groundShrink, groundShrink, actorX + frameW /2, actorY + frameH /2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
	
}
