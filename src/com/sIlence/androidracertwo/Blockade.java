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

import com.sIlence.androidracertwo.game.Game;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;
import android.util.Log;

public class Blockade extends Part {

    float width, height;

    public Blockade(Game g, float x, float y, float w, float h) {
        super(g);
        this.x = x;
        this.y = y;
        color = 0xff888888;
        startColor = color;

        width = w;
        height = h;
    }

    public Blockade(Game g) {
        this(g, 0, 0, 0, 0);
    }

    public void update() {}

    public void render(Canvas c) {
        if (!isAlive())
            return;

        brush.setColor(color);
        c.drawRect(game.getView().toXPoint(x), game.getView().toYPoint(y),
                game.getView().toXPoint(x + width), game.getView().toYPoint(y + height), brush);
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
        width = getRand().nextFloat() * game.width() * 0.3f + 1;
        height = getRand().nextFloat() * game.height() * 0.3f + 1;

        findSpawn(parts);
    }

    public void findSpawn(ArrayList<Part> parts) {
        for (int tries = 0; tries < 10; tries++) {
            x = getRand().nextFloat() * (game.width() - width - 8) + 4;
            y = getRand().nextFloat() * (game.height() - height - 8) + 4;

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

        int start;
        double Od;
        float speed, O, xd, yd, xp, yp;
        boolean left, up;

        float w = Particle.width(game);
        float h = Particle.height(game);
        for (float x = 0; x < width; x += w) {
            for (float y = 0; y < height; y += h) {
                xp = this.x + x;
                yp = this.y + y;

                xd = xp - hx;
                yd = yp - hy;

                O = directionFromDifferences(xd, yd);

                if (di > O)
                    Od = di - O;
                else
                    Od = O - di;
                if (Od > Math.PI)
                    Od = 2 * Math.PI - Od;

                speed = 1.2f * (float) Math.pow(0.1, (2 * Od) / Math.PI);

                start = (int) Math.hypot(xd, yd) / 2;

                game.getParticles().add(
                        new Particle(game, color, xp, yp,
                            O, speed, start, 20));
            }
        }
    }
}
