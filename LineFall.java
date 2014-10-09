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
import java.util.ArrayList;
import android.util.Log;

public class LineFall extends Part {

    protected Particle[] particles;
    protected int i;
    
    public LineFall(GameView v, int color, int[] xa, int[] ya, int state) {
        super(v);
        dieing = state;

	ArrayList<Particle> particles = new ArrayList<Particle>();
	
        int x0, y0, x1, y1, distance, incdec;
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
            
            if (xd > view.boxWidth()) continue;
            if (yd > view.boxHeight()) continue;

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
		    //		    particles.add(
		    v.addParticle(
				  new Particle(color, xp, yp,
					       O, s,
					       i / 5, 40));
                    yj += yid;
                } while (yj != y1);
                xj += xid;
            } while (xj != x1);
        }

	//	this.particles = new Particle[particles.size()];
	//	for (i = 0; i < particles.size(); i++) this.particles[i] = particles.get(i);
    }

    @Override
    public void update() {
        if (dieing != 0) return;
        dieing = 1;
        for (i = 0; i < particles.length; i++) {
            if (particles[i].isAlive()) {
                particles[i].update();
                dieing = 0;
            }
        }
    }

    @Override
    public void render(Canvas c) {
        if (dieing != 0) return;
        for (i = 0; i < particles.length; i++) {
            particles[i].render(c);
        }
    }
}
