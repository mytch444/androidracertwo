package com.sIlence.androidracertwo;

public class AIRacer extends LightRacer {

    public static final int DIFF_CHILD = 10;
    public static final int DIFF_EASY = 30;
    public static final int DIFF_MEDI = 50;
    public static final int DIFF_HARD = 100;
    public static final int DIFF_INSANE = 300;

    int difficualty;
    int nd, r, d;

    public AIRacer(GameView v, int d, int stc) {
        super(v, 0xC0FFE64D, stc);
        startColor = color;
        difficualty = d;
    }

    public AIRacer(GameView v, int d, int stc, int x, int y, int dd) {
        this(v, d, stc);

        linex[0] = x;
        liney[0] = y;
        direction = dd;
    }

    @Override
    public void update() {
        updateExplosions();

        if (dieing > 0) {
            dieing++;

            if (!foundSpawn) {
                findSpawn();
            }

            if (dieing == 30) {

                view.checkScore();

                if (!foundSpawn) {
                    spawn();
                } else {
                    spawnSpec(linex[0], liney[0], safestDirection());
                }

                dieing = 0;
                foundSpawn = true;
            }

            return;
        }

        move();
        offScreen();
        updateLength();
        updateLine();
        checkCollisions();

        nd = -1;
        r = -1;
        if (rand.nextInt(16) == 1) r = safestDirection();
        d = tryToDodge();
        if (r != -1) nd = r;
        if (d != -1) nd = d;
        if (nd != -1) {
            final int lag = rand.nextInt(20000 / difficualty);
            final int di = nd;

            view.postDelayed(new Runnable() {
                public void run() {
                    changeDirection(di);
                }
            }, lag);
        }
    }


    private int tryToDodge() { // Try avoid other player
        if (!safeToTurn(direction, difficualty)) {
            return safestDirection();
        }
        return -1;
    }

/*
    @Override
    protected void newLine() {
        for (int i = 0; i < lineFall.length; i++) {
            if (!lineFall[i].isAlive()) {
                lineFall[i] = new LineFall(view, startColor, linex, liney, 0);
                break;
            }
        }

        linex = new int[length];
        liney = new int[length];
    }*/
}
