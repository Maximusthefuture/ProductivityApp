package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;

import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
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
        mAppsViewModel.getAllApps();
        verify(mGetAppsUseCase.getAllApps(true, 0));
    }

    @Test
    public void insertToIgnoreList() {
        AppsModel appsModel = new AppsModel();
        mAppsViewModel.insertToIgnoreList(appsModel);

    }

    @Test
    public void startService() {
    }

    @Test
    public void setFiltering() {
    }
}