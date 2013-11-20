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
    private boolean isPaused = false;

    private GameView view;

    public GameLoop(GameView v) {
        view = v;
        running = true;
        isPaused = true;
    }

    public int framePeriod() {
        return framePeriod;
    }

    @Override
    public void run() {
        Canvas canvas;
        long beginTime;
        long timeDiff;
        int sleepTime;

        SurfaceHolder holder = view.getHolder();

        while (running) {
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

            if (isPaused) {
                synchronized (this) {
                    while (isPaused && running) {
                        try {
                            wait();
                        } catch (Exception e) {}
                    }
		    Log.d("TAG!", "not paused any more");
                }
            }
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pauseGame() {
        isPaused = true;
    }

    public synchronized void resumeGame() {
        isPaused = false;
        notify();
	Log.d("Tag", "resumed");
    }

    public synchronized void stopGame() {
        running = false;
        notify();
	Log.d("Tag", "stoped thread!!!!!!!!!");
    }
}
