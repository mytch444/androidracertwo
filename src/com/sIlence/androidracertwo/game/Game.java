package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public abstract class Game {

    protected LightRacer local;
    protected AIRacer other;
    protected WallRacer wall1;
    protected WallRacer wall2;

    protected int otherDifficualty;

    protected int kills;
    protected int deaths;
    protected int time;

    protected GameView view;

    protected String winMessage;
    protected String loseMessage;

    public Game(int o) {
        otherDifficualty = o;

        kills = 0;
        deaths = 0;
        time = 0;
    }

    public void init(GameView v) {
        view = v;
    }

    public LightRacer local() {
        return local;
    }

    public AIRacer other() {
        return other;
    }

    public WallRacer wall1() {
        return wall1;
    }

    public WallRacer wall2() {
        return wall2;
    }

    public int getOtherDifficualty() {
        return otherDifficualty;
    }

    public GameView view() {
        return view;
    }

    public int checkScore() {
        return -1;
    }

    public void updateLengths() {}

    public String winMessage() {
        return winMessage;
    }

    public String loseMessage() {
        return loseMessage;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTime() {
        return time;
    }

    public void setKills(int k) {
        kills = k;
    }

    public void setDeaths(int d) {
        deaths = d;
    }

    public void setTime(int t) {
        time = t;
    }
}
