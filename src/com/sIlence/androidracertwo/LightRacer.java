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

    float[] linex;
    float[] liney;
    int length;

    int	scoreToChange;

    int ri, a, front, bx, by;

    boolean foundSpawn;

    boolean light;

    public LightRacer(GameView v, int c, int stc) {
        super(v);

        linex = new float[STANDARD_LENGTH];
        liney = new float[STANDARD_LENGTH];
        length = STANDARD_LENGTH;
        direction = rand.nextInt(4);
        color = c;
        startColor = color;
        brush.setStrokeWidth(0f);
        light = true;

        scoreToChange = stc;
        foundSpawn = true;
    }

    public LightRacer(GameView v, int c, int stc, float x, float y, int d) {
        this(v, c, stc);

        linex[0] = x;
        liney[0] = y;
        direction = d;
    }

    @Override
    public void update() {
	updateLength();
	updateLine();
	move();
	offScreen();
    }

    public void updateLength() {
        if (length != linex.length) {
            float[] tempx = linex.clone();
            float[] tempy = liney.clone();

            linex = new float[length];
            liney = new float[length];

            int l = (linex.length > tempx.length) ? tempx.length : linex.length;

            System.arraycopy(tempx, 0, linex, 0, l);
            System.arraycopy(tempy, 0, liney, 0, l);
        }
    }

    public void die(float hx, float hy, int di) {
	int start;
	if (hx == linex[0] && hy == liney[0])
	    alive = false;
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

        float[] lx = new float[linex.length - start];
        float[] ly = new float[liney.length - start];

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

            linex = new float[start];
            liney = new float[start];

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

    public void move() {
        switch (direction) {
            case 0:
                linex[0] += 1;
                break;
            case 1:
                liney[0] += 1;
                break;
            case 2:
                linex[0] -= 1;
                break;
            case 3: 
                liney[0] -= 1;
                break;
        }
    }

    public float getX() {
	return linex[0];
    }

    public float getY() {
	return liney[0];
    }

    public void render(Canvas c) {
	renderLines(c);
    }

    public void renderLines(Canvas c) {
        int a = 255;;

	float x = linex[0];
	float y = liney[0];

	brush.setColor(color);
	for (ri = 1; ri < linex.length && !(linex[ri] == 0 && liney[ri] == 0); ri++) {
	    if (Math.abs(x - linex[ri]) < view.width() / 2 && Math.abs(y - liney[ri]) < view.height() / 2)
		c.drawLine(view.toPoint(x, true), view.toPoint(y, false),
			   view.toPoint(linex[ri], true), view.toPoint(liney[ri], false), brush);
		
		x = linex[ri];
		y = liney[ri];
	}

	if (!light) return;

        for (ri = 0; ri < linex.length - 1 && linex[ri + 1] != 0 && liney[ri + 1] != 0; ri++) {
	    if (Math.abs(linex[ri] - linex[ri + 1]) > view.width() / 2 ||
		Math.abs(liney[ri] - liney[ri + 1]) > view.height() / 2)
		continue;
	    
	    front = Color.argb(a, 255, 255, 255);
	    brush.setColor(front);
	    c.drawLine(view.toPoint(linex[ri], true), view.toPoint(liney[ri], false),
		       view.toPoint(linex[ri + 1], true), view.toPoint(liney[ri + 1], false), brush);

	    a -= 25;
	    if (a < 0) break;
        }
    }

    public boolean changeDirection(int wd) { // Change to spicific direction
        if ((wd == direction)
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

        linex = new float[length];
        liney = new float[length];
    }

    public void spawn() {

        linex[0] = rand.nextFloat() * (view.width() - 10) + 10;
	liney[0] = rand.nextFloat() * (view.height() - 10) + 10;

        spawnSpec(linex[0], liney[0], safestDirection());
    }

    public void findSpawn() {
        linex[0] = rand.nextFloat() * (view.width() - 10) + 10;
	liney[0] = rand.nextFloat() * (view.height() - 10) + 10;

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

    public void spawnSpec(float x, float y, int di) {
        linex = new float[length];
        liney = new float[length];

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
            for (int clearance = 1; clearance < view.height(); clearance++) {
                switch (checkingDi) {
                    case 0:
                        x = linex[0] + clearance;
                        y = liney[0];
                        break;
                    case 1:
                        x = linex[0];
                        y = liney[0] + clearance;
                        break;
                    case 2:
                        x = linex[0] - clearance;
                        y = liney[0];
                        break;
                    case 3:
                        x = linex[0];
                        y = liney[0] - clearance;
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
                    distanceChecked += 1;
                    y -= 1;
                    break;
                case 1:
                    distanceChecked += 1;
                    x += 1;
                    break;
                case 2:
                    distanceChecked += 1;
                    y += 1;
                    break;
                case 3:
                    distanceChecked += 1;
                    x -= 1;
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
        float nx, ny;

        if (liney[0] <= 0) {
	    nx = view.width() / 2 + (view.width() / 2 - linex[0]);
	    ny = view.height() - 1;
        } else if (liney[0] >= view.height() - 1) {
	    nx = view.width() / 2 + (view.width() / 2 - linex[0]);
	    ny = 1;
        } else if (linex[0] <= 1) {
	    nx = view.width() - 1;
	    ny = view.height() / 2 + (view.height() / 2 - liney[0]);
	} else if (linex[0] >= view.width() - 1) {
	    nx = 1;
	    ny = view.height() / 2 + (view.height() / 2 - liney[0]);
        } else
	    return;
	
	if (view.killTailOffScreen()) {
	    Particle.initLineFall(view, startColor, linex, liney);
	    linex = new float[length];
	    liney = new float[length];
	} else
	    updateLine();
	
	linex[0] = nx;
	liney[0] = ny;
    }

    public boolean collides(Part other) {
        float x = other.getX();
        float y = other.getY();

        for (int i = 1; i < linex.length; i++) {
	    if (linex[i] == 0 && liney[i] == 0)
		break;
            if (x >= linex[i] - 0.5f && x <= linex[i] + 0.5f)
		if (y >= liney[i] - 0.5f && y <= linex[i] + 0.5f) 
                return true;
        } 
        return false;
    }

    public void setLength(int l) {
        length = l;
    }

    public int getLength() {
        return length;
    }
}
