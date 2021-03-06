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

package com.mytchel.androidracertwo.dialog;

import com.mytchel.androidracertwo.GameView;

public class NewGameDialog extends MyDialog {

    boolean make;

    public NewGameDialog(GameView v, String m, boolean n) {
        super(v, m, "Begin", "Exit");
        make = n;
    }

    public NewGameDialog(GameView v, String m) {
        this(v, m, true);
    }

    public void pos() {
        if (make)
            view.newGame();
        view.start();
    }
}
