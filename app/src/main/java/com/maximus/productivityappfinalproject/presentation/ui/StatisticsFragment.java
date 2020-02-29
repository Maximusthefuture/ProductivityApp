package com.maximus.productivityappfinalproject.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.maximus.productivityappfinalproject.presentation.MyStatisticView;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.StatisticsViewModel;


public class StatisticsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_statistics, container, false);

        StatisticsViewModel viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        MyStatisticView barChartView = (MyStatisticView) root.findViewById(R.id.my_view);

        viewModel.getList().observe(getViewLifecycleOwner(), appsModels -> {
            AppsModel[] arrayBarData = (AppsModel[]) appsModels.toArray(new AppsModel[appsModels.size()]);
            barChartView.setYAxisData(arrayBarData);
        } );


        return root;
    }
}
