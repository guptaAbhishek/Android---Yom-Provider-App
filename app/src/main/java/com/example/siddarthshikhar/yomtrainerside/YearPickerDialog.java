package com.example.siddarthshikhar.yomtrainerside;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.Exception;import java.lang.Override;import java.lang.String;import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by Siddarth Shikhar on 7/21/2015.
 */
public class YearPickerDialog extends DialogFragment{
    public interface yearPicked{
        public void populateSetYear(int year,int qualificationNo,int fromOrTo);
    }
    yearPicked listener;
    int qualificationNo,fromOrTo;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Select Year");
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_year_picker,null);
        Button saveDate=(Button)v.findViewById(R.id.save_year);
        Button cancelDate=(Button)v.findViewById(R.id.cancel_year);
        saveDate.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        cancelDate.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        final NumberPicker np = (NumberPicker)v.findViewById(R.id.numberPicker1);
        np.setMaxValue(2100);
        np.setMinValue(1900);
        if(fromOrTo==0)
            np.setValue(2010);
        else
            np.setValue(2011);
        np.setWrapSelectorWheel(false);
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.populateSetYear(np.getValue(), qualificationNo, fromOrTo);
                dismiss();
            }
        });
        cancelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        d.setContentView(v);
        return  d;
    }
}
