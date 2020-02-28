package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.Intent;
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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.presentation.AppRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.OnAppClickListener;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.presentation.SimpleItemTouchHelperCallback;
import com.maximus.productivityappfinalproject.presentation.OnSwipeAppToIgnoreList;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.presentation.AppsViewModel;
import com.maximus.productivityappfinalproject.service.CheckAppLaunchService;

import java.util.ArrayList;
import java.util.List;

public class AppsFragment extends Fragment implements OnAppClickListener, OnSwipeAppToIgnoreList {

    private static final String TAG = "AddAppsFragment";
    private AppsViewModel mViewModel;
    private AppRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private NavController mNavController;
    public static final String APP_DETAILS = "APP_DETAIL";



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
        mAdapter = new AppRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

//        mViewModel.deleteAll();

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
    public void onStart() {
        super.onStart();
        Intent service = new Intent(getContext(), CheckAppLaunchService.class);
        getContext().startService(service);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ignore_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAppClickListener(AppsModel appsModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(APP_DETAILS ,appsModel);
        mNavController.navigate(R.id.app_detail_fragment, bundle);
    }

    @Override
    public void onSwiped(int position) {
        AppsModel info = mAdapter.getDataFromPosition(position);
        String packageName = info.getPackageName();
        String appName = info.getAppName();
        IgnoreItems ignoreItems = new IgnoreItems(packageName, appName);
        List<IgnoreItems> list = new ArrayList<>();
        list.add(ignoreItems);
        mViewModel.insert(ignoreItems);
        Log.d(TAG, "onSwiped: " + info);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return super.onOptionsItemSelected(item);
    }

}
