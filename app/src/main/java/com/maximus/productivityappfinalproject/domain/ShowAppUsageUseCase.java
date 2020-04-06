package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;

public class ShowAppUsageUseCase {

    private PhoneUsageNotificationManager mManager;

    public ShowAppUsageUseCase(PhoneUsageNotificationManager manager) {
        mManager = manager;
        mManager.createNotificationChannel();
    }

    public void showNotifications(String phoneUsedTime, int count) {
//        mManager.createNotification(phoneUsedTime, count);
    }
}
