package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class AppsRepositoryImplTest {

    private AppsRepositoryImpl mAppsRepository;

    @Mock
    private IgnoreAppDataSourceImp mAppDataSource;

    @Mock
    private PhoneUsageDataSourceImp mPhoneUsageDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAppsRepository = new AppsRepositoryImpl(mAppDataSource);
    }

    @Test
    public void getIgnoreItems() {
        mAppsRepository.getIgnoreItems();
    }

    @Test
    public void insertIgnoreList() {
        IgnoreItems ignoreItems = new IgnoreItems("com.example.media", "Media");
        mAppsRepository.insertToIgnoreList(ignoreItems);

        verify(mAppsRepository).insertToIgnoreList(ignoreItems);
//        assertThat(mAppsRepository.getIgnoreItems(), is(1));
    }
}