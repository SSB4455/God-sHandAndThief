package org.bistu.kjcx.godshandandthief.actor;

import java.util.ArrayList;
import java.util.HashMap;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.GameActor;
import org.bistu.kjcx.godshandandthief.actor.GameActor.ActorStatus;
import org.bistu.kjcx.godshandandthief.actor.obstacle.*;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle.ObstacleType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class GodLayout extends GameActor {
	class RecordObstacle {
		boolean inScreen;
		long position;
		float actorX;
		ObstacleType type;
		
		RecordObstacle(long position, ObstacleType type) {
			this.position = position;
			this.type = type;
		}
		
	}
	
	public static int READ;
	public static long INTERVAL_LONG = 1000;
	private Context context;
	private long screenLong;
	
	private ProgressBar progressBar;
	private HashMap<ObstacleType, Obstacle> obstacleStorage;
	private ArrayList<RecordObstacle> obstacleLayout;		//obstacleSupervisor
	
	
	
	public GodLayout(Context context) {
		super("God Layout");
		this.context = context;
		
		READ = 1;
		progressBar = new ProgressBar();
		
		/*
		screenLong = (long) (MainSurfaceView.SCREEN_W / (float) Businessman.SPEED * 1000);
		Log.i(this.getClass().toString(), "screenLong = " + screenLong);
		
		obstacleStorage = new HashMap<ObstacleType, Obstacle>();
		obstacleLayout = new ArrayList<RecordObstacle>();
		
		Obstacle hole = new Hole(BitmapFactory.decodeResource(context.getResources(), R.drawable.hole));
		Obstacle stone = new Stone(BitmapFactory.decodeResource(context.getResources(), R.drawable.stone));
		obstacleStorage.put(ObstacleType.Hole, hole);
		obstacleStorage.put(ObstacleType.Stone, stone);
		
		paint = new Paint();
		*/
	}
	
	public boolean p() {
		READ--;
		if(READ >= 0)
			return true;
		Log.d(this.getClass().toString(), "READ = " + READ);
		return false;
	}
	
	public void v() {
		READ++;
	}
	
	@Override
	public void update(long elapsedTime) {
		//while(!p())
		//	v();
		//p();
		//int size = children.size();
		for(int i = 0; i < getObstacleSize(); i++)
			children.get(i).update(elapsedTime);
		//super.update(elapsedTime);
		//v();
		
		progressBar.update(elapsedTime);
		
		//cleanUpDead();
		
		
		/*
		long progressL = progressBar.getProgressL();
		for(RecordObstacle obstacleRecord: obstacleLayout) {
			if(obstacleRecord.position < progressL 
					&& progressL < obstacleRecord.position + screenLong) {
				obstacleRecord.inScreen = true;
				obstacleRecord.actorX = MainSurfaceView.SCREEN_W - (progressL - obstacleRecord.position) / 1000 * Businessman.SPEED;
				//Log.i(this.getClass().toString(), "a obstacle position = " + obstacleRecord.position + " type = " + obstacleRecord.type);
			} else {
				obstacleRecord.inScreen = false;
			}
		}
		*/
	}
	
	@Override
	public void render(Canvas canvas) {
		//while(!p())
		//	v();
		//p();
		//int size = children.size();
		for(int i = 0; i < getObstacleSize(); i++)
			children.get(i).render(canvas);
		//super.render(canvas);
		//v();
		
		progressBar.render(canvas);
		
		
		/*
		for(RecordObstacle obstacleRecord: obstacleLayout) {
			if(obstacleRecord.inScreen) {
				Obstacle obstacle = obstacleStone.get(obstacleRecord.type);
				obstacle.actorX = obstacleRecord.actorX;
				obstacle.render(canvas);
			}
		}
		*/
	}
	
	public void addObstacle(long position, ObstacleType type) {
		//obstacleLayout.add(new RecordObstacle(position, type));
		
		
		Obstacle obstacle = null;
		switch(type) {
		case Hole : 
			obstacle = new Hole();
			break;
		case Stone:
			obstacle = new Stone(BitmapFactory.decodeResource(context.getResources(), R.drawable.stone));
			break;
		case Pit:
			obstacle = new Pit();
			break;
		default:
			obstacle = new Pit();
			break;
		}
		obstacle.actorX = position / 1000 * Businessman.SPEED;
		
		//while(!p())
		//	v();
		Log.d(this.getClass().toString(), "add obstacle before READ = " + READ);
		p();
		Log.d(this.getClass().toString(), "add obstacle in READ = " + READ);
		children.add(obstacle);
		Log.d(this.getClass().toString(), "add obstacle end READ = " + READ);
		v();
		Log.d(this.getClass().toString(), "add obstacle edd READ = " + READ);
	}
	
	@Override
	public void cleanUpDead() {
		// TODO Auto-generated method stub
		//super.cleanUpDead();
		
		//while(!p())
		//	v();
		p();
		ArrayList<GameActor> deadList = new ArrayList<GameActor>();
		for(GameActor actorChild : children)
			if(actorChild.status == ActorStatus.Dead)
				deadList.add(actorChild);
		for(GameActor actorChild : deadList)
			children.remove(actorChild);
		v();
		
		//Log.d(this.getClass().toString(), "children.size = " + children.size());
	}
	
	public void clear() {
		cleanUpDead();
		
		//while(!p())
		//	v();
		p();
		children.clear();
		v();
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
		//while(!p())
		//	v();
		p();
		int size = children.size();
		v();
		return size;
	}
	
	public Obstacle getObstacle(int i) {
		Obstacle obstacle;
		//while(!p())
		//	v();
		p();
		if(0 < i && i < children.size())
			obstacle = (Obstacle) children.get(i);
		else
			obstacle = (Obstacle) children.get(0);
		v();
		return obstacle;
	}
	
	@Override
	public int getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getRight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
