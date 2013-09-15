package org.bistu.xgxykjcx.godshandandthief.actor;

import java.util.ArrayList;
import java.util.Random;

import org.bistu.xgxykjcx.godshandandthief.actor.GameActor;
import org.bistu.xgxykjcx.godshandandthief.actor.obstacle.*;

import android.graphics.Canvas;
import android.util.Log;

public class GodLayout extends GameActor {
	
	// �����ʱ�䳤��
	public static long INTERVAL_LONG = 1000;
	
	private boolean isLink;
	//private long screenLong;
	
	private ProgressBar progressBar;
	
	
	
	public GodLayout() {
		super("God Layout");
		
		progressBar = new ProgressBar();
		
	}
	
	@Override
	public void update(long elapsedTime) {
		// update all obstacle
		for(int i = 0; i < getObstacleSize(); i++)
			children.get(i).update(elapsedTime);
		
		progressBar.update(elapsedTime);
		
		//cleanUpDead();
		
	}
	
	@Override
	public void render(Canvas canvas) {
		// render all obstacle
		for(int i = 0; i < getObstacleSize(); i++)
			children.get(i).render(canvas);
		
		progressBar.render(canvas);
		
	}
	
	public static GodLayout createAutoGodLayout(int level) {
		Random random = new Random();
		GodLayout godLayout = new GodLayout();
		level = level % 10;
		
		long interval = GodLayout.INTERVAL_LONG;		// ���ʱ��
		long partLong = (ProgressBar.TOTAL_Long - 4000 - interval * level) / level;		//���4s��Ϊ�˸���һ���ϰ��ļ��Ԥ��2sΪ���һ���ϰ�Ԥ��2s
		for(int i = 0; i < level; i++) {
			int obstacleType = -1;
			if(i % 2 == 0)
				obstacleType = Obstacle.HOLE;
			else
				obstacleType = Obstacle.PIT;
			
			// ��һ���ϰ������2s��׼��ʱ��
			long position = 2000 + i * (partLong + interval) + random.nextInt((int) partLong);
			
			godLayout.addObstacle(position, obstacleType);
		}
		return godLayout;
	}
	
	/**
	 * ���һ���ϰ�
	 * @param position λ��
	 * @param type �ϰ�������
	 */
	public void addObstacle(long position, int type) {
		Obstacle obstacle = null;
		switch(type) {
		case Obstacle.HOLE :
			obstacle = new Hole();
			break;
		case Obstacle.STONE:
		case Obstacle.PIT:
			obstacle = new Pit();
			break;
		}
		if(obstacle != null) {
			obstacle.setLeft(position * Businessman.SPEED);
			children.add(obstacle);
			Log.i("GodLayout", "add a obstacle, position = " + position + " type = " + obstacle.getTypeString());
		}
	}
	
	@Override
	public void cleanUpDead() {
		
		ArrayList<GameActor> deadList = new ArrayList<GameActor>();
		for(GameActor actorChild : children)
			if(actorChild.status == ActorStatus.Dead)
				deadList.add(actorChild);
		for(GameActor actorChild : deadList)
			children.remove(actorChild);
		
		//Log.d(this.getClass().toString(), "cleanUpDead() and children.size = " + children.size());
	}
	
	public void clear() {
		cleanUpDead();
		
		children.clear();
	}
	
	public boolean setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
		if(this.progressBar == progressBar)
			return true;
		return false;
	}
	
	public ProgressBar zeroProgressBar() {
		progressBar.zreo();
		return progressBar;
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	public int getObstacleSize() {
		int size = children.size();
		return size;
	}
	
	public Obstacle getObstacle(int i) {
		return (Obstacle) children.get((0 <= i && i < children.size()) ? i : 0);
	}
	
	public boolean isLink() {
		return isLink;
	}
	
	@Override
	public float getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public float getRight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/*
	class RecordObstacle {
		boolean inScreen;
		long position;
		float actorX;
		int type;
		
		RecordObstacle(long position, int type) {
			this.position = position;
			this.type = type;
		}
	}*/
	
}
