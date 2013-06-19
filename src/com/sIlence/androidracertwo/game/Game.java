package com.sIlence.androidracertwo.game;

import com.sIlence.androidracertwo.*;

public abstract class Game {

    protected LightRacer local;
    protected AIRacer other;
    protected WallRacer wall1;
    protected WallRacer wall2;

    protected int otherDifficualty;

    protected GameView view;
    
    public Game(int o) {
	otherDifficualty = o;
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

    public void updateLengths() {

    }
}