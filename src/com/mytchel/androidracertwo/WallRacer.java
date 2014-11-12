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
import java.util.ArrayList;
import android.util.Log;

public class WallRacer extends LightRacer {

    int d;
    WallRacer other;

    public WallRacer(Game g) {
        super (g, 0xC05FFE3C);
        alive = true;
        linex.remove(1);
        liney.remove(1);
    }

    public WallRacer(Game g, int gap, WallRacer o) {
        this(g);
        other = o;
   
        length = game.width() + game.height() - game.getMargin() * 4 - gap - 2;
        if (gap == 0)
            light = false;

        o.setLength(length);
        o.setLight(light);

        changeIntDirection(0);
        other.changeIntDirection(2);
    }

    public boolean changeIntDirection(int di) {
        d = di;
        direction = di * (float) Math.PI / 2;
        
        if (di == 0) {
            linex.set(0, game.getMargin());
            liney.set(0, game.getMargin());
        } else if (di == 1) {
            linex.set(0, game.width() - game.getMargin());
            liney.set(0, game.getMargin());
        } else if (di ==  2) {
            linex.set(0, game.width() - game.getMargin());
            liney.set(0, game.height() - game.getMargin());
        } else if (di == 3) {
            linex.set(0, game.getMargin());
            liney.set(0, game.height() - game.getMargin());
        }
       
        linex.add(1, linex.get(0));
        liney.add(1, liney.get(0));

        if (other != null) {
            other.changeIntDirection((di + 2) % 4);
        }

        return true;
    }

    @Override
    public void offScreen() {
        if (other != null) {
            if (liney.get(0) <= game.getMargin() && d == 3) changeIntDirection(0);
            else if (linex.get(0) >= game.width() - game.getMargin() && d == 0) changeIntDirection(1);
            else if (liney.get(0) >= game.height() - game.getMargin() && d == 1) changeIntDirection(2);
            else if (linex.get(0) <= game.getMargin() && d == 2) changeIntDirection(3);
        }
    }

    @Override
    public void spawn(ArrayList<Part> parts) {}
    
    @Override
    public void die(int hx, int hy, float di, boolean lives) {
        Particle.initExplosion(game, startColor, hx, hy, di);
    }
}
