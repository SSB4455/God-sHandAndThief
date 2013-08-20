package org.bistu.kjcx.godshandandthief.actor;

import org.bistu.kjcx.godshandandthief.actor.GameActor;

import android.content.Context;
import android.graphics.Paint;

public class GodLayout extends GameActor {
	
	int frameW, frameH;
	long go_elapsed;
	boolean isBreak;
	
	
	
	public GodLayout() {
		go_elapsed = 0;
		isBreak = false;
		
		paint = new Paint();
	}
	
}
