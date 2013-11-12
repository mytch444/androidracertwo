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

public class Blockade extends Part {

    int width, height;
    Particle[] particles;

    public Blockade(GameView v, int x, int y, int w, int h) {
        super(v);
        this.x = x;
        this.y = y;
        color = 0xaa666666;
        startColor = color;

        width = w;
        height = h;

        particles = null;

        dieing = 0;
    }

    public Blockade(GameView v) {
        this(v, 0, 0, 0, 0);
    }

    public void update() {
        if (particles != null) {
            for (int i = 0; i < particles.length; i++) particles[i].update();
        }
    }

    public void render(Canvas c) {
        if (particles != null) {
           for (int i = 0; i < particles.length; i++) particles[i].render(c);
        } else {
            brush.setColor(color);
            c.drawRect(x, y, x + width, y + height, brush);
        }
    }

    public boolean collides(Part other) {
        int x = other.getX();
        int y = other.getY();

        int x0 = this.x;
        int y0 = this.y;
        int x1 = x0 + width;
        int y1 = y0 + height;

        if ((x0 >= x && x >= x1) || (x0 <= x && x <= x1)) {
            if ((y0 >= y && y >= y1) || (y0 <= y && y <= y1)) {
                return true;
            }
        }
        return false;
    }

    public void spawn(Part[] parts) {
        for (int tries = 0; tries < 10; tries++) {
            width = rand.nextInt((2 * view.boxsX() * view.boxWidth()) / 3) + view.boxWidth();
            height = (((2 * view.boxsX() * view.boxWidth()) / 3) * view.boxHeight()) / width;

            x = rand.nextInt((view.boxsX() - 8) * view.boxWidth() - width) + 4 * view.boxWidth();
            y = rand.nextInt((view.boxsY() - 8) * view.boxHeight() - view.top() - height) + view.top() + 4 * view.boxHeight();
            
            boolean good = true;
            for (int i = 0; i < parts.length; i++) {
                if (collides(parts[i])) {
                    good = false;
                    break;
                }
            }

            if (good) break;
        }
    }

    public void die(int hx, int hy, int di) {
        dieing = 1;
       
        particles = new Particle[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                particles[x * height + y] = new Particle(color, x + this.x, y + this.y, 0, 360, 1, 0.5f, 20);
            }
        }
    }
}
