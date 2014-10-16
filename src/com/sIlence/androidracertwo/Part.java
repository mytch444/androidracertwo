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
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;

public class Part {
    float x;
    float y;
    int direction;

    GameView view;

    int color;
    int startColor;
    
    boolean alive;

    Paint brush;

    public Part(GameView v) {
	view = v;
	alive = true;
	brush = v.getPaint();
    }

    public boolean isAlive() {
        return alive;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDirection() {
        return direction;
    }

    public boolean collides(Part other) {
        return false;
    }

    public void die(float hx, float hy, float di, boolean lives) {}
    public void spawn(ArrayList<Part> parts) {}

    public void update() {}
    public void render(Canvas c) {}

    public Random getRand() {
	return view.getRand();
    }

    public static int oppDirection(int di) {
        di += 2;
	di %= 4;
        return di;
    }
    
    public static float directionFromDifferences(float xd, float yd) {
	double O;
	float tmp;
	int m;
	
	boolean left = xd <= 0;
	boolean up = yd <= 0;

	if (!left && !up) {
	    O = 0;
	    m = 1;
	} else if (left && !up) {
	    O = Math.PI;
	    m = -1;
	    xd = -xd;
	} else if (left && up) {
	    O = Math.PI;
	    m = 1;
	    xd = -xd;
	    yd = -yd;
	} else if (!left && up) {
	    O = 2 * Math.PI;
	    m = -1;
	    yd = -yd;
	} else // Damn java.
	    return 0;
	
	return (float) (O + m * Math.atan(yd / xd));
    }
}
