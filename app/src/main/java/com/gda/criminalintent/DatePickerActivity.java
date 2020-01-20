package com.gda.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        if (intent == null) {
            return new DatePickerFragment();
        }
        Date date = (Date) intent.getSerializableExtra(DatePickerFragment.DATE_EXTRA);
        return DatePickerFragment.newInstance(date);
    }

    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(DatePickerFragment.DATE_EXTRA, date);
        return intent;
    }
}
