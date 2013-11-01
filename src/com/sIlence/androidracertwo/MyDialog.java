package com.sIlence.androidracertwo;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.util.Log;

public class MyDialog extends DialogFragment {

    protected GameView view;

    protected String mes, pos, neg;

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
        Log.d("tag", "canceling");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d("tag", "dismissing");
    }

    public void pos() {

    }

    public void neg() {
        ((AndroidRacerTwo) view.getContext()).menu();
    }
}
