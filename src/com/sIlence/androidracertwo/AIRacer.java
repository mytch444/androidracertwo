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

public class AIRacer extends LightRacer {

    public static final int DIFF_CHILD = 10;
    public static final int DIFF_EASY = 30;
    public static final int DIFF_MEDI = 50;
    public static final int DIFF_HARD = 100;
    public static final int DIFF_INSANE = 300;

    int difficualty;
    int nd, r, d;

    public AIRacer(GameView v, int d, int stc) {
        super(v, 0xC0FFE64D, stc);
        startColor = color;
        difficualty = d;
        lives = 0;
    }

    public AIRacer(GameView v, int d, int stc, int x, int y, int dd) {
        this(v, d, stc);

        linex[0] = x;
        liney[0] = y;
        direction = dd;
    }

    @Override
    public void update() {
	if (dieing > 0) {
	    updateDieing();
        } else {
	    updateLength();
	    updateLine();
	    move();
	    offScreen();

	    nd = -1;
	    r = -1;
	    if (rand.nextInt(16) == 1) r = safestDirection();
	    d = tryToDodge();
	    if (r != -1) nd = r;
	    if (d != -1) nd = d;
	    if (nd != -1) {
		final int lag = rand.nextInt(20000 / difficualty);
		final int di = nd;

		view.postDelayed(new Runnable() {
			public void run() {
			    changeDirection(di);
			}
		    }, lag);
	    }
	}
    }

    private int tryToDodge() { // Try avoid other player
        if (!safeToTurn(direction, difficualty)) {
            return safestDirection();
        }
        return -1;
    }
}
