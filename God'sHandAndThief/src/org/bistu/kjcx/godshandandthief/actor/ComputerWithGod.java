package org.bistu.kjcx.godshandandthief.actor;

import java.util.Random;

import org.bistu.kjcx.godshandandthief.actor.obstacle.Obstacle.ObstacleType;

import android.content.Context;
import android.util.Log;

public class ComputerWithGod {
	Context context;
	
	Random random;
	GodLayout godLayout;
	
	 
	 
	 public ComputerWithGod(Context context) {
		this.context = context;
		random = new Random();
		
	}
	 
	public ComputerWithGod(GodLayout godLayout) {
		this.godLayout = godLayout;
		random = new Random();
	}
	
	public GodLayout createLayout(int level) {		//难度只有0-9
		godLayout = godLayout != null ? godLayout : new GodLayout(context);
		level = level % 10;
		long interval = 2000;		//至少间隔2秒
		long partLong = (ProgressBar.TOTAL_Long - interval * level) / level;
		for(int i = 0; i < level; i++) {
			long position = i * (partLong + interval) + random.nextInt((int) partLong);
			godLayout.addObstacle(position, i % 2 == 0 ? ObstacleType.Stone : ObstacleType.Hole);
			Log.i(this.getClass().toString(), "create a obstacle, position = " + position + " type = " + (i % 2 == 0 ? ObstacleType.Stone + "" : ObstacleType.Hole + ""));
		}
		
		return godLayout;
	}
}
