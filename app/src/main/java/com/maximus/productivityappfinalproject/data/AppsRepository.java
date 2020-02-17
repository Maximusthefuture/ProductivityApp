package com.maximus.productivityappfinalproject.data;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public interface AppsRepository {


    LiveData<List<IgnoreItems>> getIgnoreItems();
    void insertToIgnoreList(IgnoreItems item);
    void deleteFromIgnoreList(IgnoreItems item);
    void deleteAllIgnoreList();
}
