package com.gda.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private TimePicker mTimePicker;

    private static final String ARG_DATE = "date";
    public static final String EXTRA_HOUR = "com.gda.criminalintent.hour";
    public static final String EXTRA_MINUTE = "com.gda.criminalintent.minute";

    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mTimePicker = view.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setHour(calendar.get(Calendar.HOUR));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        sendResult(Activity.RESULT_OK, hour, minute);
                    }
                })
                .setTitle(R.string.time_picker_title)
                .setView(view)
                .create();
        return dialog;
    }

    private void sendResult(int resultCode, int hour, int minute) {
        Fragment fragment = getTargetFragment();
        if (fragment == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MINUTE, minute);
        fragment.onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
