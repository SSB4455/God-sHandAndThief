package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hole extends Obstacle {
	private int interval;
	
	
	
	public Hole(Bitmap bitmap) {
		actorBitmap = bitmap;
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		hight = MainSurfaceView.SCREEN_H / 2;
		shrink = hight / (float) frameH;
		width = (int) (frameW * shrink);
		incrementWHalf = (int) (frameW * (shrink - 1) / 2);
		incrementHHalf = (int) (frameH * (shrink - 1) / 2);
		
		actorX = MainSurfaceView.SCREEN_W + incrementWHalf;
		interval = MainSurfaceView.SCREEN_W / 9;
		actorY = Background.FLOOR - interval - frameH - incrementHHalf;
		
		type = Obstacle.ObstacleType.Hole;
		//Log.i(this.getClass().toString(), "shrink = " + shrink);
	}
	
	@Override
	public void update(long elapsedTime) {
		if(!isBreak) {
			actorX -= (Businessman.SPEED * elapsedTime) / 1000;
		}
		if(getRight() < 0) {
			status = GameActor.ActorStatus.Dead;
		}
	}
	
	@Override
	public void render(Canvas canvas) {
		//Log.i(this.getClass().toString(), "yes Hole render, actorX = " + actorX + " actorY = " + actorY);
		
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
}
