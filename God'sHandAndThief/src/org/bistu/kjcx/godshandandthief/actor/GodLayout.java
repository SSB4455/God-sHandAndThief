package org.bistu.kjcx.godshandandthief.actor;

import java.util.ArrayList;
import java.util.HashMap;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
import org.bistu.kjcx.godshandandthief.R;
import org.bistu.kjcx.godshandandthief.actor.GameActor;
import org.bistu.kjcx.godshandandthief.actor.obstacle.*;
import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle.ObstacleType;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class GodLayout extends GameActor {
	Context context;
	
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
	
	private long screenLong;
	
	private ProgressBar progressBar;
	private HashMap<ObstacleType, Obstacle> obstacleStone;
	private ArrayList<RecordObstacle> obstacleLayout;		//obstacleSupervisor
	
	
	
	public GodLayout(Context context) {
		super("God Layout");
		this.context = context;
		
		screenLong = (long) (MainSurfaceView.SCREEN_W / (float) Businessman.SPEED * 1000);
		Log.i(this.getClass().toString(), "screenLong = " + screenLong);
		
		obstacleStone = new HashMap<ObstacleType, Obstacle>();
		obstacleLayout = new ArrayList<RecordObstacle>();
		
		Obstacle hole = new Hole(BitmapFactory.decodeResource(context.getResources(), R.drawable.hole));
		Obstacle stone = new Stone(BitmapFactory.decodeResource(context.getResources(), R.drawable.stone));
		obstacleStone.put(ObstacleType.Hole, hole);
		obstacleStone.put(ObstacleType.Stone, stone);
		
		paint = new Paint();
	}
	
	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
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
		super.render(canvas);
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
		obstacleLayout.add(new RecordObstacle(position, type));
		
		
		Obstacle obstacle;
		switch(type) {
		case Hole : 
			obstacle = new Hole(BitmapFactory.decodeResource(context.getResources(), R.drawable.hole));
			obstacle.actorX = position / 1000 * Businessman.SPEED;
			children.add(obstacle);
			break;
		case Stone:
			obstacle = new Stone(BitmapFactory.decodeResource(context.getResources(), R.drawable.stone));
			obstacle.actorX = position / 1000 * Businessman.SPEED;
			children.add(obstacle);
			break;
		default:
			break;
		}
		
	}
	
	public boolean setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
		if(this.progressBar == progressBar)
			return true;
		return false;
	}
	
}
