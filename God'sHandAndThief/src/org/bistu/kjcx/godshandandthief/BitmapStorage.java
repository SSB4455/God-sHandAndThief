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
	
}
