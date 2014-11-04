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
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;
import android.util.Log;

public class Life extends Blockade {

    public Life(Game g, int x, int y) {
        super(g, x, y, 10, 10);
        color = 0xff0000ff;
        startColor = color;
    }

    public Life(Game g) {
        this(g, 0, 0);
    }

    public void spawn(ArrayList<Part> parts) {
        width = 10;
        height = 10;
        findSpawn(parts);
    }
}
