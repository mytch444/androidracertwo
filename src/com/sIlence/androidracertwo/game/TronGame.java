package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public class TronGame extends Game {

    protected int startKills;
    protected int startDeaths;

    public TronGame(int o) {
        super(o);

        winMessage = "Round Over\nYou won";
        loseMessage = "Round Over\nYou Lost";
    }

    public void init(GameView v) {
        super.init(v);


        boolean vertstart = true;
        if (v.boxsX() > v.boxsY()) vertstart = false;

        if (vertstart) {
            local = new LightRacer(v, 0xC003CCF1, GameView.INCREASE_DEATHS, (v.boxsX() / 2) * v.boxWidth(), (v.boxsY() / 2 - 5) * v.boxHeight(), 0);
            other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS, (v.boxsX() / 2) * v.boxWidth(), (v.boxsY() / 2 + 5) * v.boxHeight(), 2);
        } else {
            local = new LightRacer(v, 0xC003CCF1, GameView.INCREASE_DEATHS, (v.boxsX() / 2 - 5) * v.boxWidth(), (v.boxsY() / 2) * v.boxHeight(), 3);
            other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS, (v.boxsX() / 2 + 5) * v.boxWidth(), (v.boxsY() / 2) * v.boxHeight(), 1);
        }
        
        wall1 = new WallRacer(v, v.boxWidth(), v.top() + v.boxHeight(), 0);
        wall2 = new WallRacer(v, v.boxWidth() * (v.boxsX() - 1), v.boxHeight() * (v.boxsY() - 1) + v.top(), 0);

        Part[] parts = new Part[] {other, local, wall1, wall2};

        local.setOpps(parts);
        other.setOpps(parts);

        other.setLength(1);
        local.setLength(1);

        startKills = kills;
        startDeaths = deaths;
    }

    public int checkScore() {
        int score = -1;

        if (getKills() > startKills) {
            view.gameOver(true);
            score = view.getTime();
        } else if (getDeaths() > startDeaths) {
            view.gameOver(false);
        }

        return score;
    }

    public void updateLengths() {
        local.setLength(local.getLength() + 1);
        other.setLength(other.getLength() + 1);
    }

    public void update() {
        updateLengths();
        super.update();
    }
}
