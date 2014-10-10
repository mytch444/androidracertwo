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
import android.graphics.Color;
import android.view.Surface;
import java.util.Arrays;
import android.util.Log;

public class LightRacer extends Part {
    public static final int STANDARD_LENGTH = 70;

    int[] linex;
    int[] liney;
    int length;

    int	scoreToChange;

    int ri, a, front, bx, by;

    boolean foundSpawn;

    boolean light;

    public LightRacer(GameView v, int c, int stc) {
        super(v);

        linex = new int[STANDARD_LENGTH];
        liney = new int[STANDARD_LENGTH];
        length = STANDARD_LENGTH;
        direction = rand.nextInt(4);
        color = c;
        startColor = color;
        brush.setStrokeWidth(0f);
        light = true;

        scoreToChange = stc;
        foundSpawn = true;
        lives = 5;
    }

    public LightRacer(GameView v, int c, int stc, int x, int y, int d) {
        this(v, c, stc);

        linex[0] = x;
        liney[0] = y;
        direction = d;
    }

    @Override
    public void update() {
        if (dieing > 0) {
	    updateDieing();
            return;
        } else {
	    updateLength();
	    updateLine();
	    move();
	    offScreen();
	}
    }

    public void updateDieing() {
	dieing++;

	if (!foundSpawn) {
	    findSpawn();
	}

	if (dieing == 30) {

	    view.checkScore();

	    if (!foundSpawn) {
		spawn();
	    } else {
		spawnSpec(linex[0], liney[0], safestDirection());
	    }

	    dieing = 0;
	    foundSpawn = true;
	}
    }

    public void updateLength() {
        if (length != linex.length) {
            int[] tempx = linex.clone();
            int[] tempy = liney.clone();

            linex = new int[length];
            liney = new int[length];

            int l = (linex.length > tempx.length) ? tempx.length : linex.length;

            System.arraycopy(tempx, 0, linex, 0, l);
            System.arraycopy(tempy, 0, liney, 0, l);
        }
    }

    public void die(Part p, int hx, int hy, int di) {
	int start;
	/*	if (p != this & lives() > 0 && hx == linex[0] && hy == liney[0]) {
	    p.die(this, hx, hy, di);
	    return;
	    }*/
		
	/*	if (lives() <= 0 && hx == linex[0] && hy == liney[0]) {
	    self = true;
	    //            view.changeScore(scoreToChange);
            foundSpawn = false;
            dieing = 1;
	    }*/

	//	downLives();
	//	p.downLives();

	// I've been killed and need to fine somewhere to spawn.
	if (lives() <= 0 && hx == linex[0] && hy == liney[0])
	    dieing = 1;
	
	/*
	 * Something hit me so make particles and shorten line however far it needs to.
	 */
	
	Particle.initExplosion(view, startColor, hx, hy, di);

	/*
	 * Line fall stuff.
	 */
	
	// Work out from where in my tail needs to become particles for linefall.
        start = 0;
	for (start = linex.length - 1; 
	     start > 0 
		 && !(hx == linex[start] && hy == liney[start]); 
	     start--);

        int[] lx = new int[linex.length - start];
        int[] ly = new int[liney.length - start];

        System.arraycopy(linex, start, lx, 0, lx.length);
        System.arraycopy(liney, start, ly, 0, ly.length); 

	Particle.initLineFall(view, startColor, lx, ly);

	/*
	 * END of line fall stuff.
	 */

	// I didn't completely die so give me the rest.
        if (start > 0) {
            lx = linex.clone();
            ly = liney.clone();

            linex = new int[start];
            liney = new int[start];

            System.arraycopy(lx, 0, linex, 0, linex.length);
            System.arraycopy(ly, 0, liney, 0, liney.length);
        }
    }

    public void updateLine() {
        for (int i = linex.length - 1; i > 0; i--) {
            linex[i] = linex[i - 1];
            liney[i] = liney[i - 1];
        }
    }

    /*    public void checkCollisions() {
        if (opps == null) return;

        x = linex[0];
        y = liney[0];
        for (int i = 0; i < opps.length; i++) {
	    if (opps[i].isAlive()
		&& opps[i].collides(this)) {
		die(opps[i], x, y, getDirection());
            }
        }
	}*/

    public void move() {
        switch (direction) {
            case 0:
                linex[0] += view.boxWidth();
                break;
            case 1:
                liney[0] += view.boxHeight();
                break;
            case 2:
                linex[0] -= view.boxWidth();
                break;
            case 3: 
                liney[0] -= view.boxHeight();
                break;
        }
    }

    public int getX() {
	return linex[0];
    }

    public int getY() {
	return liney[0];
    }

    public void render(Canvas c) {
        if (dieing == 0) {
            renderLines(c);
        }
    }

    public void renderLines(Canvas c) {
        int a = 255;;

	int x = linex[0];
	int y = liney[0];

	brush.setColor(color);
	for (ri = 1; ri < linex.length && !(linex[ri] == 0 && liney[ri] == 0); ri++) {
	    if (Math.abs(x - linex[ri]) < view.boxWidth() * 2 && Math.abs(y - liney[ri]) < view.boxHeight() * 2)
		c.drawLine(x, y, linex[ri], liney[ri], brush);
		
		x = linex[ri];
		y = liney[ri];
	}

	if (!light) return;

        for (ri = 0; ri < linex.length - 1 && linex[ri + 1] != 0 && liney[ri + 1] != 0; ri++) {
	    if (Math.abs(linex[ri] - linex[ri + 1]) > view.boxWidth() * 2 ||
		Math.abs(liney[ri] - liney[ri + 1]) > view.boxHeight() * 2)
		continue;
	    
	    front = Color.argb(a, 255, 255, 255);
	    brush.setColor(front);
	    c.drawLine(linex[ri], liney[ri], linex[ri + 1], liney[ri + 1], brush);

	    a -= 25;
	    if (a < 0) break;
        }
    }

    public boolean changeDirection(int wd) { // Change to spicific direction
        if ((wd == direction)
	    || (dieing != 0)
	    || (wd == oppDirection(direction)) 
	    || (lastTurn + view.turnDelay() * view.framePeriod() > view.getTime())
	    ) {
            return false;
        } else { // Can turn this way8
            direction = wd;
            lastTurn = view.getTime();

            return true;
        }
    }

    protected void newLine() {
	Particle.initLineFall(view, startColor, linex, liney);

        linex = new int[length];
        liney = new int[length];
    }

    public void spawn() {

        linex[0] = (rand.nextInt(view.boxsX() - 20) + 10) * view.boxWidth();
        liney[0] = (rand.nextInt(view.boxsY() - 25) + 15) * view.boxHeight();

        spawnSpec(linex[0], liney[0], safestDirection());
    }

    public void findSpawn() {
        linex[0] = (rand.nextInt(view.boxsX() - 20) + 10) * view.boxWidth();
        liney[0] = (rand.nextInt(view.boxsY() - 25) + 15) * view.boxHeight();

        if (!safeToTurn(0, 100)) {
            return;
        } else if (!safeToTurn(1, 100)) {
            return;
        } else if (!safeToTurn(2, 100)) {
            return;
        } else if (!safeToTurn(3, 100)) {
            return;
        }

        foundSpawn = true;
    }

    public void spawnSpec(int x, int y, int di) {
        linex = new int[length];
        liney = new int[length];

        lastTurn = 0;
        color = startColor;

        linex[0] = x;
        liney[0] = y;
        direction = di;
    }

    public int safestDirection() {
        int newDi = -1;
        int bestClearance = 0;

        for (int checkingDi = 0; checkingDi < 4; checkingDi++) {

            x = linex[0];
            y = liney[0];

clearancetesting:
            for (int clearance = 1; clearance < view.gratestLengthInSegments(); clearance++) {
                switch (checkingDi) {
                    case 0:
                        x = linex[0] + (clearance * view.boxWidth());
                        y = liney[0];
                        break;
                    case 1:
                        x = linex[0];
                        y = liney[0] + (clearance * view.boxHeight());
                        break;
                    case 2:
                        x = linex[0] - (clearance * view.boxWidth());
                        y = liney[0];
                        break;
                    case 3:
                        x = linex[0];
                        y = liney[0] - (clearance * view.boxHeight());
                        break;
                }

                if (clearance > bestClearance) {
                    newDi = checkingDi;
                    bestClearance = clearance;
                }


                for (int i = 0; i < opps.length; i++) {
                    if (opps[i].collides(this)) {

                        if (clearance > bestClearance) {
                            newDi = checkingDi;
                            bestClearance = clearance;
                        }

                        break clearancetesting;
                    }
                }
            }
        }

        return newDi;
    }

    public boolean safeToTurn(int wd, int distance) {
        int distanceChecked = 0;

        x = linex[0];
        y = liney[0];

        while (distanceChecked < distance) {
            switch (wd) {
                case 0:
                    distanceChecked += view.boxHeight();
                    y -= view.boxHeight();
                    break;
                case 1:
                    distanceChecked += view.boxWidth();
                    x += view.boxWidth();
                    break;
                case 2:
                    distanceChecked += view.boxHeight();
                    y += view.boxHeight();
                    break;
                case 3:
                    distanceChecked += view.boxWidth();
                    x -= view.boxWidth();
                    break;
            }

            for (int i = 0; i < opps.length; i++) {
                if (opps[i].collides(this)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void offScreen() {
        int nx, ny;
	nx = linex[0];
	ny = liney[0];

        if (liney[0] < view.top()) {

	    //            nx = view.boxsX() / 2 * view.boxWidth() + (view.boxsX() / 2 * view.boxWidth() - x);
            ny = (view.boxsY() - 1) * view.boxHeight() + view.top();

        } else if (liney[0] > view.boxsY() * view.boxHeight() + view.top()) {

	    //            nx = view.boxsX() / 2 * view.boxWidth() + (view.boxsX() / 2 * view.boxWidth() - x);
            ny = view.top() + view.boxHeight();

        } else if (linex[0] < 0) {

            nx = (view.boxsX() - 1) * view.boxWidth();
	    //            ny = view.boxsY() / 2  * view.boxHeight() + (view.boxsY() / 2 * view.boxHeight() - y) + view.top();

        } else if (linex[0] > view.boxsX() * view.boxWidth()) {

            nx = view.boxWidth();
	    //            ny = view.boxsY() / 2 * view.boxHeight() + (view.boxsY() / 2 * view.boxHeight() - y) + view.top();

        } else
	    return;
	
	if (view.killTailOffScreen()) {
	    Particle.initLineFall(view, startColor, linex, liney);
	    linex = new int[length];
	    liney = new int[length];
	}
	
	linex[0] = nx;
	liney[0] = ny;
    }

    public boolean collides(Part other) {
        int x = other.getX();
        int y = other.getY();

        for (int i = 1; i < linex.length; i++) {
            if (x == linex[i] && y == liney[i]) 
                return true;
        } 
        return false;
    }

    public void pause() {

    }

    public void resume() {

    }

    public void setLength(int l) {
        length = l;
    }

    public int getLength() {
        return length;
    }
}
