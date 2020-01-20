package com.gda.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String ARG_DATE = "DATE";
    public static final String DATE_EXTRA = "com.gda.criminalintent.date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
//
//        Date date = (Date) getArguments().getSerializable(ARG_DATE);
//        mDatePicker = view.findViewById(R.id.dialog_date_date_picker);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        mDatePicker.init(year, month, day, null);
//
//        Dialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setTitle(R.string.date_picker_title)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        int year = mDatePicker.getYear();
//                        int month = mDatePicker.getMonth();
//                        int day = mDatePicker.getDayOfMonth();
//                        Date date = new GregorianCalendar(year, month, day).getTime();
//                        sendResult(Activity.RESULT_OK, date);
//                    }
//                })
//                .setView(view)
//                .create();
//        return alertDialog;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        mDatePicker = view.findViewById(R.id.dialog_date_date_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, null);

        Button ok_button = view.findViewById(R.id.dialog_date_ok_button);
        ok_button.setText(R.string.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        });

        return view;
    }

    public void sendResult (int resultCode, Date date){
        Intent intent = new Intent();
        intent.putExtra(DATE_EXTRA, date);

        Fragment fragment = getTargetFragment();
        if (fragment == null) {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            fragment.onActivityResult(getTargetRequestCode(), resultCode, intent);
            dismiss();
        }
    }
}

