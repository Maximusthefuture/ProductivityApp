package com.maximus.productivityappfinalproject.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class AppsRepositoryImplTest {


    private AppsRepositoryImpl mAppsRepository;

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private IgnoreAppDataSourceImp mIgnoreAppDataSource;

    @Mock
    private PhoneUsageDataSourceImp mPhoneUsageDataSource;

    @Mock
    private SharedPrefManagerImpl mSharedPrefManager;

    @Mock
    private AppLimitDataSourceImpl mAppLimitDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAppsRepository = new AppsRepositoryImpl(mIgnoreAppDataSource, mPhoneUsageDataSource, mAppLimitDataSource, mSharedPrefManager);
    }

    @Test
    public void getIgnoreItems() {
        mAppsRepository.getIgnoreItems();
        verify(mIgnoreAppDataSource).getAll();

    }

    @Test
    public void insertIgnoreList() {
        IgnoreItems ignoreItems = new IgnoreItems("com.example.f", 1000, "f");
        mAppsRepository.insertToIgnoreList(ignoreItems);
        verify(mIgnoreAppDataSource).add(ignoreItems);

    }

    @Test
    public void getIgnoreItemsFromLocalDataSource() {
//       verify(mDataSourceImp).(any(IgnoreAppDataSource.class));
    }

    @Test
    public void insertPhoneUsageDat() {
        PhoneUsage phoneUsage = new PhoneUsage("com.example.media", 34, "10", 1000, 2000);
        mAppsRepository.insertPhoneUsage(phoneUsage);
        verify(mPhoneUsageDataSource).insertPhoneUsage(phoneUsage);
    }

    @Test
    public void insertToAppLimit() {
        AppUsageLimitModel appUsageLimitModel = new AppUsageLimitModel(
                "Media", "com.example.media", 4000, 1000, 0, true);
        mAppsRepository.addToLimit(appUsageLimitModel);
        verify(mAppLimitDataSource).addToLimit(appUsageLimitModel);
//            assertThat(mAppLimitDataSource.getLimitedApps().size(), is(1));
    }

    @Test
    public void getLimitedItems() {
        mAppsRepository.getLimitedItems();
        verify(mAppLimitDataSource).getLimitedApps();
    }

    @Test
    public void deleteIgnoreItemFromDb() {
        IgnoreItems ignoreItems = new IgnoreItems("com.example.media", 1000, "Media");
        mAppsRepository.insertToIgnoreList(ignoreItems);
        assertThat(mIgnoreAppDataSource.getAll().get(1), is(true));
//        verify(mIgnoreAppDataSource).removeItem(ignoreItems.getPackageName());
//        mAppsRepository.deleteFromIgnoreList(ignoreItems.getPackageName());

    }




}