package com.sIlence.androidracertwo;

public class NewGameDialog extends MyDialog {
    
    public NewGameDialog(GameView v, String m, String p, String n) {
        super(v, m, p, n);
    }

    public void pos() {
        view.newGame();
        view.resumeGame();
    }
}
