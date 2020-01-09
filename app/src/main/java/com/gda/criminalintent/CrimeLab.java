package com.gda.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimeList;

    private CrimeLab(Context context) {
        mCrimeList = new ArrayList<Crime>();
        for (int i=0; i < 20; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime # " + i);
            crime.setSolved(i % 2 == 0);
            mCrimeList.add(crime);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public Crime getCrime(UUID crimeId) {
        for (Crime crime: mCrimeList) {
            if (crime.getId().equals(crimeId)) {
                return crime;
            }
        }
        return null;
    }
}
