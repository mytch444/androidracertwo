package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import java.util.ArrayList;
import android.util.Log;

public class SnakeGame extends Game {

    public static int SPAWN_DELAY = 30;
    
    LightRacer other;
    int localSpawnDelay, otherSpawnDelay;
    
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

	localSpawnDelay = otherSpawnDelay = -1;

	particles = new ArrayList<Particle>();
	parts = new ArrayList<Part>();

	local = new LightRacer(view, 0xC003CCF1);
        parts.add((Part) local);
	other = new AIRacer(view, getOtherDifficualty());
	parts.add((Part) other);
        parts.add((Part) new WallRacer(view, 1, 1, getOtherDifficualty() / 4));
        parts.add((Part) new WallRacer(view, view.width() - 1, view.height() - 1, getOtherDifficualty() / 4));

        local.setLength(LightRacer.STANDARD_LENGTH);
        other.setLength(LightRacer.STANDARD_LENGTH);

        local.spawn(parts);
        other.spawn(parts);

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
	for (int i = 0; i < parts.size(); i++) {
	    if (!parts.get(i).isAlive())
		continue;
	    
	    if (local.isAlive() && parts.get(i).collides(local)) {
		local.die(local.getX(), local.getY(), local.getDirection());
		localSpawnDelay = SPAWN_DELAY;
		setDeaths(getDeaths() + 1);
		checkScore();
	    }

	    if (other.isAlive() && parts.get(i).collides(other)) {
		other.die(other.getX(), other.getY(), other.getDirection());
		otherSpawnDelay = SPAWN_DELAY;
		setKills(getKills() + 1);
		checkScore();
	    }
	}
    }

    public void update() {
	super.update();

	if (localSpawnDelay > 0) {
	    localSpawnDelay--;
	    if (localSpawnDelay == 0)
		local.spawn(parts);
	}

	if (otherSpawnDelay > 0) {
	    otherSpawnDelay--;
	    if (otherSpawnDelay == 0)
		other.spawn(parts);
	}
    }

    public void updateLengths() {
	local.setLength(getKills() * getKills() + LightRacer.STANDARD_LENGTH);
        other.setLength(getDeaths() * getDeaths() + LightRacer.STANDARD_LENGTH);
    }

    public void hud(Canvas c) {
	int t;
        brush.setColor(0xffffffff);
	brush.setTextSize(view.topBorder() - 6);

	t = getTime() / 1000;
	c.drawText("Time: " + t, 10, view.topBorder() - 4, brush);
        textString = getKills() + " : " + getDeaths();
        c.drawText(textString, view.getWidth() - 10 - view.textWidth(textString, brush), view.topBorder() - 4, brush);
    }
}
