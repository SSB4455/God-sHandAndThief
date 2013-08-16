package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Ground extends GameActor {
	
	private int frameW, frameH;
	private long go_elapsed;
	
	
	
	public Ground(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.super_mario_ground);
		
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		shrink = (MainSurfaceView.SCREEN_H - Background.FLOOR) / (float) frameH;
		
		actorX = frameW * (shrink - 1) / 2;
		actorY = MainSurfaceView.SCREEN_H * 3 / 4 + (MainSurfaceView.SCREEN_H / 4 - frameH) / 2;
		
		Log.i("Ground", "groundShrink = " + shrink + "\nactorX = " + actorX + "  actorY = " + actorY);
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
		if(actorX < MainSurfaceView.SCREEN_W - frameW * (shrink + 1) / 2) {
			actorX = frameW * (shrink - 1) / 2;
		}
		if(actorX > frameW * (shrink - 1) / 2) {
			actorX = MainSurfaceView.SCREEN_W - frameW * (shrink + 1) / 2;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		else
			canvas.scale(shrink, shrink, actorX + frameW /2, actorY + frameH /2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
	
}
