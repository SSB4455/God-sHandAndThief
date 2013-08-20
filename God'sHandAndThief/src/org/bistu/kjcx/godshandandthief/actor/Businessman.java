package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Businessman extends GameActor {
	
	public static int SPEED = 300;
	
	int health, frameW, frameH, currentFrame;
	private long go_elapsed;
	
	boolean [] fling;
	
	private Bitmap [] frame;
	
	
	
	public Businessman(Context context) {
		
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.businessman_run);
		frame = new Bitmap[5];
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight() / frame.length;
		for(int i = 0; i < frame.length; i++) {
			frame[i] = Bitmap.createBitmap(actorBitmap, 0, frameH * i, frameW, frameH);
		}
		currentFrame = 0;
		go_elapsed = 0;
		
		shrink = (MainSurfaceView.SCREEN_H / 4) / (float) frameH;
		
		actorX = MainSurfaceView.SCREEN_W / 5 + frameW * (shrink - 1) / 2;
		actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
		
		fling = new boolean[6];
		
		health = 3;
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(go_elapsed > 80) {
			//Log.i(this.getClass().toString(), "go_elapsed = " + go_elapsed);
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
