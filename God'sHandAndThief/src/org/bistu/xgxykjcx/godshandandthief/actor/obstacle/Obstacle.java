package org.bistu.xgxykjcx.godshandandthief.actor.obstacle;

import org.bistu.xgxykjcx.godshandandthief.actor.GameActor;

import android.graphics.Paint;

public class Obstacle extends GameActor {
	// 障碍种类的代码
	public static final int HOLE = 0;
	public static final int STONE = 1;
	public static final int PIT = 2;
	public static final String HOLE_STRING = "hole";
	public static final String STONE_STRING = "stone";
	public static final String PIT_STRING = "pit";
	
	protected int frameW, frameH, incrementWHalf, incrementHHalf;
	protected int obstacleType;
	protected boolean isBreak;
	
	
	
	public Obstacle() {
		isBreak = false;
		
		paint = new Paint();
	}
	
	public Obstacle(String name) {
		super(name);
		
	}
	
	public int getType() {
		return obstacleType;
	}
	public String getTypeString() {
		if(obstacleType == HOLE)
			return HOLE_STRING;
		if(obstacleType == PIT)
			return PIT_STRING;
		return null;
	}
	
	public float setLeft(float left) {
		return actorX = left + incrementWHalf;
	}
	
	public float setTop(float top) {
		return actorY = top + incrementWHalf;
	}
	
	@Override
	public float getLeft() {
		return actorX - incrementWHalf;
	}
	
	@Override
	public float getRight() {
		return actorX + frameW + incrementWHalf;
	}
	
}
