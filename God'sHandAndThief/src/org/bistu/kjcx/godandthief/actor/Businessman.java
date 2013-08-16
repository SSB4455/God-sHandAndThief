package org.bistu.kjcx.godandthief.actor;

import org.bistu.kjcx.godandthief.MainSurfaceView;
import org.bistu.kjcx.godandthief.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Businessman extends GameActor {
	
	public static int SPEED = 300;
	
	int health;
	int floor, speed = 80;
	
	int currentFrame = 0;
	int frameW, frameH;
	
	long go_elapsed = 0;
	
	boolean [] fling;
	
	
	
	public Businessman(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run);
		
		frameW = actorBitmap.getWidth() / 4;
		frameH = actorBitmap.getHeight() / 4;
		
		floor = MainSurfaceView.SCREEN_H * 5 / 8;
		
		actorX = 200;
		actorY = floor - frameH;
		
		
		currentFrame = 0;
		
		fling = new boolean[6];
		
		paint = new Paint();
		
	}
	
	@Override
	public void update(long elapsedTime) {
		
		//Log.i("fishMan", "elapsedTime = " + elapsedTime);
		go_elapsed += elapsedTime;
		if(go_elapsed > 50){
			Log.i("fishMan", "go_elapsed = " + go_elapsed);
			currentFrame = ++currentFrame % 4;
			if(Background.FACE_TO == Background.TO_LEFT)
				actorX -= (speed * go_elapsed) / 1000;
			else
				actorX += (speed * go_elapsed) / 1000;
			go_elapsed = 0;
			
		}
		
	}
	
	@Override
	public void render(Canvas canvas) {
		
		canvas.save();
		canvas.clipRect(actorX, actorY, actorX + frameW, actorY + frameH);
		if(Background.FACE_TO == Background.TO_LEFT){
			canvas.scale(-1f, 1f, actorX - (currentFrame * frameW) + actorBitmap.getWidth() /2, actorY - (2 * frameH) - 1 + actorBitmap.getHeight() /2);
			
		}
		canvas.drawBitmap(actorBitmap, actorX - (currentFrame * frameW), actorY - (2 * frameH) - 1, paint);
		
		//Log.i("fishMan.render", "actorBitmap.x = " + (screenW - actorX));
		canvas.restore();
		
		
	}
}
