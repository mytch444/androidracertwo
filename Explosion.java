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

public class Explosion extends Part {

    private Particle[] particles;
    private int age;

    public Explosion (GameView v, int color, int x, int y, int direction, int pixels, int s) {
        super(v);

        dieing = s;

	float O, speed;
	int d;
        particles = new Particle[pixels];
        for (int i = 0; i < particles.length; i++) {
	    O = direction * (float) Math.PI / 2;
	    speed = v.boxHeight() * 0.2f;
            particles[i] = new Particle(color, x, y,
				      O, speed,
				      0, 100);
        }
        age = 0;
    }

    public Explosion(GameView v, int color, int x, int y, int direction, int pixels) {
        this(v, color, x, y, direction, pixels,  0);
    }

    @Override
    public void update() {
        if (dieing != 0) return;

        dieing = 1;
        for (int i = 0; i < particles.length; i++) {
            if (particles[i].isAlive()) {
                particles[i].update();
                dieing = 0;
            }
        }
        age++;
    }

    @Override
    public void render(Canvas c) {
        if (dieing != 0) return;
        for (int i = 0; i < particles.length; i++) {
            if (particles[i].isAlive()) {
                particles[i].render(c);
            }
        }
    }
}
