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
import com.maximus.productivityappfinalproject.databinding.FragmentIgnoreListBinding;
import com.maximus.productivityappfinalproject.presentation.IgnoreListAdapter;
import com.maximus.productivityappfinalproject.presentation.IgnoreListViewModel;
import com.maximus.productivityappfinalproject.presentation.OnIgnoreItemClickListener;

public class IgnoreListFragment extends Fragment implements OnIgnoreItemClickListener {

    private RecyclerView mRecyclerView;
    private IgnoreListAdapter mAdapter;
    private IgnoreListViewModel mModelView;
    private FragmentIgnoreListBinding mListBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_ignore_list, container, false);

        mModelView = new ViewModelProvider(this).get(IgnoreListViewModel.class);
        mRecyclerView = root.findViewById(R.id.ignore_list_recycler_view);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new IgnoreListAdapter(requireContext(), this);
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

                //TODO
                //Callback
                //OnDeleteAll
        }
        return super.onOptionsItemSelected(item);
    }


    //TODO ?????   refresh() ?
    @Override
    public void onItemClickListener(String packageName) {
        mModelView.deleteIgnoreItem(packageName);
        mAdapter.notifyDataSetChanged();

    }
}
