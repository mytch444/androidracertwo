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
import com.sIlence.androidracertwo.dialog.*;
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

    boolean pausing;
    boolean gameOver;
    boolean won;
    
    int countdown;
    
    GameLoop loop;

    float width, height;
    float topBorder, bottomBorder, leftBorder, rightBorder;
    float yPartSize, xPartSize;
    
    Paint brush;

    Game game;
    InputHandler handler;
    MyDialog dialog;

    public GameView(Context context, Game g, boolean usingArrows) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        game = g;
        loop = new GameLoop(this);

	handler = new InputHandler(this, usingArrows);
    }

    public GameView(Context context, Game g) {
	this(context, g, false);
    }

    public void newGame() {
	stop();
	
        game.init();

        gameOver = false;
        won = false;
	pausing = false;
	
        countdown = 1000;
	
	tick();
    }

    public void update() {
        if (gameOver) return;        
        
        if (countdown > 0) {
	    countdown -= framePeriod();
            return;
        }
     
        game.update();

	messages();
    }

    public void render(Canvas c) {
        if (c == null) return;

        background(c);
        game.render(c);
	game.hud(c);

	if (countdown > 0) {
            brush.setColor(0xffffffff);
            brush.setTextSize(getHeight() / 10);

            String message = "" + countdown;
            c.drawText(message, getWidth() / 2 - textWidth(message, brush) / 2, getHeight() / 2, brush);
        }
	
	handler.overlay(c);
    }

    public void messages() {
	if (gameOver) {
            if (won) {
                showDialog(new NewGameDialog(this, game.winMessage()));
	    } else {
                showDialog(new NewGameDialog(this, game.loseMessage()));
            }
	} else if (pausing) {
	    showDialog(new PauseDialog(this));
        }
    }

    public FragmentTransaction cleanupFragments() {
        FragmentTransaction ft = ((Activity) getContext()).getFragmentManager().beginTransaction();

        Fragment f = ((Activity) getContext()).getFragmentManager().findFragmentByTag("dialog");
        if (f != null) ft.remove(f);

        return ft;
    }

    public void showDialog(MyDialog d) {
	stop();
	FragmentTransaction ft = cleanupFragments();
	dialog = d;
	dialog.show(ft, "dialog");
    }

    public void gameOver(boolean w) {
        won = w;
        gameOver = true;
    }

    public int textWidth(String text, Paint p) {
        Rect bounds = new Rect();
        p.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
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

    public void surfaceCreated(SurfaceHolder arg0) {
	Log.d("TAG", "surfaceCreated");
	
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);

	width = getWidth() / 3;
	height = getHeight() / 3;

	leftBorder = 0;
	rightBorder = 0;
	bottomBorder = 0;
	topBorder = 20;

	xPartSize = (getWidth() - leftBorder() - rightBorder()) / width();
	yPartSize = (getHeight() - topBorder() - bottomBorder()) / height();

	handler.init();

	game.setView(this);

	pausing = false;
	
	if (game.getParts() == null) {
	    newGame();
	    showDialog(new NewGameDialog(this, game.startMessage(), false));
	} else {
	    pausing = true;
	}

	tick();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
	Log.d("TAG", "surfaceDestroyed");
	stop();
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}


    public void pause() {
        pausing = true;
    }

    public void stop() {
	pausing = false;
	loop.stopLoop();
    }

    public void start() {
	stop();

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
	if (x)
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

    public int getDeaths() {
        return game.getDeaths();
    }

    public int getTime() {
        return game.getTime();
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
