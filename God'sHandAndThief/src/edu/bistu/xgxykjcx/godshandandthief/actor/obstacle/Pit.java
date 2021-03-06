package edu.bistu.xgxykjcx.godshandandthief.actor.obstacle;


import edu.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;
import edu.bistu.xgxykjcx.godshandandthief.actor.Background;
import edu.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import edu.bistu.xgxykjcx.godshandandthief.actor.GameActor;

import android.graphics.Canvas;

public class Pit extends Obstacle {
	
	
	
	public Pit() {
		actorBitmap = BitmapStorage.getPit();
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		hight = GHTSurfaceView.SCREEN_H - Background.FLOOR;
		shrink = hight / (float) frameH;
		width = (int) (frameW * shrink);
		incrementWHalf = (int) (frameW * (shrink - 1) / 2);
		incrementHHalf = (int) (frameH * (shrink - 1) / 2);
		
		actorX = GHTSurfaceView.SCREEN_W + incrementWHalf;
		actorY = Background.FLOOR + incrementHHalf;
		
		obstacleType = Obstacle.PIT;
	}
	
	public Pit(ActorStatus status) {
		this();
		this.status = status;
	}
	
	@Override
	public void update(long elapsedTime) {
		if(status == ActorStatus.Action) {
			if(!isBreak) {
				actorX -= Businessman.SPEED * elapsedTime;
			}
			if(getRight() < 0) {
				status = GameActor.ActorStatus.Dead;
			}
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
