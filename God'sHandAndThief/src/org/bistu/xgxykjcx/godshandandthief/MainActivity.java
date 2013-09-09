package org.bistu.xgxykjcx.godshandandthief;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {
	public static Context CONTEXT;
	public RelativeLayout relativeLayout;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CONTEXT = this;
		BitmapStorage.setResources(this.getResources());
		
		relativeLayout = new RelativeLayout(this);
		MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
		setContentView(relativeLayout);
		relativeLayout.addView(mainSurfaceView);
		
	}
	
}
