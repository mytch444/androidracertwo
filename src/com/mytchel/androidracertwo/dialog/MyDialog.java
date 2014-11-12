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
import com.mytchel.androidracertwo.AndroidRacerTwo;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.util.Log;

public class MyDialog extends DialogFragment {

    GameView view;
    String mes, pos, neg;

    public MyDialog(GameView v, String m, String p, String n) {
        super();

        view = v;

        mes = m;
        pos = p;
        neg = n;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mes);
        builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pos();
            }
        });
        builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                neg();
            }
        });

        Dialog dialog = builder.create();

        return dialog; 
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    public void pos() {

    }

    public void neg() {
        ((AndroidRacerTwo) view.getContext()).menu();
    }
}
