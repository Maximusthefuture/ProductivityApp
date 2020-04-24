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
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.presentation.AppDetailFragmentRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.AppsDetailViewModel;
import com.maximus.productivityappfinalproject.presentation.LimitedListViewModel;
import com.maximus.productivityappfinalproject.presentation.OnIgnoreItemClickListener;
import com.maximus.productivityappfinalproject.presentation.OnTrackingItemLongClickListener;
import com.maximus.productivityappfinalproject.presentation.TrackingListAdapter;
import com.maximus.productivityappfinalproject.presentation.UsageLimitViewModel;
import com.maximus.productivityappfinalproject.utils.Utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.maximus.productivityappfinalproject.utils.IntervalEnum.THIS_WEEK;
import static com.maximus.productivityappfinalproject.utils.IntervalEnum.TODAY;
import static com.maximus.productivityappfinalproject.utils.IntervalEnum.YESTERDAY;

public class TrackingListFragment extends Fragment implements OnIgnoreItemClickListener, MinutesLimitDialogFragment.MinutesLimitDialogListener, OnTrackingItemLongClickListener {

    private static final String TAG = "TrackingListFragment";
    Disposable disposable;
    boolean isChanged;
    private RecyclerView mLimitedRecyclerView;
    private TrackingListAdapter mTrackingListAdapter;
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
    private ChipGroup mSelectDay;
    private LimitedAppDetailSheet mLimitedAppDetailSheet;
    private LinearLayout mLineaLVL;
    private TextView mNothingTextView;
    private CompositeDisposable mCompositeDisposable;
    private TextView mRemainTime;
    //TODO move to usecase
    private SharedPrefManagerImpl mSharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_tracking_list, container, false);
        AppsFragment appsFragment = new AppsFragment();
        mDetailFragmentAdapter = new AppDetailFragmentRecyclerViewAdapter();
        mCompositeDisposable = new CompositeDisposable();
        init(root);

        mLimitViewModel = new ViewModelProvider(this).get(UsageLimitViewModel.class);
        mAppsDetailViewModel = new ViewModelProvider(this).get(AppsDetailViewModel.class);
        mModelView = new ViewModelProvider(this).get(LimitedListViewModel.class);

        mMinutesLimitDialogFragment = new MinutesLimitDialogFragment();

        mSharedPrefManager = new SharedPrefManagerImpl(getActivity().getApplicationContext());


        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mLimitedRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mLimitedRecyclerView.addItemDecoration(itemDecoration);
        mLimitedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mTrackingListAdapter = new TrackingListAdapter(requireContext(), this);
        mLimitedRecyclerView.setAdapter(mTrackingListAdapter);

        mActionButton.setOnClickListener(view -> {
            mNavController.navigate(R.id.apps_dest);
        });

        setHasOptionsMenu(true);
        bottomSheetBehaviorInit();

        mModelView.getAllIgnoreItems().observe(getViewLifecycleOwner(), apps -> {
            mTrackingListAdapter.setList(apps);
        });




        return root;
    }

    private void init(View root) {
        mAppname = root.findViewById(R.id.app_name_bottom_sheet);
        mTimeUsed = root.findViewById(R.id.time_used_bottom_sheet);
        mLastUsed = root.findViewById(R.id.last_used_bottom_sheet);
        mLimitDailyChip = root.findViewById(R.id.limit_in_day);
        mLimitHourlyChip = root.findViewById(R.id.limit_in_hour);
        mTimeUsedRecyclerView = root.findViewById(R.id.app_limit_recycler_view);
        mSelectDay = root.findViewById(R.id.interval_chip_group);
        mRemainTime = root.findViewById(R.id.remain_time_tv);
        mNothingTextView = root.findViewById(R.id.nothing);
        mActionButton = root.findViewById(R.id.fab_add_app);
        mLimitedRecyclerView = root.findViewById(R.id.limited_list_recycler_view);
        mLinearLayout = root.findViewById(R.id.bottom_sheet);
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
                mLimitedRecyclerView.setPadding(0, 0, 0, (int) (bottomSheet.getHeight() * slideOffset));
                mLimitedRecyclerView.invalidate();
            }
        });
    }

    private void sortSelectedDays() {
        mSelectDay.setOnCheckedChangeListener((chipGroup, i) -> {
            Chip chip = chipGroup.findViewById(i);
            if (chip != null) {
                switch (i) {
                    case R.id.chip_today:
                        mAppsDetailViewModel.setFiltering(TODAY);
                        mTimeUsedRecyclerView.scrollToPosition(0);
                        break;
                    case R.id.chip_yesterday:
                        mAppsDetailViewModel.setFiltering(YESTERDAY);
                        mTimeUsedRecyclerView.scrollToPosition(0);
                        break;
                    case R.id.chip_this_week:
                        mAppsDetailViewModel.setFiltering(THIS_WEEK);
                        mTimeUsedRecyclerView.scrollToPosition(0);
                        break;
                }
                mAppsDetailViewModel.intervalList(mPackageName);
            }
        });
    }

    private void initChips() {
        isChanged = mSharedPrefManager.isTimeLimitChanged();
        mLimitDailyChip.setOnClickListener(view -> {
            mLimitDailyChip.setSelected(true);
            TimePickerDialog.OnTimeSetListener t =
                    (view1, hourOfDay, minute) -> {
                        mLimitViewModel.setDailyLimit(mPackageName, appName, hourOfDay, minute);
                        mLimitViewModel.getSnackBarMessage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                Snackbar.make(getView(), getString(integer, appName), Snackbar.LENGTH_LONG).show();
                            }
                        });
                        //TODO Add plurals
                        mLimitDailyChip.setText(getString(R.string.limit_in_day_chip_with_args, hourOfDay + " часа " + minute + " минут"));
                        mSharedPrefManager.setTimeLimitChanged(true);
                        //Ставим true когда 1 раз в день установили лимит, можно через шеред преф сделать булево значение
                        //и обновлять его в конце дня.
//                        mLimitViewModel.resetChanged(isChanged);
                    };


            TimePickerDialog f = new TimePickerDialog(requireContext(), t, 0, 0, true);
            f.setTitle("Установить ограничение в день");

            //TODO добавить для разных приложений нужен метод во viewmodel
            if (!isChanged) {
                f.show();
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.cannot_change_time), Toast.LENGTH_SHORT).show();
            }
        });

        mLimitHourlyChip.setOnClickListener(hourlyView -> {
            if (!isChanged) {
                mMinutesLimitDialogFragment.show(TrackingListFragment.this.getChildFragmentManager(), "f");
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.cannot_change_time), Toast.LENGTH_SHORT).show();
            }

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

    }

    private void initAppsModelList() {
        mTimeUsedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mTimeUsedRecyclerView.setAdapter(mDetailFragmentAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                mModelView.deleteAll();
                break;
            case R.id.sort_by_usage_time:
            case R.id.sort_by_limited_time:
                if (item.isChecked()) {
//                    mModelView.sortByUsageTime();
                    item.setChecked(false);

                } else {
                    item.setChecked(true);
                }
                break;
        }
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(IgnoreItems items) {
        mPackageName = items.getPackageName();
        appName = items.getName();
        mBottomSheetBehavior.setPeekHeight(getView().getHeight() / 4, true);
        mBottomSheetBehavior.setFitToContents(true);

        mLinearLayout.setVisibility(View.VISIBLE);
        mAppname.setText(items.getName());
        mLastUsed.setText(items.getLastTimeUsed());
        initChips();
        sortSelectedDays();
        initAppsModelList();
        showLimitedTime();


        //TODO
        /// TODO: add this data when add? and when onPause?
        mModelView.refresh(items);
        mAppsDetailViewModel.intervalList(mPackageName).observe(getViewLifecycleOwner(), apps -> {
            mDetailFragmentAdapter.setList(apps);
        });

        mTimeUsed.setText(Utils.formatMillisToSeconds(items.getTimeUsed()));
    }

    private void showLimitedTime() {
        mCompositeDisposable.add(mModelView.showLimitedTimeOnChips(mPackageName, mLimitHourlyChip, mLimitDailyChip));
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, NumberPicker p) {
        mLimitHourlyChip.setText(getString(R.string.hourly_limit_set_to, String.valueOf(p.getValue())));
        mLimitViewModel.setHourlyLimit(mPackageName, appName, p.getValue());
        mLimitViewModel.getSnackBarMessage().observe(getViewLifecycleOwner(),
                integer ->
                        Snackbar.make(getView(), getString(integer, appName), Snackbar.LENGTH_LONG).show());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, NumberPicker picker) {

    }

    @Override
    public void onItemDelete(String ignoreItems) {
        //Call dialog before delete
        mModelView.deleteItem(ignoreItems);
        mTrackingListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
    }
}
