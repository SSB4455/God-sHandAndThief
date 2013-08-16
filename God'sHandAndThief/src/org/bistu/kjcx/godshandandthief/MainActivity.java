package org.bistu.kjcx.godshandandthief;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
		setContentView(mainSurfaceView);
	}
	
}
