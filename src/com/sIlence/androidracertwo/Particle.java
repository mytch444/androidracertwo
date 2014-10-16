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
    float xv, yv;
    float w, h;
    int age;
    int start;
    int	life;
    int	a, r, g, b;
    int starta;
    int bounced;

    public static float width(GameView v) {
	return v.width() / v.getWidth() * 2;
    }

    public static float height(GameView v) {
	return v.height() / v.getHeight() * 2;
    }
    
    public Particle(GameView v, int color, float x, float y,
		    float O, float speed,
		    int sta, int lif) {
	super(v);

        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);
        starta = a;
        
        this.x = x;
        this.y = y;

	w = width(v);
	h = height(v);

        start = sta;
        life = lif;//(int) (v.getRand().nextFloat() * lif) + lif / 2;
        age = 0;

	bounced = 0;

        xv = (float) Math.cos(O) * speed;
        yv = (float) Math.sin(O) * speed;
    }

    public void update() {
        age++;

        if (age > start && age < start + life) {
            x += -(xv / (life * life)) * ((age - start) + life) * ((age - start) - life);
	    y += -(yv / (life * life)) * ((age - start) + life) * ((age - start) - life);
        }

	a = (int) -(((float) starta / ((start + life) * (start + life))) * (age + (start + life)) * (age - (start + life)));
        if (a < 30) alive = false;

	color = Color.argb(a, r, g, b);
	/*
	// Ohh sweet mother of lag this should be interesting.
	if (bounced == 0) {
	    for (int i = 0; i < view.getParts().size(); i++) {
		Part p = view.getParts().get(i);
		if (p.isAlive() && p.collides(this)) {
		    bounced = 5;
		    xv = -xv;
		    yv = -yv;
		    break;
		}
	    }
	} else
	    bounced--;
	*/
    }

    public void render (Canvas c) {
        brush.setColor(color);
	c.drawRect(view.toXPoint(x), view.toYPoint(y),
		   view.toXPoint(x + w), view.toYPoint(y + h), brush);
    }

    /****************************** Particle layouts ***********************************/

    public static void initLineFall(GameView v, int color, float[] xa, float[] ya, int startIndex) {
        float x0, y0, x1, y1, xd, yd, xj, yj, xid, yid;
	int i;

	float stepSizeX = width(v);
	float stepSizeY = height(v);
	
        for (i = 1; startIndex + i < xa.length; i++) {
	    if (startIndex + i - 1 < 0)
		continue;
	    
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
                    O = v.getRand().nextFloat() * (float) Math.PI * 2;
		    s = 0.15f + v.getRand().nextFloat() * 0.1f;
		    v.getParticles().add(
				  new Particle(v, color, xj, yj,
					       O, s,
					       i < 160 ? i / 8 : 20, 20));
                    yj += yid;
                } while ((yid > 0 && yj < y1) || (yid < 0 && yj > y1));
                xj += xid;
            } while ((xid > 0 && xj < x1) || (xid < 0 && xj > x1));
        }
    }
    
    public static void initExplosion(GameView v, int color, float x, float y, float O) {
	double Od, step;
	float speed;
	int n;

	n = 60;
	step = 2 * Math.PI / n;
        for (Od = -Math.PI; Od < Math.PI; Od += step) {
	    speed = (float) (Math.cos(1.3 * Od) + 1.5f) * (v.getRand().nextFloat() * 0.5f + 0.1f);

            v.getParticles().add(new Particle(v, color, x, y,
					      O + (float) Od, speed,
					      0, 40));
	}
    }
}
