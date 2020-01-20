package com.gda.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleText;
    private Button mDateBtn;
    private Button mTimeBtn;
    private CheckBox mSolvedChBox;
    public static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_DIALOG = "date_picker_dialog";
    private static final String TIME_PICKER_DIALOG = "time_picker_dialog";

    private static final int DATE_REQUEST_CODE = 0;
    private static final int TIME_REQUEST_CODE = 1;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        UUID crimeId = (UUID) args.getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleText = view.findViewById(R.id.crimeTitle);
        mTitleText.setText(mCrime.getTitle());
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateBtn = view.findViewById(R.id.crime_date);
        updateDate();
        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
//                datePickerFragment.setTargetFragment(CrimeFragment.this, DATE_REQUEST_CODE);
//                FragmentManager fragmentManager = getFragmentManager();
//                datePickerFragment.show(fragmentManager, DATE_PICKER_DIALOG);

                // TODO: choose method based on device type

                Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getDate());
                startActivityForResult(intent, DATE_REQUEST_CODE, null);

            }
        });

        mTimeBtn = view.findViewById(R.id.crime_time);
        mTimeBtn.setText(R.string.time_picker_title);
        mTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, TIME_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), TIME_PICKER_DIALOG);
            }
        });


        mSolvedChBox = view.findViewById(R.id.crime_solved);
        mSolvedChBox.setChecked(mCrime.isSolved());
        mSolvedChBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == DATE_REQUEST_CODE && data != null) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE_EXTRA);
            if (date != null) {
                mCrime.setDate(date);
                updateDate();
            }
        }

        if (requestCode == TIME_REQUEST_CODE && data != null) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);
            Date date = mCrime.getDate();
            date.setHours(hour);
            date.setMinutes(minute);
            updateDate();
        }

    }

    private void updateDate() {
        mDateBtn.setText(mCrime.getDate().toString());
    }

}
