package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;
import android.util.Log;

public abstract class Game {

    public static int LOCALPOS = 0;
    
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
        return (LightRacer) parts.get(LOCALPOS);
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
