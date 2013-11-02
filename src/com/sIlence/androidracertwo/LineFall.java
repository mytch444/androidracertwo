package com.sIlence.androidracertwo;

import android.graphics.Canvas;
import java.util.ArrayList;
import android.util.Log;

public class LineFall extends Part {

    protected ArrayList<Particle> particles;

    public LineFall(GameView v, int color, int[] xa, int[] ya, int state) {
        super(v);
        dieing = state;

        particles = new ArrayList<Particle>();

        int x0, y0, x1, y1, distance, incdec;
        for (int i = 0; i < xa.length - 1; i++) {
            if (xa[i + 1] == 0 && ya[i + 1] == 0) break;

            x0 = xa[i];
            y0 = ya[i];

            x1 = xa[i + 1];
            y1 = ya[i + 1];

            int xd = x1 - x0;
            int yd = y1 - y0;
            int xid = 1;
            if (xd < 0) xid = -1;
            int yid = 1;
            if (yd < 0) yid = -1;
            
            if (xd > view.boxWidth()) continue;
            if (yd > view.boxHeight()) continue;

            int xj = x0 - xid;
            do {
                int yj = y0 - yid; 
                do {
                    particles.add(newParticle(color, xj + xid, yj + yid));
                    yj += yid;
                } while (yj != y1);
                xj += xid;
            } while (xj != x1);
        }
    }

    public static Particle newParticle(int color, int x, int y) {
        return new Particle(color, x, y, 0, 360, 0.25f, 0.125f, 30);
    }

    @Override
    public void update() {
        if (dieing != 0) return;
        dieing = 1;
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i).isAlive()) {
                particles.get(i).update();
                dieing = 0;
            }
        }
    }

    @Override
    public void render(Canvas c) {
        if (dieing != 0) return;
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).render(c);
        }
    }
}
