package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;
import java.util.ArrayList;
import java.util.ArrayList;

public class DodgeGame extends Game {

    static int GAME_END_DELAY = 30;

    int level, stopDelay;

    public DodgeGame(int o) {
        super(o);
    }

    public void init() {
	super.init();

	stopDelay = 0;
	
	setTime(0);
	setKills(0);

	level = deaths;

	particles = new ArrayList<Particle>();
	
	Blockade[] blockades;
	Life[] lives;

	int nblockades, nlives;
	nblockades = 1 + getOtherDifficualty() / 20;
	nlives = 0;

	if (kills > 0) {
	    nblockades += rand.nextInt(level) + level / 2;
	    nlives += rand.nextInt(level) + level / 2;
	}

        parts = new ArrayList<Part>();

	parts.add(LOCALPOS,
		  (Part) new LightRacer(view, 0xC004CCF1));

        for (int i = 0; i < nblockades; i++)
            parts.add((Part) new Blockade(view));
	for (int i = 0; i < nlives; i++)
	    parts.add((Part) new Life(view));

        getLocal().setLength(1);

	for (int i = 0; i < parts.size(); i++)
	    parts.get(i).spawn(parts);
        for (int i = 0; i < 5; i++) update();
    }

    public void checkScore() {
        if (getDeaths() > level) {
	    view.gameOver(false);
        }
    }

    public void checkCollisions() {
	LightRacer local = getLocal();
	if (!local.isAlive())
	    return;
	for (int i = 0; i < parts.size(); i++) {
	    if (parts.get(i).isAlive() && parts.get(i).collides(local)) {
		local.die(local.getX(), local.getY(), local.getDirection());
		if (stopDelay == 0) {
		    setDeaths(getDeaths() + 1);
		    stopDelay = GAME_END_DELAY;
		}
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
        getLocal().setLength(getLocal().getLength() + 1);
    }

    public void hud(Canvas c) {
	int t;
        brush.setColor(0xffffffff);
	brush.setTextSize(view.topBorder() - 6);

	t = getTime() / 1000;
	c.drawText("Time: " + t, 10, view.topBorder() - 4, brush);
        textString = "Level: " + getDeaths();
        c.drawText(textString, view.getWidth() - 10 - view.textWidth(textString, brush), view.topBorder() - 4, brush);

	brush.setStrokeWidth(0.0f);
	c.drawLine(0, view.topBorder(), view.getWidth(), view.topBorder(), brush);
    }

    public boolean killTailOffScreen() {
	return false;
    }

    public String winMessage() {
	return "ERROR, YOU CANNOT WIN!";
    }

    public String loseMessage() {
	return "You survived for " + (view.getTime() / 1000) + " seconds.";
    }
}
