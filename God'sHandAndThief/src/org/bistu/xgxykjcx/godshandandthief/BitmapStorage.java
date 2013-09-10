package org.bistu.xgxykjcx.godshandandthief;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapStorage {
	static Resources RESOURCES;
	
	Bitmap bitmap;
	
	
	
	public BitmapStorage(Context context) {
	}
	
	public static void setContext(Context context) {
		RESOURCES = context.getResources();
	}
	
	public static void setResources(Resources resources) {
		BitmapStorage.RESOURCES = resources;
	}
	
	public static Bitmap getBusinessmanRun() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.businessman_run);
		else
			return null;
	}
	
	public static Bitmap getPit() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.pit);
		else
			return null;
	}
	
	public static Bitmap getPitMenu() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.pit_menu);
		else
			return null;
	}
	
	public static Bitmap getCatchComputer() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.catch_computer);
		else
			return null;
	}
	
	public static Bitmap getCatchThief() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.catch_thief);
		else
			return null;
	}
	
	public static Bitmap getGodHappy() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.god_happy);
		else
			return null;
	}
	
	public static Bitmap getImGodhand() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.im_godhand_catch_thief);
		else
			return null;
	}
	
	public static Bitmap getLose() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.lose);
		else
			return null;
	}
	
	public static Bitmap getWin() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.win);
		else
			return null;
	}
	
	public static Bitmap getWaitMoment() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.wait_a_moment);
		else
			return null;
	}
	
	public static Bitmap getHole() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.hole);
		else
			return null;
	}
	
	public static Bitmap getHoleMenu() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.hole_menu);
		else
			return null;
	}
	
	
	
	
	
	
	
	
}
