package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.maximus.productivityappfinalproject.utils.IntervalEnum;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.AppDetailFragmentRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.AppsDetailViewModel;
import com.maximus.productivityappfinalproject.utils.Utils;


public class AppDetailsFragment extends Fragment {
    private ImageView mImageView;
    private Spinner mSpinner;
    private TextView mUsageTime;
    private TextView mUsageCount;
    private TextView mAppName;
    private RecyclerView mRecyclerView;
    private int mDay = 0;
    private AppsDetailViewModel mViewModel;
    private AppsModel appsModel;
    private ChipGroup mSelectDay;
    private static final String TAG = "AppDetailsFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_app_details, container, false);
        appsModel = getArguments().getParcelable(AppsFragment.APP_DETAILS);
        mViewModel = new ViewModelProvider(this).get(AppsDetailViewModel.class);
        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        AppDetailFragmentRecyclerViewAdapter mAdapter = new AppDetailFragmentRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        mSpinner = root.findViewById(R.id.spinner_days);
        mSelectDay = root.findViewById(R.id.interval_chip_group);

        root.findViewById(R.id.cut_usage_time_button).setOnClickListener(v ->
                mViewModel.startService());
        initSpinner();

        mViewModel.intervalList(appsModel.getPackageName()).observe(getViewLifecycleOwner(), apps -> {
//                    Log.d(TAG, "onCreateView: " + mDay);
            mAdapter.setList(apps);
        });


        //TODO ЭТО!!!!! Safe ARGS navigation?????
        mImageView = root.findViewById(R.id.app_icon_image_view);
        mImageView.setImageDrawable(appsModel.getAppIcon());
        mUsageTime = root.findViewById(R.id.usage_time_text_view);
        mUsageTime.setText(Utils.formatMillisToSeconds(appsModel.getAppUsageTime()));
        mUsageCount = root.findViewById(R.id.usage_count);
        mUsageCount.setText(String.valueOf(appsModel.getCount()));
        mAppName = root.findViewById(R.id.app_name_text_view);
        mAppName.setText(appsModel.getAppName());

        return root;
    }


    public void initSpinner() {



        mSelectDay.setOnCheckedChangeListener((chipGroup, i) -> {
            Chip chip = chipGroup.findViewById(i);
            int [][] states = new int[][] {
//                    new int[] { android.R.attr.state_enabled}, // enabled
//                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };
            int[] colors = new int[] {
//                    Color.BLUE,
//                    Color.YELLOW,
                    Color.RED,
                    Color.YELLOW
            };
            ColorStateList myState = new ColorStateList(states, colors);
            chip.setChipBackgroundColor(myState);
            if (chip != null) {
                switch (i){
                    case R.id.chip_today:
                        mViewModel.setFiltering(IntervalEnum.TODAY);
                        break;
                    case R.id.chip_yesterday:
                        mViewModel.setFiltering(IntervalEnum.YESTERDAY);
                        break;
                    case R.id.chip_this_week:
                        mViewModel.setFiltering(IntervalEnum.THIS_WEEK);
                        break;

                }
                mViewModel.intervalList(appsModel.getPackageName());
            }
        });
    }
}
