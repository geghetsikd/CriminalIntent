package com.gda.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    public static final String DATE_PICKER_DIALOG = "date_picker_dialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton("OK", null)
                .setView(view)
                .create();
        return alertDialog;
    }
}
