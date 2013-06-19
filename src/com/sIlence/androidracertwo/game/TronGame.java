package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public class TronGame extends Game {

    public TronGame(int o) {
	super(o);
    }

    public void init(GameView v) {
	super.init(v);

	other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS);
	local = new LightRacer(v, 0xC003CCF1, GameView.INCREASE_DEATHS);
	wall1 = new WallRacer(v, v.boxWidth(), v.top() + v.boxHeight(), 0);
	wall2 = new WallRacer(v, v.boxWidth() * (v.boxsX() - 1), v.boxHeight() * (v.boxsY() - 1) + v.top(), 0);
	
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
	
	if (view.getKills() > 0) {
	    view.gameOver(true);
	    score = view.getTime();
	} else if (view.getDeaths() > 0) {
	    view.gameOver(false);
	}

	return score;
    }

    public void updateLengths() {
	local.setLength(local.getLength() + 1);
	other.setLength(other.getLength() + 1);
    }
}