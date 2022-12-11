package com.example.criminalintent;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.criminalintent.databinding.FragmentCrimeBinding;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String CRIME_DATE = "crimeDate";
    private static final String REQ_KEY = "requestKey";

    private Crime mCrime;
    private FragmentCrimeBinding crimeBinding;

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        //testing
        getParentFragmentManager().setFragmentResultListener(REQ_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Date crimeDate = (Date)result.getSerializable(CRIME_DATE);
                Log.d(" Result",result.toString());
                crimeBinding.crimeDate.setText(crimeDate.toString());
                mCrime.setDate(crimeDate);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        crimeBinding = FragmentCrimeBinding.inflate(getLayoutInflater());
        return crimeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        crimeBinding.crimeTitle.setText(mCrime.getTitle());

        crimeBinding.crimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle((s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        //button
        crimeBinding.crimeDate.setText(mCrime.getDate().toString());
        //crimeBinding.crimeDate.setEnabled(false);
        crimeBinding.crimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.show(manager,CRIME_DATE);
            }
        });

        crimeBinding.crimeSolved.setChecked(mCrime.isSolved());

        //is solved?
        crimeBinding.crimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
    }
}
