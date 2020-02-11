package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.presentation.IgnoreListAdapter;
import com.maximus.productivityappfinalproject.presentation.IgnoreListViewModel;

import java.util.ArrayList;

public class IgnoreListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private IgnoreListAdapter mAdapter;
    private IgnoreListViewModel mModelView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_ignore_list, container, false);

        mModelView = new ViewModelProvider(this).get(IgnoreListViewModel.class);
        mRecyclerView = root.findViewById(R.id.ignore_list_recycler_view);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new IgnoreListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mModelView.getAllIgnoreItems().observe(getViewLifecycleOwner(), apps -> {
            mAdapter.setList(apps);
        });

        setHasOptionsMenu(true);



        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_all_ignore_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                mModelView.deleteAll();
                //Callback
                //OnDeleteAll
        }
        return super.onOptionsItemSelected(item);
    }
}
