package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Paint;

public class Obstacle extends GameActor {
	public enum ObstacleType {
		Hole,		//¶´
		Stone,		//Ê¯Í·
	}
	protected ObstacleType type;
	
	int frameW, frameH;
	long go_elapsed;
	boolean isBreak;
	
	
	
	public Obstacle() {
		go_elapsed = 0;
		isBreak = false;
		
		paint = new Paint();
	}
	
	public Obstacle(String name) {
		super(name);
		
	}
	
}
