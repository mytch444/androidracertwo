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

public class Part {
    int x;
    int y;
    GameView view;
    int direction;
    int color;
    int startColor;
    Random rand;
    Paint brush;
    Part[] opps;
    long lastTurn;
    int dieing;
    int lives;

    public Part(GameView v) {
        this.view = v;
        x = 0;
        y = 0;
        direction = 1;
        color = 0xff00ffff;
        startColor = color;
        rand = new Random();
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);
        opps = null;
        lastTurn = 0;
        dieing = 0;
        lives = 0;
    }
    public void setOpps(Part[] o) {
        opps = o.clone();
    }
    public boolean isAlive() {
        if (dieing == 0) return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public boolean collides(Part other) {
        return false;
    }

    public static int oppDirection(int di) {
        di -= 2;
        while (di < 0) di += 4;
        return di;
    }

    public void upLives() {
        lives++;
    }

    public void downLives() {
        lives--;
    }

    public int lives() {
        return lives;
    }

    public void die(int hx, int hy, int di) {}

    public void update() {}
    public void render(Canvas c) {}

    public void stop() {}
}
