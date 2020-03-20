package com.maximus.productivityappfinalproject.presentation.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.presentation.AppDetailFragmentRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.AppsDetailViewModel;
import com.maximus.productivityappfinalproject.presentation.LimitedListAdapter;
import com.maximus.productivityappfinalproject.presentation.LimitedListViewModel;
import com.maximus.productivityappfinalproject.presentation.OnIgnoreItemClickListener;
import com.maximus.productivityappfinalproject.presentation.UsageLimitViewModel;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrackingListFragment extends Fragment implements OnIgnoreItemClickListener, MinutesLimitDialogFragment.MinutesLimitDialogListener {

    private static final String TAG = "TrackingListFragment";
    private RecyclerView mRecyclerView;
    private LimitedListAdapter mAdapter;
    private LimitedListViewModel mModelView;
    private FloatingActionButton mActionButton;
    private NavController mNavController;
    private TextView mAppname;
    private TextView mTimeUsed;
    private UsageLimitViewModel mLimitViewModel;
    private String mPackageName;
    private String appName;
    private Chip mLimitDailyChip;
    private Chip mLimitHourlyChip;
    private TextView mLastUsed;
    private LinearLayout mLinearLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private MinutesLimitDialogFragment mMinutesLimitDialogFragment;
    private RecyclerView mTimeUsedRecyclerView;
    private AppsDetailViewModel mAppsDetailViewModel;
    private AppDetailFragmentRecyclerViewAdapter mDetailFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_tracking_list, container, false);
        mAppname = root.findViewById(R.id.app_name_bottom_sheet);
        mTimeUsed = root.findViewById(R.id.time_used_bottom_sheet);
        mLastUsed = root.findViewById(R.id.last_used_bottom_sheet);
        mLimitDailyChip = root.findViewById(R.id.limit_in_day);
        mLimitHourlyChip = root.findViewById(R.id.limit_in_hour);
        mTimeUsedRecyclerView = root.findViewById(R.id.app_limit_recycler_view);
        mDetailFragmentAdapter = new AppDetailFragmentRecyclerViewAdapter();


        mTimeUsedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mTimeUsedRecyclerView.setAdapter(mDetailFragmentAdapter);
        ArrayList<AppsModel> models = new ArrayList<>();
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 2));
        models.add(new AppsModel(0, 30, 2));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 2));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 1));
        models.add(new AppsModel(0, 30, 2));
        models.add(new AppsModel(0, 30, 1));
        mDetailFragmentAdapter.setList(models);

        mLimitViewModel = new ViewModelProvider(this).get(UsageLimitViewModel.class);
        mMinutesLimitDialogFragment = new MinutesLimitDialogFragment();
        mAppsDetailViewModel = new ViewModelProvider(this).get(AppsDetailViewModel.class);


        mModelView = new ViewModelProvider(this).get(LimitedListViewModel.class);
        mActionButton = root.findViewById(R.id.fab_add_app);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mRecyclerView = root.findViewById(R.id.limited_list_recycler_view);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new LimitedListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        mActionButton.setOnClickListener(view -> {
            mNavController.navigate(R.id.apps_dest);
        });


        setHasOptionsMenu(true);

        mLinearLayout = root.findViewById(R.id.bottom_sheet);
        bottomSheetBehaviorInit();
        initChips(root);


        mModelView.getAllIgnoreItems().observe(getViewLifecycleOwner(), apps -> {
//            mAdapter.notifyDataSetChanged();
            mAdapter.setList(apps);
//            for (IgnoreItems app : apps) {
//                mModelView.refresh(app);
//            }
        });
        return root;
    }

    private void bottomSheetBehaviorInit() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mLinearLayout);

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

//                    mRecyclerView.setClickable(false);
//                    mRecyclerView.setActivated(false);
                }
                if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
//                    mRecyclerView.setClickable(false);
//                    mRecyclerView.setActivated(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mRecyclerView.setPadding(0, 0, 0, (int) (bottomSheet.getHeight() * slideOffset));
                mRecyclerView.invalidate();
            }
        });
    }


    private void initChips(View root) {
        mLimitDailyChip.setOnClickListener(view -> {
            mLimitDailyChip.setSelected(true);
            TimePickerDialog.OnTimeSetListener t =
                    (view1, hourOfDay, minute) -> {
                        mLimitViewModel.setLimit(mPackageName, appName, hourOfDay, 0, minute);
                        mLimitViewModel.getSnackBarMessage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                Snackbar.make(getView(), getString(integer, appName), Snackbar.LENGTH_LONG).show();
                            }
                        });
                        //TODO Add plurals
                        mLimitDailyChip.setText(getString(R.string.limit_in_day_chip_with_args, hourOfDay + " часа " + minute + " минут"));
                    };
            TimePickerDialog f = new TimePickerDialog(requireContext(), t, 0, 0, true);
            f.setTitle("Установить ограничение в день");
            f.show();

//


        });

        mLimitHourlyChip.setOnClickListener(hourlyView -> {
            mMinutesLimitDialogFragment.show(TrackingListFragment.this.getChildFragmentManager(), "f");

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_all_ignore_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mAdapter.notifyDataSetChanged();
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
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClickListener(IgnoreItems items) {
        mPackageName = items.getPackageName();
        appName = items.getName();
        mBottomSheetBehavior.setPeekHeight(getView().getHeight() / 4, true);
        mLinearLayout.setVisibility(View.VISIBLE);
        mAppname.setText(items.getName());
        mLastUsed.setText(items.getLastTimeUsed());
        //TODO EMPTY LIST
//        mAppsDetailViewModel.intervalList(mPackageName).observe(getViewLifecycleOwner(), apps -> {
//            mDetailFragmentAdapter.setList(apps);
//        });
        //TODO this method call error on view thread
        //todo Animators may only be run on Looper threads
//        AppsDatabase.datatbaseWriterExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                if (mModelView.isLimit(mPackageName)) {
////                    mChip.setSelected(true);
//                    String time = mModelView.getLimitTimePerDay(mPackageName);
////                    mChip.setPressed(true);
//                    mChip.setText(getString(R.string.limit_in_day_chip_with_args, time));
//                } else {
//                    mChip.setText("Лимит не установлен");
//                }
//            }
//        });

//        Log.d(TAG, "onItemClickListener: " + mModelView.isLimit(mPackageName));
        mTimeUsed.setText(Utils.formatMillisToSeconds(items.getTimeUsed()));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, NumberPicker p) {
            mLimitHourlyChip.setText(getString(R.string.hourly_limit_set_to, p.getValue()));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, NumberPicker picker) {

    }
}
