package com.aar.tryhuawei

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import kotlin.concurrent.thread

class HuaweiPushService: HmsMessageService() {

    companion object {
        private const val TAG = "HuaweiPushService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "remote message received")
        remoteMessage?.let {
            Log.d(TAG, " - getData: ${it.data}")
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(TAG, "huawei push kit token: $token")
    }

}

class GetTokenAction {
    private val handler: Handler = Handler(Looper.getMainLooper())

    public fun getToken(context: Context, callback: (String) -> Unit) {
        thread {
            try {
                val appID = AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                val token = HmsInstanceId.getInstance(context).getToken(appID, "HCM")
                handler.post { callback(token) }
            } catch (err: Exception) {
                Log.d("GetTokenAction", "get token error: $err")
            }
        }
    }
}