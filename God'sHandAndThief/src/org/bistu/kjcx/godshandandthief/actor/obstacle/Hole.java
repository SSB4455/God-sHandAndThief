package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hole extends Obstacle {
	
	
	
	public Hole(Bitmap bitmap) {
		actorBitmap = bitmap;
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		shrink = (MainSurfaceView.SCREEN_H / 2) / (float) frameH;
		
		actorX = MainSurfaceView.SCREEN_W + frameW * (shrink - 1) / 2;
		actorY = Background.FLOOR - MainSurfaceView.SCREEN_W / 9 - frameH - frameH * (shrink - 1) / 2;
		
		type = Obstacle.ObstacleType.Hole;
		//Log.i(this.getClass().toString(), "shrink = " + shrink);
	}
	
	@Override
	public void update(long elapsedTime) {
		if(!isBreak) {
			actorX -= (Businessman.SPEED * elapsedTime) / 1000;
		}
		if(actorX < -(frameW * ( 1 + shrink) / 2)) {
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
