package org.bistu.kjcx.godshandandthief.actor.obstacle;

import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.graphics.Paint;

public class Obstacle extends GameActor {
	public enum ObstacleType {
		Hole,		//¶´
		Stone,		//Ê¯Í·
		Pit,		//¿Ó
	}
	protected ObstacleType obstacleType;
	
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
		return obstacleType;
	}
	
	public int setLeft(int left) {
		return (int) (actorX = left + incrementWHalf);
	}
	
	public int setTop(int top) {
		return (int) (actorY = top + incrementWHalf);
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
