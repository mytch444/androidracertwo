package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import java.util.Random;

public abstract class Game {

    public static int LOCALPOS = 0;
    public static int OTHERPOS = 1;
    public static int WALL1POS = 2;
    public static int WALL2POS = 3;
    
    protected Part[] parts;

    protected int otherDifficualty;

    protected int kills;
    protected int deaths;
    protected int time;

    protected GameView view;

    protected String winMessage;
    protected String loseMessage;

    protected Random rand;

    public Game(int o) {
        otherDifficualty = o;

        kills = 0;
        deaths = 0;
        time = 0;

	parts = new Part[4];
	
        rand = new Random();
    }

    public void init() {}

    public void setView(GameView v) {
	view = v;
    }
    
    public LightRacer local() {
        return (LightRacer) parts[LOCALPOS];
    }

    public AIRacer other() {
        return (AIRacer) parts[OTHERPOS];
    }

    public WallRacer wall1() {
        return (WallRacer) parts[WALL1POS];
    }

    public WallRacer wall2() {
        return (WallRacer) parts[WALL2POS];
    }

    public int getOtherDifficualty() {
        return otherDifficualty;
    }

    public GameView view() {
        return view;
    }

    public int checkScore() {
        return -1;
    }

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

    public Random rand() {
        return rand;
    }

    public void update() {
	updateLengths();
	for (int i = 0; i < parts.length; i++) {
	    parts[i].update();
	}
    }

    public void render(Canvas c) {
        for (int i = 0; i < parts.length; i++) {
	    parts[i].render(c);
	}
    }
}
