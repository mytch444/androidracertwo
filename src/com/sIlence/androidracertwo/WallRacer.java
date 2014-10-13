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

public class WallRacer extends LightRacer {

    public WallRacer (GameView v, float x, float y, float gap) {
        super (v, 0xC05FFE3C);

        direction = 3;

        length = (int) (view.width() + view.height() - gap - 2);

        if (gap == 0)
	    light = false;

	alive = true;
	
        linex = new float[length];
        liney = new float[length];

        linex[0] = x;
        liney[0] = y;

        for (int i = 0; i < length; i++) {
            update();
        }
    }

    public void spawn(ArrayList<Part> parts) {

    }

    @Override
    public boolean changeDirection(int di) {
        direction = di;
        return true;
    }

    @Override
    public void update() {
	offScreen();
        move();
        updateLine();
    }

    @Override
    public void offScreen() {
        if (linex[0] < 2 && liney[0] > view.height() / 2) changeDirection(3);
        else if (liney[0] < 2 && linex[0] < view.width() / 2) changeDirection(0);
        else if (linex[0] > view.width() - 2 && liney[0] < view.height() / 2) changeDirection(1);
        else if (liney[0] > view.height() - 2 && linex[0] > view.width() / 2) changeDirection(2);
    }

    @Override
    public void render(Canvas c) {
        renderLines(c);
    }

    public void die(float hx, float hy, float di, boolean lives) {
	Particle.initExplosion(view, startColor, hx, hy, di);
    }
}
