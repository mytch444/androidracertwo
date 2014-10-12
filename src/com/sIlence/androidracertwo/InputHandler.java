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

package com.sIlence.androidracertwo;

import com.sIlence.androidracertwo.game.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.*;
import android.util.Log;

public class InputHandler {
    private boolean usingArrows;
    private Rect[] arrows;
    private float x, y, xDiff, yDiff;

    private Paint brush;

    private GameView view;

    public InputHandler(GameView v, boolean a) {
	view = v;
	usingArrows = a;
        brush = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void init() {
	if (usingArrows) {
	    int arrowWidth = view.getWidth() / 5;
	    
	    arrows = new Rect[4];
	    arrows[3] =
		new Rect(
			 view.getWidth() / 2 - arrowWidth / 2,
			 view.getHeight() - arrowWidth * 2 - 20,
			 view.getWidth() / 2 + arrowWidth / 2,
			 view.getHeight() - arrowWidth - 20
			 );
	    arrows[1] =
		new Rect(
			 view.getWidth() / 2 - arrowWidth / 2,
			 view.getHeight() - arrowWidth - 10,
			 view.getWidth() / 2 + arrowWidth / 2,
			 view.getHeight() - 10
			 );
	    arrows[0] =
		new Rect(
			 view.getWidth() / 2 + arrowWidth / 2 + 10,
			 view.getHeight() - arrowWidth - 10,
			 view.getWidth() / 2 + arrowWidth / 2 + arrowWidth + 10,
			 view.getHeight() - 10
			 );
	    arrows[2] =
		new Rect(
			 view.getWidth() / 2 - arrowWidth / 2 - arrowWidth - 10,
			 view.getHeight() - arrowWidth - 10,
			 view.getWidth() / 2 - arrowWidth / 2 - 10,
			 view.getHeight() - 10
			 );			 
	}
    }
    
    public void overlay(Canvas c) {
	if (usingArrows) {
	    brush.setColor(0xffaaaaaa);
	    brush.setStyle(Paint.Style.STROKE);
	    
	    for (int i = 0; i < arrows.length; i++) {
		c.drawRect(arrows[i], brush);
	    }
	}
    }
    
    public boolean onTouchEvent(MotionEvent e) {
	if (usingArrows) {
	    for (int i = 0; i < arrows.length; i++) {
		if (arrows[i].contains((int) e.getX(), (int) e.getY())) {
		    view.getLocal().changeDirection(i);
		}
	    }
	} else {
	    if (e.getAction() == MotionEvent.ACTION_DOWN) {
		x = e.getX();
		y = e.getY();
	    } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
		xDiff = e.getX() - x;
		yDiff = e.getY() - y;
		x = e.getX();
		y = e.getY();
		
		int g = vertorhorz(xDiff, yDiff);
		if (g == 0) {
		    if (xDiff > 0 && view.getLocal().changeDirection(0));
		    if (xDiff < 0 && view.getLocal().changeDirection(2));
		} else if (g == 1) {
		    if (yDiff < 0 && view.getLocal().changeDirection(3));
		    if (yDiff > 0 && view.getLocal().changeDirection(1));
		}
	    }
	}
	return true;
    }
    
    protected int vertorhorz(float x, float y) {
	if (x < 0) x = -x;
	if (y < 0) y = -y;
	
	if (x > y) return 0;
	if (y > x) return 1;
	return -1;
    }
}

