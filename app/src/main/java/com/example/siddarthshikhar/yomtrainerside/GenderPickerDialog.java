package com.example.siddarthshikhar.yomtrainerside;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Siddarth Shikhar on 7/22/2015.
 */
public class GenderPickerDialog extends DialogFragment {
    public interface GenderDialogTaskDone {
        public void genderDialogEnded(String genderPicked);
    }
    GenderDialogTaskDone listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enter Your Gender")
                .setPositiveButton("Male", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.genderDialogEnded("Male");
                    }
                })
                .setNegativeButton("Female", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        listener.genderDialogEnded("Female");
                    }
                });
        return builder.create();
    }
}
