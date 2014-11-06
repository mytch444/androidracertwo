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
import java.util.ArrayList;
import android.util.Log;

public class WallRacer extends LightRacer {

    public WallRacer (Game g, int x, int y, int gap) {
        super (g, 0xC05FFE3C, x, y, 3f * (float) Math.PI / 2f);
        
        length = (game.width() + game.height() - gap - 2) / speed;

        Log.d("fdsa", "set length to " + length);

        if (gap == 0)
            light = false;

        alive = true;

        for (int i = 0; i < length; i++)
            update();
    }

    public void spawn(ArrayList<Part> parts) {}

    @Override
    public boolean changeDirection(float di) {
        direction = di;
        linex.add(1, linex.get(0));
        liney.add(1, liney.get(0));

        return true;
    }

    @Override
    public void update() {
        offScreen();
        move();
        updateLength();
    }

    @Override
    public void offScreen() {
        if (linex.get(0) < 2 && liney.get(0) > game.height() / 2) changeDirection(3f * (float) Math.PI / 2f);
        else if (liney.get(0) < 2 && linex.get(0) < game.width() / 2) changeDirection(0);
        else if (linex.get(0) > game.width() - 2 && liney.get(0) < game.height() / 2) changeDirection((float) Math.PI / 2f);
        else if (liney.get(0) > game.height() - 2 && linex.get(0) > game.width() / 2) changeDirection((float) Math.PI);
    }

    public void die(int hx, int hy, float di, boolean lives) {
        Particle.initExplosion(game, startColor, hx, hy, di);
    }
}
