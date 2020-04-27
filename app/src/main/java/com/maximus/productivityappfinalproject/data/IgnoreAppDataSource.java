package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.LimitedApps;

import java.util.List;

import io.reactivex.Flowable;

public interface IgnoreAppDataSource {

    void add(LimitedApps item);

    Flowable<List<LimitedApps>> getAll();

    void removeItem(String packageName);

    void removeAll();

    void update(LimitedApps items);

}
