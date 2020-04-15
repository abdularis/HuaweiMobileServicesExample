package com.aar.tryhuawei

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.huawei.hms.api.HuaweiApiAvailability

class HuaweiUtils {
    companion object {
        private const val HUAWEI_MANUFACTURER = "huawei"

        fun shouldUseHuaweiServices(context: Context): Boolean {
            return checkHMSAvailable(context) && isHuaweiDevice
        }

        fun checkHMSAvailable(context: Context): Boolean
            = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context) == 0

        val isHuaweiDevice: Boolean @SuppressLint("DefaultLocale")
            get() = TextUtils.equals(Build.MANUFACTURER.toLowerCase(), HUAWEI_MANUFACTURER)
    }
}