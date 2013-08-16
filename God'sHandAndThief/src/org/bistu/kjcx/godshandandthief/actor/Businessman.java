package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Businessman extends GameActor {
	
	public static int SPEED = 300;
	
	int health, frameW, frameH, currentFrame = 0;
	long go_elapsed = 0;
	
	boolean [] fling;
	
	Bitmap [] frame;
	
	
	
	public Businessman(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run);
		frame = new Bitmap[5];
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight() / frame.length;
		Log.i("Businessman", "frame.length = " + frame.length);
		for(int i = 0; i < frame.length; i++) {
			Log.i("Businessman", "actorBitmap.getWidth() = " + actorBitmap.getWidth() + ", actorBitmap.getHeight() = " + actorBitmap.getHeight());
			frame[i] = Bitmap.createBitmap(actorBitmap, 0, frameH * i, frameW, frameH);
			Log.i("Businessman", "frame[" + i + "] = " + 0 + "," + frameH * i + "," + frameW + "," + frameH);
		}
		currentFrame = 0;
		Log.i("Businessman", "currentFrame = " + currentFrame);
		
		shrink = (MainSurfaceView.SCREEN_H / 5) / (float) frameH;
		
		actorX = MainSurfaceView.SCREEN_W / 5 + frameW * (shrink - 1) / 2;
		actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
		
		fling = new boolean[6];
		
		health = 3;
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(go_elapsed > 50) {
			Log.i("Businessman", "go_elapsed = " + go_elapsed);
			currentFrame = ++currentFrame % frame.length;
			go_elapsed = 0;
			
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		canvas.drawBitmap(frame[currentFrame], actorX, actorY, paint);
		canvas.restore();
	}
}
