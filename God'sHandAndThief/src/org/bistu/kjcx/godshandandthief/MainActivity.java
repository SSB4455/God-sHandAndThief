package org.bistu.kjcx.godshandandthief;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {
	public static Context CONTEXT;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CONTEXT = this;
		BitmapStorage.setResources(this.getResources());
		
		MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
		setContentView(mainSurfaceView);
		
	}
	
}
