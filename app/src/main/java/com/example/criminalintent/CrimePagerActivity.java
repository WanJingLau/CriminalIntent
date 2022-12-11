package com.example.criminalintent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.criminalintent.databinding.ActivityCrimePagerBinding;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "crime_id";
    private UUID crimeId;

    private List<Crime> mCrimes;
    private ActivityCrimePagerBinding pagerBinding;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerBinding = ActivityCrimePagerBinding.inflate(getLayoutInflater());
        setContentView(pagerBinding.getRoot());

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mCrimes = CrimeLab.get(this).getCrimes();

        pagerBinding.activityCrimePagerViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Crime crime = mCrimes.get(position);
                Log.d("testing", crime.getTitle().toString());
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getItemCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                pagerBinding.activityCrimePagerViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}