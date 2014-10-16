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
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;
import android.util.Log;

public abstract class Game {

    LightRacer local;
    ArrayList<Part> parts;
    ArrayList<Particle> particles;

    int otherDifficualty;

    int kills;
    int deaths;
    int time;

    GameView view;

    String winMessage;
    String loseMessage;

    String textString;
    Paint brush;

    Random rand;

    public Game(int o) {
        otherDifficualty = o;

        kills = 0;
        deaths = 0;
        time = 0;

        rand = new Random();
    }

    public void init() {
	brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setView(GameView v) {
	view = v;
    }
    
    public LightRacer getLocal() {
        return local;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public ArrayList<Particle> getParticles() {
	return particles;
    }

    public int getOtherDifficualty() {
        return otherDifficualty;
    }

    public GameView view() {
        return view;
    }

    public void checkScore() {}

    public void updateLengths() {}

    public String winMessage() {
        return winMessage;
    }

    public String loseMessage() {
        return loseMessage;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTime() {
        return time;
    }

    public void setKills(int k) {
        kills = k;
    }

    public void setDeaths(int d) {
        deaths = d;
    }

    public void setTime(int t) {
        time = t;
    }

    public boolean killTailOffScreen() {
	return true;
    }

    public Random rand() {
        return rand;
    }

    public String startMessage() {
	return "You have as good of an idea as I of what you should do. Try pressing begin (but that may not exist so do whatever) and find the person that gave you this and scream at them. Unless it's me. Then I'm just enlightening you with this messsage and the lack of a start message was completely intentional. Good luck.";
    }
    
    public void checkCollisions() {}

    public void update() {
	int i;

	time += view.framePeriod();
	
	updateLengths();
	for (i = 0; i < parts.size(); i++) {
	    parts.get(i).update();
	}

	for (i = 0; i < particles.size(); i++) {
	    Particle p = particles.get(i);
	    if (!p.isAlive())
		particles.remove(p);
	    else
		p.update();
	}

	checkCollisions();
    }

    public void render(Canvas c) {
	int i;
        for (i = 0; i < parts.size(); i++) {
	    parts.get(i).render(c);
	}

	for (i = 0; i < particles.size(); i++) {
	    particles.get(i).render(c);
	}
    }

    public void hud(Canvas c) {}
}
