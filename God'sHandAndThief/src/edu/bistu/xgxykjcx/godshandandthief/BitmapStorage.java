package edu.bistu.xgxykjcx.godshandandthief;

import edu.bistu.xgxykjcx.godshandandthief.R;

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
	
	public static Bitmap getBitmap(int RdrawableID) {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, RdrawableID);
		else
			return null;
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
	
	public static Bitmap getGround() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.super_mario_ground);
		else
			return null;
	}
	
	public static Bitmap getCloud() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.super_mario_cloud);
		else
			return null;
	}
	
	public static Bitmap getBeat() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.beta);
		else
			return null;
	}
	
	public static Bitmap getThief() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.thief);
		else
			return null;
	}
	
	public static Bitmap getGodHand() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.god_hand);
		else
			return null;
	}
	
	public static Bitmap getImThiefComeOn() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.im_thief_come_on);
		else
			return null;
	}
	
	public static Bitmap getThiefHappy() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.thief_happy);
		else
			return null;
	}
	
	public static Bitmap getGodPursueMe() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.god_pursue_me);
		else
			return null;
	}
	
	public static Bitmap getComputerPursueMe() {
		if(RESOURCES != null)
			return BitmapFactory.decodeResource(RESOURCES, R.drawable.computer_pursue_me);
		else
			return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
