package com.sIlence.androidracertwo;

import android.graphics.Canvas;

public class WallRacer extends LightRacer {

    public WallRacer (GameView v, int x, int y, int gap) {
        super (v, 0xC05FFE3C, GameView.INCREASE_NULL);

        direction = 3;

        lineFall = null;
        explosions = null;

        length = view.boxsX() + view.boxsY() - gap - 2;

        if (gap == 0) light = false;

        linex = new int[length];
        liney = new int[length];

        linex[0] = x;
        liney[0] = y;

        int loops = (v.boxsX() + v.boxsY()) * 2;
        for (int i = 0; i < loops; i++) {
            update();
        }
    }

    @Override
    public boolean changeDirection(int di) {
        direction = di;

        return true;
    }

    @Override
    public void update() {
        offScreen();
        move();
        updateLine();
    }

    @Override
    public void offScreen() {
        if (linex[0] < view.boxWidth() * 2 && liney[0] > view.getHeight() / 2) changeDirection(0);
        else if (liney[0] < view.top() + view.boxHeight() * 2 && linex[0] < view.getWidth() / 2) changeDirection(1);
        else if (linex[0] > (view.boxsX() - 2) * view.boxWidth() && liney[0] < view.getHeight() / 2) changeDirection(2);
        else if (liney[0] > (view.boxsY() - 2) * view.boxHeight() + view.top() && linex[0] > view.getWidth() / 2) changeDirection(3);
    }

    @Override
    public void render(Canvas c) {
        renderLines(c);
    }
}
