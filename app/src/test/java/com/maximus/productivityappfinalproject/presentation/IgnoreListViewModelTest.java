package com.maximus.productivityappfinalproject.presentation;

import androidx.lifecycle.Observer;

import com.maximus.productivityappfinalproject.domain.AddIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.GetIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class IgnoreListViewModelTest {

    private IgnoreListViewModel mIgnoreListViewModel;

    @Mock
    private GetIgnoreListUseCase mGetIgnoreListUseCase;

    @Mock
    private AddIgnoreListUseCase mAddIgnoreListUseCase;

    @Mock
    private Observer<List<IgnoreItems>> mObserver;

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

    @Test
    public void insert() {
        IgnoreItems ignoreItems = new IgnoreItems("com.example.Example", 1000, "Example");
        mAddIgnoreListUseCase.addToIgnoreList(ignoreItems);
        mGetIgnoreListUseCase.getIgnoreList().observeForever(mObserver);
        verify(mObserver).onChanged(Collections.singletonList(ignoreItems));
    }
}