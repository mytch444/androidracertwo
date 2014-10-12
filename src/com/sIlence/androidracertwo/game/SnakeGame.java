package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import java.util.ArrayList;
import android.util.Log;

public class SnakeGame extends Game {

    LightRacer other;
    
    public SnakeGame(int o) {
        super(o);

        winMessage = "Game Over \nYou Won";
        loseMessage = "Game Over \nYou Lost";
    }

    public void init() {
	super.init();
	
        setTime(0);
        setKills(0);
        setDeaths(0);

	particles = new ArrayList<Particle>();
	parts = new ArrayList<Part>();
	
        parts.add(LOCALPOS, (Part) new LightRacer(view, 0xC003CCF1, GameView.INCREASE_DEATHS));
	other = new AIRacer(view, getOtherDifficualty(), GameView.INCREASE_KILLS);
	parts.add((Part) other);
        parts.add((Part) new WallRacer(view, 1, 1, getOtherDifficualty() / 4));
        parts.add((Part) new WallRacer(view, view.width() - 1, view.height() - 1, getOtherDifficualty() / 4));

	//        local().setOpps(parts);
	//        other().setOpps(parts);

        getLocal().setLength(LightRacer.STANDARD_LENGTH);
        other.setLength(LightRacer.STANDARD_LENGTH);

        getLocal().spawn();
        other.spawn();

        for (int i = 0; i < 5; i++) update();
    }

    public void checkScore() {
	int score;
        if (getKills() >= 10 && getKills() - 2 >= getDeaths()) {
            if (getDeaths() > 0) {
                score = (int) (10000 / (getTime() / 1000) * ((float) getKills() / getDeaths()));
            } else {
                score = (int) (10000 / (getTime() / 1000) * 15);
            }

            view.gameOver(true);

        } else if (getDeaths() >= 10 && getDeaths() - 2 >= getKills()) {
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
	getLocal().setLength(getKills() * getKills() + LightRacer.STANDARD_LENGTH);
        other.setLength(getDeaths() * getDeaths() + LightRacer.STANDARD_LENGTH);
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
