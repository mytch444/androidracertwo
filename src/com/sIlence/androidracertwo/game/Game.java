package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;
import java.util.ArrayList;

public abstract class Game {

    public static int LOCALPOS = 0;
    public static int OTHERPOS = 1;
    public static int WALL1POS = 2;
    public static int WALL2POS = 3;
    
    protected Part[] parts;
    protected ArrayList<Particle> particles;

    protected int otherDifficualty;

    protected int kills;
    protected int deaths;
    protected int time;

    protected GameView view;

    protected String winMessage;
    protected String loseMessage;

    private String textString;
    private int	fromRight;
    private Paint brush;

    protected Random rand;

    public Game(int o) {
        otherDifficualty = o;

        kills = 0;
        deaths = 0;
        time = 0;

	parts = new Part[4];
	
        rand = new Random();
    }

    public void init() {
	brush = new Paint(Paint.ANTI_ALIAS_FLAG);
	fromRight = view.getWidth() / 8;
    }

    public void setView(GameView v) {
	view = v;
    }
    
    public LightRacer local() {
        return (LightRacer) parts[LOCALPOS];
    }

    public LightRacer other() {
        return (LightRacer) parts[OTHERPOS];
    }

    public WallRacer wall1() {
        return (WallRacer) parts[WALL1POS];
    }

    public WallRacer wall2() {
        return (WallRacer) parts[WALL2POS];
    }

    public Particle particles(int i) {
	return particles.get(i);
    }

    public void addParticle(Particle p) {
	particles.add(p);
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
	
	updateLengths();
	for (i = 0; i < parts.length; i++) {
	    parts[i].update();
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
        for (i = 0; i < parts.length; i++) {
	    parts[i].render(c);
	}

	for (i = 0; i < particles.size(); i++) {
	    particles.get(i).render(c);
	}
    }

    public void hud(Canvas c) {
        brush.setColor(0xffffffff);

	int t = getTime() / 1000;

	c.drawText("Time: " + t, 10, brush.getFontSpacing(), brush);

        textString = getKills() + " : " + getDeaths();
        c.drawText(textString, view.getWidth() - fromRight - view.halfWidth(textString), brush.getFontSpacing(), brush);

	//        textString = "Lives: " + local().lives();
	//        c.drawText(textString, view.getWidth() / 2 - view.halfWidth(textString), brush.getFontSpacing(), brush);
    }
}
