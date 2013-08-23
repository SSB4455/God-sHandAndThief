package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Paint;

public class Obstacle extends GameActor {
	public enum ObstacleType {
		Hole,		//¶´
		Stone,		//Ê¯Í·
	}
	protected ObstacleType type;
	
	protected int frameW, frameH, incrementWHalf, incrementHHalf;
	protected boolean isBreak;
	
	
	
	public Obstacle() {
		isBreak = false;
		
		paint = new Paint();
	}
	
	public Obstacle(String name) {
		super(name);
		
	}
	
	public ObstacleType getType() {
		return type;
	}
	
	@Override
	public int getLeft() {
		return (int) (actorX - incrementWHalf);
	}
	
	@Override
	public int getRight() {
		return (int) (actorX + frameW + incrementWHalf);
	}
	
}
