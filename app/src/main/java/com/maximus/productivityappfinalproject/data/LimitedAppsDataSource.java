package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.LimitedApps;

import java.util.List;

public interface LimitedAppsDataSource {

    void add(LimitedApps item);

    List<LimitedApps> getAll();

    void removeItem(String packageName);

    void removeAll();

    void update(LimitedApps items);

}
