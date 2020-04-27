package com.maximus.productivityappfinalproject.framework

import android.content.Context
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase
import javax.inject.Inject

class PhoneUsageDataSourceImpKt constructor(
        private val context: Context,
        private val appsDatabase: AppsDatabase){



}