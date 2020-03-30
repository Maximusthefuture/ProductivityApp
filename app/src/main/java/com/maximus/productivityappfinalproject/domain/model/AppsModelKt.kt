package com.maximus.productivityappfinalproject.domain.model

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

data class AppsModelKt(var mPackageName: String? = null,
                       var mAppName: String? = null,
                       var mAppIcon: Drawable? = null,
                       var mAppUsageTime: Long? = 0,
                       var mEventTime: Long? = 0,
                       var mEventType: Int? = 0,
                       var mLastTimeUsed: String? = null): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            TODO("mAppIcon"),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mPackageName)
        parcel.writeString(mAppName)
        parcel.writeValue(mAppUsageTime)
        parcel.writeString(mLastTimeUsed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppsModelKt> {
        override fun createFromParcel(parcel: Parcel): AppsModelKt {
            return AppsModelKt(parcel)
        }

        override fun newArray(size: Int): Array<AppsModelKt?> {
            return arrayOfNulls(size)
        }
    }
}