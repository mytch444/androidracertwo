package com.sIlence.androidracertwo;

import android.graphics.Canvas;

public class Explosion extends Part {

    private Particle[] particles;
    private int age;

    public Explosion (GameView v, int color, int x, int y, int direction, int pixels, int stop, float maxSpeed, int s) {
        super(v);

        dieing = s;

        particles = new Particle[pixels];
        for (int i = 0; i < particles.length; i++) {
            Particle p = new Particle(color, x, y, direction * 90, 60, maxSpeed, 1f, stop);
            particles[i] = p;
        }
        age = 0;
    }

    public Explosion(GameView v, int color, int x, int y, int direction, int pixels, int stop, float maxSpeed) {
        this(v, color, x, y, direction, pixels, stop, maxSpeed, 0);
    }

    @Override
    public void update() {
        if (dieing != 0) return;

        dieing = 1;
        for (int i = 0; i < particles.length; i++) {
            if (particles[i].isAlive()) {
                particles[i].update();
                dieing = 0;
            }
        }
        age++;
    }

    @Override
    public void render(Canvas c) {
        if (dieing != 0) return;
        for (int i = 0; i < particles.length; i++) {
            if (particles[i].isAlive()) {
                particles[i].render(c);
            }
        }
    }
}
