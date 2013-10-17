package edu.bistu.xgxykjcx.godshandandthief.actor.obstacle;


import edu.bistu.xgxykjcx.godshandandthief.BitmapStorage;
import edu.bistu.xgxykjcx.godshandandthief.GHTSurfaceView;
import edu.bistu.xgxykjcx.godshandandthief.actor.Background;
import edu.bistu.xgxykjcx.godshandandthief.actor.Businessman;
import edu.bistu.xgxykjcx.godshandandthief.actor.GameActor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hole extends Obstacle {
	private int interval;
	
	
	
	public Hole() {
		this(BitmapStorage.getHole());
		
	}
	
	public Hole(Bitmap bitmap) {
		actorBitmap = bitmap;
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		hight = GHTSurfaceView.SCREEN_H / 2;
		shrink = hight / (float) frameH;
		width = (int) (frameW * shrink);
		incrementWHalf = (int) (frameW * (shrink - 1) / 2);
		incrementHHalf = (int) (frameH * (shrink - 1) / 2);
		
		actorX = GHTSurfaceView.SCREEN_W + incrementWHalf;
		interval = GHTSurfaceView.SCREEN_W / 9;
		actorY = Background.FLOOR - interval - frameH - incrementHHalf;
		
		obstacleType = Obstacle.HOLE;
	}
	
	public Hole(ActorStatus status) {
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
