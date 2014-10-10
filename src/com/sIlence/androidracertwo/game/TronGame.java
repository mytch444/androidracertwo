package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;
import java.util.ArrayList;
import java.util.ArrayList;

public class TronGame extends Game {

    protected int startKills;
    protected int startDeaths;

    public TronGame(int o) {
        super(o);

        winMessage = "Round Over\nYou won";
        loseMessage = "Round Over\nYou Lost";
    }

    public void init() {
	super.init();
	
        float x0, y0, x1, y1;
	int d0, d1;

        x0 = x1 = view.width() / 2;
	
        y0 = view.height() / 2 - 5;
	d0 = 3;
	y1 = view.height() / 2 + 5;
	d1 = 1;

	particles = new ArrayList<Particle>();
	
	Blockade[] blockades;
	Life[] lives;

	int nblockades, nlives;
	nblockades = 0;
	nlives = 0;

	if (kills > 0) {
	    nblockades += rand.nextInt(kills) + kills / 2;
	    nlives += rand.nextInt(kills) + kills / 2;
	}

	blockades = new Blockade[nblockades];
	lives = new Life[nlives];

	for (int i = 0; i < blockades.length; i++) {
	    blockades[i] = new Blockade(view);
	}
	for (int i = 0; i < lives.length; i++) {
	    lives[i] = new Life(view);
	}

        parts = new Part[4 + blockades.length + lives.length];

	parts[LOCALPOS] =
	    new LightRacer(view, 0xC004CCF1, GameView.INCREASE_DEATHS,
			   x0, y0, d0);
        parts[OTHERPOS] =
	    new AIRacer(view, getOtherDifficualty(), GameView.INCREASE_KILLS,
			x1, y1, d1);
        parts[WALL1POS] =
	    new WallRacer(view, 1, 1, 0);
        parts[WALL2POS] =
	    new WallRacer(view, view.width() - 1, view.height() - 1, 0);

        for (int i = 0; i < blockades.length; i++) {
            parts[i + 4] = blockades[i];
        }
	for (int i = 0; i < lives.length; i++) {
	    parts[i + blockades.length + 4] = lives[i];
	}

        local().setOpps(parts);
        other().setOpps(parts);

        local().setLength(1);
        other().setLength(1);

        startKills = kills;
        startDeaths = deaths;

        for (int i = 0; i < 5; i++) update();
        for (int i = 0; i < blockades.length; i++) blockades[i].spawn(parts);
	for (int i = 0; i < lives.length; i++) lives[i].spawn(parts);
    }

    public void checkScore() {
        if (getKills() > startKills) {
            view.gameOver(true);
        } else if (getDeaths() > startDeaths) {
            view.gameOver(false);
        }
    }

    public void checkCollisions() {
	LightRacer local = local();
	LightRacer other = other();
	if (!local.isAlive())
	    return;
	if (!other.isAlive())
	    return;
	for (int i = 0; i < parts.length; i++) {
	    if (parts[i].collides(local)) {
		local.die(local.getX(), local.getY(), local.getDirection());
		setDeaths(getDeaths() + 1);
		checkScore();
	    }

	    if (parts[i].collides(other)) {
		other.die(other.getX(), other.getY(), other.getDirection());
		setKills(getKills() + 1);
		checkScore();
	    }
	}
    }

    public void updateLengths() {
        local().setLength(local().getLength() + 1);
	other().setLength(other().getLength() + 1);
    }

    public boolean killTailOffScreen() {
	return false;
    }
}
