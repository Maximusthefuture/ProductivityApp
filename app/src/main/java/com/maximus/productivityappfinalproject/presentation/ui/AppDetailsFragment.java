package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.IntervalEnum;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.Utils;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.AppDetailFragmentRecyclerViewAdapter;
import com.maximus.productivityappfinalproject.presentation.AppsDetailViewModel;


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
        mSpinner = root.findViewById(R.id.spinner_days);


        initSpinner();

        mViewModel.intervalList(appsModel.getPackageName()).observe(getViewLifecycleOwner(), apps -> {
//                    Log.d(TAG, "onCreateView: " + mDay);
            mAdapter.setList(apps);
        });






        //TODO ЭТО!!!!!
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.days_statistic_array, android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mViewModel.setFiltering(IntervalEnum.TODAY);
                        break;
                    case 1:
                        mViewModel.setFiltering(IntervalEnum.YESTERDAY);
                        break;

                }

                mViewModel.intervalList(appsModel.getPackageName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
