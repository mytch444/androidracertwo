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
    Random rand;
    Paint brush;
    long lastTurn;
    boolean alive;

    public Part(GameView v) {
        this.view = v;
        x = 0;
        y = 0;
        direction = 0;
        color = 0xff00ffff;
        startColor = color;
        rand = new Random();
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);
        lastTurn = 0;
	alive = true;
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

    public static int oppDirection(int di) {
        di += 2;
	di %= 4;
        return di;
    }

    public void die(float hx, float hy, float di) {}
    public void spawn(ArrayList<Part> parts) {}

    public void update() {}
    public void render(Canvas c) {}

    public void stop() {}
}
