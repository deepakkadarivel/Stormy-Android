package com.upbeat.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.upbeat.stormy.R;

public class AlertDialogFragment extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context mContext = getActivity();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_message_button_text, null);
        AlertDialog dialog = mBuilder.create();
        return dialog;
    }



}
