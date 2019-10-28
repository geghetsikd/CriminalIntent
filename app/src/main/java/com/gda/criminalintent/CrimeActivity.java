package com.gda.criminalintent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
