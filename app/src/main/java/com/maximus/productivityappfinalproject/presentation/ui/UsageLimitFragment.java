package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_set_usage_limit, container, false);
        mLimitViewModel = new ViewModelProvider(this).get(UsageLimitViewModel.class);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mAppsModel = getArguments().getParcelable("APP_DETAIL_LIMIT");
        mAppName = root.findViewById(R.id.app_name_usage_limit);

        mAppName.setText(mAppsModel.getAppName());

        mLimitPerHour = root.findViewById(R.id.edit_text_limit_in_hour);
        mLimitPerDayInHours = root.findViewById(R.id.edit_text_limit_in_day_by_hours);
        mLimitPerDayInMinutes = root.findViewById(R.id.edit_text_limit_in_day_by_minutes);
        mSetLimitButton = root.findViewById(R.id.set_limit_button);


        mSetLimitButton.setOnClickListener(v -> {
            int perDayHour = Integer.parseInt("0" + mLimitPerDayInHours.getText().toString());
            int perDayMinutes = Integer.parseInt("0" + mLimitPerDayInMinutes.getText().toString());
            int perHourMinutes = Integer.parseInt("0" + mLimitPerHour.getText().toString());
            mLimitViewModel.setLimit(mAppsModel.getPackageName(), mAppsModel.getAppName(), perDayHour, perDayMinutes, perHourMinutes);
            mNavController.navigate(R.id.apps_dest);
        });

        return root;
    }
}
