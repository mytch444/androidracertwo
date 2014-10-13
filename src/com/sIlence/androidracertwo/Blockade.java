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
import android.util.Log;

public class Blockade extends Part {

    float width, height;

    public Blockade(GameView v, float x, float y, float w, float h) {
        super(v);
        this.x = x;
        this.y = y;
        color = 0xff888888;
        startColor = color;

        width = w;
        height = h;
    }

    public Blockade(GameView v) {
        this(v, 0, 0, 0, 0);
    }

    public void update() {}

    public void render(Canvas c) {
        if (!isAlive())
	    return;
	brush.setColor(color);
	c.drawRect(view.toPoint(x, true), view.toPoint(y, false),
		   view.toPoint(x + width, true), view.toPoint(y + height, false), brush);
    }

    public boolean collides(Part other) {
        float x = other.getX();
        float y = other.getY();

        float x0 = this.x;
        float y0 = this.y;
        float x1 = x0 + width;
        float y1 = y0 + height;

        if ((x0 >= x && x >= x1) || (x0 <= x && x <= x1)) {
            if ((y0 >= y && y >= y1) || (y0 <= y && y <= y1)) {
                return true;
            }
        }
        return false;
    }

    public void spawn(ArrayList<Part> parts) {
	width = rand.nextFloat() * view.width() * 0.3f + 1;
	height = rand.nextFloat() * view.height() * 0.3f + 1;

	findSpawn(parts);
    }

    public void findSpawn(ArrayList<Part> parts) {
        for (int tries = 0; tries < 10; tries++) {
            x = rand.nextFloat() * (view.width() - width - 8) + 4;
            y = rand.nextFloat() * (view.height() - height - 8) + 4;
            
            boolean good = true;
            for (int i = 0; i < parts.size(); i++) {
                if (collides(parts.get(i))) {
                    good = false;
                    break;
                }
            }

            if (good) break;
        }
    }

    public void die(float hx, float hy, float di, boolean lives) {
	alive = false;
	int start, stop;
        float anglerange, O, speed, xd, yd, Oo;

	float stepSizeX = (float) view.width() / view.getWidth();
	float stepSizeY = (float) view.height() / view.getHeight();
	
        for (float x = 0; x < width; x += stepSizeX) {
            for (float y = 0; y < height; y += stepSizeY) {
                
                xd = this.x + x - hx;
                yd = this.y + y - hy;

		speed = 0.3f;
		//		    speed = (float) ((-0.4 / (2 * Math.PI)) * Oo + 0.4) + 0.05f;
		
		O = (float) Math.atan(yd / xd);

		start = 0;//(int) ((Math.abs(O - di)) * 10);
                stop = 30;

                view.getParticles().add(
				      new Particle(view, color, x + this.x, y + this.y,
						   O, speed, start, stop));
            }
        }
    }
}
