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
import java.util.ArrayList;
import android.util.Log;

public class LightRacer extends Part {
    public static final int STANDARD_LENGTH = 70;

    float[] linex;
    float[] liney;
    int length;

    int ri, a, front, bx, by;

    boolean light;

    public LightRacer(GameView v, int c) {
        super(v);

        linex = new float[STANDARD_LENGTH];
        liney = new float[STANDARD_LENGTH];
        length = STANDARD_LENGTH;
        direction = rand.nextInt(4);
        color = c;
        startColor = color;
        brush.setStrokeWidth(0f);
        light = true;
    }

    public LightRacer(GameView v, int c, float x, float y, int d) {
        this(v, c);

        linex[0] = x;
        liney[0] = y;
        direction = d;
    }

    @Override
    public void update() {
	if (!isAlive())
	    return;
	
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

    public void die(float hx, float hy, float di) {
	int start;
	Particle.initExplosion(view, startColor, hx, hy, di);

	for (start = length - 1; start > 0 &&
		 !((hx >= linex[start] - 0.5f && hx <= linex[start] + 0.5f) &&
		   (hy >= liney[start] - 0.5f && hy <= liney[start] + 0.5f))
		 ; start--);

	alive = start != 0;

	Particle.initLineFall(view, startColor, linex, liney, start);

        if (start > 0) {
	    for (; start < length; start++) {
		linex[start] = 0;
		liney[start] = 0;
	    }
        }
    }

    public void updateLine() {
        for (int i = length - 1; i > 0; i--) {
            linex[i] = linex[i - 1];
            liney[i] = liney[i - 1];
        }
    }

    public void move() {
	linex[0] += (float) Math.cos(direction * Math.PI / 2);
	liney[0] += (float) Math.sin(direction * Math.PI / 2);
    }

    public float getX() {
	return linex[0];
    }

    public float getY() {
	return liney[0];
    }

    public void render(Canvas c) {
	if (!isAlive())
	    return;
	
	renderLines(c);
    }

    public void renderLines(Canvas c) {
	float x = linex[1];
	float y = liney[1];

	brush.setColor(color);
	for (ri = 1; ri < length && linex[ri] != 0 && liney[ri] != 0; ri++) {
	    // Last or next is not strait
	    if (ri == length - 1 || linex[ri + 1] != x && liney[ri + 1] != y) {
		c.drawLine(view.toPoint(x, true), view.toPoint(y, false),
			   view.toPoint(linex[ri], true), view.toPoint(liney[ri], false), brush);

		x = linex[ri];
		y = liney[ri];
	    }

	    // Next one jumps accross screen
	    if (ri < length - 1
		//&& !(linex[ri + 1] == 0 && liney[ri + 1] == 0)
		&& (Math.abs(linex[ri + 1] - linex[ri]) > view.width() / 2
		    || Math.abs(liney[ri + 1] - liney[ri]) > view.height() / 2)) {
		ri++;
		x = linex[ri];
		y = liney[ri];
	    }
	}

	if (light) {
	    int a = 255;
	    for (ri = 0; ri < length - 1 && linex[ri + 1] != 0 && liney[ri + 1] != 0; ri++) {
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
    }

    public float getDirection() {
        return direction * (float) Math.PI / 2;
    }
    
    public boolean changeDirection(int wd) {
        if ((wd == direction)
	    || (wd == oppDirection(direction)) 
	    || (lastTurn + view.turnDelay() * view.framePeriod() > view.getTime())
	    ) {
            return false;
        } else { // Can turn this way
            direction = wd;
            lastTurn = view.getTime();

            return true;
        }
    }

    protected void newLine() {
	Particle.initLineFall(view, startColor, linex, liney, 0);
	for (int i = 0; i < length; i++)
	    linex[i] = liney[i] = 0;
    }

    public void spawn(ArrayList<Part> parts) {
	linex = new float[length];
	liney = new float[length];

	for (int i = 0; i < 10; i++)
	    if (findSpawn(parts))
		break;

	direction = safestDirection(parts);
	color = startColor;

	lastTurn = 0;
	alive = true;
    }

    public boolean findSpawn(ArrayList<Part> parts) {
        linex[0] = rand.nextFloat() * (view.width() - 10) + 5;
	liney[0] = rand.nextFloat() * (view.height() - 10) + 5;

	for (int i = 0; i < 4; i++)
	    if (!safeToTurn(parts, i, 100))
		return false;

	return true;
    }

    public int safestDirection(ArrayList<Part> parts) {
	int newDi = -1;
        int bestClearance = 0;

	x = linex[0];
	y = liney[0];
	
        for (int checkingDi = 0; checkingDi < 4; checkingDi++) {
	    clearancetesting:
            for (int clearance = 1; clearance < view.height(); clearance++) {
		linex[0] = x + (float) Math.cos(Math.PI / 2 * checkingDi) * clearance;
		liney[0] = y + (float) Math.sin(Math.PI / 2 * checkingDi) * clearance;

                if (clearance > bestClearance) {
                    newDi = checkingDi;
                    bestClearance = clearance;
                }

                for (int i = 0; i < parts.size(); i++) {
		    if (parts.get(i) != this && parts.get(i).collides(this)) {
                        if (clearance > bestClearance) {
                            newDi = checkingDi;
                            bestClearance = clearance;
                        }

			break clearancetesting;
                    }
                }
            }
        }

	linex[0] = x;
	liney[0] = y;

        return newDi;
    }

    public boolean safeToTurn(ArrayList<Part> parts, int wd, int distance) {
        x = linex[0];
        y = liney[0];

        for (int distanceChecked = 0; distanceChecked < distance; distanceChecked++) {
	    linex[0] = x + (float) Math.cos(Math.PI / 2 * wd) * distanceChecked;
	    liney[0] = y + (float) Math.sin(Math.PI / 2 * wd) * distanceChecked;

	    for (int i = 0; i < parts.size(); i++) {
                if (parts.get(i).collides(this)) {
		    linex[0] = x;
		    liney[0] = y;
                    return false;
                }
            }
        }

	linex[0] = x;
	liney[0] = y;
	
        return true;
    }

    public void offScreen() {
        float nx, ny;

        if (liney[0] <= 0) {
	    nx = view.width() - linex[0];
	    ny = view.height() - 1;
        } else if (liney[0] >= view.height()) {
	    nx = view.width() - linex[0];
	    ny = 1;
        } else if (linex[0] <= 0) {
	    nx = view.width() - 1;
	    ny = view.height() - liney[0];
	} else if (linex[0] >= view.width()) {
	    nx = 1;
	    ny = view.height() - liney[0];
        } else
	    return;

	if (view.killTailOffScreen())
	    newLine();

	linex[0] = nx;
	liney[0] = ny;
    }

    public boolean collides(Part other) {
	if (!isAlive())
	    return false;
	
        float x = other.getX();
        float y = other.getY();

	if (linex == null || liney == null)
	    Log.d("fdjaskl f", "what the actuall fuck!");
	
        for (int i = 1; i < length; i++) {
            if (x >= linex[i] - 0.5f && x <= linex[i] + 0.5f)
		if (y >= liney[i] - 0.5f && y <= liney[i] + 0.5f) 
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
