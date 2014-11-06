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
import java.util.ArrayList;

public class AIRacer extends LightRacer {

    int difficualty;

    ArrayList<Part> parts;

    public AIRacer(Game g) {
        super(g, 0xC0FFE64D);
        difficualty = g.getDifficualty() * 10;
        startColor = color;
    }

    public AIRacer(Game g, int x, int y, float d) {
        this(g);

        linex[0] = x;
        liney[0] = y;
        direction = d;
    }

    public void spawn(ArrayList<Part> parts) {
        super.spawn(parts);
        this.parts = parts;
    }

    @Override
    public void update() {
        if (!isAlive())
            return;

        updateLength();
        updateLine();
        move();
        offScreen();

        float nd = -1;
        if (getRand().nextInt(16) == 1 || (game.getTime() % 10 == 0 && !safeToTurn(parts, direction, difficualty)))
            nd = safestDirection(parts);
        if (nd != -1) {
            int lag = getRand().nextInt(20000 / difficualty);
            final float di = nd;

            game.getView().postDelayed(new Runnable() {
                public void run() {
                    changeDirection(di);
                }
            }, lag);
        }
    }
}
