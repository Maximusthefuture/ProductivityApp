package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.db.IgnoreEntity;

import java.util.List;

public interface IgnoreAppDataSource {

    void add(IgnoreItems ignoreItems);

    List<IgnoreItems> getAll();

    void removeItem(IgnoreItems ignoreItems);

    void removeItem(String packageName);

    void removeAll();

}
