package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;
import android.graphics.Canvas;
import android.util.Log;

public class TronGame extends Game {

    protected int startKills;
    protected int startDeaths;

    protected Blockade[] blockades;

    public TronGame(int o) {
        super(o);

        winMessage = "Round Over\nYou won";
        loseMessage = "Round Over\nYou Lost";
    }

    public void init(GameView v) {
        super.init(v);

        int x0, y0, d0, x1, y1, d1;
        x0 = x1 = v.boxsX() / 2;
        y0 = y1 = v.boxsY() / 2;
        if (v.boxsX() < v.boxsY()) {
            y0 -= 5;
            y1 += 5;
            d0 = 0;
            d1 = 2;
        } else {
            x0 -= 5;
            x1 += 5;
            d0 = 3;
            d1 = 1;
        }
       
        local = new LightRacer(v, 0xC004CCF1, GameView.INCREASE_DEATHS, x0 * view.boxWidth(), y0 * view.boxHeight(), d0);
        other = new AIRacer(v, getOtherDifficualty(), GameView.INCREASE_KILLS, x1 * view.boxWidth(), y1 * view.boxHeight(), d1);
        wall1 = new WallRacer(v, v.boxWidth(), v.top() + v.boxHeight(), 0);
        wall2 = new WallRacer(v, v.boxWidth() * (v.boxsX() - 1), v.boxHeight() * (v.boxsY() - 1) + v.top(), 0);

        if (kills > 0) { 
            int nblockades = (int) (rand.nextInt(kills)) + kills / 2;
            blockades = new Blockade[nblockades];

            int x, y, w, h;
            for (int i = 0; i < blockades.length; i++) {
                blockades[i] = new Blockade(v);
            }
        } else {
            blockades = new Blockade[0];
        }

        Part[] parts = new Part[4 + blockades.length];
        parts[0] = local;
        parts[1] = other;
        parts[2] = wall1;
        parts[3] = wall2;
        for (int i = 0; i < blockades.length; i++) {
            parts[i + 4] = blockades[i];
        }

        local.setOpps(parts);
        other.setOpps(parts);

        other.setLength(1);
        local.setLength(1);

        startKills = kills;
        startDeaths = deaths;

        for (int i = 0; i < 5; i++) update();
        for (int i = 0; i < blockades.length; i++) blockades[i].spawn(parts);
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

        for (int i = 0; i < blockades.length; i++) {
            blockades[i].update();
        }
    }

    public void render(Canvas c) {
        for (int i = 0; i < blockades.length; i++) {
            blockades[i].render(c);
        }

        super.render(c);
    }
}
