package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public class SnakeGame extends Game {

    public SnakeGame(int o) {
        super(o);

        winMessage = "Game Over \nYou Won";
        loseMessage = "Game Over \nYou Lost";
    }

    public void init(GameView v) {
        super.init(v);

        other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS);
        local = new LightRacer(v, 0xC003CCF1, GameView.INCREASE_DEATHS);
        wall1 = new WallRacer(v, v.boxWidth(), v.top() + v.boxHeight(), getOtherDifficualty());
        wall2 = new WallRacer(v, v.boxWidth() * (v.boxsX() - 1), v.boxHeight() * (v.boxsY() - 1) + v.top(), getOtherDifficualty());

        Part[] parts = new Part[] {other, local, wall1, wall2};

        local.setOpps(parts);
        other.setOpps(parts);

        other.setLength(LightRacer.STANDARD_LENGTH);
        local.setLength(LightRacer.STANDARD_LENGTH);

        other.spawn();
        local.spawn();
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
