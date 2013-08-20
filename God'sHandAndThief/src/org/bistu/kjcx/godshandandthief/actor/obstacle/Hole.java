package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.actor.Background;
import org.bistu.kjcx.godshandandthief.actor.Businessman;
import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Hole extends Obstacle {
	
	int frameW, frameH;
	long go_elapsed;
	boolean isBreak;
	
	
	
	public Hole(Context context) {
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hole);
		frameW = actorBitmap.getWidth();
		frameH = actorBitmap.getHeight();
		
		shrink = (MainSurfaceView.SCREEN_H / 4) / (float) frameH;
		
		actorX = MainSurfaceView.SCREEN_W + frameW * (shrink - 1) / 2;
		actorY = Background.FLOOR - frameH - frameH * (shrink - 1) / 2;
		
		type = Obstacle.ObstacleType.Hole;
	}
	
	@Override
	public void update(long elapsedTime) {
		go_elapsed += elapsedTime;
		if(!isBreak && go_elapsed > 50) {
			actorX -= (Businessman.SPEED * go_elapsed) / 1000;
			go_elapsed = 0;
		}
		if(actorX < -(frameW * ( 1 + shrink) / 2)) {
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
