package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.AddIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.GetIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class AppsRepositoryImplTest {

    private AddIgnoreListUseCase mAddIgnoreListUseCase;
    private GetIgnoreListUseCase mGetIgnoreListUseCase;
    @Mock
    private AppsRepositoryImpl mAppsRepository;

    @Mock
    private IgnoreAppDataSourceImp mDataSourceImp;

    @Mock
    private PhoneUsageDataSource mPhoneUsageDataSource;





    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAddIgnoreListUseCase = new AddIgnoreListUseCase(mAppsRepository);
        mGetIgnoreListUseCase = new GetIgnoreListUseCase(mAppsRepository);
    }

    @Test
    public void getIgnoreItems() {
        mAppsRepository.getIgnoreItems();
    }

    @Test
    public void insertIgnoreList() {
        IgnoreItems ignoreItems = new IgnoreItems("com.example.media", "Media");
        mAddIgnoreListUseCase.addToIgnoreList(ignoreItems);
        verify(mAppsRepository).insertToIgnoreList(ignoreItems);

    }

    @Test
    public void getIgnoreItemsFromLocalDataSource() {
        mGetIgnoreListUseCase.getIgnoreList();
//       verify(mDataSourceImp).(any(IgnoreAppDataSource.class));
    }


}