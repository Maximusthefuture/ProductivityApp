package com.maximus.productivityappfinalproject.presentation;

import android.graphics.drawable.Drawable;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppIntervalUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class AppsDetailViewModelTest {

    @Rule
    public TestRule mTestRule = new InstantTaskExecutorRule();

    private GetAppIntervalUseCase mGetAppIntervalUseCase;
    @Mock
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;

    @Before
    public void setUp() throws Exception {
        mGetAppIntervalUseCase = new GetAppIntervalUseCase(mMyUsageStatsManagerWrapper);
    }

    @Test
    public void intervalList() {

    }


}