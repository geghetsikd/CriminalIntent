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

    public void addCrime(Crime c) {
        mCrimeList.add(c);
    }
}
