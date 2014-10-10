package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import java.util.ArrayList;
import android.util.Log;

public class SnakeGame extends Game {

    public SnakeGame(int o) {
        super(o);

        winMessage = "Game Over \nYou Won";
        loseMessage = "Game Over \nYou Lost";
    }

    public void init() {
        setTime(0);
        setKills(0);
        setDeaths(0);

	particles = new ArrayList<Particle>();
	parts = new Part[4];
	
        parts[LOCALPOS] = new LightRacer(view, 0xC003CCF1, GameView.INCREASE_DEATHS);
        parts[OTHERPOS] = new AIRacer(view, getOtherDifficualty(), GameView.INCREASE_KILLS);
        parts[WALL1POS] = new WallRacer(view, view.boxWidth(), view.top() + view.boxHeight(), getOtherDifficualty());
        parts[WALL2POS] = new WallRacer(view, view.boxWidth() * (view.boxsX() - 1), view.boxHeight() * (view.boxsY() - 1) + view.top(), getOtherDifficualty());

        local().setOpps(parts);
        other().setOpps(parts);

        local().setLength(LightRacer.STANDARD_LENGTH);
        other().setLength(LightRacer.STANDARD_LENGTH);

        local().spawn();
        other().spawn();

        for (int i = 0; i < 5; i++) update();
    }

    public int checkScore() {
        int score = -1;

        if (view.getKills() >= 10 && view.getKills() - 2 >= view.getDeaths()) {
            if (view.getDeaths() > 0) {
                score = (int) (10000 / (view.getTime() / 1000) * ((float) view.getKills() / view.getDeaths()));
            } else {
                score = (int) (10000 / (view.getTime() / 1000) * 15);
            }

            view.gameOver(true);

        } else if (view.getDeaths() >= 10 && view.getDeaths() - 2 >= view.getKills()) {
            view.gameOver(false);
        }

        return score;
    }

    public void updateLengths() {
	local().setLength(getKills() * getKills() + LightRacer.STANDARD_LENGTH);
        other().setLength(getDeaths() * getDeaths() + LightRacer.STANDARD_LENGTH);
    }

    public void checkCollisions() {
	int i;
	LightRacer l = local();
	LightRacer o = other();
	for (i = 0; i < parts.length; i++) {
	    if (parts[i].collides(l)) {
		Log.d("Controler!!!", "local collided with part " + i);
	    }
	    if (parts[i].collides(o)) {
		Log.d("Controler!!!", "other collided with part " + i);
	    }
	}	
    }
}
