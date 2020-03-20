package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.AppRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.AppsViewModel;
import com.maximus.productivityappfinalproject.presentation.OnSwipeAppToLimitedList;
import com.maximus.productivityappfinalproject.presentation.SimpleItemTouchHelperCallback;

public class AppsFragment extends Fragment implements OnSwipeAppToLimitedList {

    public static final String APP_DETAILS = "APP_DETAIL";
    private static final String TAG = "AddAppsFragment";
    private AppsViewModel mViewModel;
    private AppRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private NavController mNavController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_app_list, container, false);


        mViewModel = new ViewModelProvider(this).get(AppsViewModel.class);

        recyclerView = root.findViewById(R.id.apps_add_recycler_view);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        mAdapter = new AppRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        mViewModel.getAllApps().observe(getViewLifecycleOwner(), apps -> {
            mAdapter.setList(apps);
        });
        setHasOptionsMenu(true);
        mViewModel.startService();

        return root;
    }

    @Override
    public void onSwiped(int position) {
        AppsModel info = mAdapter.getDataFromPosition(position);
        mViewModel.insertToIgnoreList(info);
        Log.d(TAG, "onSwiped: " + info);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_app_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_system_app:
                item.setChecked(true);
                if (item.isChecked()) {
                    item.setChecked(false);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
