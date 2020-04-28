package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;

import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.presentation.viewmodels.AppsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

public class AppsViewModelTest {

    private AppsViewModel mAppsViewModel;

    public Application mContext;
    @Mock
    private GetAppsUseCase mGetAppsUseCase;


    @Before
    public void setUp() {
        mAppsViewModel = new AppsViewModel(mContext);
    }

    @Test
    public void getAllApps() {

    }

    @Test
    public void insertToIgnoreList() {
        AppsModel appsModel = new AppsModel();
        mAppsViewModel.insertToIgnoreList(appsModel);

    }

}