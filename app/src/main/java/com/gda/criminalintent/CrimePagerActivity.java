package com.gda.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID = "com.gda.criminalintent.crime_id";


    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mToolbar = findViewById(R.id.fragment_crime_toolbar);
        setSupportActionBar(mToolbar);


        mCrimes = CrimeLab.get(this).getCrimeList();
        mViewPager = findViewById(R.id.activity_crime_pager_view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                if (crime.getTitle() != null) {
                    mToolbar.setTitle(crime.getTitle());
                } else {
                    mToolbar.setTitle(R.string.edit_crime);
                }
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (crimeId.equals(mCrimes.get(i).getId())) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
