package org.bistu.xgxykjcx.godshandandthief.actor.obstacle;

import org.bistu.xgxykjcx.godshandandthief.actor.GameActor;

import android.graphics.Paint;

public class Obstacle extends GameActor {
	// 障碍种类的代码
	public static final int HOLE = 0;
	public static final int STONE = 1;
	public static final int PIT = 2;
	
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
