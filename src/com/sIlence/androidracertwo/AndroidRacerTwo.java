/*
 *
 * This file is part of AndroidRacerTwo
 *
 * AndroidRacerTwo is free software: you can redistribute it and/or modify
 * it under the term of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the Licence, or
 * (at your option) any later version.
 * 
 * AndroidRacerTwo is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with AndroidRacerTwo. If not, see <http://www.gnu.org/licenses/>
 *
 * Copyright: 2013 Mytchel Hammond <mytchel.hammond@gmail.com>
 *
*/


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
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

public class AndroidRacerTwo extends Activity {

    private GameView view;

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
		    openGame(new GameView(context, new SnakeGame(AIRacer.DIFF_MEDI)));
		}
	    });

        Button tron = (Button) findViewById(R.id.tron);
        tron.setOnClickListener(new OnClickListener() {
		public void onClick(android.view.View arg0) {
		    openGame(new GameView(context, new TronGame(AIRacer.DIFF_MEDI)));
		}
	    });

	Button trondpad = (Button) findViewById(R.id.trondpad);
	trondpad.setOnClickListener(new OnClickListener() {
		public void onClick(android.view.View arg0) {
		    openGame(new GameView(context, new TronGame(AIRacer.DIFF_MEDI), true));
		}
	    });

	Button dodge = (Button) findViewById(R.id.dodge);
	dodge.setOnClickListener(new OnClickListener() {
		public void onClick(android.view.View arg0) {
		    openGame(new GameView(context, new DodgeGame(AIRacer.DIFF_MEDI)));
		}
	    });

    }

    @Override
    public void onBackPressed() {
        if (view != null) view.pause();
        else menu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
	Log.d("TAG", "onstop()");
        if (view != null) view.stop();

        super.onStop();
        onDestroy();
    }

    @Override
    protected void onPause() {
	Log.d("TAG", "onpause()");
        if (view != null && !view.isPaused()) {
            view.stop();
        }

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle b) {
        if (view != null) {
	    view.stop();
            view.killDialog();
        }

        super.onSaveInstanceState(b);
    }

    @Override
    protected void onResume() {
	Log.d("TAG", "onresume()");

	if (view != null) {
	    view.tick();
	}
        super.onResume();
    }

    @Override
    protected void onRestart() {
	Log.d("TAG", "onrestart()");
	
        super.onRestart();
    }

    @Override
    protected void onStart() {
	Log.d("TAG", "onstart()");

	super.onStart();

	if (view == null)
	    menu();
	else {
	    openGame(view);
	}
	
        unlockOrientation();
    }

    public void openGame(final GameView newView) {
        lockOrientation(); 
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
