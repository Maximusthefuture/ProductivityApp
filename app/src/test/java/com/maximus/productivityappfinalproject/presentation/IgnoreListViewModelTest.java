package com.maximus.productivityappfinalproject.presentation;

import com.maximus.productivityappfinalproject.domain.AddIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.GetIgnoreListUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class IgnoreListViewModelTest {

    private IgnoreListViewModel mIgnoreListViewModel;

    @Mock
    private GetIgnoreListUseCase mGetIgnoreListUseCase;

    @Mock
    private AddIgnoreListUseCase mAddIgnoreListUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllIgnoreItems() {
    }

    @Test
    public void deleteAll() {
    }

    @Test
    public void deleteIgnoreItem() {
    }
}