package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public class SnakeGame extends Game {

    public SnakeGame(int o) {
	super(o);
    }

    public void init(GameView v) {
	super.init(v);
	
	other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS);
	local = new LightRacer(v, 0xC003CCF1, GameView.INCREASE_DEATHS);
	wall1 = new WallRacer(v, v.boxWidth(), v.top() + v.boxHeight(), getOtherDifficualty());
	wall2 = new WallRacer(v, v.boxWidth() * (v.boxsX() - 1), v.boxHeight() * (v.boxsY() - 1) + v.top(), getOtherDifficualty());
	
	int loops = (v.boxsX() + v.boxsY()) * 2;
	for (int i = 0; i < loops; i++) {
	    wall1.preupdate();
	    wall2.preupdate();
	}
	
	Part[] parts = new Part[] {other, local, wall1, wall2};
	
	local.setOpps(parts);
	other.setOpps(parts);
	
	other.spawn(LightRacer.STANDARD_LENGTH);
	local.spawn(LightRacer.STANDARD_LENGTH);
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
	local.setLength(view.getKills() * view.getKills() + LightRacer.STANDARD_LENGTH);
	other.setLength(view.getDeaths() * view.getDeaths() + LightRacer.STANDARD_LENGTH);
    }

}