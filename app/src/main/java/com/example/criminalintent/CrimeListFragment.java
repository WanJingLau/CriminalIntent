package com.example.criminalintent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.databinding.FragmentCrimeListBinding;
import com.example.criminalintent.databinding.ListItemCrimeBinding;

import java.util.Collections;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private FragmentCrimeListBinding crimeListBinding;
    CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        crimeListBinding = FragmentCrimeListBinding.inflate(getLayoutInflater());
        return crimeListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crimeListBinding.crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        public TextView mTitleTextView;
        private final TextView mDateTextView;
        private final CheckBox mSolvedCheckBox;

        //locate the data
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //xml find view by id
        public CrimeHolder(ListItemCrimeBinding itemBinding) {
            super(itemBinding.getRoot());
            mTitleTextView = itemBinding.listItemCrimeTitleTextView;
            mDateTextView = itemBinding.listItemCrimeDateTextView;
            mSolvedCheckBox = itemBinding.listItemCrimeSolvedCheckBox;

            itemBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private final List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemCrimeBinding itemBinding = ListItemCrimeBinding.inflate(getLayoutInflater());
            return new CrimeHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void notifyItemRemoved(int from, int to) {

        }
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            crimeListBinding.crimeRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(crimes, from, to);
                mAdapter.notifyItemRemoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                crimes.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        //attached helper object into RecyclerView
        helper.attachToRecyclerView(crimeListBinding.crimeRecyclerView);
    }
}
