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
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import java.util.ArrayList;
import android.util.Log;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static int INCREASE_KILLS = 1;
    public static int INCREASE_NULL = 0;
    public static int INCREASE_DEATHS = -1;

    boolean pausing;
    boolean starting;
    boolean gameOver;
    boolean won;
    
    int countdown;
    int startcount;
    long endTime;
    
    GameLoop loop;

    float width, height;
    float topBorder, bottomBorder, leftBorder, rightBorder;
    float yPartSize, xPartSize;
    boolean horizontalOrientation;
    
    InputHandler handler;

    String textString;

    Paint brush;

    Game game;

    MyDialog dialog;

    public GameView(Context context, Game g, boolean usingArrows) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        game = g;
        loop = new GameLoop(this);

	handler = new InputHandler(this, usingArrows);

        textString = "";

	endTime = 0;
    }

    public GameView(Context context, Game g) {
	this(context, g, false);
    }

    protected void newGame() {
	stop();
	
        game.init();

	starting = true;
        gameOver = false;
        won = false;
	pausing = false;
	
        startcount = getTime();
        countdown = 3;
	
	tick();
    }

    public void update() {
        if (starting || gameOver) return;
         
        incTime(loop.framePeriod());
        
        if (countdown > 0) {
            if ((getTime() - startcount) / 1000 >= 4 - countdown) {
                countdown--;
            }
	    if (countdown == 0) setTime(startcount);
            return;
        }
     
        game.update();
    }

    public void render(Canvas c) {
        if (c == null) return;

        background(c);
        game.render(c);
	game.hud(c, countdown == 0);

	if (countdown > 0) {
	    int t = getTime() / 1000;
            brush.setColor(0xffffffff);
            brush.setTextSize(getHeight() / 10);

            String message = "" + countdown;
            c.drawText(message, getWidth() / 2 - halfWidth(message, brush), getHeight() / 2, brush);
        }
	
	handler.overlay(c);
        messages();
    }

    public void messages() {
        if (gameOver) {
            if (won) {
                dialog(new NewGameDialog(this, game.winMessage(), "New Game", "Exit"));
            } else {
                dialog(new NewGameDialog(this, game.loseMessage(), "New Game", "Exit"));
            }
        } else if (starting) {
            dialog(new PauseDialog(this, "You Are Blue\nSwipe To Turn\nMake Yellow Crash\nTap To Play", "Start", "Exit"));
        } else if (!loop.running() || pausing) {
            dialog(new PauseDialog(this, "Paused", "Resume", "Exit"));
        }
    }

    public FragmentTransaction cleanupFragments() {
        FragmentTransaction ft = ((Activity) getContext()).getFragmentManager().beginTransaction();

        Fragment f = ((Activity) getContext()).getFragmentManager().findFragmentByTag("dialog");
        if (f != null) ft.remove(f);

        return ft;
    }

    public void dialog(MyDialog d) {
	stop();
	FragmentTransaction ft = cleanupFragments();
	dialog = d;
	dialog.show(ft, "dialog");
    }

    public void gameOver(boolean w) {
        won = w;

        gameOver = true;
        endTime = System.currentTimeMillis();
    }

    public void changeScore(int scoreType) {
        if (scoreType == INCREASE_DEATHS) {
            incDeaths();
        } else if (scoreType == INCREASE_KILLS) {
            incKills();
        }
    }

    public int halfWidth(String text, Paint p) {
        Rect bounds = new Rect();
        p.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width() / 2;
    }

    public void background(Canvas c) {
        brush.setColor(0xFF000308);
        brush.setStyle(Paint.Style.FILL);
        c.drawRect(0, 0, getWidth(), getHeight(), brush);

        brush.setColor(0x206FC0DF);
        brush.setStyle(Paint.Style.STROKE);

        for (float x = 0; x < getWidth(); x += 70) {
            c.drawLine(x, topBorder(), x, getHeight(), brush);
        }
        for (float y = topBorder(); y < getHeight(); y += 70) {
            c.drawLine(0, y, getWidth(), y, brush);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
	return handler.onTouchEvent(e);
    }

    public void pause() {
        pausing = true;
    }

    public void stop() {
	pausing = false;
	loop.stopLoop();
    }

    public void start() {
	stop();

	starting = false;
        
	loop = new GameLoop(this);
        loop.start();
    }

    public void tick() {
	loop.tick();
    }
    
    public boolean isPaused() {
        if (gameOver) return true;
        return loop.running();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
	Log.d("TAG", "surfaceCreated");
	
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);

	if (getWidth() > getHeight()) {
	    width = getHeight() / 3;
	    height = getWidth() / 3;
	    horizontalOrientation = true;
	} else {
	    width = getWidth() / 3;
	    height = getHeight() / 3;
	    horizontalOrientation = false;
	}
	
	leftBorder = 0;
	rightBorder = 0;
	bottomBorder = 0;
	topBorder = 20;

	xPartSize = (getWidth() - leftBorder() - rightBorder()) / width();
	yPartSize = (getHeight() - topBorder() - bottomBorder()) / height();
	
	handler.init();

	game.setView(this);
	pause();
	
	if (game.getParts() == null)
	    newGame();
	else
	    tick();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
	Log.d("TAG", "surfaceDestroyed");
	stop();
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

    /*
     * Positioning.
     */

    public float width() {
	return width;
    }

    public float height() {
	return height;
    }

    public float leftBorder() {
	return leftBorder;
    }

    public float rightBorder() {
	return rightBorder;
    }

    public float topBorder() {
	return topBorder;
    }

    public float bottomBorder() {
	return bottomBorder;
    }

    public float toPoint(float p, boolean x) {
	if (x ^ horizontalOrientation)
	    return leftBorder() + p * xPartSize;
	else
	    return topBorder() + p * yPartSize;
    }

    /*
     * End of positioning.
     */    

    public int getKills() {
        return game.getKills();
    }

    public void incKills() {
        game.setKills(game.getKills() + 1); 
    }

    public void setKills(int k) {
        game.setKills(k); 
    }

    public int getDeaths() {
        return game.getDeaths();
    }

    public void incDeaths() {
        game.setDeaths(game.getDeaths() + 1);
    }

    public void setDeaths(int d) {
        game.setDeaths(d);
    }

    public int getTime() {
        return game.getTime();
    }

    public void incTime(int a) {
        game.setTime(game.getTime() + a);
    }

    public void setTime(int t) {
        game.setTime(t);
    }

    public int framePeriod() {
        return loop.framePeriod();
    }

    public LightRacer getLocal() {
        return game.getLocal();
    }

    public ArrayList<Part> getParts() {
	return game.getParts();
    }

    public ArrayList<Particle> getParticles() {
	return game.getParticles();
    }

    public void killDialog() {
	if (dialog != null) dialog.dismiss();
    }

    public int turnDelay() {
        return 5;
    }

    public boolean killTailOffScreen() {
	return game.killTailOffScreen();
    }
}
