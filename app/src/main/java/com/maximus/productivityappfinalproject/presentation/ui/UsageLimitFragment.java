package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.UsageLimitViewModel;

public class UsageLimitFragment extends Fragment {
    private static final String TAG = "UsageLimitFragment";

    private EditText mLimitPerHour;
    private EditText mLimitPerDayInHours;
    private EditText mLimitPerDayInMinutes;
    private Button mSetLimitButton;
    private UsageLimitViewModel mLimitViewModel;
    private NavController mNavController;
    private AppsModel mAppsModel;
    private TextView mAppName;
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_set_usage_limit, container, false);
        mLimitViewModel = new ViewModelProvider(this).get(UsageLimitViewModel.class);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mAppsModel = getArguments().getParcelable(AppDetailsFragment.EXTRA_LIMIT_APP);
//        mAppName = root.findViewById(R.id.app_name_usage_limit);
        mImageView = root.findViewById(R.id.app_icon_usage);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        mAppName.setText(mAppsModel.getAppName());
        mImageView.setImageDrawable(mAppsModel.getAppIcon());

        mLimitPerHour = root.findViewById(R.id.edit_text_limit_in_hour);
        mLimitPerDayInHours = root.findViewById(R.id.edit_text_limit_in_day_by_hours);
        mLimitPerDayInMinutes = root.findViewById(R.id.edit_text_limit_in_day_by_minutes);
        mSetLimitButton = root.findViewById(R.id.set_limit_button);


        mLimitPerHour.addTextChangedListener(mLimitViewModel.checkInput(mLimitPerHour, mLimitPerDayInHours, mLimitPerDayInMinutes, mSetLimitButton));
        mLimitPerDayInMinutes.addTextChangedListener(mLimitViewModel.checkInput(mLimitPerHour, mLimitPerDayInHours, mLimitPerDayInMinutes, mSetLimitButton));
        mLimitPerDayInHours.addTextChangedListener(mLimitViewModel.checkInput(mLimitPerHour, mLimitPerDayInHours, mLimitPerDayInMinutes, mSetLimitButton));

        mSetLimitButton.setOnClickListener(v -> {
            int perDayHour = Integer.parseInt("0" + mLimitPerDayInHours.getText().toString());
            int perDayMinutes = Integer.parseInt("0" + mLimitPerDayInMinutes.getText().toString());
            int perHourMinutes = Integer.parseInt("0" + mLimitPerHour.getText().toString());
            mLimitViewModel.setLimit(mAppsModel.getPackageName(), mAppsModel.getAppName(), perDayHour, perDayMinutes, perHourMinutes);
            mNavController.navigate(R.id.apps_dest);
        });

        mLimitViewModel.getSnackBarMessage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Snackbar.make(getView(), getString(integer, mAppsModel.getAppName()), Snackbar.LENGTH_LONG).show();
            }
        });
        return root;
    }
}
