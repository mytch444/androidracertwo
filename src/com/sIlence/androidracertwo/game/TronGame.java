package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;
import java.util.ArrayList;
import java.util.ArrayList;

public class TronGame extends Game {

    LightRacer other;
    int startKills;
    int startDeaths;

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

	int nblockades, nlives;
	nblockades = 0;
	nlives = 0;

	if (kills > 0) {
	    nblockades += rand.nextInt(kills) + kills / 2;
	    nlives += rand.nextInt(kills) + kills / 2;
	}

	particles = new ArrayList<Particle>();
	parts = new ArrayList<Part>();
	
	parts.add(LOCALPOS,
		  (Part) new LightRacer(view, 0xC004CCF1, GameView.INCREASE_DEATHS,
				 x0, y0, d0));
	other = new AIRacer(view, getOtherDifficualty(), GameView.INCREASE_KILLS,
			x1, y1, d1);
	parts.add((Part) other);
        parts.add((Part) new WallRacer(view, 1, 1, 0));
        parts.add((Part) new WallRacer(view, view.width() - 1, view.height() - 1, 0));

	for (int i = 0; i < nblockades; i++)
	    parts.add((Part) new Blockade(view));
	for (int i = 0; i < nlives; i++)
	    parts.add((Part) new Life(view));

	//        local().setOpps(parts);
	//        other().setOpps(parts);

        getLocal().setLength(1);
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
	LightRacer local = getLocal();
	for (int i = 0; i < parts.size(); i++) {
	    if (!parts.get(i).isAlive())
		continue;
	    
	    if (local.isAlive() && parts.get(i).collides(local)) {
		local.die(local.getX(), local.getY(), local.getDirection());
		setDeaths(getDeaths() + 1);
		checkScore();
	    }

	    if (other.isAlive() && parts.get(i).collides(other)) {
		other.die(other.getX(), other.getY(), other.getDirection());
		setKills(getKills() + 1);
		checkScore();
	    }
	}
    }

    public void updateLengths() {
        getLocal().setLength(getLocal().getLength() + 1);
	other.setLength(other.getLength() + 1);
    }

    public boolean killTailOffScreen() {
	return false;
    }

    public void hud(Canvas c, boolean started) {
	int t;
        brush.setColor(0xffffffff);
	brush.setTextSize(view.topBorder() - 6);

	if (!started)
	    t = 0;
	else
	    t = getTime() / 1000;
	c.drawText("Time: " + t, 10, view.topBorder() - 4, brush);
        textString = getKills() + " : " + getDeaths();
        c.drawText(textString, view.getWidth() - fromRight - view.halfWidth(textString, brush), view.topBorder() - 4, brush);
    }
}
