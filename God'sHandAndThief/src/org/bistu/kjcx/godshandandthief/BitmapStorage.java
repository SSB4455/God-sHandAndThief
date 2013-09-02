package org.bistu.kjcx.godshandandthief;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapStorage {
	Context context;
	static Resources resources;
	
	Bitmap bitmap;
	
	
	
	public BitmapStorage(Context context) {
		this.context = context;
		resources = context.getResources();
	}
	
	public static void setContext(Context context) {
		resources = context.getResources();
	}
	
	public static void setResources(Resources resources) {
		BitmapStorage.resources = resources;
	}
	
	public static Bitmap getBusinessmanRun() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.businessman_run);
		else
			return null;
	}
	
	public static Bitmap getPit() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.pit);
		else
			return null;
	}
	
	public static Bitmap getPitMenu() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.pit_menu);
		else
			return null;
	}
	
	public static Bitmap getCatchComputer() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.catch_computer);
		else
			return null;
	}
	
	public static Bitmap getCatchThief() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.catch_thief);
		else
			return null;
	}
	
	public static Bitmap getGodHappy() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.god_happy);
		else
			return null;
	}
	
	public static Bitmap getImGodhand() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.im_godhand_catch_thief);
		else
			return null;
	}
	
	public static Bitmap getWaitMoment() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.wait_a_moment);
		else
			return null;
	}
	
	public static Bitmap getHole() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.hole);
		else
			return null;
	}
	
	public static Bitmap getHoleMenu() {
		if(resources != null)
			return BitmapFactory.decodeResource(resources, R.drawable.hole_menu);
		else
			return null;
	}
	
	
	
	
	
	
	
	
}
