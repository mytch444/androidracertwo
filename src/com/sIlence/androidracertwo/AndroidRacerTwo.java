package com.sIlence.androidracertwo;

import com.sIlence.androidracertwo.game.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.*;
import android.widget.*;
//import java.net.*;

public class AndroidRacerTwo extends Activity {
    
    private GameView			view;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    public void menu() {
	unlockOrientation();
	
        final Context context = this;
	
        setContentView(R.layout.main);
	
	
        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new OnClickListener() {
		public void onClick(android.view.View arg0) {
		    lockOrientation();
		    newGame(new GameView(context, new SnakeGame(AIRacer.DIFF_MEDI)));
		}
	    });

	Button tron = (Button) findViewById(R.id.tron);
        tron.setOnClickListener(new OnClickListener() {
		public void onClick(android.view.View arg0) {
		    lockOrientation();
		    newGame(new GameView(context, new TronGame(AIRacer.DIFF_MEDI)));
		}
	    });

    }
    
    @Override
    public void onBackPressed() {
	menu();
	view.pause();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
    }
    
    @Override
    protected void onStop() {
	if (view != null) view.stopGame();
	
	super.onStop();
	onDestroy();
    }
    
    @Override
    protected void onPause() {
	if (view != null && !view.isPaused()) {
	    view.pause();
	}
	
	super.onPause();
    }
    
    @Override
    protected void onResume() {
	super.onResume();
    }
    
    @Override
    protected void onRestart() {
	super.onRestart();
    }
    
    @Override
    protected void onStart() {
	super.onStart();
	
	menu();
		
	unlockOrientation();
    }
    
    public void newGame(final GameView newView) {
	runOnUiThread(new Runnable() {
		public void run() {	
		    view = newView;
		    setContentView(view);
		}
	    });
    }
    
    public void lockOrientation() {
	int orientation = getResources().getConfiguration().orientation;
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
	
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }
    
    public void unlockOrientation() {
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}