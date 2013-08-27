package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.BitmapStorage;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Canvas;

public class Pit extends Obstacle {
	
	
	
	public Pit() {
		actorBitmap = BitmapStorage.getPit();
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		hight = MainSurfaceView.SCREEN_H - Background.FLOOR;
		shrink = hight / (float) frameH;
		width = (int) (frameW * shrink);
		incrementWHalf = (int) (frameW * (shrink - 1) / 2);
		incrementHHalf = (int) (frameH * (shrink - 1) / 2);
		
		actorX = MainSurfaceView.SCREEN_W + incrementWHalf;
		actorY = Background.FLOOR + incrementHHalf;
		
		type = Obstacle.ObstacleType.Pit;
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
		canvas.save();
		if(Background.FACE_TO == Background.TO_RIGHT)
			canvas.scale(-shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		else
			canvas.scale(shrink, shrink, actorX + frameW / 2, actorY + frameH / 2);
		canvas.drawBitmap(actorBitmap, actorX, actorY, paint);
		canvas.restore();
	}
	
}
