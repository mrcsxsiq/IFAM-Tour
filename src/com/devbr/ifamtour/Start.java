package com.devbr.ifamtour;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Start extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	 
 	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
       
       Typeface tf = Typeface.createFromAsset(getAssets(), "font/markerfeltnormal.ttf");  
        
       TextView touchhere = (TextView)findViewById(R.id.touchherebutton);
       touchhere.setTypeface(tf);
       touchhere.setTextSize(64);
    //   touchhere.setShadowLayer(20, 20, 20, 3);

       touchhere.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent it = new Intent(Start.this,Game.class);
				Start.this.startActivity(it);
				Start.this.finish();
			}	
       });
       
       ImageView info = (ImageView)findViewById(R.id.aboutbutton);
       info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent it = new Intent(Start.this,About.class);
				Start.this.startActivity(it);
				//Start.this.finish();
			}	
       });
       
    }
}