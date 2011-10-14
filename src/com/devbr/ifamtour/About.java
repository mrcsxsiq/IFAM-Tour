package com.devbr.ifamtour;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class About extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	 
 	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
       super.onCreate(savedInstanceState);
       setContentView(R.layout.about);
       
       Typeface tf = Typeface.createFromAsset(getAssets(), "font/markerfeltnormal.ttf");  
        
       TextView info = (TextView)findViewById(R.id.textView1);
       info.setTypeface(tf);
       info.setTextSize(18);
       info.setTextColor(Color.BLACK);
       info.setText("Sistema Simulador de Excursao\nIFAM Tour\nDesenvolvido por: Marcos Paulo Siqueira de Farias\nLazaro Torres de Albuquerque\nDesigner de Diego Mendes Valério\nOrientador: Claudio de Oliveira Santos");

       
    }
}