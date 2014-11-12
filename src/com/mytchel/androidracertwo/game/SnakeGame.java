/*
 *
 * This file is part of AndroidRacerTwo
 *
 * AndroidRacerTwo is free software: you can redistribute it and/or modify
 * it under the term of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the Licence, or
 * (at your option) any later version.
 * 
 * AndroidRacerTwo is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with AndroidRacerTwo. If not, see <http://www.gnu.org/licenses/>
 *
 * Copyright: 2013 Mytchel Hammond <mytchel.hammond@gmail.com>
 *
 */

package com.mytchel.androidracertwo.game;

import com.mytchel.androidracertwo.*;
import android.graphics.Canvas;
import java.util.ArrayList;
import android.util.Log;

public class SnakeGame extends Game {

    public static int SPAWN_DELAY = 30;

    LightRacer other;
    int localSpawnDelay, otherSpawnDelay;

    public SnakeGame(int o) {
        super(o);
    }

    public void init() {
        super.init();

        setTime(0);
        setKills(0);
        setDeaths(0);

        localSpawnDelay = otherSpawnDelay = -1;

        particles = new ArrayList<Particle>();
        parts = new ArrayList<Part>();

        local = new LightRacer(this, 0xC003CCF1);
        parts.add((Part) local);
        other = new AIRacer(this);
        parts.add((Part) other);

        WallRacer sub = new WallRacer(this);
        WallRacer master = new WallRacer(this, getDifficualty() * 100, sub);
        parts.add((Part) sub);
        parts.add((Part) master);

        local.spawn(parts);
        other.spawn(parts);

        for (int i = 0; i < 5; i++) update();
    }

    public void checkScore() {
        int score;
        if (getKills() >= 10 && getKills() - 2 >= getDeaths()) {
            Log.d("fdsa", "checking score when time = " + getTime() / 1000 + " kills = " + getKills() + " and deaths = " + getDeaths());
            if (getDeaths() > 0) {
                score = (int) (10000 / (getTime() / 1000 + 1) * ((float) getKills() / getDeaths()));
            } else {
                score = (int) (10000 / (getTime() / 1000 + 1) * 15);
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
                local.die(local.getX(), local.getY(), local.getDirection(), false);
                localSpawnDelay = SPAWN_DELAY;
                setDeaths(getDeaths() + 1);
                checkScore();
            }

            if (other.isAlive() && parts.get(i).collides(other)) {
                other.die(other.getX(), other.getY(), other.getDirection(), false);
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
            if (localSpawnDelay == 0) {
                local = new LightRacer(this, 0xC003CCF1);
                local.spawn(parts);
                parts.add((Part) local);
            }
        }

        if (otherSpawnDelay > 0) {
            otherSpawnDelay--;
            if (otherSpawnDelay == 0) {
                other = new AIRacer(this);
                other.spawn(parts);
                parts.add((Part) other);
            }
        }
    }

    public void updateLengths() {
        local.setLength(getKills() * getKills() + local.getStartLength());
        other.setLength(getDeaths() * getDeaths() + other.getStartLength());
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

    public String winMessage() {
        return "Game Over\nYou Won!";
    }

    public String loseMessage() {
        return "Game Over\nYou Lost!";
    }

    public String startMessage() {
        return "You Are Blue.\nMake Yellow Crash.";
    }
}
