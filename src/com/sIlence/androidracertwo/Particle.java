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
import android.graphics.Paint;
import android.view.Surface;
import java.util.Random;
import java.lang.Math;
import android.util.Log;

public class Particle extends Part {
    static Paint brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    //    GameView view;
    float mx, my;
    float xv, yv;
    float w, h;
    int age;
    int start;
    int	life;
    int	a, r, g, b;
    Random rand;
    boolean alive;
    int starta;

    public Particle(GameView v, int color, float x, float y, float width, float height,
		    float O, float speed,
		    int sta, int lif) {
	super(v);
	view = v;
        rand = new Random();
	//        brush = new Paint(Paint.ANTI_ALIAS_FLAG);

        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);
        starta = a;
        
        mx = x;
        my = y;

	w = width;
	h = height;

        start = sta;
        life = rand.nextInt(lif) + lif / 2;
        age = 0;
        alive = true;

        xv = (float) Math.cos(O) * speed;
        yv = (float) Math.sin(O) * speed;
    }

    public Particle(GameView v, int color, float x, float y,
		    float O, float speed,
		    int sta, int lif) {
	this(v, color, x, y, 0, 0, O, speed, sta, lif);
    }


    public void update() {
        if (!alive) return;
            
        age++;

        if (age > start && age < start + life) {
            mx += -(xv / (life * life)) * ((age - start) + life) * ((age - start) - life);
	    my += -(yv / (life * life)) * ((age - start) + life) * ((age - start) - life);
        }

	a = //(int) (255 / (age));
	    (int) -(((float) starta / ((start + life) * (start + life))) * (age + (start + life)) * (age - (start + life)));
        if (a < 20) alive = false;

	// Ohh sweet mother of lag this should be interesting.
	for (int i = 0; i < view.getParts().size; i++) {
	    Part p = view.getParts().get(i);
	    if (p.isAlive() && collides(p)) {
		xv = -xv;
		yv = -yv;
		break;
	    }
	}
    }

    public void render (Canvas c) {
        if (!alive) return;

        brush.setColor(Color.argb(a, r, g, b));
	if (w == 0 || h == 0)
	    c.drawPoint(view.toPoint(mx, true), view.toPoint(my, false), brush);
	else
	    c.drawRect(view.toPoint(mx, true), view.toPoint(my, false),
		       view.toPoint(mx + w, true), view.toPoint(my + h, false), brush);
    }

    public boolean isAlive() {
        return alive;
    }

    /****************************** Particle layouts ***********************************/

    public static void initLineFall(GameView v, int color, float[] xa, float[] ya, int startIndex) {
	Random rand = new Random();
        float x0, y0, x1, y1, xd, yd, xj, yj, xid, yid;
	int i;

	float stepSizeX = (float) v.width() / v.getWidth() / 3;
	float stepSizeY = (float) v.height() / v.getHeight() / 3;
	
        for (i = 1; startIndex + i < xa.length; i++) {
            x0 = xa[startIndex + i];
            y0 = ya[startIndex + i];

            x1 = xa[startIndex + i - 1];
            y1 = ya[startIndex + i - 1];

	    if (x0 == 0 && y0 == 0 ||
		x1 == 0 && y1 == 0)
		continue;

            xd = x1 - x0;
            yd = y1 - y0;

	    if (Math.abs(xd) > v.width() / 2 || 
		Math.abs(yd) > v.height() / 2)
		continue;

            xid = xd > 0 ? stepSizeX : -stepSizeX;
            yid = yd > 0 ? stepSizeY : -stepSizeY;
            
	    float O, s;
	    float xp, yp;
	    
	    xj = x0;
            do {
		yj = y0;
                do {
                    O = rand.nextFloat() * (float) Math.PI * 2;
		    s = 0.15f + rand.nextFloat() * 0.1f;
		    v.getParticles().add(
				  new Particle(v, color, xj, yj,
					       O, s,
					       i / 8, 20));
                    yj += yid;
                } while ((yid > 0 && yj < y1) || (yid < 0 && yj > y1));
                xj += xid;
            } while ((xid > 0 && xj < x1) || (xid < 0 && xj > x1));
        }
    }
    
    public static void initExplosion(GameView v, int color, float x, float y, float O) {
	Random rand = new Random();
	
	float Op, Od, speed, step;
	int d, i, n;

	n = 150;
	step = 2 * (float) Math.PI / n;
	Od = (float) -Math.PI;;
        for (i = 0; i < n; i++) {
	    Od += step;
	    speed = (float) (Math.cos(1.3 * Od) + 1.5f) * (rand.nextFloat() * 0.5f + 0.1f);

            v.getParticles().add(new Particle(v, color, x, y,
				       O + Od, speed,
				       0, 40));
	}
    }
}
