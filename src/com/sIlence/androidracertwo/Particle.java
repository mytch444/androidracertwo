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

public class Particle {
    private float mx, my;
    private float xv, yv;
    private int age;
    private int start;
    private int	life;
    private int	a, r, g, b;
    private Random rand;
    private boolean alive;
    private Paint brush;
    private int starta;

    public Particle(int color, int x, int y,
		    float O, float speed,
		    int sta, int lif) {
        rand = new Random();
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);

        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);
        starta = a;
        
        mx = x;
        my = y;

        start = sta;
        life = rand.nextInt(lif) + lif / 2;
        age = 0;
        alive = true;

        xv = (float) Math.cos(O) * speed;
        yv = (float) Math.sin(O) * speed;
    }

    public void update() {
        if (!alive) return;
            
        age++;

        if (age > start && age < start + life) {
            mx += xv;//-(xv / (life * life)) * ((age - start) + life) * ((age - start) - life);
	    my += yv;//-(yv / (life * life)) * ((age - start) + life) * ((age - start) - life);
        }

        a = (int) -(((float) starta / ((start + life) * (start + life))) * (age + (start + life)) * (age - (start + life)));
        if (a < 20) alive = false;
    }

    public void render (Canvas c) {
        if (!alive) return;

        brush.setColor(Color.argb(a, r, g, b));
        c.drawPoint(mx, my, brush);
    }

    public boolean isAlive() {
        return alive;
    }



    /****************************** Particle layouts ***********************************/

    public static void initLineFall(GameView v, int color, int[] xa, int[] ya, int state) {
	Random rand = new Random();
        int x0, y0, x1, y1, distance, incdec;
	int i;
	
        for (i = 0; i < xa.length - 1; i++) {
            if (xa[i + 1] == 0 && ya[i + 1] == 0) break;

            x0 = xa[i];
            y0 = ya[i];

            x1 = xa[i + 1];
            y1 = ya[i + 1];

            int xd = x1 - x0;
            int yd = y1 - y0;
            int xid = 1;
            if (xd < 0) xid = -1;
            int yid = 1;
            if (yd < 0) yid = -1;
            
            if (xd > v.boxWidth()) continue;
            if (yd > v.boxHeight()) continue;

	    float O, s;
	    int xp, yp;
	    
            int xj = x0 - xid;
            do {
                int yj = y0 - yid; 
                do {
		    xp = xj + xid;
		    yp = yj + yid;
                    O = rand.nextFloat() * (float) Math.PI * 2;
		    s = 0.3f + rand.nextFloat() * 0.2f - 0.1f;
		    v.addParticle(
				  new Particle(color, xp, yp,
					       O, s,
					       i / 5, i / 5 + 20));
                    yj += yid;
                } while (yj != y1);
                xj += xid;
            } while (xj != x1);
        }
    }
    
    public static void initExplosion(GameView v, int color, int x, int y, int direction, int pixels) {
	Random rand = new Random();
	
	float O, Op, Od, speed, step;
	int d;
	/*
	step = (float) Math.PI * 2 / pixels;
	for (int i = 0; i < pixels; i++) {
	    O += step;
	    speed = v.boxHeight() * (rand.nextFloat() * 0.2f + 1.5f);
	    v.addParticle(new Particle(color, x, y,
				       O, speed,
				       0, 100));
	}
	*/
	O = direction * (float) Math.PI / 2;
	step = 2 * (float) Math.PI / pixels;
	Od = (float) -Math.PI;;
        for (int i = 0; i < pixels; i++) {
	    Od += step;
	    speed = (float) (Math.cos(1.2 * Od) + 1.5f) * (rand.nextFloat() / 2 + 0.2f);

            v.addParticle(new Particle(color, x, y,
				       O + Od, speed,
				       0, 40));
	}
    }
}
