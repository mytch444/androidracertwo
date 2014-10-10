package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;
import java.util.ArrayList;
import java.util.ArrayList;

public class DodgeGame extends Game {

    protected int level;

    public DodgeGame(int o) {
        super(o);
    }

    public void init() {
	super.init();
	
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

	blockades = new Blockade[nblockades];
	lives = new Life[nlives];

	for (int i = 0; i < blockades.length; i++) {
	    blockades[i] = new Blockade(view);
	}
	for (int i = 0; i < lives.length; i++) {
	    lives[i] = new Life(view);
	}
	    
        parts = new Part[1 + blockades.length + lives.length];

	parts[LOCALPOS] =
	    new LightRacer(view, 0xC004CCF1, GameView.INCREASE_DEATHS);

        for (int i = 0; i < blockades.length; i++) {
            parts[1 + i] = blockades[i];
        }
	for (int i = 0; i < lives.length; i++) {
	    parts[1 + i + blockades.length] = lives[i];
	}

        local().setOpps(parts);
        local().setLength(1);
	local().spawn();

        for (int i = 0; i < 5; i++) update();
        for (int i = 0; i < blockades.length; i++) blockades[i].spawn(parts);
	for (int i = 0; i < lives.length; i++) lives[i].spawn(parts);
    }

    public void checkScore() {
        if (getDeaths() > level) {
	    view.gameOver(false);
        }
    }

    public void checkCollisions() {
	LightRacer local = local();
	if (!local.isAlive())
	    return;
	for (int i = 0; i < parts.length; i++) {
	    if (parts[i].collides(local)) {
		local.die(local.getX(), local.getY(), local.getDirection());
		setDeaths(getDeaths() + 1);
		checkScore();
	    }
	}
    }

    public void updateLengths() {
        local().setLength(local().getLength() + 1);
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

    public LightRacer other() {
        return null;
    }

    public WallRacer wall1() {
	return null;
    }

    public WallRacer wall2() {
	return null;
    }
}
