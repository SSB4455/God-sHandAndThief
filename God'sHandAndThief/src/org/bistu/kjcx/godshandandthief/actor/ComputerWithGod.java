package org.bistu.kjcx.godshandandthief.actor;

import java.util.Random;

import org.bistu.kjcx.godshandandthief.MainSurfaceView;
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
		long over = 0;
		for(int i = 0; i < level; i++) {
			long position = over + random.nextInt((int) ((ProgressBar.TOTAL_Long - over) / (level - i + 1)));
			godLayout.addObstacle(position, i % 2 == 0 ? ObstacleType.Stone : ObstacleType.Hole);
			over += (position - over) + MainSurfaceView.SCREEN_W / 2;
			Log.i(this.getClass().toString(), "create a obstacle,over = " + over + " position = " + position + " type = " + (i % 2 == 0 ? ObstacleType.Stone + "" : ObstacleType.Hole + ""));
		}
		
		return godLayout;
	}
}
