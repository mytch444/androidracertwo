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
import android.util.Log;

public class Life extends Blockade {

    public Life(GameView v, int x, int y) {
        super(v, x, y, 30, 30);
        color = 0xff0000ff;
        startColor = color;
    }

    public Life(GameView v) {
        this(v, 0, 0);
    }

    public void die(Part p, int hx, int hy, int di) {
        int xd, yd, start, stop;
        float anglerange, speed;
	
	p.upLives();

	dieing = 1;
        anglerange = 0;

	color = 0xffff00ff;
	
        particles = new Particle[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                
                xd = this.x + x - hx;
                yd = this.y + y - hy;

                anglerange = (0.005f * xd * xd + 0.051f) * (0.005f * yd * yd + 0.051f) * 360;

                start = (int) (anglerange / 10);
                if (start > 50) start = 50;
                stop = 20;
                speed = 4f / anglerange + 1f;

                particles[x * height + y] = 
                    new Particle(color, x + this.x, y + this.y, di * 90, (int) anglerange, speed, 0.1f, start, stop);
            }
        }
    }
}
