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
import android.graphics.Color;
import android.view.Surface;
import java.util.ArrayList;
import android.util.Log;

public class LightRacer extends Part {
    public static final int STANDARD_LENGTH = 70;

    int[] linex;
    int[] liney;
    int length;

    int ri, a, front;
    boolean light;

    long lastTurn;

    public LightRacer(Game g, int c) {
        super(g);

        speed = 3;

        linex = new int[STANDARD_LENGTH];
        liney = new int[STANDARD_LENGTH];
        length = STANDARD_LENGTH;

        direction = 0;

        color = c;
        startColor = color;
        light = true;
    }

    public LightRacer(Game g, int c, int x, int y, float d) {
        this(g, c);

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
            int[] tempx = linex.clone();
            int[] tempy = liney.clone();

            linex = new int[length];
            liney = new int[length];

            int l = (linex.length > tempx.length) ? tempx.length : linex.length;

            System.arraycopy(tempx, 0, linex, 0, l);
            System.arraycopy(tempy, 0, liney, 0, l);
        }
    }

    @Override
    public void die(int hx, int hy, float di, boolean lives) {
        int start;
    
        Particle.initExplosion(game, startColor, hx, hy, di);

        for (start = linex.length - 1; start > 0 &&
                !((hx >= linex[start] - 0.5f && hx <= linex[start] + 0.5f) &&
                    (hy >= liney[start] - 0.5f && hy <= liney[start] + 0.5f))
                ; start--);

        if (linex[0] == hx && liney[0] == hy && !lives) {
            alive = false;
            start = 0;
        }

        if (start > 4)
            start -= 2;

        Particle.initLineFall(game, startColor, linex, liney,
                // It looks nice to have the last few also go up in pixels.
                start > 4 ? start - 2 : start);

        if (start > 0) {
            for (; start < linex.length; start++) {
                linex[start] = 0;
                liney[start] = 0;
            }
        }
    }

    public void updateLine() {
        for (int i = linex.length - 1; i > 0; i--) {
            linex[i] = linex[i - 1];
            liney[i] = liney[i - 1];
        }
    }

    public void move() {
        linex[0] += (int) (Math.cos(direction) * speed);
        liney[0] += (int) (Math.sin(direction) * speed);
    }

    public int getX() {
        return linex[0];
    }

    public int getY() {
        return liney[0];
    }

    public void render(Canvas c) {
        if (!isAlive())
            return;

        renderLines(c);
    }

    public void renderLines(Canvas c) {
        int x = linex[1];
        int y = liney[1];
        int dist;

        //	brush.setStrokeWidth(0f);
        brush.setColor(color);
        for (ri = 1; ri < linex.length && linex[ri] != 0 && liney[ri] != 0; ri++) {
            // Last or next is not strait
            if (ri == linex.length - 1 || linex[ri + 1] != x && liney[ri + 1] != y) {
                c.drawLine(game.getView().toXPoint(x), game.getView().toYPoint(y),
                        game.getView().toXPoint(linex[ri]), game.getView().toYPoint(liney[ri]), brush);

                x = linex[ri];
                y = liney[ri];
            }

            // Next one jumps accross screen
            if (ri < linex.length - 1) {
                dist = (linex[ri] - linex[ri + 1]) * (linex[ri] - linex[ri + 1])
                    + (liney[ri] - liney[ri + 1]) * (liney[ri] - liney[ri + 1]);
                if (dist > (game.width() / 2) * (game.width() / 2)) {
                    ri++;
                    x = linex[ri];
                    y = liney[ri];
                }
            }
        }

        if (light) {
            int a = 255;
            for (ri = 0; ri < linex.length - 1 && linex[ri + 1] != 0 && liney[ri + 1] != 0; ri++) {
                if ((linex[ri] - linex[ri + 1]) * (linex[ri] - linex[ri + 1])
                        + (liney[ri] - liney[ri + 1]) * (liney[ri] - liney[ri + 1])
                        > (game.width() / 2) * (game.width() / 2))
                    continue;
                front = Color.argb(a, 255, 255, 255);
                brush.setColor(front);
                c.drawLine(game.getView().toXPoint(linex[ri]), game.getView().toYPoint(liney[ri]),
                        game.getView().toXPoint(linex[ri + 1]), game.getView().toYPoint(liney[ri + 1]), brush);

                a -= 25;
                if (a < 0) break;
            }
        }
    }

    public boolean changeDirection(float wd) {
        if ((wd == direction)
                || (wd == oppDirection(direction)) 
                || (lastTurn + game.getView().turnDelay() * game.getView().framePeriod() > game.getView().getTime())
           ) {
            return false;
        } else { // Can turn this way
            direction = wd;
            lastTurn = game.getView().getTime();

            return true;
        }
    }

    protected void newLine() {
        Particle.initLineFall(game, startColor, linex, liney, 0);
        for (int i = 0; i < linex.length; i++)
            linex[i] = liney[i] = 0;
    }

    public void spawn(ArrayList<Part> parts) {
        linex = new int[length];
        liney = new int[length];

        for (int i = 0; i < 10; i++)
            if (findSpawn(parts))
                break;

        direction = safestDirection(parts);
        color = startColor;

        lastTurn = 0;
        alive = true;
        
        Log.d("ffdsa", "found spawn! " + linex[0] + "," + liney[0] + " going " + direction);
    }

    public boolean findSpawn(ArrayList<Part> parts) {
        linex[0] = (int) (getRand().nextFloat() * (game.width() - 10) + 5);
        liney[0] = (int) (getRand().nextFloat() * (game.height() - 10) + 5);

        for (int i = 0; i < 4; i++)
            if (!safeToTurn(parts, i * (float) Math.PI / 2, speed * 10))
                return false;

        return true;
    }

    public float safestDirection(ArrayList<Part> parts) {
        float newDi, checkingDi;
        int bestClearance = 0;
        boolean collided;
        int clearance;

        x = linex[0];
        y = liney[0];

        newDi = 0;
        for (checkingDi = 0; checkingDi <= (float) Math.PI * 2; checkingDi += (float) Math.PI / 2) {
            for (clearance = 1, collided = false; !collided && clearance < game.height(); clearance += speed) {
                linex[0] = x + (int) Math.cos(checkingDi) * clearance;
                liney[0] = y + (int) Math.sin(checkingDi) * clearance;

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

                        collided = true;
                        break;
                    }
                }
            }
        }

        linex[0] = x;
        liney[0] = y;

        return newDi;
    }

    public boolean safeToTurn(ArrayList<Part> parts, float wd, int distance) {
        x = linex[0];
        y = liney[0];

        for (int distanceChecked = 0; distanceChecked < distance; distanceChecked++) {
            linex[0] = x + (int) Math.cos(wd) * distanceChecked;
            liney[0] = y + (int) Math.sin(wd) * distanceChecked;

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
        int nx, ny;

        if (liney[0] <= 0) {
            nx = game.width() - linex[0];
            ny = game.height() - 1;
        } else if (liney[0] >= game.height()) {
            nx = game.width() - linex[0];
            ny = 1;
        } else if (linex[0] <= 0) {
            nx = game.width() - 1;
            ny = game.height() - liney[0];
        } else if (linex[0] >= game.width()) {
            nx = 1;
            ny = game.height() - liney[0];
        } else
            return;

        if (game.killTailOffScreen())
            newLine();

        linex[0] = nx;
        liney[0] = ny;
    }

    public boolean collides(Part other) {
        int x = other.getX();
        int y = other.getY();

        for (int i = 1; i < linex.length; i++)
            if (x == linex[i])
                if (y == liney[i])
                    return true;
        return false;
    }

    public void setLength(int l) {
        length = l;
    }

    public int getLength() {
        return length;
    }
}
