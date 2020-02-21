package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public interface IgnoreAppDataSource {

    void add(IgnoreItems item);

    List<IgnoreItems> getAll();

//    void removeItem(IgnoreItems item);

    void removeItem(String packageName);

    void removeAll();

}
