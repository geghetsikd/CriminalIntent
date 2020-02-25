package com.gda.criminalintent;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivityForResult(intent, CrimeListFragment.CRIME_STATE);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) != null) {
            CrimeListFragment fragment = (CrimeListFragment) getSupportFragmentManager().
                    findFragmentById(R.id.fragment_container);
            fragment.updateUI();
        }
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) != null) {
            CrimeFragment fragment = (CrimeFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detail_fragment_container);
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
            CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
            listFragment.updateUIRemoved();
            Log.d("DELETE:" , "fragment") ;
        }
    }
}
