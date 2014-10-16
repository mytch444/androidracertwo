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

package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;
import java.util.ArrayList;
import java.util.ArrayList;

public class TronGame extends Game {

    static int GAME_END_DELAY = 30;
    
    LightRacer other;
    int startKills, startDeaths;
    int stopDelay;
    int localLives, otherLives;

    public TronGame(int o) {
        super(o);

        winMessage = "Round Over\nYou won";
        loseMessage = "Round Over\nYou Lost";

	localLives = otherLives = 0;
    }

    public void init() {
	super.init();

	stopDelay = 0;
	
        float x0, y0, x1, y1;
	int d0, d1;

        x0 = x1 = view.width() / 2;
	
        y0 = view.height() / 2 - 5;
	d0 = 3;
	y1 = view.height() / 2 + 5;
	d1 = 1;

	int nblockades, nlives;
	nblockades = 0;
	nlives = 0;

	if (kills > 0) {
	    nblockades += rand.nextInt(kills) + kills / 2;
	    nlives += rand.nextInt(kills) + kills / 2;
	}

	particles = new ArrayList<Particle>();
	parts = new ArrayList<Part>();

	local = new LightRacer(view, 0xC004CCF1, x0, y0, d0);
	other = new AIRacer(view, getOtherDifficualty(), x1, y1, d1);
	parts.add((Part) local);
	parts.add((Part) other);
        parts.add((Part) new WallRacer(view, 1, 1, 0));
        parts.add((Part) new WallRacer(view, view.width() - 1, view.height() - 1, 0));

	for (int i = 0; i < nblockades; i++)
	    parts.add((Part) new Blockade(view));
	for (int i = 0; i < nlives; i++)
	    parts.add((Part) new Life(view));

        local.setLength(1);
        other.setLength(1);

        startKills = kills;
        startDeaths = deaths;

	for (int i = 0; i < parts.size(); i++)
	    parts.get(i).spawn(parts);
        for (int i = 0; i < 5; i++) update();
    }

    public void checkScore() {
        if (getKills() > startKills) {
            view.gameOver(true);
        } else if (getDeaths() > startDeaths) {
            view.gameOver(false);
        }
    }

    public void checkCollisions() {
	for (int i = 0; i < parts.size(); i++) {
	    if (!parts.get(i).isAlive())
		continue;
	    
	    if (local.isAlive() && parts.get(i).collides(local))
		handleCollision(parts.get(i), true);

	    if (other.isAlive() && parts.get(i).collides(other))
		handleCollision(parts.get(i), false);
	}
    }

    public void handleCollision(Part p, boolean localL) {
	if (p.getClass() == Life.class) {
	    if (localL) {
		p.die(local.getX(), local.getY(), local.getDirection(), false);
		localLives++;
	    } else {
		p.die(other.getX(), other.getY(), other.getDirection(), false);
		otherLives++;		
	    }
	} else if (localL && localLives > 0) {
	    p.die(local.getX(), local.getY(), local.getDirection(), true);
	    localLives--;
	} else if (!localL && otherLives > 0) {
	    p.die(other.getX(), other.getY(), other.getDirection(), true);
	    otherLives--;
	} else if (localL) {
	    local.die(local.getX(), local.getY(), local.getDirection(), false);
	    if (stopDelay == 0) {
		setDeaths(getDeaths() + 1);
		stopDelay = GAME_END_DELAY;
	    }
	} else if (!localL) {
	    other.die(other.getX(), other.getY(), other.getDirection(), false);
	    if (stopDelay == 0) {
		setKills(getKills() + 1);
		stopDelay = GAME_END_DELAY;
	    }
	}
    }

    public void update() {
	super.update();
	if (stopDelay > 0) {
	    time -= view.framePeriod();
	    stopDelay--;
	    if (stopDelay == 0)
		checkScore();
	}
    }

    public void updateLengths() {
        local.setLength(local.getLength() + 1);
	other.setLength(other.getLength() + 1);
    }

    public boolean killTailOffScreen() {
	return false;
    }

    public void hud(Canvas c) {
	int t;
        brush.setColor(0xffffffff);
	brush.setTextSize(view.topBorder() - 6);

	t = getTime() / 1000;
	c.drawText("Time: " + t, 10, view.topBorder() - 4, brush);
	textString = "Lives: " + localLives + ":" + otherLives;
	c.drawText(textString, view.getWidth() / 2 - view.textWidth(textString, brush) / 2, view.topBorder() - 4, brush);
        textString = getKills() + " : " + getDeaths();
        c.drawText(textString, view.getWidth() - 10 - view.textWidth(textString, brush), view.topBorder() - 4, brush);
    }

    public String startMessage() {
	return "You are blue. Make yellow crash. The blue objects are lives, the others surprises. Good luck.";
    }
}
