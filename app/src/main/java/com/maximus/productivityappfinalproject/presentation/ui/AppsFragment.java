package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.Context;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.MyApplication;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.AppRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.viewmodels.AppsViewModel;
import com.maximus.productivityappfinalproject.presentation.OnSwipeAppToLimitedList;
import com.maximus.productivityappfinalproject.presentation.SimpleItemTouchHelperCallback;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

public class AppsFragment extends Fragment implements OnSwipeAppToLimitedList {

    public static final String APP_DETAILS = "APP_DETAIL";
    private static final String TAG = "AddAppsFragment";
    @Inject
    AppsViewModel mViewModel;
    private AppRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private NavController mNavController;
    private CompositeDisposable mCompositeDisposable;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_app_list, container, false);

        mCompositeDisposable = new CompositeDisposable();
//        mViewModel = new ViewModelProvider(this).get(AppsViewModel.class);

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

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) context.getApplicationContext()).getAppComponent().inject(this);
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        Flowable<String> search = Flowable.create(emitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    emitter.onNext(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    emitter.onNext(newText);
                    return false;
                }
            });
        }, BackpressureStrategy.BUFFER);

        mCompositeDisposable.add(mViewModel.searchWithSearchView(search, mAdapter));

        searchView.setOnCloseListener(() -> {
            mViewModel.getAllApps().observe(getViewLifecycleOwner(),
                    appsModels -> mAdapter.setList(appsModels));
            return true;
        });

        Log.d(TAG, "onCreateOptionsMenu: " + searchView.getQuery());
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
