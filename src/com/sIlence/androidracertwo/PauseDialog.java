package com.sIlence.androidracertwo;

public class PauseDialog extends MyDialog {

    public PauseDialog(GameView v, String m, String p, String n) {
        super(v, m, p, n);
    }

    public void pos() {
        view.resumeGame();
    }
}
