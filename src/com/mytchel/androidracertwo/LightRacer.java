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

package com.mytchel.androidracertwo;

import com.mytchel.androidracertwo.game.Game;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Surface;
import java.util.ArrayList;
import android.util.Log;

public class LightRacer extends Part {
    public static int STANDARD_SPEED(Game g) {
        return g.width() / 70;
    }

    public static int STANDARD_LENGTH(Game g) {
        return (int) (g.width() * 0.7f);
    }

    ArrayList<Integer> linex, liney;
    int length, startLength;

    boolean light;

    long lastTurn;

    public LightRacer(Game g, int c) {
        super(g);

        linex = new ArrayList<Integer>();
        liney = new ArrayList<Integer>();
        linex.add(0);
        liney.add(0);
        linex.add(0);
        liney.add(0);

        speed = STANDARD_SPEED(g);
        length = STANDARD_LENGTH(g);
        startLength = length;

        direction = 0;

        color = c;
        startColor = color;
        light = true;
    }

    public LightRacer(Game g, int c, int x, int y, float d) {
        this(g, c);

        linex.set(0, x);
        liney.set(0, y);
        linex.set(1, x);
        liney.set(1, y);
        direction = d;
    }

    @Override
    public void update() {
        move();
        offScreen();
        updateLength();
    }

    public void updateLength() {
        if (length == -1)
            return;

        int l, ll, i, size, e;
        int px, py;
        float d;

        l = 0;
        size = linex.size();
        
        for (i = 1; i < size; i++) {
            if (linex.get(i) == -1 && liney.get(i) == -1) {
                i++;
                continue;
            }

            px = linex.get(i) - linex.get(i - 1); 
            py = liney.get(i) - liney.get(i - 1); 

            ll = (px < 0 ? -px : px) + (py < 0 ? -py : py);
            
            if (l + ll >= length) {
                if (l + ll > length) {
                    d = directionFromDifferences(px, py);

                    ll = length - l;

                    linex.set(i, linex.get(i - 1) + (int) (Math.cos(d) * ll));
                    liney.set(i, liney.get(i - 1) + (int) (Math.sin(d) * ll));
                }

                for (e = i, i = size - 1; i > e; i--) {
                    linex.remove(i);
                    liney.remove(i);
                }

                break;
            }

            l += ll;
        }
    }

    @Override
    public void die(int hx, int hy, float di, boolean lives) {
        int start;
    
        Particle.initExplosion(game, startColor, hx, hy, di);

    /*    for (start = linex.length - 1; start > 0 &&
                !((hx >= linex[start] - 0.5f && hx <= linex[start] + 0.5f) &&
                    (hy >= liney[start] - 0.5f && hy <= liney[start] + 0.5f))
                ; start--);
*/
        if (linex.get(0) == hx && liney.get(0) == hy && !lives) {
            alive = false;
            start = 0;
        }

        start = 30;
        if (start > 4)
            start -= 2;

        Particle.initLineFall(game, startColor, linex, liney,
                // It looks nice to have the last few also go up in pixels.
                start > 4 ? start - 2 : start);
/*
        if (start > 0) {
            for (; start < linex.length; start++) {
                linex[start] = 0;
                liney[start] = 0;
            }
        }
        */
    }

    public void move() {
        linex.set(0, linex.get(0) + (int) (Math.cos(direction) * speed));
        liney.set(0, liney.get(0) + (int) (Math.sin(direction) * speed));
    }

    public int getX() {
        return linex.get(0);
    }

    public int getY() {
        return liney.get(0);
    }

    public void setLength(int l) {
        length = l;
    }

    public int getLength() {
        return length;
    }

    public int getStartLength() {
        return startLength;
    }

    public void setLight(boolean l) {
        light = l;
    }

    public boolean getLight() {
        return light;
    }

    public void render(Canvas c) {
        float x, y, x1, y1;

        int i, size;
        size = linex.size();

        brush.setStrokeWidth(0f);
        brush.setColor(color);

        x = game.getView().toXPoint(linex.get(0));
        y = game.getView().toYPoint(liney.get(0));
        for (i = 1; i < size; i++) {
            if (linex.get(i) == -1 && liney.get(i) == -1) { // If break skip the next two.
                if (i + 1 >= size)
                    break;
                x = game.getView().toXPoint(linex.get(i + 1));
                y = game.getView().toYPoint(liney.get(i + 1));
                i++;
                continue;
            }

            x1 = game.getView().toXPoint(linex.get(i));
            y1 = game.getView().toYPoint(liney.get(i));

            c.drawLine(x, y, x1, y1, brush);
            
            x = x1;
            y = y1;
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

            linex.add(1, linex.get(0));
            liney.add(1, liney.get(0));

            return true;
        }
    }

    protected void newLine() {
        //Particle.initLineFall(game, startColor, linex, liney, 0);

        x = linex.get(0);
        y = liney.get(0);
        linex = new ArrayList<Integer>();
        liney = new ArrayList<Integer>();

        linex.add(x);
        linex.add(x);

        liney.add(y);
        liney.add(y);
    }

    public void spawn(ArrayList<Part> parts) {
        linex = new ArrayList<Integer>();
        liney = new ArrayList<Integer>();
        linex.add(10);
        liney.add(100);

        for (int i = 0; i < 10; i++)
            if (findSpawn(parts))
                break;

        linex.add(linex.get(0));
        liney.add(liney.get(0));

        direction = safestDirection(parts);
        color = startColor;

        lastTurn = 0;
        alive = true;
    }

    public boolean findSpawn(ArrayList<Part> parts) {
        linex.set(0, (int) (getRand().nextFloat() * (game.width() - 10) + 5));
        liney.set(0, (int) (getRand().nextFloat() * (game.height() - 10) + 5));

        for (int i = 0; i < 4; i++)
            if (!safeToTurn(parts, i * (float) Math.PI / 2, speed * 10))
                return false;

        return true;
    }

    public float safestDirection(ArrayList<Part> parts) {
        return 0;
        /*
        float newDi, checkingDi;
        int bestClearance = 0;
        boolean collided;
        int clearance;

        x = linex.get(0);
        y = liney.get(0);

        newDi = 0;
        for (checkingDi = 0; checkingDi <= (float) Math.PI * 2; checkingDi += (float) Math.PI / 2) {
            for (clearance = 1, collided = false; !collided && clearance < game.height(); clearance += speed) {
                linex.set(0, x + (int) Math.cos(checkingDi) * clearance);
                liney.set(0, y + (int) Math.sin(checkingDi) * clearance);

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

        linex.set(0, x);
        liney.set(0, y);

        return newDi;
        */
    }

    public boolean safeToTurn(ArrayList<Part> parts, float wd, int distance) {
        x = linex.get(0);
        y = liney.get(0);

        for (int distanceChecked = 0; distanceChecked < distance; distanceChecked++) {
            linex.set(0, x + (int) Math.cos(wd) * distanceChecked);
            liney.set(0, y + (int) Math.sin(wd) * distanceChecked);

            for (int i = 0; i < parts.size(); i++) {
                if (parts.get(i).collides(this)) {
                    linex.set(0, x);
                    liney.set(0, y);
                    return false;
                }
            }
        }

        linex.set(0, x);
        liney.set(0, y);

        return true;
    }

    public void offScreen() {
        int nx, ny, ox, oy;

        if (liney.get(0) <= game.getMargin()) {
            nx = game.width() - linex.get(0);
            ny = game.height() - game.getMargin();
            ox = linex.get(0);
            oy = game.getMargin();
        } else if (liney.get(0) >= game.height() - game.getMargin()) {
            nx = game.width() - linex.get(0);
            ny = game.getMargin();
            ox = linex.get(0);
            oy = game.height() - game.getMargin();
        } else if (linex.get(0) <= game.getMargin()) {
            nx = game.width() - game.getMargin();
            ny = game.height() - liney.get(0);
            ox = game.getMargin();
            oy = liney.get(0);
        } else if (linex.get(0) >= game.width() - game.getMargin()) {
            nx = game.getMargin();
            ny = game.height() - liney.get(0);
            ox = game.width() - game.getMargin();
            oy = liney.get(0);
        } else
            return;

        if (game.killTailOffScreen()) {
            newLine();
            linex.remove(1);
            liney.remove(1);
        } else {
            linex.add(1, ox);
            liney.add(1, oy);
            
            // Remember that there is a break here.
            linex.add(1, -1);
            liney.add(1, -1);
        }

        linex.add(1, nx);
        liney.add(1, ny);

        linex.set(0, nx);
        liney.set(0, ny);
    }

    public boolean collides(Part other) {
        if (linex.size() < 3)
            return false;

        int i, size, xo1, xo2, yo1, yo2;
        int x1, x2, y1, y2;
        int xc, yc;

        xo1 = other.getX();
        yo1 = other.getY();
        xo2 = xo1 + (int) (Math.cos(other.getDirection()) * other.getSpeed());
        yo2 = yo1 + (int) (Math.sin(other.getDirection()) * other.getSpeed());
        
        x1 = linex.get(1);
        y1 = liney.get(1);

        size = linex.size() - 1;
    /*    for (i = 2; i < size; i++) {
            x2 = linex.get(i);
            y2 = liney.get(i);

            if (doBoundingBoxesIntersect(xo1, yo1, xo2, yo2, x1, y1, x2, y2)
                    && lineSegmentTouchesOrCrossesLine(xo1, yo1, xo2, yo2, x1, y1, x2, y2)
                    && lineSegmentTouchesOrCrossesLine(x1, y1, x2, y2, xo1, yo1, xo2, yo2))
                return true;

            x1 = x2;
            y1 = y2;
        }
*/
        return false;
    }

    public static boolean doBoundingBoxesIntersect(int ax1, int ay1, int ax2, int ay2,
            int bx1, int by1, int bx2, int by2) {
        return ax1 <= bx1 && ax2 >= bx1
            && ay1 <= by2 && ay2 >= by1;
    }

    public static int crossProduct(int ax, int ay, int bx, int by) {
        return ax * by - bx * ay;
    }

    public static int crossProductOfLineAndPoint(int ax1, int ay1, int ax2, int ay2, int bx, int by) {
        ax2 -= ax1;
        ay2 -= ay1;
        bx -= ax1;
        by -= ay1;
        ax1 = 0;
        ay1 = 0;
        return crossProduct(ax2, ay2, bx, by);
    }

    public static boolean isPointOnLine(int ax1, int ay1, int ax2, int ay2, int bx, int by) {
        return crossProductOfLineAndPoint(ax1, ay1, ax2, ay2, bx, by) == 0;
    }

    public static boolean isPointRightOfLine(int ax1, int ay1, int ax2, int ay2, int bx, int by) {
        return crossProductOfLineAndPoint(ax1, ay1, ax2, ay2, bx, by) < 0;
    }

    public static boolean lineSegmentTouchesOrCrossesLine(int ax1, int ay1, int ax2, int ay2,
            int bx1, int by1, int bx2, int by2) {
        return isPointOnLine(ax1, ay1, ax2, ay2, bx1, by1)
            || isPointOnLine(ax1, ay1, ax2, ay2, bx2, by2)
            || (isPointRightOfLine(ax1, ay1, ax2, ay2, bx1, by1) 
                    ^ isPointRightOfLine(ax1, ay1, ax2, ay2, bx2, by2));
    }
}
