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

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.util.Log;

public class GameLoop extends Thread {

    private final int wantedFps = 40;
    private final int framePeriod = 1000 / wantedFps;

    private boolean running = false;

    private GameView view;

    private Canvas canvas;
    private long beginTime;
    private long timeDiff;
    private int sleepTime;

    private SurfaceHolder holder;
    
    public GameLoop(GameView v) {
        view = v;
        running = false;

	holder = view.getHolder();
    }

    public int framePeriod() {
        return framePeriod;
    }

    public void run() {
	while (running) {
	    tick();
	}
    }
    
    public void tick() {
	canvas = null;
	try {
	    canvas = holder.lockCanvas(null);
	    synchronized (holder) {
		beginTime = System.currentTimeMillis();
		
		view.update();
		view.render(canvas);

		timeDiff = System.currentTimeMillis() - beginTime;
		sleepTime = (int) (framePeriod - timeDiff);

		if (sleepTime > 0) {
		    try {
			Thread.sleep(sleepTime);
		    } catch (Exception e) {};
		}
	    }
	} finally {
	    if (canvas != null) {
		holder.unlockCanvasAndPost(canvas);
	    }
	}
    }

    public boolean running() {
	return running;
    }

    public void start() {
	running = true;
	super.start();
    }
    
    public void stopLoop() {
        running = false;
    }
}
